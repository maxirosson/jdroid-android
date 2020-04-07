package com.jdroid.android.sample.ui.http

import android.os.Bundle
import android.view.View
import com.jdroid.android.concurrent.AppExecutors
import com.jdroid.android.exception.DialogErrorDisplayer
import com.jdroid.android.exception.ErrorDisplayer
import com.jdroid.android.fragment.AbstractFragment
import com.jdroid.android.sample.R
import com.jdroid.android.sample.api.SampleApiService
import com.jdroid.java.exception.AbstractException
import com.jdroid.java.utils.LoggerUtils

class HttpFragment : AbstractFragment() {

    override fun getContentFragmentLayout(): Int? {
        return R.layout.http_fragment
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        findView<View>(R.id.httpGet).setOnClickListener {
            AppExecutors.networkIOExecutor.execute {
                val sampleResponse = SampleApiService().httpGetSample()
                LOGGER.debug("Sample response key: " + sampleResponse.sampleKey)
            }
        }

        findView<View>(R.id.httpPost).setOnClickListener { AppExecutors.networkIOExecutor.execute { SampleApiService().httpPostSample() } }

        findView<View>(R.id.httpPut).setOnClickListener { AppExecutors.networkIOExecutor.execute { SampleApiService().httpPutSample() } }

        findView<View>(R.id.httpDelete).setOnClickListener { AppExecutors.networkIOExecutor.execute { SampleApiService().httpDeleteSample() } }

        findView<View>(R.id.httpPatch).setOnClickListener { AppExecutors.networkIOExecutor.execute { SampleApiService().httpPatchSample() } }

        findView<View>(R.id.connectionExceptionParser).setOnClickListener { AppExecutors.networkIOExecutor.execute { SampleApiService().connectionExceptionParser() } }

        findView<View>(R.id.unexpectedExceptionParser).setOnClickListener { AppExecutors.networkIOExecutor.execute { SampleApiService().unexpectedExceptionParser() } }
    }

    override fun createErrorDisplayer(abstractException: AbstractException): ErrorDisplayer {
        DialogErrorDisplayer.markAsNotGoBackOnError(abstractException)
        return super.createErrorDisplayer(abstractException)
    }

    companion object {

        private val LOGGER = LoggerUtils.getLogger(HttpFragment::class.java)
    }
}
