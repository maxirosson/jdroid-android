package com.jdroid.android.androidx.lifecycle;

import com.jdroid.android.utils.TagUtils;
import com.jdroid.java.utils.LoggerUtils;

import org.slf4j.Logger;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

/**
 * A generic class that can provide a resource backed by the sqlite database.
 *
 * @param <DatabaseDataType> Type for the Resource data.
 */
public abstract class DatabaseBoundResource<DatabaseDataType> {

	private static final Logger LOGGER = LoggerUtils.getLogger(DatabaseBoundResource.class);

	private LiveData<Resource<DatabaseDataType>> result;

	public DatabaseBoundResource() {
		LOGGER.info(getTag() + ": Loading resource from database");
		LiveData<DatabaseDataType> dbSource = loadFromDb();
		result = Transformations.map(dbSource, new Function<DatabaseDataType, Resource<DatabaseDataType>>() {

			@Override
			public Resource<DatabaseDataType> apply(DatabaseDataType input) {
				return Resource.success(input);
			}
		});
	}

	protected String getTag() {
		return TagUtils.INSTANCE.getTag(getClass());
	}

	// Called to get the cached data from the database.
	@NonNull
	@MainThread
	protected abstract LiveData<DatabaseDataType> loadFromDb();

	// Returns a LiveData object that represents the resource that's implemented
	// in the base class.
	public LiveData<Resource<DatabaseDataType>> getLiveData() {
		return result;
	}
}