package com.jdroid.android.sample.database.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.jdroid.android.room.converters.DateConverter
import com.jdroid.android.room.converters.StringListConverter

@Database(entities = [SampleEntity::class, Sample2Entity::class], version = 1)
@TypeConverters(DateConverter::class, StringListConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun sampleEntityDao(): SampleEntityDao
}
