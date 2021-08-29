package com.ooooonly.lma.data.converters

import androidx.room.TypeConverter
import com.ooooonly.luaMirai.base.ScriptLang

class ScriptEntityConverter {
    @TypeConverter
    fun fromString(value: String?): ScriptLang? = when(value) {
        ScriptLang.Lua.langName -> ScriptLang.Lua
        else -> null
    }

    @TypeConverter
    fun stringToStringLang(lang: ScriptLang?): String? = lang?.langName
}