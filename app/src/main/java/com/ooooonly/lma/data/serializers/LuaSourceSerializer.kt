package com.ooooonly.lma.data.serializers

import com.ooooonly.luaMirai.lua.LuaSource
import com.ooooonly.luaMirai.lua.LuaSourceFactory
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.*
import net.mamoe.mirai.utils.MiraiInternalApi
import java.io.File
import java.net.URL

@Serializer(forClass = LuaSource::class)
@OptIn(MiraiInternalApi::class, kotlinx.serialization.ExperimentalSerializationApi::class)
class LuaSourceSerializer : KSerializer<LuaSource> {
    companion object {
        val instance by lazy {
            LuaSourceSerializer()
        }
    }

    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("LuaSource") {
        element<String>("type")
        element<String>("data")
    }

    override fun serialize(encoder: Encoder, value: LuaSource) {
        encoder.encodeStructure(descriptor) {
            when (value) {
                is LuaSource.LuaFileSource -> {
                    encodeStringElement(descriptor, 0, "file")
                    encodeStringElement(descriptor, 1, value.file.canonicalPath)
                }
                is LuaSource.LuaContentSource -> {
                    encodeStringElement(descriptor, 0, "content")
                    encodeStringElement(descriptor, 1, value.content)
                }
                is LuaSource.LuaURLSource -> {
                    encodeStringElement(descriptor, 0, "url")
                    encodeStringElement(descriptor, 1, value.url.toString())
                }
            }
        }
    }

    override fun deserialize(decoder: Decoder): LuaSource {
        return decoder.decodeStructure(descriptor) {
            var type = "file"
            var data = ""
            while (true) {
                when (val index = decodeElementIndex(descriptor)) {
                    0 -> type = decodeStringElement(descriptor, 0)
                    1 -> data = decodeStringElement(descriptor, 1)
                    CompositeDecoder.DECODE_DONE -> break
                    else -> error("Unexpected index: $index")
                }
            }
            runBlocking {
                when (type) {
                    "file" -> LuaSourceFactory.buildSource(File(data))
                    "content" -> LuaSourceFactory.buildSource(File(data))
                    "url" -> LuaSourceFactory.buildSource(File(data))
                    else -> error("Unexpected type: $type")
                }
            }
        }
    }
}