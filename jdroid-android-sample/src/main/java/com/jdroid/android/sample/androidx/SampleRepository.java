package com.jdroid.android.sample.androidx;

import com.jdroid.android.androidx.lifecycle.ApiResponse;
import com.jdroid.android.androidx.lifecycle.ApiSuccessResponse;
import com.jdroid.android.androidx.lifecycle.NetworkBoundResource;
import com.jdroid.android.androidx.lifecycle.Resource;
import com.jdroid.android.room.RoomHelper;
import com.jdroid.android.sample.database.room.AppDatabase;
import com.jdroid.android.sample.database.room.SampleEntity;
import com.jdroid.android.sample.database.room.SampleEntityDao;
import com.jdroid.java.concurrent.ExecutorUtils;
import com.jdroid.java.date.DateUtils;
import com.jdroid.java.exception.UnexpectedException;
import com.jdroid.java.utils.RandomUtils;

import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

public class SampleRepository {

	public static final String ID = "1";

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
				return forceRefresh || data == null || (data.getDate() != null && DateUtils.nowMillis() - data.getDate().getTime() > 5000);
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

	private static SampleEntityDao getSampleEntityDao() {
		return RoomHelper.getDefaultDatabase(AppDatabase.class).sampleEntityDao();
	}
}
