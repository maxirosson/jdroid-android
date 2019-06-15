package com.jdroid.android.sample.application.lifecycle

import android.content.Context
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.jdroid.android.lifecycle.ApplicationLifecycleCallback
import com.jdroid.android.room.RoomHelper
import com.jdroid.java.utils.LoggerUtils

class SampleApplicationLifecycleCallback : ApplicationLifecycleCallback() {

    companion object {
        private val LOGGER = LoggerUtils.getLogger(SampleApplicationLifecycleCallback::class.java)
    }

    override fun onProviderInit(context: Context) {
        LOGGER.debug("Executing sample onProviderInit")
        RoomHelper.addMigration(object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE TABLE IF NOT EXISTS `sample_2` (`id` TEXT NOT NULL, `field` TEXT, PRIMARY KEY(`id`))")
            }
        })
    }

    override fun onCreate(context: Context) {
        LOGGER.debug("Executing sample onCreate")
    }

    override fun getInitOrder(): Int {
        return -1
    }
}
