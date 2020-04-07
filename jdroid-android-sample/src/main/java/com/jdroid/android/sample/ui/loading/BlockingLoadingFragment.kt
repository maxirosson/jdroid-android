package com.jdroid.android.sample.ui.loading

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.jdroid.android.androidx.lifecycle.Resource
import com.jdroid.android.androidx.lifecycle.ResourceObserver
import com.jdroid.android.fragment.AbstractFragment
import com.jdroid.android.sample.R
import com.jdroid.android.sample.androidx.SampleRepository
import com.jdroid.android.sample.androidx.SampleViewModel
import com.jdroid.android.sample.database.room.SampleEntity

open class BlockingLoadingFragment : AbstractFragment() {

    private lateinit var sampleViewModel: SampleViewModel
    private lateinit var observer: Observer<Resource<SampleEntity>>

    override fun getContentFragmentLayout(): Int? {
        return R.layout.blocking_loading_fragment
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        observer = object : ResourceObserver<SampleEntity>() {

            override fun onDataChanged(data: SampleEntity) {}

            override fun onStartLoading(data: SampleEntity?) {
                getActivityIf()!!.showLoading()
            }

            override fun onStopLoading(data: SampleEntity?) {
                getActivityIf()!!.dismissLoading()
            }

            override fun getFragment(): AbstractFragment {
                return this@BlockingLoadingFragment
            }
        }
        sampleViewModel = ViewModelProviders.of(this).get(SampleViewModel::class.java)
        execute()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fail = findView<Button>(R.id.fail)
        fail.setOnClickListener { execute() }
    }

    private fun execute() {
        sampleViewModel.sampleEntity?.removeObserver(observer)
        sampleViewModel.setFailLoadFromNetwork(true)
        sampleViewModel.setLoadFromNetworkDelaySeconds(5)
        sampleViewModel.load(SampleRepository.ID, true).observe(this, observer)
    }
}
