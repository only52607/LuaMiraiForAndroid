package com.ooooonly.lma.data.dao

import androidx.room.*
import com.ooooonly.lma.model.entity.ScriptEntity

@Dao
interface ScriptDao {
    @Query("SELECT * FROM script")
    suspend fun loadAllScripts(): Array<ScriptEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveScript(scripts: ScriptEntity): Long

    @Delete
    suspend fun deleteScript(scripts: ScriptEntity)
}