package com.jdroid.android.about.appinvite;

import android.app.Activity;
import android.content.Intent;

import com.google.android.gms.appinvite.AppInviteInvitation;
import com.jdroid.android.about.AboutAppModule;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.java.collections.Lists;

public class AppInviteHelper {

	private AppInviteHelper(){}

	public static void onActivityResult(int appInviteRequestCode, int requestCode, int resultCode, Intent data) {

		if (requestCode == appInviteRequestCode) {
			if (resultCode == Activity.RESULT_OK) {
				// Check how many invitations were sent and log a message
				// The ids array contains the unique invitation ids for each invitation sent
				// (one for each contact select by the user). You can use these for analytics
				// as the ID will be consistent on the sending and receiving devices.
				String[] ids = AppInviteInvitation.getInvitationIds(resultCode, data);
				if (ids != null) {
					for (String invitationId : ids) {
						AboutAppModule.get().getAnalyticsSender().trackSendAppInvitation(invitationId);
					}
					AppInviteStats.invitesSent(Lists.newArrayList(ids));
				}
			} else if (resultCode != Activity.RESULT_CANCELED) {
				AbstractApplication.get().getExceptionHandler().logWarningException("Error when sending app invite: " + resultCode);
			}
		}
	}
}
