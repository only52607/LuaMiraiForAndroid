package com.ooooonly.lma.di

import com.ooooonly.lma.mirai.BotConstructor
import com.ooooonly.lma.mirai.LoginSolverDelegate
import com.ooooonly.lma.mirai.impl.BotConstructorImpl
import com.ooooonly.lma.mirai.impl.LoginSolverDelegateImpl
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
        botConstructorImpl: BotConstructorImpl
    ): BotConstructor

    @Binds
    @Singleton
    abstract fun bindLoginSolverDelegate(
        loginSolverDelegateImpl: LoginSolverDelegateImpl
    ): LoginSolverDelegate
}