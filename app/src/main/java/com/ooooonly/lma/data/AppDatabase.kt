package com.ooooonly.lma.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ooooonly.lma.data.converters.BotEntityConverters
import com.ooooonly.lma.data.converters.DateConverter
import com.ooooonly.lma.data.converters.ScriptEntityConverter
import com.ooooonly.lma.data.dao.BotDao
import com.ooooonly.lma.data.dao.LogDao
import com.ooooonly.lma.data.dao.ScriptDao
import com.ooooonly.lma.model.entity.BotEntity
import com.ooooonly.lma.model.entity.LogEntity
import com.ooooonly.lma.model.entity.ScriptEntity

@Database(entities = [ScriptEntity::class, BotEntity::class, LogEntity::class], version = 2)
@TypeConverters(BotEntityConverters::class, DateConverter::class, ScriptEntityConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun scriptDao(): ScriptDao
    abstract fun botDao(): BotDao
    abstract fun logDao(): LogDao
}