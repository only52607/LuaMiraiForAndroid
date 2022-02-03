package com.ooooonly.lma.di

import android.content.ContentResolver
import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.ooooonly.lma.AppFiles
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.io.File
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class AppModule {
    @Provides
    @Singleton
    fun provideAppFiles(@ApplicationContext appContext: Context): AppFiles {
        return AppFiles(
            scriptDirectory = File(appContext.filesDir, "scripts").also { if(!it.exists()) it.mkdir() },
            botWorkingDirBase = File(appContext.filesDir, "bots").also { if(!it.exists()) it.mkdir() },
            mclWorkingDirBase = File(appContext.filesDir, "mcl").also { if(!it.exists()) it.mkdir() },
            odexDirectory = File(appContext.filesDir, "odex").also { if(!it.exists()) it.mkdir() },
        )
    }

    @Provides
    @Singleton
    fun provideSharePreferences(
        @ApplicationContext appContext: Context
    ): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(appContext)
    }

    @Provides
    @Singleton
    fun provideContentResolver(
        @ApplicationContext appContext: Context
    ): ContentResolver {
        return appContext.contentResolver
    }
}