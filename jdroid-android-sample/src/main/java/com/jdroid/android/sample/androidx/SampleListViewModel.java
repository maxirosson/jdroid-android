package com.jdroid.android.sample.androidx;

import com.jdroid.android.androidx.lifecycle.Resource;
import com.jdroid.android.androidx.viewmodel.AbstractViewModel;
import com.jdroid.android.sample.database.room.SampleEntity;

import java.util.List;

import androidx.lifecycle.LiveData;

public class SampleListViewModel extends AbstractViewModel {

	private LiveData<Resource<List<SampleEntity>>> sampleEntities;
	private LiveData<Resource<List<Object>>> mixedTypes;
	private LiveData<Resource<List<String>>> strings;

	public LiveData<Resource<List<SampleEntity>>> load(Boolean forceRefresh, Boolean failExecution) {
		if (sampleEntities == null || forceRefresh) {
			sampleEntities = SampleRepository.getAll(forceRefresh, failExecution);
		}
		return sampleEntities;
	}

	public LiveData<Resource<List<Object>>> loadMixedTypes() {
		if (mixedTypes == null) {
			mixedTypes = SampleRepository.getMixedTypes();
		}
		return mixedTypes;
	}

	public LiveData<Resource<List<String>>> loadStrings() {
		if (strings == null) {
			strings = SampleRepository.getStrings();
		}
		return strings;
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
