package com.jdroid.android.sample.androidx

import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.jdroid.android.androidx.lifecycle.Resource
import com.jdroid.android.androidx.lifecycle.ResourceObserver
import com.jdroid.android.fragment.AbstractFragment
import com.jdroid.android.loading.FragmentLoading
import com.jdroid.android.loading.NonBlockingLoading
import com.jdroid.android.sample.R
import com.jdroid.android.sample.database.room.SampleEntity
import com.jdroid.java.utils.TypeUtils

class ArchitectureFragment : AbstractFragment() {

    companion object {
        private var failLoadFromNetwork: Boolean = false
        private var failLoadFromDb: Boolean = false
        private var failSaveToDb: Boolean = false
    }

    private lateinit var sampleViewModel: SampleViewModel
    private lateinit var observer: Observer<Resource<SampleEntity>>

    private lateinit var loadFromNetworkDelaySecondsEditText: EditText
    private lateinit var loadFromDbDelaySecondsEditText: EditText
    private lateinit var saveToDbDelaySecondsEditText: EditText

    override fun getContentFragmentLayout(): Int? {
        return R.layout.architecture_fragment
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        observer = object : ResourceObserver<SampleEntity>() {

            override fun onDataChanged(data: SampleEntity) {
                val result = findView<TextView>(R.id.result)
                result.text = data.toString()
            }

            override fun onStarting() {
                findView<View>(R.id.internalLoading).visibility = View.VISIBLE
            }

            override fun onStartLoading(data: SampleEntity?) {
                super.onStartLoading(data)
                findView<View>(R.id.internalLoading).visibility = View.VISIBLE
            }

            override fun onStopLoading(data: SampleEntity?) {
                super.onStopLoading(data)
                findView<View>(R.id.internalLoading).visibility = View.GONE
            }

            override fun getFragment(): AbstractFragment {
                return this@ArchitectureFragment
            }
        }
        sampleViewModel = ViewModelProviders.of(this).get(SampleViewModel::class.java)
        execute(false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val failLoadFromNetworkCheckBox = findView<CheckBox>(R.id.failLoadFromNetwork)
        failLoadFromNetworkCheckBox.isChecked = failLoadFromNetwork
        failLoadFromNetworkCheckBox.setOnCheckedChangeListener { buttonView, isChecked ->
            failLoadFromNetwork = isChecked
        }

        loadFromNetworkDelaySecondsEditText = findView(R.id.loadFromNetworkDelaySeconds)
        loadFromNetworkDelaySecondsEditText.setText("5")

        val failLoadFromDbCheckBox = findView<CheckBox>(R.id.failLoadFromDb)
        failLoadFromDbCheckBox.isChecked = failLoadFromDb
        failLoadFromDbCheckBox.setOnCheckedChangeListener { buttonView, isChecked -> failLoadFromDb = isChecked }

        loadFromDbDelaySecondsEditText = findView(R.id.loadFromDbDelaySeconds)
        loadFromDbDelaySecondsEditText.setText("5")

        val failSaveToDbCheckBox = findView<CheckBox>(R.id.failSaveToDb)
        failSaveToDbCheckBox.isChecked = failSaveToDb
        failSaveToDbCheckBox.setOnCheckedChangeListener { buttonView, isChecked -> failSaveToDb = isChecked }

        saveToDbDelaySecondsEditText = findView(R.id.saveToDbDelaySeconds)
        saveToDbDelaySecondsEditText.setText("5")

        findView<View>(R.id.execute).setOnClickListener { execute(true) }
    }

    private fun execute(forceRefresh: Boolean) {
        if (sampleViewModel.sampleEntity != null && forceRefresh) {
            sampleViewModel.sampleEntity!!.removeObserver(observer)
        }
        sampleViewModel.setFailLoadFromNetwork(failLoadFromNetwork)
        sampleViewModel.setLoadFromNetworkDelaySeconds(TypeUtils.getInteger(loadFromNetworkDelaySecondsEditText.text))
        sampleViewModel.setFailLoadFromDb(failLoadFromDb)
        sampleViewModel.setLoadFromDbDelaySeconds(TypeUtils.getInteger(loadFromDbDelaySecondsEditText.text))
        sampleViewModel.setFailSaveToDb(failSaveToDb)
        sampleViewModel.setSaveToDbDelaySeconds(TypeUtils.getInteger(saveToDbDelaySecondsEditText.text))
        sampleViewModel.load(SampleRepository.ID, forceRefresh).observe(this, observer)
    }

    override fun getDefaultLoading(): FragmentLoading? {
        return NonBlockingLoading()
    }
}
