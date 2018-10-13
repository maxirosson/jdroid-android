package com.jdroid.android.utils;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.os.Build;
import android.os.Looper;
import androidx.annotation.RequiresPermission;

import com.jdroid.android.application.AbstractApplication;
import com.jdroid.java.collections.Lists;
import com.jdroid.java.utils.ValidationUtils;

import java.util.List;

public class AndroidUtils {

	public static Integer getApiLevel() {
		return android.os.Build.VERSION.SDK_INT;
	}

	public static Boolean isPreLollipop() {
		return android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP;
	}

	public static String getPlatformVersion() {
		return android.os.Build.VERSION.RELEASE;
	}

	@RequiresPermission(Manifest.permission.GET_ACCOUNTS)
	public static List<String> getAccountsEmails() {
		List<String> emails = Lists.newArrayList();
		for (Account account : AccountManager.get(AbstractApplication.get()).getAccounts()) {
			if (ValidationUtils.isValidEmail(account.name) && !emails.contains(account.name)) {
				emails.add(account.name);
			}
		}
		return emails;
	}

	public static Boolean isMainThread() {
		return Looper.getMainLooper().getThread() == Thread.currentThread();
	}
}
