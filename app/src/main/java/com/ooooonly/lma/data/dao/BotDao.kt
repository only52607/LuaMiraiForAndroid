package com.ooooonly.lma.data.dao

import androidx.room.*
import com.ooooonly.lma.model.entity.BotEntity

@Dao
interface BotDao {
    @Query("SELECT * FROM bot")
    suspend fun loadAllBots(): Array<BotEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveBots(vararg bots: BotEntity)

    @Delete
    suspend fun deleteBots(vararg bots: BotEntity)
}