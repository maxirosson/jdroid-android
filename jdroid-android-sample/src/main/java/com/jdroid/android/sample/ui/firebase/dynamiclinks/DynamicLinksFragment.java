package com.jdroid.android.sample.ui.firebase.dynamiclinks;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.jdroid.android.concurrent.AppExecutors;
import com.jdroid.android.firebase.dynamiclink.FirebaseDynamicLinksAppContext;
import com.jdroid.android.fragment.AbstractFragment;
import com.jdroid.android.sample.R;
import com.jdroid.android.utils.AppUtils;
import com.jdroid.java.firebase.dynamiclinks.ShortDynamicLinkService;
import com.jdroid.java.firebase.dynamiclinks.domain.AnalyticsInfo;
import com.jdroid.java.firebase.dynamiclinks.domain.AndroidInfo;
import com.jdroid.java.firebase.dynamiclinks.domain.DynamicLink;
import com.jdroid.java.firebase.dynamiclinks.domain.DynamicLinkInfo;
import com.jdroid.java.firebase.dynamiclinks.domain.DynamicLinkResponse;
import com.jdroid.java.firebase.dynamiclinks.domain.GooglePlayAnalytics;
import com.jdroid.java.firebase.dynamiclinks.domain.SuffixOption;
import com.jdroid.java.utils.StringUtils;
import com.jdroid.java.utils.TypeUtils;

public class DynamicLinksFragment extends AbstractFragment {


	private TextView linkUrlTextView;
	private TextView minVersionCodeTextView;
	private TextView fallbackLinkTextView;
	private TextView customAppLocationTextView;
	private TextView utmSourceTextView;
	private TextView utmMediumTextView;
	private TextView utmCampaignTextView;
	private TextView utmTermTextView;
	private TextView utmContentTextView;
	private CheckBox unguessableCheckBox;

	private TextView shortLinkTextView;
	private TextView longLinkTextView;

	@Override
	public Integer getContentFragmentLayout() {
		return R.layout.dynamic_links_fragment;
	}

	@SuppressLint("SetTextI18n")
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);


		linkUrlTextView = findView(R.id.linkUrl);
		linkUrlTextView.setText("http://www.jdroidtools.com/");

		minVersionCodeTextView = findView(R.id.minVersionCode);
		fallbackLinkTextView = findView(R.id.fallbackLink);
		customAppLocationTextView = findView(R.id.customAppLocation);
		utmSourceTextView = findView(R.id.utmSource);
		utmMediumTextView = findView(R.id.utmMedium);
		utmCampaignTextView = findView(R.id.utmCampaign);
		utmTermTextView = findView(R.id.utmTerm);
		utmContentTextView = findView(R.id.utmContent);
		unguessableCheckBox = findView(R.id.unguessable);

		shortLinkTextView = findView(R.id.shortLink);
		longLinkTextView = findView(R.id.longLink);


		findView(R.id.buildLink).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				AppExecutors.INSTANCE.getNetworkIOExecutor().execute(new Runnable() {
					@Override
					public void run() {
						final DynamicLink dynamicLink = new DynamicLink();

						DynamicLinkInfo dynamicLinkInfo = new DynamicLinkInfo();
						dynamicLinkInfo.setDynamicLinkDomain(FirebaseDynamicLinksAppContext.INSTANCE.getDynamicLinksDomain());
						dynamicLinkInfo.setLink(linkUrlTextView.getText().toString());

						AndroidInfo androidInfo = new AndroidInfo();
						androidInfo.setAndroidPackageName(AppUtils.getApplicationId());
						if (minVersionCodeTextView.getText().length() > 0) {
							androidInfo.setAndroidMinPackageVersionCode(TypeUtils.getInteger(minVersionCodeTextView.getText().toString()));
						}
						androidInfo.setAndroidFallbackLink(fallbackLinkTextView.getText().toString());
						androidInfo.setAndroidLink(customAppLocationTextView.getText().toString());
						dynamicLinkInfo.setAndroidInfo(androidInfo);

						AnalyticsInfo analyticsInfo = new AnalyticsInfo();
						GooglePlayAnalytics googlePlayAnalytics = new GooglePlayAnalytics();
						googlePlayAnalytics.setUtmSource(utmSourceTextView.getText().toString());
						googlePlayAnalytics.setUtmMedium(utmMediumTextView.getText().toString());
						googlePlayAnalytics.setUtmCampaign(utmCampaignTextView.getText().toString());
						googlePlayAnalytics.setUtmTerm(utmTermTextView.getText().toString());
						googlePlayAnalytics.setUtmContent(utmContentTextView.getText().toString());
						analyticsInfo.setGooglePlayAnalytics(googlePlayAnalytics);
						dynamicLinkInfo.setAnalyticsInfo(analyticsInfo);

						dynamicLink.setDynamicLinkInfo(dynamicLinkInfo);

						executeOnUIThread(new Runnable() {
							@Override
							public void run() {
								longLinkTextView.setText(dynamicLink.build());
							}
						});
					}
				});
			}
		});

		findView(R.id.shortenLink).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				final String longLink = longLinkTextView.getText().toString();
				if (StringUtils.isNotEmpty(longLink)) {
					AppExecutors.INSTANCE.getNetworkIOExecutor().execute(new Runnable() {
						@Override
						public void run() {
							final DynamicLinkResponse dynamicLinkResponse = new ShortDynamicLinkService().getShortDynamicLink(
								FirebaseDynamicLinksAppContext.INSTANCE.getWebApiKey(), longLink,
								unguessableCheckBox.isChecked() ? SuffixOption.UNGUESSABLE : SuffixOption.SHORT);
							executeOnUIThread(new Runnable() {
								@Override
								public void run() {
									shortLinkTextView.setText(dynamicLinkResponse.getShortLink());
								}
							});
						}
					});
				}
			}
		});
	}
}
