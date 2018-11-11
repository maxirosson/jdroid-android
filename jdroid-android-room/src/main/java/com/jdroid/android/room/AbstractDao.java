package com.jdroid.android.room;

import java.util.List;

import androidx.annotation.WorkerThread;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Update;

public abstract class AbstractDao<T> {

	@WorkerThread
	@Insert
	public abstract void insert(T entity);

	@WorkerThread
	@Update
	public abstract int update(T entity);

	@WorkerThread
	public void upsert(T entity) {
		int rowsUpdated = update(entity);
		if (rowsUpdated == 0) {
			insert(entity);
		}
	}

	@WorkerThread
	@Insert
	public abstract void insertAll(List<T> entities);

	@WorkerThread
	@Delete
	public abstract void delete(T entity);

}
