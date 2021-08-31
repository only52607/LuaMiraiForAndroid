package com.ooooonly.lma.mcl

import net.mamoe.mirai.console.plugin.loader.PluginLoader

interface PluginLoaderProvider {
    fun providePluginLoader(): PluginLoader<*, *>
}