package com.jdroid.android.androidx.lifecycle;

import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.concurrent.AppExecutors;
import com.jdroid.android.utils.TagUtils;
import com.jdroid.java.exception.AbstractException;
import com.jdroid.java.exception.UnexpectedException;
import com.jdroid.java.utils.LoggerUtils;

import org.slf4j.Logger;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.WorkerThread;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

/**
 * A generic class that can provide a resource backed by both the sqlite database and the network.
 *
 * @param <DatabaseDataType> Type for the Resource data.
 * @param <NetworkDataType> Type for the API response.
 */
public abstract class NetworkBoundResource<DatabaseDataType, NetworkDataType> {

	private static final Logger LOGGER = LoggerUtils.getLogger(NetworkBoundResource.class);

	private MediatorLiveData<Resource<DatabaseDataType>> result;

	public NetworkBoundResource() {
		result = new MediatorLiveData<Resource<DatabaseDataType>>() {
			@Override
			protected void onActive() {
				super.onActive();
				LOGGER.info(getTag() + ": LiveData " + toString() + " active");
			}

			@Override
			protected void onInactive() {
				super.onInactive();
				LOGGER.info(getTag() + ": LiveData " + toString() + " inactive");
			}
		};
		result.setValue(Resource.starting());
		LOGGER.info(getTag() + ": Loading resource from database");
		LiveData<DatabaseDataType> dbSource = loadFromDb();
		result.addSource(dbSource, new Observer<DatabaseDataType>() {
			@Override
			public void onChanged(DatabaseDataType data) {
				result.removeSource(dbSource);
				if (shouldFetch(data)) {
					fetchFromNetwork(dbSource);
				} else {
					LOGGER.info(getTag() + ": Network fetch not required");
					result.addSource(dbSource, new Observer<DatabaseDataType>() {
						@Override
						public void onChanged(DatabaseDataType newData) {
							setValue(Resource.success(newData));
						}
					});
				}
			}
		});
	}

	@MainThread
	private void setValue(Resource<DatabaseDataType> newValue) {
		if (!result.getValue().equals(newValue)) {
			result.setValue(newValue);
		}
	}

	private void fetchFromNetwork(LiveData<DatabaseDataType> dbSource) {
		LiveData<ApiResponse<NetworkDataType>> apiResponse = loadFromNetwork();
		// we re-attach dbSource as a new source, it will dispatch its latest value quickly
		result.addSource(dbSource, new Observer<DatabaseDataType>() {
			@Override
			public void onChanged(DatabaseDataType newData) {
				setValue(Resource.loading(newData));
			}
		});
		result.addSource(apiResponse, new Observer<ApiResponse>() {
			@Override
			public void onChanged(ApiResponse response) {
				result.removeSource(apiResponse);
				result.removeSource(dbSource);
				if (response instanceof ApiSuccessResponse) {
					AppExecutors.getDiskIOExecutor().execute(new Runnable() {
						@Override
						public void run() {
							LOGGER.info(getTag() + ": Saving resource to database");
							try {
								saveToDb((NetworkDataType)processResponse((ApiSuccessResponse)response));
								AppExecutors.getMainThreadExecutor().execute(new Runnable() {
									@Override
									public void run() {
										// we specially request a new live data,
										// otherwise we will get immediately last cached value,
										// which may not be updated with latest results received from network.
										LOGGER.info(getTag() + ": Loading resource from database");
										result.addSource(loadFromDb(), new Observer<DatabaseDataType>() {
											@Override
											public void onChanged(DatabaseDataType newData) {
												setValue(Resource.success(newData));
											}
										});
									}
								});
							} catch (Exception e) {
								AbstractException abstractException = wrapException(e);
								logHandledException(abstractException);
								AppExecutors.getMainThreadExecutor().execute(new Runnable() {
									 @Override
									 public void run() {
										 result.addSource(dbSource, new Observer<DatabaseDataType>() {
											 @Override
											 public void onChanged(DatabaseDataType newData) {
												 setValue(Resource.error(abstractException, newData));
											 }
										 });
									 }
								 });
							}
						}
					});
				} else if (response instanceof ApiEmptyResponse) {
					// TODO See this
					AppExecutors.getMainThreadExecutor().execute(new Runnable() {
						@Override
						public void run() {
							// reload from disk whatever we had
							LOGGER.info(getTag() + ": Loading resource from database");
							result.addSource(loadFromDb(), new Observer<DatabaseDataType>() {
								@Override
								public void onChanged(DatabaseDataType newData) {
									setValue(Resource.success(newData));
								}
							});
						}
					});
				} else if (response instanceof ApiErrorResponse) {
					onFetchFailed();
					result.addSource(dbSource, new Observer<DatabaseDataType>() {
						@Override
						public void onChanged(DatabaseDataType newData) {
							setValue(Resource.error(((ApiErrorResponse)response).getException(), newData));
						}
					});
				}
			}
		});

	}

	protected String getTag() {
		return TagUtils.getTag(getClass());
	}

	// Called to save the result of the API response into the database.
	@WorkerThread
	protected abstract void saveToDb(@NonNull NetworkDataType item);

	// Called with the data in the database to decide whether to fetch
	// potentially updated data from the network.
	@MainThread
	protected abstract boolean shouldFetch(@Nullable DatabaseDataType data);

	// Called to get the cached data from the database.
	@NonNull
	@MainThread
	protected abstract LiveData<DatabaseDataType> loadFromDb();

	// Called to create the API call.
	@NonNull
	@MainThread
	protected LiveData<ApiResponse<NetworkDataType>> loadFromNetwork() {
		MutableLiveData<ApiResponse<NetworkDataType>> mutableLiveData = new MutableLiveData<>();
		AppExecutors.getNetworkIOExecutor().execute(new Runnable() {
			@Override
			public void run() {
				LOGGER.info(getTag() + ": Loading resource from network");
				ApiResponse<NetworkDataType> apiResponse;
				try {
					apiResponse = doLoadFromNetwork();
				} catch (Exception e) {
					AbstractException abstractException = wrapException(e);
					logHandledException(abstractException);
					apiResponse = new ApiErrorResponse<>(abstractException);
				}
				mutableLiveData.postValue(apiResponse);
			}
		});
		return mutableLiveData;
	}

	private AbstractException wrapException(Exception e) {
		final AbstractException abstractException;
		if (e instanceof AbstractException) {
			abstractException = (AbstractException)e;
		} else {
			abstractException = new UnexpectedException(e);
		}
		return abstractException;
	}

	protected void logHandledException(AbstractException abstractException) {
		AbstractApplication.get().getExceptionHandler().logHandledException(abstractException);
	}
	
	@WorkerThread
	protected abstract ApiResponse<NetworkDataType> doLoadFromNetwork();

	@WorkerThread
	protected NetworkDataType processResponse(ApiSuccessResponse<NetworkDataType> response) {
		return response.getBody();
	}

	// Called when the fetch fails. The child class may want to reset components
	// like rate limiter.
	@MainThread
	protected void onFetchFailed() {
		// Do nothing
	}

	// Returns a LiveData object that represents the resource that's implemented
	// in the base class.
	public LiveData<Resource<DatabaseDataType>> getLiveData() {
		return result;
	}
}