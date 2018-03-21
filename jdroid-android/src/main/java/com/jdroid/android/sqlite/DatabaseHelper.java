package com.jdroid.android.sqlite;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.jdroid.android.application.AbstractApplication;
import com.jdroid.java.collections.Sets;
import com.jdroid.java.utils.LoggerUtils;

import org.slf4j.Logger;

import java.util.Set;

public class DatabaseHelper extends SQLiteOpenHelper {
	
	private static final Logger LOGGER = LoggerUtils.getLogger(DatabaseHelper.class);
	
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "database";
	
	private static final DatabaseHelper INSTANCE = new DatabaseHelper();
	private Set<String> createSQLs = Sets.newHashSet();
	private Set<String> dropSQLs = Sets.newHashSet();
	
	public static DatabaseHelper get() {
		return INSTANCE;
	}
	
	private DatabaseHelper() {
		super(AbstractApplication.get(), DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	public void addCreateSQL(String sql) {
		createSQLs.add(sql);
	}
	
	public void addDropSQL(String sql) {
		dropSQLs.add(sql);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		for (String createSQL : createSQLs) {
			db.execSQL(createSQL);
		}
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		LOGGER.warn("Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
		for (String dropSQL : dropSQLs) {
			db.execSQL(dropSQL);
		}
		onCreate(db);
	}
}