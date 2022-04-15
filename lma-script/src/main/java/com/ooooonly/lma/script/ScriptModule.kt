package com.ooooonly.lma.script

import com.ooooonly.lma.script.impl.ScriptRepositoryImpl
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
    abstract fun bindScriptRepository(
        scriptRepositoryImpl: ScriptRepositoryImpl
    ): ScriptRepository
}