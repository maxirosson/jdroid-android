package com.jdroid.android.sample.ui.loading

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.jdroid.android.androidx.lifecycle.Resource
import com.jdroid.android.androidx.lifecycle.ResourceObserver
import com.jdroid.android.fragment.AbstractFragment
import com.jdroid.android.loading.FragmentLoading
import com.jdroid.android.loading.NonBlockingLoading
import com.jdroid.android.sample.R
import com.jdroid.android.sample.androidx.SampleRepository
import com.jdroid.android.sample.androidx.SampleViewModel
import com.jdroid.android.sample.database.room.SampleEntity

class NonBlockingLoadingFragment : AbstractFragment() {

    private lateinit var sampleViewModel: SampleViewModel
    private lateinit var observer: Observer<Resource<SampleEntity>>

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        observer = object : ResourceObserver<SampleEntity>() {

            override fun onDataChanged(data: SampleEntity) {}

            override fun onStartLoading(data: SampleEntity?) {
                fragment.showLoading()
            }

            override fun onStopLoading(data: SampleEntity?) {
                fragment.dismissLoading()
            }

            override fun getFragment(): AbstractFragment {
                return this@NonBlockingLoadingFragment
            }
        }
        sampleViewModel = ViewModelProviders.of(this).get(SampleViewModel::class.java)
        sampleViewModel.setLoadFromNetworkDelaySeconds(5)
        sampleViewModel.load(SampleRepository.ID, true).observe(this, observer)
    }

    override fun getContentFragmentLayout(): Int? {
        return R.layout.non_blocking_loading_fragment
    }

    override fun getDefaultLoading(): FragmentLoading? {
        return NonBlockingLoading()
    }
}
