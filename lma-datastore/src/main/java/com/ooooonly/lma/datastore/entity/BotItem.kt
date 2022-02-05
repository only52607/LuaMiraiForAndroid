package com.ooooonly.lma.datastore.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import net.mamoe.mirai.utils.BotConfiguration
import net.mamoe.mirai.utils.DeviceInfo

@Entity(tableName = "bot")
data class BotItem(
    @PrimaryKey val id: Long,
    val password: String,
    @ColumnInfo(name = "device_info")
    val deviceInfo: DeviceInfo,
    val protocol: BotConfiguration.MiraiProtocol,
    @ColumnInfo(name = "auto_login")
    val autoLogin: Boolean
)