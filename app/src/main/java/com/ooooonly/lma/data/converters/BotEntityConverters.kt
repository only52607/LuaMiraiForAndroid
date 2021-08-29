package com.ooooonly.lma.data.converters

import androidx.room.TypeConverter
import com.ooooonly.lma.utils.fromJsonString
import com.ooooonly.lma.utils.toJsonString
import net.mamoe.mirai.utils.BotConfiguration
import net.mamoe.mirai.utils.DeviceInfo

class BotEntityConverters {
    @TypeConverter
    fun fromDeviceInfoString(value: String?): DeviceInfo? {
        if (value == null) return null
        return DeviceInfo.fromJsonString(value)
    }

    @TypeConverter
    fun toDeviceInfoString(deviceInfo: DeviceInfo?): String? {
        return deviceInfo?.toJsonString()
    }

    @TypeConverter
    fun fromMiraiProtocolInt(value: String?): BotConfiguration.MiraiProtocol? {
        if (value == null) return null
        return when (value) {
            "ANDROID_PHONE" -> BotConfiguration.MiraiProtocol.ANDROID_PHONE
            "ANDROID_PAD" -> BotConfiguration.MiraiProtocol.ANDROID_PAD
            "ANDROID_WATCH" -> BotConfiguration.MiraiProtocol.ANDROID_WATCH
            else -> BotConfiguration.MiraiProtocol.ANDROID_PHONE
        }
    }

    @TypeConverter
    fun toMiraiProtocolString(protocol: BotConfiguration.MiraiProtocol?): String? {
        return when (protocol) {
            BotConfiguration.MiraiProtocol.ANDROID_PHONE -> "ANDROID_PHONE"
            BotConfiguration.MiraiProtocol.ANDROID_PAD -> "ANDROID_PAD"
            BotConfiguration.MiraiProtocol.ANDROID_WATCH -> "ANDROID_WATCH"
            else -> null
        }
    }
}