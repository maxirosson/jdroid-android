package com.jdroid.android.sample.androidx;

import com.jdroid.android.androidx.lifecycle.ApiResponse;
import com.jdroid.android.androidx.lifecycle.ApiSuccessResponse;
import com.jdroid.android.androidx.lifecycle.NetworkBoundResource;
import com.jdroid.android.androidx.lifecycle.Resource;
import com.jdroid.android.concurrent.AppExecutors;
import com.jdroid.android.room.RoomHelper;
import com.jdroid.android.sample.database.room.AppDatabase;
import com.jdroid.android.sample.database.room.SampleEntity;
import com.jdroid.android.sample.database.room.SampleEntityDao;
import com.jdroid.java.collections.Lists;
import com.jdroid.java.concurrent.ExecutorUtils;
import com.jdroid.java.date.DateUtils;
import com.jdroid.java.exception.UnexpectedException;
import com.jdroid.java.utils.RandomUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

public class SampleRepository {

	public static final String ID = "1";

	public static void removeItem(String id) {
		AppExecutors.getDiskIOExecutor().execute(new Runnable() {
			@Override
			public void run() {
				getSampleEntityDao().delete(id);
			}
		});
	}

	public static void addItem() {
		AppExecutors.getDiskIOExecutor().execute(new Runnable() {
			@Override
			public void run() {
				SampleEntity sampleEntity = new SampleEntity();
				sampleEntity.setId(RandomUtils.getLong().toString());
				sampleEntity.setField(RandomUtils.getLong().toString());
				sampleEntity.setDate(DateUtils.now());
				getSampleEntityDao().upsert(sampleEntity);
			}
		});
	}

	public static LiveData<Resource<SampleEntity>> get(String id, Boolean forceRefresh, Boolean failExecution) {
		return new NetworkBoundResource<SampleEntity, NetworkResponse>() {

			@Override
			protected void saveToDb(@NonNull NetworkResponse item) {
				SampleEntity sampleEntity = new SampleEntity();
				sampleEntity.setId(item.getId());
				sampleEntity.setField(item.getValue());
				sampleEntity.setDate(DateUtils.now());
				getSampleEntityDao().insert(sampleEntity);
			}

			@Override
			protected boolean shouldFetch(@Nullable SampleEntity data) {
				return forceRefresh || data == null || (data.getDate() != null && DateUtils.nowMillis() - data.getDate().getTime() > 10000);
			}

			@NonNull
			@Override
			protected LiveData<SampleEntity> loadFromDb() {
				return getSampleEntityDao().get(id);
			}

			@Override
			protected ApiResponse<NetworkResponse> doLoadFromNetwork() {
				// Simulate a request
				ExecutorUtils.sleep(5, TimeUnit.SECONDS);
				if (failExecution) {
					throw new UnexpectedException("Sample network request failed");
				} else {
					NetworkResponse networkResponse = new NetworkResponse();
					networkResponse.setId(ID);
					networkResponse.setValue(RandomUtils.getLong().toString());

					return new ApiSuccessResponse<>(networkResponse);
				}
			}
		}.getLiveData();
	}

	public static LiveData<Resource<List<SampleEntity>>> getAll(String id, Boolean forceRefresh, Boolean failExecution) {
		return new NetworkBoundResource<List<SampleEntity>, List<NetworkResponse>>() {

			@Override
			protected void saveToDb(@NonNull List<NetworkResponse> items) {
				List<SampleEntity> entities = Lists.newArrayList();
				for (NetworkResponse networkResponse : items) {
					SampleEntity sampleEntity = new SampleEntity();
					sampleEntity.setId(networkResponse.getId());
					sampleEntity.setField(networkResponse.getValue());
					sampleEntity.setDate(DateUtils.now());
					entities.add(sampleEntity);
				}
				getSampleEntityDao().replaceAll(entities);
			}

			@Override
			protected boolean shouldFetch(@Nullable List<SampleEntity> data) {
				return forceRefresh || data == null;
			}

			@NonNull
			@Override
			protected LiveData<List<SampleEntity>> loadFromDb() {
				return getSampleEntityDao().getAllLiveData();
			}

			@Override
			protected ApiResponse<List<NetworkResponse>> doLoadFromNetwork() {
				// Simulate a request
				ExecutorUtils.sleep(5, TimeUnit.SECONDS);
				if (failExecution) {
					throw new UnexpectedException("Sample network request failed");
				} else {
					List<NetworkResponse> networkResponses = Lists.newArrayList();
					NetworkResponse networkResponse = new NetworkResponse();
					networkResponse.setId(ID);
					networkResponse.setValue(RandomUtils.getLong().toString());
					networkResponses.add(networkResponse);

					return new ApiSuccessResponse<List<NetworkResponse>>(networkResponses);
				}
			}
		}.getLiveData();
	}

	private static SampleEntityDao getSampleEntityDao() {
		return RoomHelper.getDefaultDatabase(AppDatabase.class).sampleEntityDao();
	}
}
