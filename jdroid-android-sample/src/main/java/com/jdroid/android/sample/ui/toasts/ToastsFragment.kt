package com.jdroid.android.sample.ui.toasts

import android.os.Bundle
import android.view.View
import com.jdroid.android.concurrent.AppExecutors
import com.jdroid.android.fragment.AbstractFragment
import com.jdroid.android.sample.R
import com.jdroid.android.utils.ToastUtils

class ToastsFragment : AbstractFragment() {

    override fun getContentFragmentLayout(): Int? {
        return R.layout.toasts_fragment
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        findView<View>(R.id.displayToastFromUIThread).setOnClickListener { ToastUtils.showToast(R.string.toastFromUIThread) }
        findView<View>(R.id.displayToastFromWorkerThread).setOnClickListener {
            AppExecutors.networkIOExecutor.execute {
                ToastUtils.showToastOnUIThread(
                    R.string.toastFromWorkerThread
                )
            }
        }
    }
}
