package com.ooooonly.lma.di

import com.ooooonly.lma.script.ScriptBuilder
import com.ooooonly.lma.script.impl.ScriptBuilderImpl
import com.ooooonly.lma.utils.GiteeFile
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ScriptCenterRoot

@InstallIn(SingletonComponent::class)
@Module
abstract class ScriptModule {
    @Binds
    @Singleton
    abstract fun bindScriptBuilder(
        scriptBuilderImpl: ScriptBuilderImpl
    ): ScriptBuilder
}

@InstallIn(SingletonComponent::class)
@Module
class ScriptCenterModule {
    @OptIn(ExperimentalStdlibApi::class)
    @Provides
    @Singleton
    @ScriptCenterRoot
    fun provideScriptCenterRoot() = GiteeFile(
        owner = "ooooonly",
        repository = "lua-mirai-project",
        path = "ScriptCenter",
        showParent = true,
        rootLevel = 2
    )
}