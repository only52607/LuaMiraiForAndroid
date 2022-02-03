package com.ooooonly.lma.datastore.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.github.only52607.luamirai.core.script.BotScriptSource
import com.ooooonly.lma.datastore.entity.ScriptEntity.Companion.TYPE_CONTENT
import com.ooooonly.lma.datastore.entity.ScriptEntity.Companion.TYPE_FILE
import com.ooooonly.lma.datastore.entity.ScriptEntity.Companion.TYPE_URL
import java.io.File
import java.net.URL

@Entity(tableName = "script")
data class ScriptEntity(
    @PrimaryKey(autoGenerate = true) var id: Long? = null,
    var source: String,
    var enabled: Boolean = false,
    var type: Int,
    var lang: String = "lua"
) {
    companion object {
        const val TYPE_FILE = 0
        const val TYPE_URL = 1
        const val TYPE_CONTENT = 2
    }
}

fun ScriptEntity.toBotScriptSource(): BotScriptSource =
    when(type) {
        TYPE_FILE -> BotScriptSource.FileSource(File(source), lang)
        TYPE_URL -> BotScriptSource.URLSource(URL(source), lang)
        TYPE_CONTENT -> BotScriptSource.StringSource(source, lang)
        else -> error("Unknown source type $type")
    }

fun BotScriptSource.toScriptEntity() = when(this) {
    is BotScriptSource.FileSource -> ScriptEntity(source = file.path, enabled = false, type = TYPE_FILE, lang = scriptLang)
    is BotScriptSource.URLSource -> ScriptEntity(source = url.path, enabled = false, type = TYPE_URL, lang = scriptLang)
    is BotScriptSource.StringSource -> ScriptEntity(source = content, enabled = false, type = TYPE_CONTENT, lang = scriptLang)
    else -> error("Unsupported BotScriptSource $this")
}