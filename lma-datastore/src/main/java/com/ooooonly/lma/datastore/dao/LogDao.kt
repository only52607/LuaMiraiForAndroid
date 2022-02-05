package com.ooooonly.lma.datastore.dao

import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery
import com.ooooonly.lma.datastore.entity.LogItem
import kotlinx.coroutines.flow.Flow


@Dao
interface LogDao {
    @Query("SELECT * FROM log")
    fun loadAllLogs(): Flow<List<LogItem>>

    @Query("SELECT * FROM log order by id desc limit :count")
    fun loadLastNLogs(count: Int): Flow<List<LogItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveLog(logs: LogItem): Long

    @Delete
    suspend fun deleteLogs(vararg log: LogItem)

    @Query("delete from log where 1=1")
    suspend fun deleteAllLog()

    @RawQuery
    fun getLogs(query: SupportSQLiteQuery): Flow<List<LogItem>>
}