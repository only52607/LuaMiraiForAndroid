package com.ooooonly.lma.datastore.converters

import androidx.room.TypeConverter
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
            "IPAD" -> BotConfiguration.MiraiProtocol.IPAD
            "MACOS" -> BotConfiguration.MiraiProtocol.MACOS
            else -> BotConfiguration.MiraiProtocol.ANDROID_PHONE
        }
    }

    @TypeConverter
    fun toMiraiProtocolString(protocol: BotConfiguration.MiraiProtocol?): String? {
        return when (protocol) {
            BotConfiguration.MiraiProtocol.ANDROID_PHONE -> "ANDROID_PHONE"
            BotConfiguration.MiraiProtocol.ANDROID_PAD -> "ANDROID_PAD"
            BotConfiguration.MiraiProtocol.ANDROID_WATCH -> "ANDROID_WATCH"
            BotConfiguration.MiraiProtocol.IPAD -> "IPAD"
            BotConfiguration.MiraiProtocol.MACOS -> "MACOS"
            else -> null
        }
    }
}