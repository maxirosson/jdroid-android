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
 * @param <ViewDataType> Type for the View.
 * @param <DatabaseDataType> Type for the Database model.
 * @param <NetworkDataType> Type for the API response.
 */
public abstract class NetworkBoundResource<ViewDataType, DatabaseDataType, NetworkDataType> {

	private static final Logger LOGGER = LoggerUtils.getLogger(NetworkBoundResource.class);

	private MediatorLiveData<Resource<ViewDataType>> result;

	public NetworkBoundResource() {
		result = new MediatorLiveData<Resource<ViewDataType>>() {
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
		setValue(Resource.starting());
		LOGGER.info(getTag() + ": Loading resource from database");
		LiveData<DatabaseDataType> dbSource = loadFromDb();
		addSource(dbSource, new Observer<DatabaseDataType>() {
			@Override
			public void onChanged(DatabaseDataType data) {
				removeSource(dbSource);
				if (shouldFetch(data)) {
					fetchFromNetwork(dbSource, data);
				} else {
					LOGGER.info(getTag() + ": Network fetch not required");
					dispatch(dbSource, null, Resource.Status.SUCCESS);
				}
			}
		});
	}

	@MainThread
	protected void dispatch(LiveData<DatabaseDataType> source, AbstractException abstractException, Resource.Status status) {
		addSource(source, new Observer<DatabaseDataType>() {
			@Override
			public void onChanged(DatabaseDataType newData) {
				ViewDataType viewDataType = toViewDataType(newData);
				if (status.equals(Resource.Status.SUCCESS)) {
					setValue(Resource.success(viewDataType));
				} else if (status.equals(Resource.Status.LOADING))  {
					setValue(Resource.loading(viewDataType));
				} else if (status.equals(Resource.Status.ERROR))  {
					setValue(Resource.error(abstractException, viewDataType));
				}
			}
		});
	}

	private void fetchFromNetwork(LiveData<DatabaseDataType> dbSource, DatabaseDataType data) {
		LiveData<ApiResponse<NetworkDataType>> apiResponse = loadFromNetwork();
		// we re-attach dbSource as a new source, it will dispatch its latest value quickly
		dispatch(dbSource, null, Resource.Status.LOADING);

		addSource(apiResponse, new Observer<ApiResponse>() {
			@Override
			public void onChanged(ApiResponse response) {
				removeSource(apiResponse);
				removeSource(dbSource);
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
										dispatch(loadFromDb(), null, Resource.Status.SUCCESS);

									}
								});
							} catch (Exception e) {
								AbstractException abstractException = wrapException(e);
								logHandledException(abstractException);
								AppExecutors.getMainThreadExecutor().execute(new Runnable() {
									 @Override
									 public void run() {
										 dispatch(dbSource, abstractException, Resource.Status.ERROR);
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
							dispatch(loadFromDb(), null, Resource.Status.SUCCESS);
						}
					});
				} else if (response instanceof ApiErrorResponse) {
					onFetchFailed();
					dispatch(dbSource, ((ApiErrorResponse)response).getException(), Resource.Status.ERROR);

				}
			}
		});
	}

	protected String getTag() {
		return TagUtils.getTag(getClass());
	}

	/*
	 *  Called to save the result of the API response into the database.
	 */
	@WorkerThread
	protected abstract void saveToDb(@NonNull NetworkDataType item);

	/*
	 * Called with the data in the database to decide whether to fetch potentially updated data from the network.
	 */
	@MainThread
	protected abstract boolean shouldFetch(@Nullable DatabaseDataType data);

	/*
	 * Called to get the cached data from the database.
	 */
	@NonNull
	@MainThread
	protected abstract LiveData<DatabaseDataType> loadFromDb();

	/*
	 * Called to create the API call.
	 */
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

	protected ViewDataType toViewDataType(DatabaseDataType databaseDataType) {
		return (ViewDataType)databaseDataType;
	}

	@WorkerThread
	protected NetworkDataType processResponse(ApiSuccessResponse<NetworkDataType> response) {
		return response.getBody();
	}

	/*
	 * Called when the fetch fails. The child class may want to reset components like rate limiter.
	 */
	@MainThread
	protected void onFetchFailed() {
		// Do nothing
	}

	@MainThread
	protected void setValue(@NonNull Resource<ViewDataType> newValue) {
		if (result.getValue() == null || !result.getValue().equals(newValue)) {
			result.setValue(newValue);
		}
	}

	@MainThread
	protected <S> void addSource(@NonNull LiveData<S> source, @NonNull Observer<? super S> onChanged) {
		result.addSource(source, onChanged);
	}

	@MainThread
	protected <S> void removeSource(@NonNull LiveData<S> toRemote) {
		result.removeSource(toRemote);
	}

	/*
	 * Returns a LiveData object that represents the resource that's implemented in the base class.
	 */
	public LiveData<Resource<ViewDataType>> getLiveData() {
		return result;
	}
}