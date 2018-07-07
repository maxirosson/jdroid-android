package com.jdroid.android.about;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.google.android.gms.plus.PlusOneButton;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.facebook.FacebookHelper;
import com.jdroid.android.firebase.invites.AppInviteHelper;
import com.jdroid.android.firebase.invites.AppInviteSender;
import com.jdroid.android.fragment.AbstractFragment;
import com.jdroid.android.google.GooglePlayServicesUtils;
import com.jdroid.android.google.GooglePlayUtils;
import com.jdroid.android.google.plus.GooglePlusHelperFragment;
import com.jdroid.android.google.plus.GooglePlusOneButtonHelper;
import com.jdroid.android.instagram.InstagramHelper;
import com.jdroid.android.linkedin.LinkedInHelper;
import com.jdroid.android.share.GooglePlusSharingItem;
import com.jdroid.android.share.HangoutsSharingItem;
import com.jdroid.android.share.MoreSharingItem;
import com.jdroid.android.share.ShareView;
import com.jdroid.android.share.SharingData;
import com.jdroid.android.share.SharingDataItem;
import com.jdroid.android.share.SharingItem;
import com.jdroid.android.share.SharingMedium;
import com.jdroid.android.share.SmsSharingItem;
import com.jdroid.android.share.TelegramSharingItem;
import com.jdroid.android.share.TwitterSharingItem;
import com.jdroid.android.share.WhatsAppSharingItem;
import com.jdroid.android.social.twitter.TwitterHelper;
import com.jdroid.java.collections.Lists;
import com.jdroid.java.collections.Maps;

import java.util.List;
import java.util.Map;

public abstract class SpreadTheLoveFragment extends AbstractFragment {

	private GooglePlusOneButtonHelper googlePlusOneButtonHelper;

	@Override
	public Integer getContentFragmentLayout() {
		return R.layout.jdroid_spread_the_love_fragment;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		Boolean followUsVisible = false;

		View facebook = findView(R.id.facebook);
		if (getFacebookPageId() != null) {
			facebook.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					FacebookHelper.openPage(getFacebookPageId());
				}
			});
			followUsVisible = true;
		} else {
			facebook.setVisibility(View.GONE);
		}

		View googlePlus = findView(R.id.googlePlus);
		if (getGooglePlusCommunityId() != null) {
			googlePlus.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					GooglePlusHelperFragment.openCommunity(getActivity(), getGooglePlusCommunityId());
				}
			});
			followUsVisible = true;
		} else {
			googlePlus.setVisibility(View.GONE);
		}

		View twitter = findView(R.id.twitter);
		if (getTwitterAccount() != null) {
			twitter.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					TwitterHelper.openProfile(getTwitterAccount());
				}
			});
			followUsVisible = true;
		} else {
			twitter.setVisibility(View.GONE);
		}
		
		View instagram = findView(R.id.instagram);
		if (getInstagramAccount() != null) {
			instagram.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					InstagramHelper.openProfile(getInstagramAccount());
				}
			});
			followUsVisible = true;
		} else {
			instagram.setVisibility(View.GONE);
		}
		
		View linkedin = findView(R.id.linkedin);
		if (getLinkedInCompanyPageId() != null) {
			linkedin.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					LinkedInHelper.openCompanyPage(getLinkedInCompanyPageId());
				}
			});
			followUsVisible = true;
		} else {
			linkedin.setVisibility(View.GONE);
		}

		findView(R.id.followUs).setVisibility(followUsVisible ? View.VISIBLE : View.GONE);
		
		Map<String, SharingDataItem> sharingDataItemsMap = Maps.newHashMap();
		sharingDataItemsMap.put(SharingMedium.TWITTER.getName(), new SharingDataItem(getTwitterShareText()));
		sharingDataItemsMap.put(SharingMedium.GOOGLE_PLUS.getName(), new SharingDataItem(getGooglePlusShareText()));
		sharingDataItemsMap.put(SharingMedium.WHATSAPP.getName(), new SharingDataItem(getWhatsAppShareText()));
		sharingDataItemsMap.put(SharingMedium.TELEGRAM.getName(), new SharingDataItem(getTelegramShareText()));
		sharingDataItemsMap.put(SharingMedium.HANGOUTS.getName(), new SharingDataItem(getHangoutsShareText()));
		sharingDataItemsMap.put(SharingMedium.SMS.getName(), new SharingDataItem(getSmsShareText()));
		SharingData sharingData = new SharingData(getShareKey(), sharingDataItemsMap, new SharingDataItem(getDefaultShareSubject(), getDefaultShareText()));
		
		List<SharingItem> sharingItems = Lists.newArrayList();
		sharingItems.add(new TwitterSharingItem(sharingData));
		sharingItems.add(new GooglePlusSharingItem(sharingData));
		sharingItems.add(new WhatsAppSharingItem(sharingData));
		sharingItems.add(new TelegramSharingItem(sharingData));
		sharingItems.add(new HangoutsSharingItem(sharingData));
		sharingItems.add(new SmsSharingItem(sharingData));
		
		Boolean displayShareTitle = ShareView.initShareSection(getActivity(), sharingItems, new MoreSharingItem(sharingData));
		
		View appInvite = findView(R.id.appInvite);
		if (displayAppInviteButton() && GooglePlayServicesUtils.isGooglePlayServicesAvailable(getActivity())) {
			appInvite.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					createAppInviteSender().sendInvitation();
				}
			});
			displayShareTitle = true;
		} else {
			appInvite.setVisibility(View.GONE);
		}
		
		if (displayGooglePlusOneButton()) {
			PlusOneButton plusOneButton = findView(R.id.plusOneButton);
			googlePlusOneButtonHelper = new GooglePlusOneButtonHelper(this, plusOneButton);
			if (GooglePlayServicesUtils.isGooglePlayServicesAvailable(getActivity())) {
				displayShareTitle = true;
			} else {
				findView(R.id.plusOneSection).setVisibility(View.GONE);
			}
		} else {
			findView(R.id.plusOneSection).setVisibility(View.GONE);
		}

		if (!displayShareTitle) {
			findView(R.id.shareSectionTitle).setVisibility(View.GONE);
		}
	}
	
	protected String getFacebookPageId() {
		return AbstractApplication.get().getAppContext().getFacebookPageId();
	}
	
	protected String getGooglePlusCommunityId() {
		return AbstractApplication.get().getAppContext().getGooglePlusCommunityId();
	}
	
	protected String getTwitterAccount() {
		return AbstractApplication.get().getAppContext().getTwitterAccount();
	}
	
	protected String getInstagramAccount() {
		return AbstractApplication.get().getAppContext().getInstagramAccount();
	}
	
	protected String getLinkedInCompanyPageId() {
		return AbstractApplication.get().getAppContext().getLinkedInCompanyPageId();
	}

	protected Boolean displayAppInviteButton() {
		return false;
	}

	protected AppInviteSender createAppInviteSender() {
		return new AppInviteSender();
	}

	protected Boolean displayGooglePlusOneButton() {
		return true;
	}
	
	public String getShareKey() {
		return GooglePlayUtils.getGooglePlayLink();
	}
	
	protected String getDefaultShareSubject() {
		return getString(R.string.jdroid_appName);
	}
	
	protected abstract String getDefaultShareText();
	
	protected String getTwitterShareText() {
		return getDefaultShareText();
	}
	
	protected String getGooglePlusShareText() {
		return getDefaultShareText();
	}
	
	protected String getWhatsAppShareText() {
		return getDefaultShareText();
	}
	
	protected String getTelegramShareText() {
		return getDefaultShareText();
	}
	
	protected String getHangoutsShareText() {
		return getDefaultShareText();
	}
	
	protected String getSmsShareText() {
		return getDefaultShareText();
	}
	
	@Override
	public void onResume() {
		super.onResume();
		
		if (googlePlusOneButtonHelper != null) {
			googlePlusOneButtonHelper.onResume();
		}
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		AppInviteHelper.onActivityResult(requestCode, resultCode, data);

		if (googlePlusOneButtonHelper != null) {
			googlePlusOneButtonHelper.onActivityResult(requestCode, resultCode, data);
		}
	}
	
}
