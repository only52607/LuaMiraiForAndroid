package com.ooooonly.lma.data.dao

import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery
import com.ooooonly.lma.model.entity.LogEntity


@Dao
interface LogDao {
    @Query("SELECT * FROM log")
    suspend fun loadAllLogs(): Array<LogEntity>

    @Query("SELECT * FROM log order by id desc limit :count")
    suspend fun loadLastNLogs(count: Int): Array<LogEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveLog(logs: LogEntity): Long

    @Delete
    suspend fun deleteLog(log: LogEntity)

    @Query("delete from log where 1=1")
    suspend fun deleteAllLog()

    @RawQuery
    fun getLogs(query: SupportSQLiteQuery): Array<LogEntity>
}