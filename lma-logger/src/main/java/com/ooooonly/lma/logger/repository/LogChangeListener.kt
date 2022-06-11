package com.ooooonly.lma.logger.repository

interface LogChangeListener {
    fun onClear()
    fun onInsert(vararg logs: Log)
}
