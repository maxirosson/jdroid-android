package com.jdroid.android.sample.androidx;

import com.jdroid.android.androidx.lifecycle.Resource;
import com.jdroid.android.sample.database.room.SampleEntity;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class SampleViewModel extends ViewModel {

	private LiveData<Resource<SampleEntity>> sampleEntity;

	public void init(String id) {
		if (sampleEntity == null) {
			sampleEntity = SampleRepository.get(id, false);
		}
	}

	public LiveData<Resource<SampleEntity>> getSampleEntity() {
		return sampleEntity;
	}

	public void refresh(String id) {
		sampleEntity = SampleRepository.get(id, true);
	}
}
