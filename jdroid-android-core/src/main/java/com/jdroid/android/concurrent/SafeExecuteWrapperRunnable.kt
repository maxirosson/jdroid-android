package com.jdroid.android.concurrent

import androidx.fragment.app.Fragment

/**
 * Runnable implementation to wrap runnables to run them safely in the UI thread from a fragment. This class only call
 * the "run()" method of the wrapped runnable if the fragment is not detached.
 */
class SafeExecuteWrapperRunnable(private val fragment: Fragment, private val runnable: Runnable) : Runnable {

    override fun run() {
        val activity = fragment.activity
        activity?.let {
            if (!it.isDestroyed && !fragment.isDetached) {
                runnable.run()
            }
        }
    }
}
