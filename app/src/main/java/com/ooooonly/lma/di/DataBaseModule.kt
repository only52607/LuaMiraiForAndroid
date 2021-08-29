package com.ooooonly.lma.di

import android.content.Context
import androidx.room.Room
import com.ooooonly.lma.data.AppDatabase
import com.ooooonly.lma.data.dao.BotDao
import com.ooooonly.lma.data.dao.LogDao
import com.ooooonly.lma.data.dao.ScriptDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DataBaseModule {
    @Provides
    fun provideScriptDao(appDatabase: AppDatabase): ScriptDao {
        return appDatabase.scriptDao()
    }

    @Provides
    fun provideBotDao(appDatabase: AppDatabase): BotDao {
        return appDatabase.botDao()
    }

    @Provides
    fun provideLogDao(appDatabase: AppDatabase): LogDao {
        return appDatabase.logDao()
    }

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "lua-mirai-for-android"
        ).fallbackToDestructiveMigration().build()
    }
}