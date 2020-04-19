package com.jdroid.android.sample.ui.exceptions

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.jdroid.android.androidx.lifecycle.Resource
import com.jdroid.android.androidx.lifecycle.ResourceObserver
import com.jdroid.android.exception.AbstractErrorDisplayer
import com.jdroid.android.exception.DialogErrorDisplayer
import com.jdroid.android.exception.ErrorDisplayer
import com.jdroid.android.exception.SnackbarErrorDisplayer
import com.jdroid.android.fragment.AbstractFragment
import com.jdroid.android.sample.R
import com.jdroid.android.sample.androidx.SampleRepository
import com.jdroid.android.sample.androidx.SampleViewModel
import com.jdroid.android.sample.database.room.SampleEntity
import com.jdroid.java.exception.AbstractException

class ErrorDisplayerFragment : AbstractFragment() {

    private lateinit var sampleViewModel: SampleViewModel
    private lateinit var observer: Observer<Resource<SampleEntity>>

    private var errorDisplayer: ErrorDisplayer? = null
    private var goBackOnError: Boolean = true

    override fun getContentFragmentLayout(): Int? {
        return R.layout.exception_handling_fragment
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        observer = object : ResourceObserver<SampleEntity>() {

            override fun onDataChanged(data: SampleEntity) {}

            override fun onStartLoading(data: SampleEntity?) {}

            override fun onError(exception: AbstractException, data: SampleEntity?) {
                fragment.createErrorDisplayer(exception).displayError(fragment.activity, exception)
            }

            override fun getFragment(): AbstractFragment {
                return this@ErrorDisplayerFragment
            }
        }
        sampleViewModel = ViewModelProvider(this).get(SampleViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        findView<View>(R.id.defaultErrorDisplayer).setOnClickListener {
            this@ErrorDisplayerFragment.errorDisplayer = null
            this@ErrorDisplayerFragment.goBackOnError = true
            execute()
        }

        findView<View>(R.id.defaultErrorDisplayerNotGoBack).setOnClickListener {
            this@ErrorDisplayerFragment.errorDisplayer = null
            this@ErrorDisplayerFragment.goBackOnError = false
            execute()
        }

        findView<View>(R.id.snackbarErrorDisplayer).setOnClickListener {
            val errorDisplayer = SnackbarErrorDisplayer()
            errorDisplayer.setParentLayoutId(R.id.container)
            errorDisplayer.setActionTextResId(R.string.jdroid_retry)
            errorDisplayer.setOnClickListener(View.OnClickListener { execute() })
            this@ErrorDisplayerFragment.errorDisplayer = errorDisplayer
            execute()
        }
    }

    private fun execute() {
        sampleViewModel.sampleEntity?.removeObserver(observer)
        sampleViewModel.setFailLoadFromNetwork(true)
        sampleViewModel.load(SampleRepository.ID, true).observe(viewLifecycleOwner, observer)
    }

    override fun createErrorDisplayer(abstractException: AbstractException): ErrorDisplayer {
        AbstractErrorDisplayer.setErrorDisplayer(abstractException, errorDisplayer)
        if (errorDisplayer == null && !goBackOnError) {
            DialogErrorDisplayer.markAsNotGoBackOnError(abstractException)
        }
        return super.createErrorDisplayer(abstractException)
    }
}
