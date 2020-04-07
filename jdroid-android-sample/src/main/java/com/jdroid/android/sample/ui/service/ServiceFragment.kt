package com.jdroid.android.sample.ui.service

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import androidx.work.WorkStatus
import com.firebase.jobdispatcher.FirebaseJobDispatcher
import com.firebase.jobdispatcher.GooglePlayDriver
import com.jdroid.android.fragment.AbstractFragment
import com.jdroid.android.sample.R
import com.jdroid.java.utils.TypeUtils
import java.util.UUID
import java.util.concurrent.TimeUnit

class ServiceFragment : AbstractFragment() {

    private lateinit var failCheckBox: CheckBox
    private lateinit var delayEditText: EditText
    private lateinit var status: TextView

    override fun getContentFragmentLayout(): Int? {
        return R.layout.service_fragment
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        failCheckBox = findView(R.id.fail)
        delayEditText = findView(R.id.delay)
        status = findView(R.id.status)

        findView<View>(R.id.workerService).setOnClickListener {
            val intent = Intent()
            intent.putExtra("a", "1")
            intent.putExtra("fail", failCheckBox.isChecked)
            SampleWorkerService.runIntentInService(intent)
        }
        findView<View>(R.id.sampleWorker1).setOnClickListener {
            val sampleWorkRequestBuilder = OneTimeWorkRequest.Builder(SampleWorker1::class.java)
            val dataBuilder = createCommonDataBuilder()
            dataBuilder!!.putString("a", "3")
            dataBuilder.putBoolean("fail", failCheckBox.isChecked)
            sampleWorkRequestBuilder.setInputData(dataBuilder!!.build())
            val constrainsBuilder: Constraints.Builder = Builder()
            constrainsBuilder.setRequiredNetworkType(NetworkType.CONNECTED)
            sampleWorkRequestBuilder.setConstraints(constrainsBuilder.build())
            enqueue(sampleWorkRequestBuilder)
        }
        findView<View>(R.id.sampleWorker2).setOnClickListener {
            val sampleWorkRequestBuilder = OneTimeWorkRequest.Builder(SampleWorker2::class.java)
            val dataBuilder = createCommonDataBuilder()
            dataBuilder!!.putString("a", "4")
            sampleWorkRequestBuilder.setInputData(dataBuilder!!.build())
            enqueue(sampleWorkRequestBuilder)
        }
        findView<View>(R.id.sampleWorker3).setOnClickListener {
            val sampleWorkRequestBuilder = OneTimeWorkRequest.Builder(SampleWorker3::class.java)
            val dataBuilder = createCommonDataBuilder()
            dataBuilder!!.putString("a", "5")
            sampleWorkRequestBuilder.setInputData(dataBuilder!!.build())
            enqueue(sampleWorkRequestBuilder)
        }
        findView<View>(R.id.sampleWorker4).setOnClickListener {
            val sampleWorkRequestBuilder = OneTimeWorkRequest.Builder(SampleWorker4::class.java)
            val dataBuilder = createCommonDataBuilder()
            sampleWorkRequestBuilder.setInputData(dataBuilder!!.build())
            enqueue(sampleWorkRequestBuilder)
        }

        findView<View>(R.id.periodicWorker).setOnClickListener {
            val sampleWorkRequestBuilder =
                PeriodicWorkRequest.Builder(SampleWorker1::class.java, 15, TimeUnit.MINUTES)
            val dataBuilder = createCommonDataBuilder()
            sampleWorkRequestBuilder.setInputData(dataBuilder!!.build())
            WorkManager.getInstance().enqueue(sampleWorkRequestBuilder.build())
        }

        findView<View>(R.id.cancelAllWork).setOnClickListener {
            WorkManager.getInstance().cancelAllWork()
        }
    }

    private fun enqueue(sampleWorkRequestBuilder: OneTimeWorkRequest.Builder): UUID? {
        val delay: Int = TypeUtils.getSafeInteger(delayEditText.text)
        if (delay != null) {
            sampleWorkRequestBuilder.setInitialDelay(delay.toLong(), TimeUnit.SECONDS)
        }
        val oneTimeWorkRequest = sampleWorkRequestBuilder.build()
        WorkManager.getInstance().enqueue(oneTimeWorkRequest)
        val uuid: UUID = oneTimeWorkRequest.id
        WorkManager.getInstance().getStatusByIdLiveData(uuid).observe(this@ServiceFragment, object : Observer<WorkStatus?>() {
            fun onChanged(@Nullable workStatus: WorkStatus?) {
                if (workStatus != null) {
                    if (workStatus.state.isFinished) {
                        status.setText("Result: " + workStatus.outputData.getString("result"))
                    } else {
                        status.setText("Status: " + workStatus.state)
                    }
                }
            }
        })
        return uuid
    }

    private fun createCommonDataBuilder(): Data.Builder? {
        val dataBuilder: Data.Builder = Builder()
        dataBuilder.putBoolean("fail", failCheckBox.isChecked)
        return dataBuilder
    }
}
