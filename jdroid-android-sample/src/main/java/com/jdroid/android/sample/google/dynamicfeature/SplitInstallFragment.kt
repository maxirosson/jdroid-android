package com.jdroid.android.sample.google.dynamicfeature

import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.jdroid.android.fragment.AbstractFragment
import com.jdroid.android.google.splitinstall.SplitInstallHelper
import com.jdroid.android.sample.R

class SplitInstallFragment : AbstractFragment() {

    override fun getContentFragmentLayout(): Int? {
        return R.layout.split_install_fragment
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        findView<View>(R.id.splitInstallInvokeDynamicFeatureSampleWithSilentInstall).setOnClickListener {
            DynamicFeatureSampleApiFacade.sampleCall()
        }

        findView<View>(R.id.splitInstallUninstallModule).setOnClickListener {
            SplitInstallHelper.uninstallModule(DynamicFeatureSampleApiFacade.MODULE_NAME)
        }

        findView<View>(R.id.splitInstallGetInstalledModules).setOnClickListener {
            findView<TextView>(R.id.splitInstalledModules).text = SplitInstallHelper.getInstalledModules().toString()
        }
    }
}
