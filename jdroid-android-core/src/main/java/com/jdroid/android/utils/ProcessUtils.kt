package com.jdroid.android.utils

import android.app.ActivityManager
import android.content.Context

object ProcessUtils {

    private var isMainProcess: Boolean? = null
    private var processInfo: ActivityManager.RunningAppProcessInfo? = null

    fun isMainProcess(context: Context): Boolean {
        init(context)
        return isMainProcess!!
    }

    fun getProcessInfo(context: Context): ActivityManager.RunningAppProcessInfo? {
        init(context)
        return processInfo
    }

    private fun init(context: Context) {
        if (isMainProcess == null) {
            processInfo = getProcessName(context)
            isMainProcess = context.packageName == processInfo!!.processName
        }
    }

    private fun getProcessName(context: Context): ActivityManager.RunningAppProcessInfo? {
        // get the current process name, you will get
        // name like "com.package.name" (main process name) or "com.package.name:remote"
        val mypid = android.os.Process.myPid()
        val manager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (info in manager.runningAppProcesses) {
            if (info.pid == mypid) {
                return info
            }
        }
        return null
    }
}
