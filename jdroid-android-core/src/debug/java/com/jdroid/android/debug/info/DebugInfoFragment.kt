package com.jdroid.android.debug.info

import android.os.Bundle
import android.view.View
import androidx.core.util.Pair
import com.jdroid.android.application.AbstractApplication
import com.jdroid.android.context.UsageStats
import com.jdroid.android.firebase.performance.FirebasePerformanceAppContext
import com.jdroid.android.leakcanary.LeakCanaryHelper
import com.jdroid.android.recycler.AbstractRecyclerFragment
import com.jdroid.android.recycler.RecyclerViewAdapter
import com.jdroid.android.recycler.RecyclerViewContainer
import com.jdroid.android.strictmode.StrictModeHelper
import com.jdroid.android.utils.AndroidUtils
import com.jdroid.android.utils.AppUtils
import com.jdroid.android.utils.DeviceUtils
import com.jdroid.android.utils.ScreenUtils
import com.jdroid.java.collections.Lists

class DebugInfoFragment : AbstractRecyclerFragment() {

    private val properties = Lists.newArrayList<Pair<String, Any>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val appContext = AbstractApplication.get().appContext

        properties.add(Pair("Build Type", AppUtils.getBuildType()))
        properties.add(Pair("Build Time", AppUtils.getBuildTime()))

        properties.add(Pair("Installation Source", appContext.installationSource))

        properties.add(Pair("Screen Width Dp", ScreenUtils.getScreenWidthDp()))
        properties.add(Pair("Screen Height Dp", ScreenUtils.getScreenHeightDp()))
        properties.add(Pair("Screen Density", ScreenUtils.getScreenDensity()))
        properties.add(Pair("Screen Density DPI", ScreenUtils.getDensityDpi()))

        properties.add(Pair("Git Branch", AbstractApplication.get().gitContext.getBranch()))
        properties.add(Pair("Git Sha", AbstractApplication.get().gitContext.getSha()))

        properties.add(Pair("Application Id", AppUtils.getApplicationId()))
        properties.add(Pair("Package Name", AbstractApplication.get().manifestPackageName))
        properties.add(Pair("Version Name", AppUtils.getVersionName()))
        properties.add(Pair("Version Code", AppUtils.getVersionCode()))
        properties.add(Pair("SO API Level", AndroidUtils.apiLevel))
        properties.add(Pair("Installer Package Name", AppUtils.getInstallerPackageName()))

        properties.add(Pair("Device Manufacturer", DeviceUtils.getDeviceManufacturer()))
        properties.add(Pair("Device Model", DeviceUtils.getDeviceModel()))
        properties.add(Pair("Device Year Class", DeviceUtils.getDeviceYearClass()))

        properties.add(Pair("Network Operator Name", DeviceUtils.getNetworkOperatorName()))
        properties.add(Pair("Sim Operator Name", DeviceUtils.getSimOperatorName()))

        properties.add(Pair("App Loads", UsageStats.getAppLoads()))

        properties.add(Pair("Strict Mode Enabled", StrictModeHelper.isStrictModeEnabled()))
        properties.add(Pair("Leak Canary Enabled", LeakCanaryHelper.isLeakCanaryEnabled()))

        properties.add(Pair("Firebase Performance Enabled", FirebasePerformanceAppContext.isFirebasePerformanceEnabled()))

        for (each in DebugInfoHelper.getDebugInfoAppenders()) {
            properties.addAll(each.getDebugInfoProperties())
        }

        properties.addAll(AbstractApplication.get().debugContext.getCustomDebugInfoProperties())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        properties.sortWith(Comparator { o1, o2 -> o1.first!!.compareTo(o2.first!!) })

        setAdapter(RecyclerViewAdapter(object : PairItemRecyclerViewType() {

            override fun isTextSelectable(): Boolean {
                return true
            }

            override fun getRecyclerViewContainer(): RecyclerViewContainer {
                return this@DebugInfoFragment
            }
        }, properties))
    }
}
