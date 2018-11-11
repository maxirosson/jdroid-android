package com.jdroid.android.sample.database.room;

import com.jdroid.android.room.AbstractDao;

import java.util.List;

import androidx.annotation.WorkerThread;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;


@Dao
public abstract class SampleEntityDao extends AbstractDao<SampleEntity> {

	@Query("SELECT * FROM sampleEntity WHERE id = :id")
	public abstract LiveData<SampleEntity> get(String id);

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

}
