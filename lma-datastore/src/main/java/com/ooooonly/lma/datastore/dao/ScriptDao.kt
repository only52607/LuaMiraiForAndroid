package com.ooooonly.lma.datastore.dao

import androidx.room.*
import com.ooooonly.lma.datastore.entity.ScriptItem

@Dao
interface ScriptDao {
    @Query("SELECT * FROM script")
    suspend fun selectAllScripts(): List<ScriptItem>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveScript(scripts: ScriptItem): Long

    @Delete
    suspend fun deleteScript(scripts: ScriptItem)
}