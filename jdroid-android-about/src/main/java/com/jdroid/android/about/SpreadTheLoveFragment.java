package com.jdroid.android.about;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.facebook.FacebookHelper;
import com.jdroid.android.fragment.AbstractFragment;
import com.jdroid.android.google.GooglePlayUtils;
import com.jdroid.android.instagram.InstagramHelper;
import com.jdroid.android.linkedin.LinkedInHelper;
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

	@Override
	public Integer getContentFragmentLayout() {
		return R.layout.jdroid_spread_the_love_fragment;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		List<FollowUsItem> followUsItems = getFollowUsItems();

		ViewGroup followUsContainer = findView(R.id.followUsContainer);

		for (FollowUsItem item : followUsItems) {
			ViewGroup itemViewGroup = (ViewGroup)LayoutInflater.from(getActivity()).inflate(R.layout.jdroid_spread_the_love_follow_us_item, followUsContainer, false);
			itemViewGroup.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					item.onSelected(getActivity());
				}
			});
			((ImageView)itemViewGroup.findViewById(R.id.image)).setImageResource(item.getImageResId());
			((TextView)itemViewGroup.findViewById(R.id.title)).setText(item.getTitleResId());
			followUsContainer.addView(itemViewGroup);
		}
		findView(R.id.followUs).setVisibility(followUsItems.isEmpty() ? View.GONE : View.VISIBLE);

		Map<String, SharingDataItem> sharingDataItemsMap = Maps.newHashMap();
		sharingDataItemsMap.put(SharingMedium.TWITTER.getName(), new SharingDataItem(getTwitterShareText()));
		sharingDataItemsMap.put(SharingMedium.WHATSAPP.getName(), new SharingDataItem(getWhatsAppShareText()));
		sharingDataItemsMap.put(SharingMedium.TELEGRAM.getName(), new SharingDataItem(getTelegramShareText()));
		sharingDataItemsMap.put(SharingMedium.HANGOUTS.getName(), new SharingDataItem(getHangoutsShareText()));
		sharingDataItemsMap.put(SharingMedium.SMS.getName(), new SharingDataItem(getSmsShareText()));
		SharingData sharingData = new SharingData(getShareKey(), sharingDataItemsMap, new SharingDataItem(getDefaultShareSubject(), getDefaultShareText()));

		List<SharingItem> sharingItems = Lists.newArrayList();
		sharingItems.add(new TwitterSharingItem(sharingData));
		sharingItems.add(new WhatsAppSharingItem(sharingData));
		sharingItems.add(new TelegramSharingItem(sharingData));
		sharingItems.add(new HangoutsSharingItem(sharingData));
		sharingItems.add(new SmsSharingItem(sharingData));

		Boolean displayShareTitle = ShareView.initShareSection(getActivity(), sharingItems, new MoreSharingItem(sharingData));
		if (!displayShareTitle) {
			findView(R.id.shareSectionTitle).setVisibility(View.GONE);
		}
	}

	protected String getFacebookPageId() {
		return AbstractApplication.get().getAppContext().getFacebookPageId();
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

	protected List<FollowUsItem> getFollowUsItems() {
		List<FollowUsItem> items = Lists.newArrayList();
		if (getFacebookPageId() != null) {
			items.add(new FollowUsItem(R.drawable.jdroid_ic_facebook_24dp, R.string.jdroid_facebook) {
				@Override
				public void onSelected(Activity activity) {
					FacebookHelper.INSTANCE.openPage(getFacebookPageId());
				}
			});
		}

		if (getTwitterAccount() != null) {
			items.add(new FollowUsItem(R.drawable.jdroid_ic_twitter_24dp, R.string.jdroid_twitter) {
				@Override
				public void onSelected(Activity activity) {
					TwitterHelper.openProfile(getTwitterAccount());
				}
			});
		}

		if (getInstagramAccount() != null) {
			items.add(new FollowUsItem(R.drawable.jdroid_ic_instagram_24dp, R.string.jdroid_instagram) {
				@Override
				public void onSelected(Activity activity) {
					InstagramHelper.INSTANCE.openProfile(getInstagramAccount());
				}
			});
		}

		if (getLinkedInCompanyPageId() != null) {
			items.add(new FollowUsItem(R.drawable.jdroid_ic_linkedin_24dp, R.string.jdroid_linkedin) {
				@Override
				public void onSelected(Activity activity) {
					LinkedInHelper.INSTANCE.openCompanyPage(getLinkedInCompanyPageId());
				}
			});
		}
		return items;
	}
}
