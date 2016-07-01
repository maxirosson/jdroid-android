package com.jdroid.android.about.appinvite;

import android.content.Context;

import com.jdroid.android.context.UsageStats;
import com.jdroid.android.google.GooglePlayServicesUtils;
import com.jdroid.android.utils.SharedPreferencesHelper;
import com.jdroid.java.date.DateUtils;
import com.jdroid.java.utils.StringUtils;

import java.util.List;

public class AppInviteStats {

	private AppInviteStats(){}

	private static final String APP_INVITE_STATS = "appInviteStats";

	private static final String LAST_INVITE_SENT_TIMESTAMP = "lastInviteSentTimestamp";
	private static final String INVITES_SENT = "invitesSent";

	private static SharedPreferencesHelper sharedPreferencesHelper;

	public static void invitesSent(List<String> invitationIds) {
		String invites = getSharedPreferencesHelper().loadPreference(INVITES_SENT, "");
		if (StringUtils.isNotBlank(invites)) {
			invites += ",";
		}
		invites += StringUtils.join(invitationIds);
		getSharedPreferencesHelper().savePreferenceAsync(INVITES_SENT, invites);
		getSharedPreferencesHelper().savePreferenceAsync(LAST_INVITE_SENT_TIMESTAMP, DateUtils.nowMillis());
	}

	public static Long getLastInviteSentTimestamp() {
		return getSharedPreferencesHelper().loadPreferenceAsLong(LAST_INVITE_SENT_TIMESTAMP, 0L);
	}

	public static void reset() {
		getSharedPreferencesHelper().removeAllPreferences();
	}

	private static SharedPreferencesHelper getSharedPreferencesHelper() {
		if (sharedPreferencesHelper == null) {
			sharedPreferencesHelper = SharedPreferencesHelper.get(APP_INVITE_STATS);
		}
		return sharedPreferencesHelper;
	}

	public static Boolean displayAppInviteView(Context context) {
		Boolean enoughDaysSinceLastInvite = DateUtils.millisecondsToDays(AppInviteStats.getLastInviteSentTimestamp()) >= 21;
		Boolean enoughDaysSinceFirstAppLoad = DateUtils.millisecondsToDays(UsageStats.getFirstAppLoadTimestamp()) >= 7;
		Boolean enoughAppLoads = UsageStats.getAppLoads() >= 10;
		return enoughDaysSinceLastInvite && enoughDaysSinceFirstAppLoad && enoughAppLoads && GooglePlayServicesUtils.isGooglePlayServicesAvailable(context);
	}
}
