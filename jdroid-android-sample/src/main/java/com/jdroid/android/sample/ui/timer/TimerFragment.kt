package com.jdroid.android.sample.ui.timer

import android.os.Bundle
import android.view.View
import com.jdroid.android.fragment.AbstractFragment
import com.jdroid.android.sample.R
import com.jdroid.android.view.TimerView

class TimerFragment : AbstractFragment() {

    private lateinit var timerView: TimerView

    override fun getContentFragmentLayout(): Int? {
        return R.layout.timer_fragment
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        timerView = findView(R.id.timer)

        findView<View>(R.id.start).setOnClickListener { timerView.start() }
        findView<View>(R.id.stop).setOnClickListener { timerView.stop() }
        findView<View>(R.id.reset).setOnClickListener { timerView.reset() }
    }

    override fun onStart() {
        super.onStart()

        if (timerView.status == TimerView.Status.PAUSED) {
            timerView.unpause()
        }
    }

    override fun onStop() {
        super.onStop()

        if (timerView.status == TimerView.Status.STARTED) {
            timerView.pause()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        if (timerView.status == TimerView.Status.STARTED) {
            timerView.pause()
        }
    }
}
