package com.jdroid.android.sample.androidx;

import com.jdroid.android.androidx.lifecycle.Resource;
import com.jdroid.android.androidx.viewmodel.AbstractViewModel;
import com.jdroid.android.sample.database.room.SampleEntity;

import androidx.lifecycle.LiveData;

public class SampleViewModel extends AbstractViewModel {

	private LiveData<Resource<SampleEntity>> sampleEntity;

	public LiveData<Resource<SampleEntity>> load(String id, Boolean forceRefresh, Boolean failExecution, Integer delaySeconds) {
		if (sampleEntity == null || forceRefresh) {
			sampleEntity = SampleRepository.get(id, forceRefresh, failExecution, delaySeconds);
		}
		return sampleEntity;
	}

	public LiveData<Resource<SampleEntity>> getSampleEntity() {
		return sampleEntity;
	}
}
