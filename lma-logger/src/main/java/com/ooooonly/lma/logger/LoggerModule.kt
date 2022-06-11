package com.ooooonly.lma.logger

import com.ooooonly.lma.logger.utils.impl.LmaLoggerImpl
import com.ooooonly.lma.logger.repository.impl.LogRepositoryImpl
import com.ooooonly.lma.logger.repository.LogRepository
import com.ooooonly.lma.logger.utils.LmaLogger
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class LoggerModule {
    @Binds
    @Singleton
    abstract fun bindLogRepository(
        logRepository: LogRepositoryImpl
    ): LogRepository

    @Binds
    @Singleton
    abstract fun bindLmaLogger(
        lmaLoggerImpl: LmaLoggerImpl
    ): LmaLogger
}