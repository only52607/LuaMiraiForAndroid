package com.ooooonly.lma.mcl.impl

import android.os.Build
import androidx.annotation.RequiresApi
import com.ooooonly.lma.mcl.ArtPluginClassLoader
import com.ooooonly.lma.mcl.mclinternal.ExportManagerImpl
import com.ooooonly.lma.mcl.mclinternal.PluginServiceHelper.findServices
import com.ooooonly.lma.mcl.mclinternal.PluginServiceHelper.loadAllServices
import kotlinx.coroutines.ensureActive
import net.mamoe.mirai.console.MiraiConsole
import net.mamoe.mirai.console.plugin.PluginManager
import net.mamoe.mirai.console.plugin.jvm.*
import net.mamoe.mirai.console.util.ConsoleExperimentalApi
import java.io.File
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
class ArtPluginLoader @Inject constructor(): JvmPluginLoader by JvmPluginLoader {

    private val classLoaders: MutableList<ArtPluginClassLoader> = mutableListOf()

    private val pluginFileToInstanceMap: MutableMap<File, JvmPlugin> = ConcurrentHashMap()

    @OptIn(ConsoleExperimentalApi::class)
    private fun Sequence<File>.extractPlugins(): List<JvmPlugin> {
        ensureActive()

        fun Sequence<Map.Entry<File, ArtPluginClassLoader>>.findAllInstances(): Sequence<Map.Entry<File, JvmPlugin>> {
            return onEach { (_, pluginClassLoader) ->
                val exportManagers = pluginClassLoader.findServices(
                    ExportManager::class
                ).loadAllServices()
                if (exportManagers.isEmpty()) {
                    val rules = pluginClassLoader.getResourceAsStream("export-rules.txt")
                    if (rules == null)
                        pluginClassLoader.declaredFilter = StandardExportManagers.AllExported
                    else rules.bufferedReader(Charsets.UTF_8).useLines {
                        pluginClassLoader.declaredFilter = ExportManagerImpl.parse(it.iterator())
                    }
                } else {
                    pluginClassLoader.declaredFilter = exportManagers[0]
                }
            }.map { (f, pluginClassLoader) ->
                f to pluginClassLoader.findServices(
                    JvmPlugin::class,
                    KotlinPlugin::class,
                    JavaPlugin::class
                ).loadAllServices()
            }.flatMap { (f, list) ->

                list.associateBy { f }.asSequence()
            }
        }

        val filePlugins = this.filterNot {
            pluginFileToInstanceMap.containsKey(it)
        }.associateWith {
            ArtPluginClassLoader(it, MiraiConsole::class.java.classLoader, classLoaders)
        }.onEach { (_, classLoader) ->
            classLoaders.add(classLoader)
        }.asSequence().findAllInstances().onEach {
            //logger.verbose { "Successfully initialized JvmPlugin ${loaded}." }
        }.onEach { (file, plugin) ->
            pluginFileToInstanceMap[file] = plugin
        } + pluginFileToInstanceMap.asSequence()

        return filePlugins.toSet().map { it.value }
    }

    private fun pluginsFilesSequence(): Sequence<File> =
        PluginManager.pluginsFolder.listFiles().orEmpty().asSequence()
            .filter { it.isFile && it.name.endsWith(fileSuffix, ignoreCase = true) }

    override fun listPlugins(): List<JvmPlugin> = pluginsFilesSequence().extractPlugins()
}