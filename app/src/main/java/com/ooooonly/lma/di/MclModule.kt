package com.ooooonly.lma.di

import com.ooooonly.lma.mcl.MclLoggerProvider
import com.ooooonly.lma.mcl.impl.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import net.mamoe.mirai.console.ConsoleFrontEndImplementation
import net.mamoe.mirai.console.MiraiConsoleImplementation
import net.mamoe.mirai.console.plugin.loader.PluginLoader
import net.mamoe.mirai.console.util.ConsoleInput
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class MclModule {
    @Binds
    @Singleton
    abstract fun bindMclLoggerProvider(
        mclLoggerProviderImpl: MclLoggerProviderImpl
    ): MclLoggerProvider

    @Binds
    @Singleton
    abstract fun bindConsoleInput(
        consoleInputImpl: ConsoleInputImpl
    ): ConsoleInput

    @OptIn(ConsoleFrontEndImplementation::class)
    @Binds
    @Singleton
    abstract fun bindConsoleCommandSender(
        consoleCommandSenderAndroidImpl: ConsoleCommandSenderAndroidImpl
    ): MiraiConsoleImplementation.ConsoleCommandSenderImpl

    @OptIn(ConsoleFrontEndImplementation::class)
    @Binds
    @Singleton
    abstract fun bindPluginLoader(
        artPluginLoader: ArtPluginLoader
    ): PluginLoader<*,*>

    @OptIn(ConsoleFrontEndImplementation::class)
    @Binds
    @Singleton
    abstract fun bindMiraiConsoleImplementation(
        miraiConsoleImplementationForAndroid: MiraiConsoleImplementationForAndroid
    ): MiraiConsoleImplementation
}