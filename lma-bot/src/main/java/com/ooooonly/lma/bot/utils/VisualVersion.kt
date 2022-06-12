package com.ooooonly.lma.bot.utils

import kotlinx.serialization.json.Json
import net.mamoe.mirai.utils.DeviceInfo

private val json by lazy {
    Json { prettyPrint = true }
}

@kotlinx.serialization.Serializable
class VisualVersion(
    val incremental: String,
    val release: String,
    val codename: String,
    val sdk: Int
){
    constructor(version: DeviceInfo.Version):this(
        incremental = String(version.incremental),
        release = String(version.release),
        codename = String(version.codename),
        sdk = version.sdk
    )

    fun toVersion(): DeviceInfo.Version = DeviceInfo.Version(
        incremental = incremental.toByteArray(),
        release = release.toByteArray(),
        codename = codename.toByteArray(),
        sdk = sdk
    )
}

fun DeviceInfo.Version.visualize() = VisualVersion(this)

fun DeviceInfo.toJsonString(): String {
    return json.encodeToString(VisualDeviceInfo.serializer(), this.visualize())
}

fun DeviceInfo.Companion.fromJsonString(jsonString: String): DeviceInfo = try {
    json.decodeFromString(VisualDeviceInfo.serializer(), jsonString).toDeviceInfo()
} catch (e: Exception) {
    random()
}