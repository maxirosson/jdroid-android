package com.jdroid.android.sample.androidx

import androidx.lifecycle.LiveData
import com.jdroid.android.androidx.lifecycle.Resource
import com.jdroid.android.androidx.viewmodel.AbstractViewModel
import com.jdroid.android.sample.database.room.SampleEntity

class SampleViewModel : AbstractViewModel() {

    var sampleEntity: LiveData<Resource<SampleEntity>>? = null
        private set

    private var failLoadFromNetwork: Boolean = false
    private var failLoadFromDb: Boolean = false
    private var failSaveToDb: Boolean = false
    private var loadFromNetworkDelaySeconds: Int = 0
    private var loadFromDbDelaySeconds: Int = 0
    private var saveToDbDelaySeconds: Int = 0

    fun load(id: String, forceRefresh: Boolean): LiveData<Resource<SampleEntity>> {
        if (sampleEntity == null || forceRefresh) {
            sampleEntity = SampleRepository.getItem(
                id,
                forceRefresh,
                failLoadFromNetwork,
                failLoadFromDb,
                failSaveToDb,
                loadFromNetworkDelaySeconds,
                loadFromDbDelaySeconds,
                saveToDbDelaySeconds
            )
        }
        return sampleEntity!!
    }

    fun setFailLoadFromNetwork(failLoadFromNetwork: Boolean) {
        this.failLoadFromNetwork = failLoadFromNetwork
    }

    fun setFailLoadFromDb(failLoadFromDb: Boolean) {
        this.failLoadFromDb = failLoadFromDb
    }

    fun setFailSaveToDb(failSaveToDb: Boolean) {
        this.failSaveToDb = failSaveToDb
    }

    fun setLoadFromNetworkDelaySeconds(loadFromNetworkDelaySeconds: Int) {
        this.loadFromNetworkDelaySeconds = loadFromNetworkDelaySeconds
    }

    fun setLoadFromDbDelaySeconds(loadFromDbDelaySeconds: Int) {
        this.loadFromDbDelaySeconds = loadFromDbDelaySeconds
    }

    fun setSaveToDbDelaySeconds(saveToDbDelaySeconds: Int) {
        this.saveToDbDelaySeconds = saveToDbDelaySeconds
    }
}
