package com.ooooonly.lma.datastore.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "log")
data class LogEntity(
    @PrimaryKey(autoGenerate = true) var id: Long? = null,
    val from: Int = FROM_BOT_PRIMARY,
    val level: Int = LEVEL_INFO,
    val identity: String = "",
    val content: String = "",
    val date: Date = Date()
) {
    companion object {
        const val LEVEL_INFO = 0
        const val LEVEL_ERROR = 1
        const val LEVEL_DEBUG = 2
        const val LEVEL_WARNING = 3

        const val FROM_BOT_PRIMARY = 0
        const val FROM_BOT_NETWORK = 1
        const val FROM_SCRIPT = 2
        const val FROM_MCL = 3
    }
}