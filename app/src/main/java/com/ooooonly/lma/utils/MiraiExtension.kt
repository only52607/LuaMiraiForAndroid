package com.ooooonly.lma.utils

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import net.mamoe.mirai.Bot
import net.mamoe.mirai.utils.DeviceInfo

@Serializable
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

fun String.decodeHex(): ByteArray {
    check(length % 2 == 0) { "Must have an even length" }
    return chunked(2)
        .map { it.toInt(16).toByte() }
        .toByteArray()
}

fun ByteArray.toHex(): String = joinToString(separator = "") { eachByte -> "%02x".format(eachByte) }

@Serializable
class VisualDeviceInfo(
    val display: String,
    val product: String,
    val device: String,
    val board: String,
    val brand: String,
    val model: String,
    val bootloader: String,
    val fingerprint: String,
    val bootId: String,
    val procVersion: String,
    val baseBand: String,
    val version: VisualVersion,
    val simInfo: String,
    val osType: String,
    val macAddress: String,
    val wifiBSSID: String,
    val wifiSSID: String,
    val imsiMd5: String,
    val imei: String,
    val apn: String
) {
    constructor(deviceInfo: DeviceInfo): this(
        display = String(deviceInfo.display),
        product = String(deviceInfo.product),
        device = String(deviceInfo.device),
        board = String(deviceInfo.board),
        brand = String(deviceInfo.brand),
        model = String(deviceInfo.model),
        bootloader = String(deviceInfo.bootloader),
        fingerprint = String(deviceInfo.fingerprint),
        bootId = String(deviceInfo.bootId),
        procVersion = String(deviceInfo.procVersion),
        baseBand = String(deviceInfo.baseBand),
        version = deviceInfo.version.visualize(),
        simInfo = String(deviceInfo.simInfo),
        osType = String(deviceInfo.osType),
        macAddress = String(deviceInfo.macAddress),
        wifiBSSID = String(deviceInfo.wifiBSSID),
        wifiSSID = String(deviceInfo.wifiSSID),
        imsiMd5 = deviceInfo.imsiMd5.toHex(),
        imei = deviceInfo.imei,
        apn = String(deviceInfo.apn)
    )

    fun toDeviceInfo() = DeviceInfo(
        display = display.toByteArray(),
        product = product.toByteArray(),
        device = device.toByteArray(),
        board = board.toByteArray(),
        brand = brand.toByteArray(),
        model = model.toByteArray(),
        bootloader = bootloader.toByteArray(),
        fingerprint = fingerprint.toByteArray(),
        bootId = bootId.toByteArray(),
        procVersion = procVersion.toByteArray(),
        baseBand = baseBand.toByteArray(),
        version = version.toVersion(),
        simInfo = simInfo.toByteArray(),
        osType = osType.toByteArray(),
        macAddress = macAddress.toByteArray(),
        wifiBSSID = wifiBSSID.toByteArray(),
        wifiSSID = wifiSSID.toByteArray(),
        imsiMd5 = imsiMd5.decodeHex(),
        imei = imei,
        apn = apn.toByteArray()
    )
}

fun DeviceInfo.visualize() = VisualDeviceInfo(this)

private val json by lazy {
    Json { prettyPrint = true }
}

fun DeviceInfo.toJsonString(): String {
    return json.encodeToString(VisualDeviceInfo.serializer(), this.visualize())
}

fun DeviceInfo.Companion.fromJsonString(jsonString: String): DeviceInfo = try {
    json.decodeFromString(VisualDeviceInfo.serializer(), jsonString).toDeviceInfo()
} catch (e: Exception) {
    random()
}

val Bot.nickOrNull: String?
    get() = try {
        bot.nick
    } catch (e: Exception) {
        null
    }

val Bot.avatarUrlOrNull: String?
    get() = try {
        bot.avatarUrl
    } catch (e: Exception) {
        null
    }

fun getAvatarUrlById(id: Long): String {
    return "https://q.qlogo.cn/headimg_dl?bs=qq&dst_uin=$id&src_uin=www.jlwz.cn&fid=blog&spec=100"
}

val Bot.avatarUrlStatic: String
    get() = getAvatarUrlById(id)