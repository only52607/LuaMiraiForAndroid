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
) : ViewModel() {
    private data class LogQueryConfig(
        val containBotMessageLog: Boolean,
        val containBotNetLog: Boolean,
        val containMclLog: Boolean,
        val containScriptOutput: Boolean
    )

    private val _logList = mutableStateListOf<LogItem>()
    val logList: List<LogItem> = _logList

    val logDisplayState = LogDisplayState()

    private var currentHistorySize = 0
    private var initialListSize =
        logDataStore.data.map { it[intPreferencesKey("log_initial_size")] ?: 200 }
    private var maximumListSize =
        logDataStore.data.map { it[intPreferencesKey("log_maximum_size")] ?: 500 }
    private var compressedListSize =
        logDataStore.data.map { it[intPreferencesKey("log_compressed_size")] ?: 200 }
    private val historyLoadSize =
        logDataStore.data.map { it[intPreferencesKey("log_load_history_size")] ?: 100 }

    init {
        initializeLogList()
        collectComingLogs()
        attackLogDisplayChanged()
    }

    fun clearLog() {
        _logList.clear()
    }

    suspend fun loadHistoryLog() {
        if (_logList.isEmpty()) return
        val result = logRepository.loadLogsBefore(_logList.first(), historyLoadSize.first())
        currentHistorySize += result.size
        _logList.addAll(0, result)
    }

    private fun collectComingLogs() {
        viewModelScope.launch {
            logRepository.logFlow.filter {
                it.from == LogItem.FROM_BOT_PRIMARY && logDisplayState.containBotMessageLog.value ||
                        it.from == LogItem.FROM_BOT_NETWORK && logDisplayState.containBotNetLog.value ||
                        it.from == LogItem.FROM_MCL && logDisplayState.containMclLog.value ||
                        it.from == LogItem.FROM_SCRIPT && logDisplayState.containScriptOutput.value
            }.collect {
                compressLog()
                _logList.add(it)
            }
        }
    }

    private fun attackLogDisplayChanged() {
        viewModelScope.launch {
            snapshotFlow {
                LogQueryConfig(
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
            _logList.addAll(0, result)
        }
    }

    private suspend fun compressLog() {
        if (_logList.size <= maximumListSize.first() + currentHistorySize) return
        val reserve = compressedListSize.first() + currentHistorySize
        _logList.removeRange(0, _logList.size - reserve - 1)
    }
}