package com.ooooonly.lma.ui.log

import androidx.compose.runtime.*
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ooooonly.lma.datastore.entity.LogItem
import com.ooooonly.lma.logger.LogRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LogScreenViewModel @Inject constructor(
    private val logRepository: LogRepository,
    private val logDataStore: DataStore<Preferences>
): ViewModel() {
    private data class LogQueryConfig(
        val containBotMessageLog: Boolean,
        val containBotNetLog: Boolean,
        val containMclLog: Boolean,
        val containScriptOutput: Boolean
    )

    private val _logList = MutableStateFlow(listOf<LogItem>())
    val logList: StateFlow<List<LogItem>> = _logList
    val logDisplayState = LogDisplayState()

    private var currentHistorySize = 0
    private var initialListSize = logDataStore.data.map { it[intPreferencesKey("log_initial_size")] ?: 200 }
    private var maximumListSize = logDataStore.data.map { it[intPreferencesKey("log_maximum_size")] ?: 500 }
    private var compressedListSize = logDataStore.data.map { it[intPreferencesKey("log_compressed_size")] ?: 200 }
    private val historyLoadSize = logDataStore.data.map { it[intPreferencesKey("log_load_history_size")] ?: 100 }
    private val logInsertListener: (LogItem) -> Unit = ::onLogInsert

    init {
        initializeLogList()
        subscribeLogInsert()
        attackLogDisplayChanged()
    }

    fun clearLog() {
        _logList.value = listOf()
    }

    suspend fun loadHistoryLog() {
        val result = logRepository.loadLogsBefore(_logList.value.first(), historyLoadSize.first())
        currentHistorySize += result.size
        _logList.value = result + _logList.value
    }

    private fun onLogInsert(logItem: LogItem) {
        viewModelScope.launch {
            compressLog()
            _logList.value = _logList.value + logItem
        }
    }

    private fun subscribeLogInsert() {
        logRepository.addLogInsertListener(logInsertListener)
    }

    private fun attackLogDisplayChanged() {
        viewModelScope.launch {
            snapshotFlow { LogQueryConfig(
                    containBotMessageLog = logDisplayState.containBotMessageLog.value,
                    containMclLog = logDisplayState.containMclLog.value,
                    containScriptOutput = logDisplayState.containScriptOutput.value,
                    containBotNetLog = logDisplayState.containBotNetLog.value
                )
            }.distinctUntilChanged().collect {
                clearLog()
                initializeLogList()
            }
        }
    }

    private fun initializeLogList() {
        viewModelScope.launch {
            val result = logRepository.loadLogs(
                containBotMessageLog = logDisplayState.containBotMessageLog.value,
                containBotNetLog = logDisplayState.containBotNetLog.value,
                containScriptOutput = logDisplayState.containScriptOutput.value,
                containMclLog = logDisplayState.containMclLog.value,
                limits = initialListSize.first().toLong()
            )
            _logList.value = result + _logList.value
        }
    }

    private suspend fun compressLog() {
        if (_logList.value.size <= maximumListSize.first() + currentHistorySize) return
        _logList.value = _logList.value.takeLast(compressedListSize.first() + currentHistorySize)
    }

    override fun onCleared() {
        logRepository.removeLogInsertListener(logInsertListener)
        super.onCleared()
    }
}