package com.ooooonly.lma.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "log")
data class LogEntity(
    @PrimaryKey(autoGenerate = true) var id: Long? = null,
    var from: Int = FROM_BOT_PRIMARY,
    var level: Int = LEVEL_INFO,
    var identity: String = "",
    var content: String = "",
    var date: Date = Date()
) {
    companion object {
        const val LEVEL_INFO = 0
        const val LEVEL_ERROR = 1

        const val FROM_BOT_PRIMARY = 0
        const val FROM_BOT_NETWORK = 1
        const val FROM_SCRIPT = 2
        const val FROM_MCL = 3
    }
}