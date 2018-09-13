package com.jdroid.android.sqlite;

public class Reference {

	private String tableName;
	private Column column;

	public Reference(String tableName, Column column) {
		this.tableName = tableName;
		this.column = column;
	}

	/**
	 * @return the tableName
	 */
	public String getTableName() {
		return tableName;
	}

	/**
	 * @return the column
	 */
	public Column getColumn() {
		return column;
	}
}
