package com.ooooonly.lma.datastore.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ooooonly.lma.datastore.converters.BotEntityConverters
import com.ooooonly.lma.datastore.converters.DateConverter
import com.ooooonly.lma.datastore.converters.ScriptEntityConverter
import com.ooooonly.lma.datastore.dao.BotDao
import com.ooooonly.lma.datastore.dao.LogDao
import com.ooooonly.lma.datastore.dao.ScriptDao
import com.ooooonly.lma.datastore.entity.BotItem
import com.ooooonly.lma.datastore.entity.LogItem
import com.ooooonly.lma.datastore.entity.ScriptItem

@Database(entities = [ScriptItem::class, BotItem::class, LogItem::class], version = 2)
@TypeConverters(BotEntityConverters::class, DateConverter::class, ScriptEntityConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun scriptDao(): ScriptDao
    abstract fun botDao(): BotDao
    abstract fun logDao(): LogDao
}