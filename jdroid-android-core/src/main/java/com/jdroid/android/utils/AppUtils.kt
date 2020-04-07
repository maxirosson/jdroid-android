package com.jdroid.android.utils

import android.app.Activity
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.ScrollView

import com.jdroid.android.context.BuildConfigUtils
import com.jdroid.android.lifecycle.AppContextContainer

object AppUtils {

    /**
     * @return The version name of the application
     */
    fun getVersionName(): String {
        return getPackageInfo().versionName
    }

    /**
     * @return The version code of the application
     */
    fun getVersionCode(): Int {
        return getPackageInfo().versionCode
    }

    /**
     * @return The application id of the application
     */
    fun getApplicationId(): String {
        return getPackageInfo().packageName
    }

    /**
     * @return The application id of the application on release mode. This method removes the .debug suffix
     */
    fun getReleaseApplicationId(): String {
        var applicationId = getApplicationId()
        if (!isReleaseBuildType()) {
            if (applicationId.endsWith(".debug")) {
                applicationId = applicationId.replace(".debug", "")
            }
        }
        return applicationId
    }

    fun getBuildType(): String {
        return BuildConfigUtils.getBuildConfigValue("BUILD_TYPE")
    }

    fun isReleaseBuildType(): Boolean {
        return getBuildType() == "release"
    }

    fun getBuildTime(): String? {
        return BuildConfigUtils.getBuildConfigValue<String>("BUILD_TIME", null)
    }

    /**
     * @return The name of the application
     */
    fun getApplicationName(): String {
        return getContext().packageManager.getApplicationLabel(getApplicationInfo()).toString()
    }

    fun getPackageInfo(): PackageInfo {
        return getContext().packageManager.getPackageInfo(AppContextContainer.applicationContext.packageName, 0)
    }

    fun getApplicationInfo(): ApplicationInfo? {
        var info: ApplicationInfo? = null
        try {
            val context = getContext()
            info = context.packageManager.getApplicationInfo(
                context.packageName,
                PackageManager.GET_META_DATA
            )
        } catch (e: PackageManager.NameNotFoundException) {
            // Do Nothing
        }

        return info
    }

    fun showSoftInput(activity: Activity) {
        activity.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
    }

    fun hideSoftInput(view: View) {
        (getContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun scrollToBottom(scroll: ScrollView?) {
        scroll?.post { scroll.fullScroll(View.FOCUS_DOWN) }
    }

    /**
     * Checks if the application is installed on the SD card.
     *
     * @return `true` if the application is installed on the sd card
     */
    fun isInstalledOnSdCard(): Boolean {
        return getPackageInfo().applicationInfo.flags and ApplicationInfo.FLAG_EXTERNAL_STORAGE == ApplicationInfo.FLAG_EXTERNAL_STORAGE
    }

    private fun getContext(): Context {
        return AppContextContainer.applicationContext
    }

    fun getInstallerPackageName(): String? {
        return getContext().packageManager.getInstallerPackageName(getApplicationId())
    }

    fun getSafeInstallerPackageName(): String {
        return getInstallerPackageName() ?: "unknown"
    }
}
