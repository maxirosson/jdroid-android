package com.jdroid.android.sample.androidx

import androidx.lifecycle.LiveData
import com.jdroid.android.androidx.lifecycle.Resource
import com.jdroid.android.androidx.viewmodel.AbstractViewModel
import com.jdroid.android.sample.database.room.SampleEntity

class SampleListViewModel : AbstractViewModel() {

    var sampleEntities: LiveData<Resource<List<SampleEntity>>>? = null
        private set
    private var mixedTypes: LiveData<Resource<List<Any>>>? = null
    private var strings: LiveData<Resource<List<String>>>? = null

    fun load(forceRefresh: Boolean, failExecution: Boolean): LiveData<Resource<List<SampleEntity>>> {
        if (sampleEntities == null || forceRefresh) {
            sampleEntities = SampleRepository.getAll(forceRefresh, failExecution)
        }
        return sampleEntities!!
    }

    fun loadMixedTypes(): LiveData<Resource<List<Any>>> {
        if (mixedTypes == null) {
            mixedTypes = SampleRepository.getMixedTypes()
        }
        return mixedTypes!!
    }

    fun loadStrings(): LiveData<Resource<List<String>>> {
        if (strings == null) {
            strings = SampleRepository.getStrings()
        }
        return strings!!
    }

    fun removeItem(id: String) {
        SampleRepository.removeItem(id)
    }

    fun addItem() {
        SampleRepository.addItem()
    }
}
