package com.ooooonly.lma.data.serializers

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import net.mamoe.mirai.utils.BotConfiguration

class MiraiProtocolSerializer : KSerializer<BotConfiguration.MiraiProtocol> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("MiraiProtocol", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: BotConfiguration.MiraiProtocol) {
        encoder.encodeString(
            when (value) {
                BotConfiguration.MiraiProtocol.ANDROID_PHONE -> "ANDROID_PHONE"
                BotConfiguration.MiraiProtocol.ANDROID_PAD -> "ANDROID_PAD"
                BotConfiguration.MiraiProtocol.ANDROID_WATCH -> "ANDROID_WATCH"
            }
        )
    }

    override fun deserialize(decoder: Decoder): BotConfiguration.MiraiProtocol {
        return when (decoder.decodeString()) {
            "ANDROID_PHONE" -> BotConfiguration.MiraiProtocol.ANDROID_PHONE
            "ANDROID_PAD" -> BotConfiguration.MiraiProtocol.ANDROID_PAD
            "ANDROID_WATCH" -> BotConfiguration.MiraiProtocol.ANDROID_WATCH
            else -> BotConfiguration.MiraiProtocol.ANDROID_PHONE
        }
    }
}