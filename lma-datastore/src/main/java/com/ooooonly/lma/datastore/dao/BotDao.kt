package com.ooooonly.lma.datastore.dao

import androidx.room.*
import com.ooooonly.lma.datastore.entity.BotEntity

@Dao
interface BotDao {
    @Query("SELECT * FROM bot")
    suspend fun selectAllBots(): Array<BotEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveBots(vararg bots: BotEntity)

    @Delete
    suspend fun deleteBots(vararg bots: BotEntity)

    @Query("SELECT * FROM bot where id=:id")
    suspend fun getBotById(id: Long): BotEntity
}