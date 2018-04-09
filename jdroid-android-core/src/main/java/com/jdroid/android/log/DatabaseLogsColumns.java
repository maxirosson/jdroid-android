package com.jdroid.android.log;

import android.content.ContentValues;
import android.database.Cursor;

import com.jdroid.android.sqlite.Column;
import com.jdroid.android.sqlite.DataType;
import com.jdroid.android.sqlite.Reference;

public enum DatabaseLogsColumns implements Column {
	
	ID(Column.ID, DataType.LONG, Column.PRIMARY_KEY_AUTOINCREMENT, Boolean.FALSE, Boolean.TRUE),
	MESSAGE("message", DataType.TEXT, null, Boolean.FALSE, Boolean.FALSE),
	DATE_TIME("dateTime", DataType.DATE_MILLISECONDS, null, Boolean.FALSE, Boolean.FALSE);
	
	private String columnName;
	private DataType dataType;
	private String extraQualifier;
	private Boolean optional;
	private Boolean unique;
	
	DatabaseLogsColumns(String columnName, DataType dataType, String extraQualifier, Boolean optional,
								Boolean unique) {
		this.columnName = columnName;
		this.dataType = dataType;
		this.extraQualifier = extraQualifier;
		this.optional = optional;
		this.unique = unique;
	}
	
	/**
	 * @see Column#addValue(ContentValues, Object)
	 */
	@Override
	public <T> void addValue(ContentValues values, T value) {
		dataType.writeValue(values, columnName, value);
	}

	/**
	 * @see Column#readValue(Cursor)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <E> E readValue(Cursor cursor) {
		return (E)dataType.readValue(cursor, columnName);
	}

	/**
	 * @see Column#getDataType()
	 */
	@Override
	public DataType getDataType() {
		return dataType;
	}

	/**
	 * @see Column#getColumnName()
	 */
	@Override
	public String getColumnName() {
		return columnName;
	}

	/**
	 * @see Column#getExtraQualifier()
	 */
	@Override
	public String getExtraQualifier() {
		return extraQualifier;
	}

	/**
	 * @see Column#isOptional()
	 */
	@Override
	public Boolean isOptional() {
		return optional;
	}

	/**
	 * @see Column#isUnique()
	 */
	@Override
	public Boolean isUnique() {
		return unique;
	}

	/**
	 * @see Column#getReference()
	 */
	@Override
	public Reference getReference() {
		return null;
	}
}