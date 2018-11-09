package com.jdroid.android.sample.database.room;

import java.util.List;

import androidx.annotation.WorkerThread;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;


@Dao
public abstract class SampleEntityDao {

	@Query("SELECT * FROM sampleEntity WHERE id = :id")
	public abstract LiveData<SampleEntity> get(String id);

	// TODO See this
	@WorkerThread
	@Query("SELECT * FROM sampleEntity WHERE id = :id")
	public abstract SampleEntity get2(String id);

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
	@Insert(onConflict = OnConflictStrategy.REPLACE)
	public abstract void insert(SampleEntity sampleEntity);

	@WorkerThread
	@Update
	public abstract void update(SampleEntity sampleEntity);

	@WorkerThread
	@Insert
	public abstract void insertAll(List<SampleEntity> sampleEntities);

	@WorkerThread
	@Delete
	public abstract void delete(SampleEntity sampleEntity);

	@WorkerThread
	public void delete(String id) {
		SampleEntity sampleEntity = get2(id);
		delete(sampleEntity);
	}

	@WorkerThread
	@Query("DELETE FROM sampleEntity")
	public abstract void deleteAll();

}
