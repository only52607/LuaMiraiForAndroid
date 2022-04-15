package com.ooooonly.lma.script

import com.ooooonly.lma.utils.GiteeFile
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