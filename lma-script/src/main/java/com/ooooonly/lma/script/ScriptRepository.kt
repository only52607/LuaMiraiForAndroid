package com.ooooonly.lma.script

import com.ooooonly.lma.datastore.entity.ScriptItem

interface ScriptRepository {
    suspend fun loadScripts(): List<ScriptItem>

    suspend fun saveScript(script: ScriptItem)

    suspend fun removeScript(script: ScriptItem)
}
