package com.jdroid.android.context;

import com.jdroid.android.utils.AppUtils;
import com.jdroid.android.utils.SharedPreferencesHelper;
import com.jdroid.java.http.Server;
import com.jdroid.java.remoteconfig.RemoteConfigParameter;

import java.util.Locale;

public class AppContext extends AbstractAppContext {

	// Environment
	private Server defaultServer;

	protected Server findServerByName(String name) {
		return null;
	}

	public Server getServer() {
		if (defaultServer == null) {
			defaultServer = findServerByName(getServerName());
		}
		return getServer(defaultServer);
	}

	@SuppressWarnings("unchecked")
	protected <T extends Server> T getServer(Server defaultServer) {
		if (AppUtils.INSTANCE.isReleaseBuildType() || !isDebugSettingsEnabled()) {
			return (T)defaultServer;
		} else {
			Class<?> clazz = defaultServer.getClass().getEnclosingClass() != null ? defaultServer.getClass().getEnclosingClass()
				: defaultServer.getClass();
			return (T)defaultServer.instance(SharedPreferencesHelper.get().loadPreference(
				clazz.getSimpleName(), defaultServer.getServerName()).toUpperCase(Locale.US));
		}
	}

	protected String getServerName() {
		return getBuildConfigValue("SERVER_NAME", null);
	}

	/**
	 * @return Whether the application should display the debug settings
	 */
	public Boolean isDebugSettingsEnabled() {
		return getBuildConfigBoolean("DEBUG_SETTINGS_ENABLED", false);
	}

	public String getLocalIp() {
		return getBuildConfigValue("LOCAL_IP", null);
	}

	public String getInstallationSource() {
		return getBuildConfigValue("INSTALLATION_SOURCE", "GooglePlay");
	}

	public Boolean isChromeInstallationSource() {
		return getInstallationSource().equals("Chrome");
	}

	/*
	 * Used by Google Sign In
	 */
	public String getServerClientId() {
		return getBuildConfigValue("GOOGLE_SERVER_CLIENT_ID", null);
	}

	public String getServerApiVersion() {
		return null;
	}

	public String getWebsite() {
		return null;
	}

	public String getContactUsEmail() {
		return null;
	}

	public String getTwitterAccount() {
		return null;
	}

	public String getInstagramAccount() {
		return null;
	}

	public String getLinkedInCompanyPageId() {
		return null;
	}

	public String getFacebookPageId() {
		return null;
	}

	public RemoteConfigParameter getPrivacyPolicyUrl() {
		return null;
	}
}
