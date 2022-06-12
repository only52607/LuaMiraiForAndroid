package com.ooooonly.lma.datastore.dao

import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery
import com.ooooonly.lma.datastore.entity.LogEntity

@Dao
interface LogDao {
    @Query("SELECT * FROM log")
    suspend fun selectAllLogs(): List<LogEntity>

    @Query("SELECT * FROM log order by id desc limit :count")
    suspend fun selectLastNLogs(count: Int): List<LogEntity>

    @Query("SELECT * FROM log where id < :id order by id desc limit :count")
    suspend fun selectLogsBefore(id: Long, count: Int): List<LogEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveLog(logs: LogEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveLogs(logs: List<LogEntity>)

    @Delete
    suspend fun deleteLogs(vararg log: LogEntity)

    @Query("delete from log where 1=1")
    suspend fun deleteAllLog()

    @RawQuery
    fun selectLogs(query: SupportSQLiteQuery): List<LogEntity>
}