package com.jdroid.android.listener

import android.view.KeyEvent
import android.view.View
import android.view.View.OnKeyListener

import com.jdroid.android.concurrent.AppExecutors
import com.jdroid.android.utils.AppUtils

abstract class OnEnterKeyListener
/**
 * @param async
 */
@JvmOverloads constructor(private val async: Boolean = true) : OnKeyListener {

    override fun onKey(v: View, keyCode: Int, event: KeyEvent): Boolean {

        if (matchKeyCode(keyCode) && event.action == KeyEvent.ACTION_UP) {
            val runnable = Runnable {
                AppUtils.hideSoftInput(v)
                onRun(v)
            }
            if (async) {
                AppExecutors.networkIOExecutor.execute(runnable)
            } else {
                runnable.run()
            }
            return true
        }
        return false
    }

    protected fun matchKeyCode(keyCode: Int): Boolean {
        return keyCode == KeyEvent.KEYCODE_ENTER
    }

    /**
     * Called when the search key on the keyword is released.
     *
     * @param view The view the key has been dispatched to.
     */
    abstract fun onRun(view: View)
}
