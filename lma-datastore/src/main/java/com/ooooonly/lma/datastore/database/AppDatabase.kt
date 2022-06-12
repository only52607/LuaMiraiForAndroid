package com.ooooonly.lma.datastore.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ooooonly.lma.datastore.converters.DateConverter
import com.ooooonly.lma.datastore.dao.BotDao
import com.ooooonly.lma.datastore.dao.LogDao
import com.ooooonly.lma.datastore.dao.ScriptDao
import com.ooooonly.lma.datastore.entity.BotEntity
import com.ooooonly.lma.datastore.entity.LogEntity
import com.ooooonly.lma.datastore.entity.ScriptEntity

@Database(entities = [ScriptEntity::class, BotEntity::class, LogEntity::class], version = 2)
@TypeConverters(DateConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun scriptDao(): ScriptDao
    abstract fun botDao(): BotDao
    abstract fun logDao(): LogDao
}