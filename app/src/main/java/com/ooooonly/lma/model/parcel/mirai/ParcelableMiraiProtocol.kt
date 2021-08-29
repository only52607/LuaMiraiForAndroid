package com.ooooonly.lma.model.parcel.mirai

import android.os.Parcelable
import com.ooooonly.lma.model.parcel.ParcelableAdapter
import kotlinx.parcelize.Parcelize
import net.mamoe.mirai.utils.BotConfiguration

@Parcelize
class ParcelableMiraiProtocol(
    private val typeId: Int
): Parcelable, ParcelableAdapter<BotConfiguration.MiraiProtocol> {
    companion object {
        const val ANDROID_PHONE = 0
        const val ANDROID_PAD = 1
        const val ANDROID_WATCH = 2
    }

    constructor(protocol: BotConfiguration.MiraiProtocol): this(when(protocol){
        BotConfiguration.MiraiProtocol.ANDROID_PHONE -> ANDROID_PHONE
        BotConfiguration.MiraiProtocol.ANDROID_PAD -> ANDROID_PAD
        BotConfiguration.MiraiProtocol.ANDROID_WATCH -> ANDROID_WATCH
    })

    override fun read(): BotConfiguration.MiraiProtocol {
        return when(typeId) {
            ANDROID_PHONE -> BotConfiguration.MiraiProtocol.ANDROID_PHONE
            ANDROID_PAD -> BotConfiguration.MiraiProtocol.ANDROID_PAD
            ANDROID_WATCH -> BotConfiguration.MiraiProtocol.ANDROID_WATCH
            else -> BotConfiguration.MiraiProtocol.ANDROID_PHONE
        }
    }
}