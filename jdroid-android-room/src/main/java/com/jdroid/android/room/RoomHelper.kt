package com.jdroid.android.room

import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import com.jdroid.android.application.AbstractApplication
import com.jdroid.java.collections.Lists
import com.jdroid.java.collections.Maps

object RoomHelper {

    private const val DEFAULT_DATABASE_NAME = "application.db"

    private val roomDatabasesMap = Maps.newHashMap<String, RoomDatabase>()

    private val migrations = Lists.newArrayList<Migration>()

    fun <T : RoomDatabase> getDefaultDatabase(klass: Class<T>): T {
        return getDatabase(klass, DEFAULT_DATABASE_NAME)
    }

    fun <T : RoomDatabase> getDatabase(klass: Class<T>, name: String): T {
        if (roomDatabasesMap.containsKey(name)) {
            return roomDatabasesMap[name] as T
        } else {
            val builder = Room.databaseBuilder(AbstractApplication.get(), klass, name)
            builder.addMigrations(*migrations.toTypedArray())
            val db = builder.build()
            roomDatabasesMap[name] = db
            return db
        }
    }

    fun addMigration(migration: Migration) {
        migrations.add(migration)
    }
}
