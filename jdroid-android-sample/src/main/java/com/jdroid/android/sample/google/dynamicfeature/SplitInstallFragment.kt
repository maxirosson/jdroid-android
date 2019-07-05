package com.jdroid.android.sample.google.dynamicfeature

import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.jdroid.android.fragment.AbstractFragment
import com.jdroid.android.google.splitinstall.SplitInstallHelper
import com.jdroid.android.sample.R
import kotlinx.android.synthetic.main.split_install_fragment.*

class SplitInstallFragment : AbstractFragment() {

    override fun getContentFragmentLayout(): Int? {
        return R.layout.split_install_fragment
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        findView<View>(R.id.splitInstallInvokeDynamicFeatureSampleWithSilentInstall).setOnClickListener {
            DynamicFeatureSampleApiFacade.sampleCall()
        }

        val moduleName = findView<TextView>(R.id.splitInstallModuleName)
        moduleName.text = DynamicFeatureSampleApiFacade.MODULE_NAME

        findView<View>(R.id.splitInstallInstallModule).setOnClickListener {
            val featureApi = object : AbstractFeatureApi() {
                override fun getModuleName(): String {
                    return moduleName.text.toString()
                }
            }
            featureApi.invokeFeatureAsync({
                executeOnUIThread(object : Runnable {
                    override fun run() {
                        result.setText(R.string.splitInstallSuccessfulFeatureInvocation)
                    }
                })
            }, {
                executeOnUIThread(object : Runnable {
                    override fun run() {
                        result.text = it?.message
                    }
                })
            })
        }

        findView<View>(R.id.splitInstallDeferredUninstallModule).setOnClickListener {
            SplitInstallHelper.deferredUninstallModule(moduleName.text.toString())
        }

        findView<View>(R.id.splitInstallGetInstalledModules).setOnClickListener {
            findView<TextView>(R.id.result).text = SplitInstallHelper.getInstalledModules().toString()
        }
    }
}
