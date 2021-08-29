package com.ooooonly.lma.model.parcel.mirai

import android.os.Parcelable
import com.ooooonly.lma.model.parcel.ParcelableAdapter
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.json.Json
import net.mamoe.mirai.utils.DeviceInfo

@Parcelize
class ParcelableDeviceInfo(
    val random: Boolean = true,
    val deviceInfoData: String = ""
): Parcelable, ParcelableAdapter<DeviceInfo> {
    companion object {
        val json by lazy {
            Json
        }
    }

    override fun read(): DeviceInfo {
        if (random) return DeviceInfo.random()
        return json.decodeFromString(DeviceInfo.serializer(), deviceInfoData)
    }
    
    constructor(deviceInfo: DeviceInfo): this(false,json.encodeToString(DeviceInfo.serializer(), deviceInfo))
}