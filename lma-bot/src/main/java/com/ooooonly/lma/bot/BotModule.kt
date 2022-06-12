package com.ooooonly.lma.bot

import com.ooooonly.lma.bot.repository.BotInstanceManger
import com.ooooonly.lma.bot.repository.impl.BotInstanceMangerImpl
import com.ooooonly.lma.bot.utils.LoginSolverDelegate
import com.ooooonly.lma.bot.utils.impl.LoginSolverDelegateImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class BotModule {
    @Binds
    @Singleton
    abstract fun bindBotConstructor(
        botInstanceMangerImpl: BotInstanceMangerImpl
    ): BotInstanceManger

    @Binds
    @Singleton
    abstract fun bindLoginSolverDelegate(
        loginSolverDelegateImpl: LoginSolverDelegateImpl
    ): LoginSolverDelegate
}