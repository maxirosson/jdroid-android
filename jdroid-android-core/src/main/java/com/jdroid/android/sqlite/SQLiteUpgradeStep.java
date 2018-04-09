package com.jdroid.android.sqlite;

import android.database.sqlite.SQLiteDatabase;

/**
 * Allows to execute db schema upgrades between each application version. Each upgrade step should be added to
 * {@link SQLiteHelper}.
 */
public interface SQLiteUpgradeStep {
	
	/**
	 * Execute the process to upgrade the database.
	 * 
	 * @param db database
	 * @param newVersion new database version
	 * @param oldVersion old database version
	 */
	public void upgrade(SQLiteDatabase db, int oldVersion, int newVersion);
	
	/**
	 * Return the db version from this upgrade step should be executed. Example: If oldVersion is 2 and newVersion is 5:
	 * a) If this method returns 2, this upgrade step will not be executed. b) If this method returns 3+, this upgrade
	 * step will be executed.
	 * 
	 * @return version db version associated to this upgrade step
	 */
	public Integer getVersion();
}
