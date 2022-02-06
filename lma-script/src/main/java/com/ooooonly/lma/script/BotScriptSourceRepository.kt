package com.ooooonly.lma.script

import com.github.only52607.luamirai.core.script.BotScriptSource
import com.ooooonly.lma.datastore.entity.ScriptItem
import java.io.File
import java.net.URL

interface BotScriptSourceRepository {

}


fun ScriptItem.toBotScriptSource(): BotScriptSource =
    when(type) {
        ScriptItem.TYPE_FILE -> BotScriptSource.FileSource(File(source), lang)
        ScriptItem.TYPE_URL -> BotScriptSource.URLSource(URL(source), lang)
        ScriptItem.TYPE_CONTENT -> BotScriptSource.StringSource(source, lang)
        else -> error("Unknown source type $type")
    }

fun BotScriptSource.toScriptEntity() = when(this) {
    is BotScriptSource.FileSource -> ScriptItem(source = file.path, enabled = false, type = ScriptItem.TYPE_FILE, lang = scriptLang)
    is BotScriptSource.URLSource -> ScriptItem(source = url.path, enabled = false, type = ScriptItem.TYPE_URL, lang = scriptLang)
    is BotScriptSource.StringSource -> ScriptItem(source = content, enabled = false, type = ScriptItem.TYPE_CONTENT, lang = scriptLang)
    else -> error("Unsupported BotScriptSource $this")
}