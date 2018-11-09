package com.jdroid.android.sample.androidx;

import com.jdroid.android.androidx.lifecycle.Resource;
import com.jdroid.android.sample.database.room.SampleEntity;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class SampleViewModel extends ViewModel {

	private LiveData<Resource<SampleEntity>> sampleEntity;

	public LiveData<Resource<SampleEntity>> load(String id, Boolean forceRefresh, Boolean failExecution) {
		sampleEntity = SampleRepository.get(id, forceRefresh, failExecution);
		return sampleEntity;
	}

	public LiveData<Resource<SampleEntity>> getSampleEntity() {
		return sampleEntity;
	}
}
