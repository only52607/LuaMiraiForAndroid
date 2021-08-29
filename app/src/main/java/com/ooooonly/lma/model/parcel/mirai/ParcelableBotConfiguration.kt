package com.ooooonly.lma.model.parcel.mirai

import android.os.Parcelable
import com.ooooonly.lma.model.parcel.ParcelableAdapter
import kotlinx.parcelize.Parcelize
import net.mamoe.mirai.utils.BotConfiguration

@Parcelize
class ParcelableBotConfiguration(
    val deviceInfo: ParcelableDeviceInfo,
    val protocol: ParcelableMiraiProtocol
) : Parcelable, ParcelableAdapter<BotConfiguration> {
    override fun read(): BotConfiguration {
        return BotConfiguration {
            deviceInfo = { this@ParcelableBotConfiguration.deviceInfo.read() }
            protocol = this@ParcelableBotConfiguration.protocol.read()
        }
    }
    constructor(configuration: BotConfiguration):
            this(ParcelableDeviceInfo(random = true), ParcelableMiraiProtocol(configuration.protocol))
}