package com.jdroid.android.sample.firebase.fcm;

import com.jdroid.android.firebase.fcm.AbstractFcmAppModule;
import com.jdroid.android.firebase.fcm.FcmSender;
import com.jdroid.android.sample.application.AndroidApplication;
import com.jdroid.java.collections.Lists;

import java.util.List;

public class AndroidFcmAppModule extends AbstractFcmAppModule {

	@Override
	public List<FcmSender> getFcmSenders() {
		return Lists.newArrayList((FcmSender)AndroidApplication.get().getAppContext().getServer());
	}
}
