package com.jdroid.android.sample.google.dynamicfeature

import com.jdroid.android.google.splitinstall.ModuleInstallListener
import com.jdroid.android.google.splitinstall.SplitInstallHelper

abstract class AbstractFeatureApi {

    fun installModule(moduleInstallListener : ModuleInstallListener) =
        SplitInstallHelper.installModule(moduleName, moduleInstallListener)

    abstract val moduleName : String
}