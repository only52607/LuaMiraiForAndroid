package com.ooooonly.lma.datastore.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bot")
data class BotEntity(
    @PrimaryKey val id: Long,
    val password: String,
    @ColumnInfo(name = "device_info")
    val deviceInfo: String,
    val protocol: String,
    @ColumnInfo(name = "auto_login")
    val autoLogin: Boolean
)