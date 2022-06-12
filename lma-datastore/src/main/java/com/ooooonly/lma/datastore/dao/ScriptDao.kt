package com.ooooonly.lma.datastore.dao

import androidx.room.*
import com.ooooonly.lma.datastore.entity.ScriptEntity

@Dao
interface ScriptDao {
    @Query("SELECT * FROM script")
    suspend fun selectAllScripts(): List<ScriptEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveScript(scripts: ScriptEntity): Long

    @Delete
    suspend fun deleteScript(scripts: ScriptEntity)
}