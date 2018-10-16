package com.jdroid.android.room;

import com.jdroid.android.application.AbstractApplication;
import com.jdroid.java.collections.Lists;
import com.jdroid.java.collections.Maps;

import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;


public class RoomHelper {

	private final static String DEFAULT_DATABASE_NAME = "application";

	private static Map<String, RoomDatabase> roomDatabasesMap = Maps.newHashMap();

	private static List<Migration> migrations = Lists.newArrayList();

	public static <T extends RoomDatabase> T getDefaultDatabase(@NonNull Class<T> klass) {
		return getDatabase(klass, DEFAULT_DATABASE_NAME);
	}

	public static <T extends RoomDatabase> T getDatabase(@NonNull Class<T> klass, @NonNull String name) {
		if (roomDatabasesMap.containsKey(name)) {
			return (T) roomDatabasesMap.get(name);
		} else {
			RoomDatabase.Builder<T> builder = Room.databaseBuilder(AbstractApplication.get(), klass, name);
			builder.addMigrations(migrations.toArray(migrations.toArray(new Migration[]{})));
			T db = builder.build();
			roomDatabasesMap.put(name, db);
			return db;
		}
	}

	public static void addMigration(Migration migration) {
		RoomHelper.migrations.add(migration);
	}
}
