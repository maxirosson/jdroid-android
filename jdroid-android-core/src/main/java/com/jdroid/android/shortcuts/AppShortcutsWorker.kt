package com.jdroid.android.shortcuts

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.jdroid.android.application.AbstractApplication
import com.jdroid.android.jetpack.work.AbstractWorker
import com.jdroid.android.utils.AndroidUtils.getApiLevel

class AppShortcutsWorker(context: Context, workerParams: WorkerParameters) : AbstractWorker(context, workerParams) {

    override fun onWork(): Result {
        val dynamicShortcutsLoader = AppShortcutsHelper.getDynamicShortcutsLoader()
        AppShortcutsHelper.setDynamicShortcuts(dynamicShortcutsLoader.loadDynamicShortcuts())
        return Result.success()
    }

    companion object {

        const val WORK_MANAGER_TAG = "app_shortcuts"

        @JvmStatic
        fun enqueue() {
            if (AppShortcutsHelper.isDynamicAppShortcutsSupported() && AppShortcutsHelper.getDynamicShortcutsLoader() != null) {
                val requestBuilder = OneTimeWorkRequest.Builder(AppShortcutsWorker::class.java)
                requestBuilder.addTag(WORK_MANAGER_TAG)
                WorkManager.getInstance(AbstractApplication.get())
                    .beginUniqueWork(AppShortcutsWorker::class.java.simpleName, ExistingWorkPolicy.KEEP, requestBuilder.build()).enqueue()
            }
        }
    }
}