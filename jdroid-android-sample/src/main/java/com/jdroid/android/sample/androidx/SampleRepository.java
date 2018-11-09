package com.jdroid.android.sample.androidx;

import com.jdroid.android.androidx.lifecycle.ApiErrorResponse;
import com.jdroid.android.androidx.lifecycle.ApiResponse;
import com.jdroid.android.androidx.lifecycle.ApiSuccessResponse;
import com.jdroid.android.androidx.lifecycle.NetworkBoundResource;
import com.jdroid.android.androidx.lifecycle.Resource;
import com.jdroid.android.room.RoomHelper;
import com.jdroid.android.sample.api.SampleApiService;
import com.jdroid.android.sample.api.SampleResponse;
import com.jdroid.android.sample.database.room.AppDatabase;
import com.jdroid.android.sample.database.room.SampleEntity;
import com.jdroid.android.sample.database.room.SampleEntityDao;
import com.jdroid.java.utils.RandomUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

public class SampleRepository {

	public static LiveData<Resource<SampleEntity>> get(String id, boolean forceRefresh) {
		return new NetworkBoundResource<SampleEntity, SampleResponse>() {

			@Override
			protected void saveToDb(@NonNull SampleResponse item) {
				SampleEntity sampleEntity = new SampleEntity();
				sampleEntity.setId(item.getSampleKey());
				sampleEntity.setField(RandomUtils.getLong().toString());
				getSampleEntityDao().insert(sampleEntity);
			}

			@Override
			protected boolean shouldFetch(@Nullable SampleEntity data) {
				return forceRefresh || data == null;
			}

			@NonNull
			@Override
			protected LiveData<SampleEntity> loadFromDb() {
				return getSampleEntityDao().get(id);
			}

			@Override
			protected ApiResponse<SampleResponse> doLoadFromNetwork() {
				try {
					SampleApiService apiService = new SampleApiService();
					SampleResponse sampleResponse = apiService.httpGetSample();
					return new ApiSuccessResponse<>(sampleResponse);
				} catch (Exception e) {
					return new ApiErrorResponse<>(e);
				}

			}

			@Override
			protected SampleResponse processResponse(ApiSuccessResponse<SampleResponse> response) {
				return response.getBody();
			}

		}.getLiveData();
	}

	private static SampleEntityDao getSampleEntityDao() {
		return RoomHelper.getDefaultDatabase(AppDatabase.class).sampleEntityDao();
	}
}
