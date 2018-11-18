package com.jdroid.android.sample.database.room;

import com.jdroid.android.room.AbstractDao;
import com.jdroid.java.collections.Lists;

import java.util.List;

import androidx.annotation.WorkerThread;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;


@Dao
public abstract class SampleEntityDao extends AbstractDao<SampleEntity> {

	@Query("SELECT * FROM sampleEntity WHERE id = :id")
	public abstract LiveData<SampleEntity> get(String id);

	@Query("SELECT * FROM sampleEntity WHERE id = :id")
	public abstract SampleEntity getSync(String id);

	@Query("SELECT * FROM sampleEntity")
	public abstract LiveData<List<SampleEntity>> getAllLiveData();

	@WorkerThread
	@Query("SELECT * FROM sampleEntity")
	public abstract List<SampleEntity> getAll();

	@WorkerThread
	@Query("SELECT * FROM sampleEntity WHERE id IN (:entitiesIds)")
	public abstract List<SampleEntity> loadAllByIds(int[] entitiesIds);

	@WorkerThread
	@Query("SELECT * FROM sampleEntity WHERE field LIKE :fieldValue LIMIT 1")
	public abstract SampleEntity findByName(String fieldValue);

	@WorkerThread
	@Query("DELETE FROM sampleEntity WHERE id == :id")
	public abstract void delete(String id);

	@WorkerThread
	@Query("DELETE FROM sampleEntity")
	public abstract void deleteAll();

	@WorkerThread
	@Transaction
	public void replaceAll(List<SampleEntity> items) {

		// Remove items from database
		List<String> excludedIds = Lists.newArrayList();
		for (SampleEntity item : items) {
			excludedIds.add(item.getId());
		}
		delete(excludedIds);

		insertOrUpdateAll(items);
	}

	@WorkerThread
	@Query("DELETE FROM sampleEntity WHERE id NOT IN (:excludedIds)")
	public abstract void delete(List<String> excludedIds);

	@WorkerThread
	@Transaction
	public void insertOrUpdateAll(List<SampleEntity> items) {
		for (SampleEntity item : items) {
			upsert(item);
		}
	}

}
