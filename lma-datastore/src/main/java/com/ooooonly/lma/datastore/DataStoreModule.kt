package com.ooooonly.lma.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import com.ooooonly.lma.datastore.dao.BotDao
import com.ooooonly.lma.datastore.dao.LogDao
import com.ooooonly.lma.datastore.dao.ScriptDao
import com.ooooonly.lma.datastore.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

val Context.logDataStore: DataStore<Preferences> by preferencesDataStore(name = "log")

@InstallIn(SingletonComponent::class)
@Module
class DataStoreModule {
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