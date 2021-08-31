package com.ooooonly.lma.di

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import androidx.room.Room
import com.microsoft.appcenter.utils.storage.SharedPreferencesManager
import com.ooooonly.lma.data.AppDatabase
import com.ooooonly.lma.data.dao.BotDao
import com.ooooonly.lma.data.dao.LogDao
import com.ooooonly.lma.data.dao.ScriptDao
import com.ooooonly.lma.model.AppFiles
import com.ooooonly.lma.log.LogViewModel
import com.ooooonly.lma.mirai.MiraiViewModel
import com.ooooonly.lma.script.ScriptViewModel
import com.ooooonly.lma.model.viewmodel.ViewModelContainer
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
    fun provideGlobalViewModelContainer(
        scriptViewModel: ScriptViewModel,
        miraiViewModel: MiraiViewModel,
        logViewModel: LogViewModel
    ): ViewModelContainer {
        return ViewModelContainer(
            scriptViewModel, miraiViewModel, logViewModel
        )
    }

    @Provides
    @Singleton
    fun provideSharePreferences(
        @ApplicationContext appContext: Context
    ): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(appContext)
    }
}