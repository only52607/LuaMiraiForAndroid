package com.ooooonly.lma.mcl.impl

import android.os.Build
import androidx.annotation.RequiresApi
import com.ooooonly.lma.BuildConfig
import com.ooooonly.lma.mcl.MclLoggerProvider
import com.ooooonly.lma.mirai.LoginSolverDelegate
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import net.mamoe.mirai.console.ConsoleFrontEndImplementation
import net.mamoe.mirai.console.MiraiConsoleFrontEndDescription
import net.mamoe.mirai.console.MiraiConsoleImplementation
import net.mamoe.mirai.console.data.MultiFilePluginDataStorage
import net.mamoe.mirai.console.data.PluginDataStorage
import net.mamoe.mirai.console.plugin.loader.PluginLoader
import net.mamoe.mirai.console.util.ConsoleExperimentalApi
import net.mamoe.mirai.console.util.ConsoleInput
import net.mamoe.mirai.console.util.SemVersion
import net.mamoe.mirai.utils.BotConfiguration
import net.mamoe.mirai.utils.LoginSolver
import net.mamoe.mirai.utils.MiraiLogger
import java.nio.file.Path
import javax.inject.Inject
import javax.inject.Provider
import kotlin.coroutines.CoroutineContext

@OptIn(ConsoleFrontEndImplementation::class, ConsoleExperimentalApi::class)
@RequiresApi(Build.VERSION_CODES.O)
class MiraiConsoleImplementationForAndroid @Inject constructor(
    private val appFiles: AppFiles,
    private val loginSolverDelegate: LoginSolverDelegate,
    private val mclLoggerProvider: MclLoggerProvider,
    override val consoleInput: ConsoleInput,
    override val consoleCommandSender: MiraiConsoleImplementation.ConsoleCommandSenderImpl,
    private val pluginLoaderProvider: Provider<PluginLoader<*,*>>
) : MiraiConsoleImplementation {

    private val errorLogger = mclLoggerProvider.provideMclLogger("Error")

    override val builtInPluginLoaders: List<Lazy<PluginLoader<*, *>>>
        get() = listOf(lazy { pluginLoaderProvider.get() })

    override val configStorageForBuiltIns: PluginDataStorage =
        MultiFilePluginDataStorage(rootPath.resolve("config"))

    override val configStorageForJvmPluginLoader: PluginDataStorage
        get() = MultiFilePluginDataStorage(rootPath.resolve("config"))

    override val coroutineContext: CoroutineContext
        get() = coroutineExceptionHandler() + SupervisorJob() + Dispatchers.Default

    override val dataStorageForBuiltIns: PluginDataStorage
        get() = MultiFilePluginDataStorage(rootPath.resolve("data"))

    override val dataStorageForJvmPluginLoader: PluginDataStorage
        get() = MultiFilePluginDataStorage(rootPath.resolve("data"))

    override val frontEndDescription: MiraiConsoleFrontEndDescription
        get() = object : MiraiConsoleFrontEndDescription {
            override val name: String get() = "Android"
            override val vendor: String get() = "LuaMiraiForAndroid"
            override val version: SemVersion = SemVersion(BuildConfig.VERSION_NAME)
        }

    override val rootPath: Path
        @RequiresApi(Build.VERSION_CODES.O)
        get() = appFiles.mclWorkingDirBase.toPath()

    override fun createLogger(identity: String?): MiraiLogger = mclLoggerProvider.provideMclLogger(identity)

    override fun createLoginSolver(
        requesterBot: Long,
        configuration: BotConfiguration
    ): LoginSolver = loginSolverDelegate

    private fun coroutineExceptionHandler() = CoroutineExceptionHandler { _, throwable ->
        errorLogger.error(throwable)
    }
}