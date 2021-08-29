package com.ooooonly.lma.data.serializers

import com.ooooonly.luaMirai.lua.LuaMiraiScript
import com.ooooonly.luaMirai.lua.LuaSource
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.*
import net.mamoe.mirai.utils.MiraiInternalApi

@OptIn(MiraiInternalApi::class)
class LuaMiraiScriptSerializer : KSerializer<LuaMiraiScript> {
    companion object {
        val instance by lazy {
            LuaMiraiScriptSerializer()
        }
    }

    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("LuaSource") {
        element<Boolean>("enable")
        element("source", LuaSourceSerializer.instance.descriptor)
    }

    override fun serialize(encoder: Encoder, value: LuaMiraiScript) {
        encoder.encodeStructure(descriptor) {
            encodeBooleanElement(descriptor, 0, value.isLoaded)
            encodeSerializableElement(
                descriptor,
                1,
                LuaSourceSerializer.instance,
                value.source
            )
        }
    }

    override fun deserialize(decoder: Decoder): LuaMiraiScript {
        return decoder.decodeStructure(descriptor) {
            var enable = false
            var source: LuaSource? = null
            while (true) {
                when (val index = decodeElementIndex(descriptor)) {
                    0 -> enable = decodeBooleanElement(descriptor, 0)
                    1 -> source =
                        decodeSerializableElement(descriptor, 1, LuaSourceSerializer.instance)
                    CompositeDecoder.DECODE_DONE -> break
                    else -> error("Unexpected index: $index")
                }
            }
            if (source == null) error("Unexpected script")
            LuaMiraiScript(source).also {
                // if (enable) kotlin.runCatching { it.load() }
            }
        }
    }
}
