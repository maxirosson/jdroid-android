package com.jdroid.android.sample.androidx;

import com.jdroid.android.androidx.lifecycle.Resource;
import com.jdroid.android.androidx.viewmodel.AbstractViewModel;
import com.jdroid.android.sample.database.room.SampleEntity;

import java.util.List;

import androidx.lifecycle.LiveData;

public class SampleListViewModel extends AbstractViewModel {

	private LiveData<Resource<List<SampleEntity>>> sampleEntities;

	public LiveData<Resource<List<SampleEntity>>> load(String id, Boolean forceRefresh, Boolean failExecution) {
		if (sampleEntities == null || forceRefresh) {
			sampleEntities = SampleRepository.getAll(id, forceRefresh, failExecution);
		}
		return sampleEntities;
	}

	public LiveData<Resource<List<SampleEntity>>> getSampleEntities() {
		return sampleEntities;
	}

	public void removeItem(String id) {
		SampleRepository.removeItem(id);
	}

	public void addItem() {
		SampleRepository.addItem();
	}
}
