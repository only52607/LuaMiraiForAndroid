package com.ooooonly.lma.mcl.impl

import android.os.Build
import androidx.annotation.RequiresApi
import com.ooooonly.lma.mcl.PluginLoaderProvider
import net.mamoe.mirai.console.plugin.loader.PluginLoader
import javax.inject.Inject

class ArtPluginLoaderProvider @Inject constructor() : PluginLoaderProvider {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun providePluginLoader(): PluginLoader<*, *> {
        return ArtPluginLoader()
    }
}