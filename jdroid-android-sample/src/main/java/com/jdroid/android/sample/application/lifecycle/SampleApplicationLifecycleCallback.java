package com.jdroid.android.sample.application.lifecycle;

import android.content.Context;

import com.jdroid.android.lifecycle.ApplicationLifecycleCallback;
import com.jdroid.android.room.RoomHelper;
import com.jdroid.java.utils.LoggerUtils;

import org.slf4j.Logger;

import androidx.annotation.NonNull;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;


public class SampleApplicationLifecycleCallback extends ApplicationLifecycleCallback {

	private static final Logger LOGGER = LoggerUtils.getLogger(SampleApplicationLifecycleCallback.class);

	@Override
	public void onProviderInit(Context context) {
		LOGGER.debug("Executing sample onProviderInit");
		RoomHelper.addMigration(new Migration(1, 2) {
			@Override
			public void migrate(@NonNull SupportSQLiteDatabase database) {
				database.execSQL("CREATE TABLE IF NOT EXISTS `sample_2` (`id` TEXT NOT NULL, `field` TEXT, PRIMARY KEY(`id`))");
			}
		});
	}

	@Override
	public void onCreate(Context context) {
		LOGGER.debug("Executing sample onCreate");
	}

	@NonNull
	@Override
	public Integer getInitOrder() {
		return -1;
	}
}
