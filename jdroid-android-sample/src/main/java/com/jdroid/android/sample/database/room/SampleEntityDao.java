package com.jdroid.android.sample.database.room;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;


@Dao
public abstract class SampleEntityDao {

	@Query("SELECT * FROM sampleEntity WHERE id = :id")
	public abstract SampleEntity get(String id);

	@Query("SELECT * FROM sampleEntity")
	public abstract List<SampleEntity> getAll();

	@Query("SELECT * FROM sampleEntity WHERE id IN (:entitiesIds)")
	public abstract List<SampleEntity> loadAllByIds(int[] entitiesIds);

	@Query("SELECT * FROM sampleEntity WHERE field LIKE :fieldValue LIMIT 1")
	public abstract SampleEntity findByName(String fieldValue);

	@Insert
	public abstract void insert(SampleEntity sampleEntity);

	@Update
	public abstract void update(SampleEntity sampleEntity);

	@Insert
	public abstract void insertAll(List<SampleEntity> sampleEntities);

	@Delete
	public abstract void delete(SampleEntity sampleEntity);

	public void delete(String id) {
		SampleEntity sampleEntity = get(id);
		delete(sampleEntity);
	}

	@Query("DELETE FROM sampleEntity")
	public abstract void deleteAll();

}
