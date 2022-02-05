package com.ooooonly.lma.datastore.dao

import androidx.room.*
import com.ooooonly.lma.datastore.entity.BotItem

@Dao
interface BotDao {
    @Query("SELECT * FROM bot")
    suspend fun loadAllBots(): Array<BotItem>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveBots(vararg bots: BotItem)

    @Delete
    suspend fun deleteBots(vararg bots: BotItem)
}