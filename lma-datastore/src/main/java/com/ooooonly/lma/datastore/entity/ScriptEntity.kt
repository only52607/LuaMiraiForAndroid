package com.ooooonly.lma.datastore.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "script")
data class ScriptEntity(
    @PrimaryKey(autoGenerate = true) var id: Long? = null,
    var source: String,
    var enabled: Boolean = false,
    var type: Int,
    var lang: String = "lua"
) {
    companion object {
        const val TYPE_FILE = 0
        const val TYPE_URL = 1
        const val TYPE_CONTENT = 2
    }
}