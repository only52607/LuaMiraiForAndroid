package com.ooooonly.lma.script.impl

import com.ooooonly.lma.datastore.dao.ScriptDao
import com.ooooonly.lma.datastore.entity.ScriptItem
import com.ooooonly.lma.script.ScriptRepository
import javax.inject.Inject

class ScriptRepositoryImpl @Inject constructor(
    private val scriptDao: ScriptDao
): ScriptRepository {
    override suspend fun loadScripts(): List<ScriptItem> {
        return scriptDao.selectAllScripts()
    }

    override suspend fun saveScript(script: ScriptItem) {
        script.id = scriptDao.saveScript(script)
    }

    override suspend fun removeScript(script: ScriptItem) {
        scriptDao.deleteScript(script)
    }
}