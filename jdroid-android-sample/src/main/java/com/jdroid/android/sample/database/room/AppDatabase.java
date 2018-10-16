package com.jdroid.android.sample.database.room;

import com.jdroid.android.room.converters.DateConverter;
import com.jdroid.android.room.converters.StringListConverter;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = { SampleEntity.class, Sample2Entity.class }, version = 1)
@TypeConverters({DateConverter.class, StringListConverter.class })
public abstract class AppDatabase extends RoomDatabase {

	public abstract SampleEntityDao sampleEntityDao();

}
