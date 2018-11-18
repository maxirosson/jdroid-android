package com.jdroid.android.sample.androidx;

import com.jdroid.android.androidx.lifecycle.Resource;
import com.jdroid.android.androidx.viewmodel.AbstractViewModel;
import com.jdroid.android.sample.database.room.SampleEntity;

import androidx.lifecycle.LiveData;

public class SampleViewModel extends AbstractViewModel {

	private LiveData<Resource<SampleEntity>> sampleEntity;

	private Boolean failLoadFromNetwork = false;
	private Boolean failLoadFromDb = false;
	private Boolean failSaveToDb = false;
	private Integer loadFromNetworkDelaySeconds = 0;
	private Integer loadFromDbDelaySeconds = 0;
	private Integer saveToDbDelaySeconds = 0;

	public LiveData<Resource<SampleEntity>> load(String id, Boolean forceRefresh) {
		if (sampleEntity == null || forceRefresh) {
			sampleEntity = SampleRepository.get(id, forceRefresh, failLoadFromNetwork, failLoadFromDb, failSaveToDb, loadFromNetworkDelaySeconds, loadFromDbDelaySeconds, saveToDbDelaySeconds);
		}
		return sampleEntity;
	}

	public LiveData<Resource<SampleEntity>> getSampleEntity() {
		return sampleEntity;
	}

	public void setFailLoadFromNetwork(Boolean failLoadFromNetwork) {
		this.failLoadFromNetwork = failLoadFromNetwork;
	}

	public void setFailLoadFromDb(Boolean failLoadFromDb) {
		this.failLoadFromDb = failLoadFromDb;
	}

	public void setFailSaveToDb(Boolean failSaveToDb) {
		this.failSaveToDb = failSaveToDb;
	}

	public void setLoadFromNetworkDelaySeconds(Integer loadFromNetworkDelaySeconds) {
		this.loadFromNetworkDelaySeconds = loadFromNetworkDelaySeconds;
	}

	public void setLoadFromDbDelaySeconds(Integer loadFromDbDelaySeconds) {
		this.loadFromDbDelaySeconds = loadFromDbDelaySeconds;
	}

	public void setSaveToDbDelaySeconds(Integer saveToDbDelaySeconds) {
		this.saveToDbDelaySeconds = saveToDbDelaySeconds;
	}
}
