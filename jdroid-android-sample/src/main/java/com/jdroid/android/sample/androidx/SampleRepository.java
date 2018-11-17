package com.jdroid.android.sample.androidx;

import com.jdroid.android.androidx.lifecycle.ApiResponse;
import com.jdroid.android.androidx.lifecycle.ApiSuccessResponse;
import com.jdroid.android.androidx.lifecycle.DatabaseBoundResource;
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
import androidx.lifecycle.MutableLiveData;

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

	public static LiveData<Resource<SampleEntity>> get(String id, Boolean forceRefresh, Boolean failExecution, Integer delaySeconds) {
		return new NetworkBoundResource<SampleEntity, NetworkResponse>() {

			@Override
			protected void saveToDb(@NonNull NetworkResponse item) {
				SampleEntity sampleEntity = new SampleEntity();
				sampleEntity.setId(item.getId());
				sampleEntity.setField(item.getValue());
				sampleEntity.setDate(DateUtils.now());
				getSampleEntityDao().upsert(sampleEntity);
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
				ExecutorUtils.sleep(delaySeconds, TimeUnit.SECONDS);
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

	public static LiveData<Resource<List<SampleEntity>>> getAll(Boolean forceRefresh, Boolean failExecution) {
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

	public static LiveData<Resource<List<String>>> getStrings() {
		return new DatabaseBoundResource<List<String>>() {

			@NonNull
			@Override
			protected LiveData<List<String>> loadFromDb() {
				List<String> items = Lists.newArrayList("one", "two", "three", "four", "five", "six", "seven", "eight", "nine", "ten", "eleven", "twelve", "thirteen", "fourteen", "fifteen", "sixteen");;
				MutableLiveData<List<String>> liveData = new MutableLiveData<>();
				liveData.setValue(items);
				return liveData;
			}

		}.getLiveData();
	}

	public static LiveData<Resource<List<Object>>> getMixedTypes() {
		return new DatabaseBoundResource<List<Object>>() {

			@NonNull
			@Override
			protected LiveData<List<Object>> loadFromDb() {
				List<Object> items = Lists.newArrayList("one", "two", true, "three", 1, 2, "four", true, "five", "six", "seven", "eight", 3, "nine", "ten", "eleven", "twelve", 4, "thirteen", false, false, "fourteen", "fifteen", "sixteen");
				MutableLiveData<List<Object>> liveData = new MutableLiveData<>();
				liveData.setValue(items);
				return liveData;
			}

		}.getLiveData();
	}

	private static SampleEntityDao getSampleEntityDao() {
		return RoomHelper.getDefaultDatabase(AppDatabase.class).sampleEntityDao();
	}
}
