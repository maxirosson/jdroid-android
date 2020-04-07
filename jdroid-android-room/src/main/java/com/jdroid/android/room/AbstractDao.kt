package com.jdroid.android.room

import androidx.annotation.WorkerThread
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update

abstract class AbstractDao<T> {

    @WorkerThread
    @Insert
    abstract fun insert(entity: T)

    @WorkerThread
    @Update
    abstract fun update(entity: T): Int

    @WorkerThread
    fun upsert(entity: T) {
        val rowsUpdated = update(entity)
        if (rowsUpdated == 0) {
            insert(entity)
        }
    }

    @WorkerThread
    @Insert
    abstract fun insertAll(entities: List<T>)

    @WorkerThread
    @Delete
    abstract fun delete(entity: T)
}
