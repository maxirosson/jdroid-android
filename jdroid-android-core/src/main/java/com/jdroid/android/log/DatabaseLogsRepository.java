package com.jdroid.android.log;

import android.content.ContentValues;
import android.database.Cursor;

import com.jdroid.android.sqlite.repository.SQLiteRepository;
import com.jdroid.android.sqlite.Column;
import com.jdroid.android.sqlite.SQLiteHelper;

import java.util.Date;

public class DatabaseLogsRepository extends SQLiteRepository<DatabaseLog> {

	public DatabaseLogsRepository(SQLiteHelper dbHelper) {
		super(dbHelper);
	}

	/**
	 * @see SQLiteRepository#getTableName()
	 */
	@Override
	protected String getTableName() {
		return "Log";
	}

	/**
	 * @see SQLiteRepository#getColumns()
	 */
	@Override
	protected Column[] getColumns() {
		return DatabaseLogsColumns.values();
	}

	/**
	 * @see SQLiteRepository#createObjectFromCursor(Cursor)
	 */
	@Override
	protected DatabaseLog createObjectFromCursor(Cursor cursor) {
		DatabaseLog log = new DatabaseLog();
		log.setId((String)DatabaseLogsColumns.ID.readValue(cursor));
		log.setMessage(DatabaseLogsColumns.MESSAGE.readValue(cursor).toString());
		log.setDateTime((Date)DatabaseLogsColumns.DATE_TIME.readValue(cursor));
		return log;
	}

	@Override
	protected ContentValues createContentValuesFromObject(DatabaseLog item) {
		ContentValues values = new ContentValues();
		DatabaseLogsColumns.ID.addValue(values, item.getId());
		DatabaseLogsColumns.MESSAGE.addValue(values, item.getMessage());
		DatabaseLogsColumns.DATE_TIME.addValue(values, item.getDateTime());
		return values;
	}
	
}
