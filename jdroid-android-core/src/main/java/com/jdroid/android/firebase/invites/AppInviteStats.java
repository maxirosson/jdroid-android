package com.jdroid.android.firebase.invites;

import android.content.Context;
import android.support.annotation.WorkerThread;

import com.jdroid.android.context.UsageStats;
import com.jdroid.android.google.GooglePlayServicesUtils;
import com.jdroid.android.utils.SharedPreferencesHelper;
import com.jdroid.java.date.DateUtils;
import com.jdroid.java.utils.StringUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class AppInviteStats {

	private static final String APP_INVITE_STATS = "appInviteStats";

	private static final String LAST_INVITE_SENT_TIMESTAMP = "lastInviteSentTimestamp";
	private static final String INVITES_SENT = "invitesSent";

	private static SharedPreferencesHelper sharedPreferencesHelper;

	@WorkerThread
	public static void invitesSent(List<String> invitationIds) {
		String invites = getSharedPreferencesHelper().loadPreference(INVITES_SENT, "");
		if (StringUtils.isNotBlank(invites)) {
			invites += ",";
		}
		invites += StringUtils.join(invitationIds);
		getSharedPreferencesHelper().savePreferenceAsync(INVITES_SENT, invites);
		getSharedPreferencesHelper().savePreferenceAsync(LAST_INVITE_SENT_TIMESTAMP, DateUtils.nowMillis());
	}

	@WorkerThread
	public static Long getLastInviteSentTimestamp() {
		return getSharedPreferencesHelper().loadPreferenceAsLong(LAST_INVITE_SENT_TIMESTAMP, 0L);
	}

	@WorkerThread
	public static void reset() {
		getSharedPreferencesHelper().removeAllPreferences();
	}

	private static SharedPreferencesHelper getSharedPreferencesHelper() {
		if (sharedPreferencesHelper == null) {
			sharedPreferencesHelper = SharedPreferencesHelper.get(APP_INVITE_STATS);
		}
		return sharedPreferencesHelper;
	}

	// FIXME Find a way to call this method from UI thread
	//@WorkerThread
	public static Boolean displayAppInviteView(Context context) {
		Boolean enoughDaysSinceLastInvite = TimeUnit.MILLISECONDS.toDays(AppInviteStats.getLastInviteSentTimestamp()) >= 21;
		Boolean enoughDaysSinceFirstAppLoad = TimeUnit.MILLISECONDS.toDays(UsageStats.getFirstAppLoadTimestamp()) >= 7;
		Boolean enoughAppLoads = UsageStats.getAppLoads() >= 10;
		return enoughDaysSinceLastInvite && enoughDaysSinceFirstAppLoad && enoughAppLoads && context != null && GooglePlayServicesUtils.isGooglePlayServicesAvailable(context);
	}
}
