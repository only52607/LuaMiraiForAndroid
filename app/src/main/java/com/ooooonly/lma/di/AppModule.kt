package com.ooooonly.lma.di

import android.content.ContentResolver
import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.io.File
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ScriptDirectory

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class BotWorkingDirectory

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class MiraiConsoleDirectory

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class OdexDirectory

@InstallIn(SingletonComponent::class)
@Module
class AppModule {
    @Provides
    @Singleton
    @ScriptDirectory
    fun provideScriptDirectory(@ApplicationContext appContext: Context) =
        File(appContext.filesDir, "scripts").also { if (!it.exists()) it.mkdir() }

    @Provides
    @Singleton
    @BotWorkingDirectory
    fun provideBotWorkingDirectory(@ApplicationContext appContext: Context) =
        File(
            appContext.filesDir,
            "bots"
        ).also { if (!it.exists()) it.mkdir() }

    @Provides
    @Singleton
    @MiraiConsoleDirectory
    fun provideMiraiConsoleDirectory(@ApplicationContext appContext: Context) =
        File(
            appContext.filesDir,
            "mcl"
        ).also { if (!it.exists()) it.mkdir() }

    @Provides
    @Singleton
    @OdexDirectory
    fun provideOdexDirectory(@ApplicationContext appContext: Context) =
        File(appContext.filesDir, "odex").also { if (!it.exists()) it.mkdir() }

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