package com.jdroid.android.sample.database.room

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.jdroid.android.room.AbstractDao

@Dao
abstract class SampleEntityDao : AbstractDao<SampleEntity>() {

    @Query("SELECT * FROM sampleEntity")
    abstract fun getAllLiveData(): LiveData<List<SampleEntity>>

    @WorkerThread
    @Query("SELECT * FROM sampleEntity")
    abstract fun getAll(): List<SampleEntity>

    @Query("SELECT * FROM sampleEntity WHERE id = :id")
    abstract fun get(id: String): LiveData<SampleEntity>

    @Query("SELECT * FROM sampleEntity WHERE id = :id")
    abstract fun getSync(id: String): SampleEntity

    @WorkerThread
    @Query("SELECT * FROM sampleEntity WHERE id IN (:entitiesIds)")
    abstract fun loadAllByIds(entitiesIds: IntArray): List<SampleEntity>

    @WorkerThread
    @Query("SELECT * FROM sampleEntity WHERE field LIKE :fieldValue LIMIT 1")
    abstract fun findByName(fieldValue: String): SampleEntity

    @WorkerThread
    @Query("DELETE FROM sampleEntity WHERE id == :id")
    abstract fun delete(id: String)

    @WorkerThread
    @Query("DELETE FROM sampleEntity")
    abstract fun deleteAll()

    @WorkerThread
    @Transaction
    open fun replaceAll(items: List<SampleEntity>) {

        // Remove items from database
        val excludedIds = mutableListOf<String>()
        for (item in items) {
            excludedIds.add(item.id)
        }
        delete(excludedIds)

        insertOrUpdateAll(items)
    }

    @WorkerThread
    @Query("DELETE FROM sampleEntity WHERE id NOT IN (:excludedIds)")
    abstract fun delete(excludedIds: List<String>)

    @WorkerThread
    @Transaction
    open fun insertOrUpdateAll(items: List<SampleEntity>) {
        for (item in items) {
            upsert(item)
        }
    }
}
