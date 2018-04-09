package com.jdroid.android.firebase.invites;

import android.app.Activity;
import android.content.Intent;

import com.google.android.gms.appinvite.AppInviteInvitation;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.java.collections.Lists;
import com.jdroid.java.utils.RandomUtils;

import java.util.List;

public class AppInviteHelper {

	public static final int REQUEST_CODE = RandomUtils.get16BitsInt();

	public static List<String> onActivityResult(int appInviteRequestCode, int requestCode, int resultCode, Intent data) {
		if (data != null && requestCode == appInviteRequestCode) {
			if (resultCode == Activity.RESULT_OK) {
				// The ids array contains the unique invitation ids for each invitation sent
				// (one for each contact select by the user). You can use these for analytics
				// as the ID will be consistent on the sending and receiving devices.
				String[] ids = AppInviteInvitation.getInvitationIds(resultCode, data);
				if (ids != null) {
					for (String invitationId : ids) {
						AbstractApplication.get().getCoreAnalyticsSender().trackSendAppInvitation(invitationId);
					}
					List<String> invitationsIds = Lists.newArrayList(ids);
					AppInviteStats.invitesSent(invitationsIds);
					return invitationsIds;
				}
			} else if (resultCode != Activity.RESULT_CANCELED) {
				AbstractApplication.get().getExceptionHandler().logWarningException("Error when sending app invite: " + resultCode);
			}
		}
		return null;
	}

	public static List<String> onActivityResult(int requestCode, int resultCode, Intent data) {
		return onActivityResult(REQUEST_CODE, requestCode, resultCode, data);
	}
}
