package com.ooooonly.lma.log

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModel
import com.ooooonly.lma.datastore.dao.LogDao
import com.ooooonly.lma.datastore.entity.LogItem
import com.ooooonly.lma.logger.LogQueryConfig
import com.ooooonly.lma.logger.LogRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class LogScreenViewModel @Inject constructor(
    private val logRepository: LogRepository,
    private val logDataStore: DataStore<Preferences>
): ViewModel() {
    var logQueryConfig by mutableStateOf(LogQueryConfig(
        containBotMessageLog = true,
        containBotNetLog = true,
        containMclLog = true,
        containScriptOutput = true
    ))
        private set

    var logDisplayState by mutableStateOf(LogDisplayState(
        autoScrollEnabled = true,
        dateVisible = false,
        identityVisible = false
    ))
        private set

    val logItemList: Flow<List<LogItem>> = logRepository.getLogs(logQueryConfig)

    fun updateLogQueryConfig(logQueryConfig: LogQueryConfig) {
        this.logQueryConfig = logQueryConfig
    }

    fun updateLogDisplayState(logDisplayState: LogDisplayState) {
        this.logDisplayState = logDisplayState
    }
}