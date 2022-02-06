package com.ooooonly.lma.datastore.dao

import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery
import com.ooooonly.lma.datastore.entity.LogItem

@Dao
interface LogDao {
    @Query("SELECT * FROM log")
    suspend fun loadAllLogs(): List<LogItem>

    @Query("SELECT * FROM log order by id desc limit :count")
    suspend fun loadLastNLogs(count: Int): List<LogItem>

    @Query("SELECT * FROM log where id < :id order by id desc limit :count")
    suspend fun loadLogsBefore(id: Long, count: Int): List<LogItem>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveLog(logs: LogItem): Long

    @Delete
    suspend fun deleteLogs(vararg log: LogItem)

    @Query("delete from log where 1=1")
    suspend fun deleteAllLog()

    @RawQuery
    fun loadLogs(query: SupportSQLiteQuery): List<LogItem>
}