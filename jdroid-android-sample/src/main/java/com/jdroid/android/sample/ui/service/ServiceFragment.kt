package com.jdroid.android.sample.ui.service

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import android.widget.EditText
import com.firebase.jobdispatcher.FirebaseJobDispatcher
import com.firebase.jobdispatcher.GooglePlayDriver
import com.jdroid.android.fragment.AbstractFragment
import com.jdroid.android.sample.R
import com.jdroid.java.utils.TypeUtils

class ServiceFragment : AbstractFragment() {

    private lateinit var failCheckBox: CheckBox
    private lateinit var delayEditText: EditText

    override fun getContentFragmentLayout(): Int? {
        return R.layout.service_fragment
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        failCheckBox = findView(R.id.fail)
        delayEditText = findView(R.id.delay)

        findView<View>(R.id.workerService).setOnClickListener {
            val intent = Intent()
            intent.putExtra("a", "1")
            intent.putExtra("fail", failCheckBox.isChecked)
            SampleWorkerService.runIntentInService(intent)
        }
        findView<View>(R.id.firebaseJobService).setOnClickListener {
            val bundle = Bundle()
            bundle.putString("a", "2")
            bundle.putBoolean("fail", failCheckBox.isChecked)
            SampleFirebaseJobService.runIntentInService(bundle)
        }
        findView<View>(R.id.commandService1).setOnClickListener {
            val bundle = createServiceCommandBundle()
            bundle.putString("a", "3")
            val serviceCommand = SampleServiceCommand1()
            serviceCommand.start(bundle)
        }
        findView<View>(R.id.commandService2).setOnClickListener {
            val bundle = createServiceCommandBundle()
            bundle.putString("a", "4")
            val serviceCommand = SampleServiceCommand2()
            serviceCommand.start(bundle)
        }
        findView<View>(R.id.commandService3).setOnClickListener {
            val bundle = createServiceCommandBundle()
            bundle.putString("a", "5")
            val serviceCommand = SampleServiceCommand3()
            serviceCommand.start(bundle)
        }
        findView<View>(R.id.commandService4).setOnClickListener {
            val bundle = createServiceCommandBundle()
            val serviceCommand = SampleServiceCommand4()
            serviceCommand.start(bundle)
        }
        findView<View>(R.id.cancelAllJobs).setOnClickListener {
            val dispatcher = FirebaseJobDispatcher(GooglePlayDriver(activity!!))
            dispatcher.cancelAll()
        }
    }

    private fun createServiceCommandBundle(): Bundle {
        val bundle = Bundle()
        bundle.putBoolean("fail", failCheckBox.isChecked)
        val delay = TypeUtils.getInteger(delayEditText.text)
        if (delay != null) {
            bundle.putInt("delay", delay)
        }
        return bundle
    }
}
