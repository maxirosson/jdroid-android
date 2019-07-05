package com.jdroid.android.utils

import android.Manifest
import android.accounts.AccountManager
import android.os.Build
import android.os.Looper
import androidx.annotation.RequiresPermission
import com.jdroid.android.application.AbstractApplication
import com.jdroid.java.collections.Lists
import com.jdroid.java.utils.ValidationUtils

object AndroidUtils {

    fun getApiLevel(): Int {
        return Build.VERSION.SDK_INT
    }

    fun getPlatformVersion(): String {
        return Build.VERSION.RELEASE
    }

    @RequiresPermission(Manifest.permission.GET_ACCOUNTS)
    fun getAccountsEmails(): List<String> {
        val emails = Lists.newArrayList<String>()
        for (account in AccountManager.get(AbstractApplication.get()).accounts) {
            if (ValidationUtils.isValidEmail(account.name) && !emails.contains(account.name)) {
                emails.add(account.name)
            }
        }
        return emails
    }

    fun isMainThread(): Boolean {
        return Looper.getMainLooper().thread === Thread.currentThread()
    }
}
