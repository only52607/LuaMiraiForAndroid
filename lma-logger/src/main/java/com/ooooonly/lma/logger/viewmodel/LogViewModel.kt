package com.ooooonly.lma.logger.viewmodel

import androidx.compose.runtime.*
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ooooonly.lma.logger.LogType
import com.ooooonly.lma.logger.repository.Log
import com.ooooonly.lma.logger.repository.LogChangeListener
import com.ooooonly.lma.logger.repository.LogRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LogViewModel @Inject constructor(
    private val logRepository: LogRepository,
    logDataStore: DataStore<Preferences>
) : ViewModel() {
    private val LogItemVO.logValue: Log
        get() = Log(
            id = id,
            type = type,
            identity = identity,
            content = content,
            level = level,
            date = date
        )

    private val Log.voValue: LogItemVO
        get() = LogItemVO(
            id = id!!,
            type = type,
            identity = identity,
            content = content,
            level = level,
            date = date
        )

    private val _logList = mutableStateListOf<LogItemVO>()
    val logList: List<LogItemVO> = _logList

    private val _logFilterConfig = MutableStateFlow(LogFilterConfig())
    val logFilterConfig: StateFlow<LogFilterConfig> = _logFilterConfig

    private var currentHistorySize = 0

    private var initialListSize =
        logDataStore.data.map { it[intPreferencesKey("log_initial_size")] ?: 200 }

    private var maximumListSize =
        logDataStore.data.map { it[intPreferencesKey("log_maximum_size")] ?: 500 }

    private var compressedListSize =
        logDataStore.data.map { it[intPreferencesKey("log_compressed_size")] ?: 200 }

    private val historyLoadSize =
        logDataStore.data.map { it[intPreferencesKey("log_load_history_size")] ?: 100 }

    private fun initializeLogList() {
        viewModelScope.launch {
            val filterConfig = _logFilterConfig.value
            val size = initialListSize.first()
            val result = logRepository.loadLogs(
                containBotMessageLog = filterConfig.containBotMessageLog,
                containBotNetLog = filterConfig.containBotNetLog,
                containScriptOutput = filterConfig.containScriptOutput,
                containMclLog = filterConfig.containMclLog,
                limits = size.toLong()
            )
            _logList.addAll(0, result.map { it.voValue })
        }
    }

    init {
        initializeLogList()
        listenLogInsert()
        listenLogDisplayChanged()
    }

    fun clearLog() {
        _logList.clear()
    }

    suspend fun setLogFilterConfig(logFilterConfig: LogFilterConfig) {
        _logFilterConfig.emit(logFilterConfig)
    }

    suspend fun loadMoreHistoryLog() {
        if (_logList.isEmpty()) return
        val result = logRepository.loadLogsBefore(_logList.first().logValue, historyLoadSize.first())
        currentHistorySize += result.size
        _logList.addAll(0, result.map { it.voValue })
    }

    private fun listenLogInsert() {
        logRepository.addListener(object: LogChangeListener {
            override fun onClear() {
                _logList.clear()
            }

            override fun onInsert(vararg logs: Log) {
                viewModelScope.launch {
                    val filterConfig = _logFilterConfig.value
                    logs.filter {
                        it.type == LogType.Bot && filterConfig.containBotMessageLog ||
                        it.type == LogType.Network && filterConfig.containBotNetLog ||
                        it.type == LogType.MiraiConsole && filterConfig.containMclLog ||
                        it.type == LogType.Script && filterConfig.containScriptOutput
                    }.forEach {
                        _logList.add(it.voValue)
                    }
                    compressLog()
                }
            }
        })
    }

    private fun listenLogDisplayChanged() {
        viewModelScope.launch {
            _logFilterConfig.distinctUntilChanged { old, new ->
                old == new
            }.collect {
                clearLog()
                initializeLogList()
            }
        }
    }

    private suspend fun compressLog() {
        if (_logList.size <= maximumListSize.first() + currentHistorySize) return
        val reserve = compressedListSize.first() + currentHistorySize
        _logList.removeRange(0, _logList.size - reserve - 1)
    }
}