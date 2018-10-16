package com.jdroid.android.sample.database.room;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = { SampleEntity.class, Sample2Entity.class }, version = 2)
public abstract class AppDatabase extends RoomDatabase {

	public abstract SampleEntityDao sampleEntityDao();

}
