package com.jdroid.android.firebase.invites;

import android.accounts.Account;
import android.app.Activity;
import android.net.Uri;

import com.google.android.gms.appinvite.AppInviteInvitation;
import com.jdroid.android.activity.ActivityLauncher;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.utils.LocalizationUtils;
import com.jdroid.java.exception.UnexpectedException;

import java.util.Map;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

public class AppInviteSender {

	private int requestCode = AppInviteHelper.REQUEST_CODE;

	private String title = AbstractApplication.get().getAppContext().getAppInviteTitle();
	private String message = AbstractApplication.get().getAppContext().getAppInviteMessage();
	private String deepLink = AbstractApplication.get().getAppContext().getAppInviteDeeplink();
	private Uri customImage;
	private String emailSubject;
	private String emailHtmlContent;
	private String callToActionText;
	private Account account;
	private Map<String, String> additionalReferralParameters;
	private Integer minimumVersionCode;

	private Activity activity;

	public void sendInvitation() {
		try {
			AppInviteInvitation.IntentBuilder intentBuilder = new AppInviteInvitation.IntentBuilder(title);

			// Message
			if (message == null) {
				throw new UnexpectedException("Missing invitationMessage when building app invite");
			}
			if (message.length() > AppInviteInvitation.IntentBuilder.MAX_MESSAGE_LENGTH) {
				throw new UnexpectedException("invitationMessage too long when building app invite");
			}
			intentBuilder.setMessage(message);

			// Deeplink
			intentBuilder.setDeepLink(Uri.parse(deepLink));

			// Custom Image
			if (customImage != null) {
				intentBuilder.setCustomImage(customImage);
			}

			// Email template
			if (emailHtmlContent != null) {
				if (emailHtmlContent.length() > AppInviteInvitation.IntentBuilder.MAX_EMAIL_HTML_CONTENT) {
					throw new UnexpectedException("emailHtmlContent too long when building app invite");
				}

				if (emailSubject == null) {
					throw new UnexpectedException("Missing emailSubject when building app invite");
				}
				intentBuilder.setEmailSubject(emailHtmlContent);
				intentBuilder.setEmailHtmlContent(emailHtmlContent);
			} else if (callToActionText != null) {
				if (callToActionText.length() < AppInviteInvitation.IntentBuilder.MIN_CALL_TO_ACTION_TEXT_LENGTH) {
					throw new UnexpectedException("callToActionText too short when building app invite");
				}
				if (callToActionText.length() > AppInviteInvitation.IntentBuilder.MAX_CALL_TO_ACTION_TEXT_LENGTH) {
					throw new UnexpectedException("callToActionText too long when building app invite");
				}
				intentBuilder.setCallToActionText(callToActionText);
			}

			// Account
			if (account != null) {
				intentBuilder.setAccount(account);
			}

			// Additiconal Referral Parameters
			if (additionalReferralParameters != null) {
				intentBuilder.setAdditionalReferralParameters(additionalReferralParameters);
			}

			// Android minimum version code
			if (minimumVersionCode != null) {
				intentBuilder.setAndroidMinimumVersionCode(minimumVersionCode);
			}

			onInitIntentBuilder(intentBuilder);

			ActivityLauncher.startActivityForResult(activity, intentBuilder.build(), requestCode);
		} catch (Exception e) {
			AbstractApplication.get().getExceptionHandler().logHandledException(e);
		}
	}

	protected void onInitIntentBuilder(AppInviteInvitation.IntentBuilder intentBuilder) {
		// Do nothing
	}

	public void setRequestCode(int requestCode) {
		this.requestCode = requestCode;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setTitle(@StringRes int titleResId) {
		this.title = LocalizationUtils.getString(titleResId);
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setMessage(@StringRes int messageResId) {
		this.message = LocalizationUtils.getString(messageResId);
	}

	public void setDeepLink(String deepLink) {
		this.deepLink = deepLink;
	}

	public void setCustomImage(Uri customImage) {
		this.customImage = customImage;
	}

	public void setCustomImage(String customImage) {
		this.customImage = Uri.parse(customImage);
	}

	public void setEmailSubject(String emailSubject) {
		this.emailSubject = emailSubject;
	}

	public void setEmailSubject(@StringRes int emailSubjectResId) {
		this.emailSubject = LocalizationUtils.getString(emailSubjectResId);
	}

	public void setEmailHtmlContent(String emailHtmlContent) {
		this.emailHtmlContent = emailHtmlContent;
	}

	public void setCallToActionText(String callToActionText) {
		this.callToActionText = callToActionText;
	}

	public void setCallToActionText(@StringRes int callToActionTextResId) {
		this.callToActionText = LocalizationUtils.getString(callToActionTextResId);
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public void setAdditionalReferralParameters(Map<String, String> additionalReferralParameters) {
		this.additionalReferralParameters = additionalReferralParameters;
	}

	public void setMinimumVersionCode(Integer minimumVersionCode) {
		this.minimumVersionCode = minimumVersionCode;
	}

	public void setActivity(@Nullable Activity activity) {
		this.activity = activity;
	}

}
