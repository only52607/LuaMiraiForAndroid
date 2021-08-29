package com.ooooonly.lma.di

import com.ooooonly.lma.script.ScriptBuilder
import com.ooooonly.lma.script.impl.ScriptBuilderImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class ScriptModule {
    @Binds
    @Singleton
    abstract fun bindScriptBuilder(
        scriptBuilderImpl: ScriptBuilderImpl
    ): ScriptBuilder
}