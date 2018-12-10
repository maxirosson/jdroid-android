package com.jdroid.android.sample.ui.about;

import com.jdroid.android.about.SpreadTheLoveFragment;
import com.jdroid.android.sample.R;

public class AndroidSpreadTheLoveFragment extends SpreadTheLoveFragment {

	public static final String TWITTER_SHARE_URL = "http://goo.gl/XcxvIh";
	public static final String GOOGLE_PLUS_SHARE_URL = "http://goo.gl/6BloFX";
	public static final String FACEBOOK_SHARE_URL = "http://goo.gl/ogJoNX";
	public static final String WHATSAPP_SHARE_URL = "http://goo.gl/6KGhXn";
	public static final String TELEGRAM_SHARE_URL = "http://goo.gl/P4t4v0";
	public static final String HANGOUTS_SHARE_URL = "http://goo.gl/vQfk3U";
	public static final String SMS_SHARE_URL = "http://goo.gl/2rPlB3";
	public static final String UNKNOWN_SHARE_URL = "http://goo.gl/6kex8n";

	@Override
	protected Boolean displayAppInviteButton() {
		return true;
	}

	/**
	 * @see com.jdroid.android.about.SpreadTheLoveFragment#getDefaultShareText()
	 */
	@Override
	protected String getDefaultShareText() {
		return getString(R.string.shareMessage, UNKNOWN_SHARE_URL);
	}

	/**
	 * @see com.jdroid.android.about.SpreadTheLoveFragment#getTwitterShareText()
	 */
	@Override
	protected String getTwitterShareText() {
		return getString(R.string.shareMessage, TWITTER_SHARE_URL);
	}

	@Override
	protected String getSmsShareText() {
		return getString(R.string.shareMessage, SMS_SHARE_URL);
	}

	@Override
	protected String getWhatsAppShareText() {
		return getString(R.string.shareMessage, WHATSAPP_SHARE_URL);
	}

	@Override
	protected String getHangoutsShareText() {
		return getString(R.string.shareMessage, HANGOUTS_SHARE_URL);
	}

	/**
	 * @see com.jdroid.android.about.SpreadTheLoveFragment#getTelegramShareText()
	 */
	@Override
	protected String getTelegramShareText() {
		return getString(R.string.shareMessage, TELEGRAM_SHARE_URL);
	}
}
