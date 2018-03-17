package com.jdroid.android.about.feedback;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jdroid.android.R;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.google.GooglePlayUtils;
import com.jdroid.android.utils.ExternalAppsUtils;

public class RateAppView extends RelativeLayout {

	private TextView rateAppTitle;
	private Button positiveButton;
	private Button negativeButton;

	public RateAppView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}
	
	public RateAppView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}
	
	public RateAppView(Context context) {
		super(context);
		init(context);
	}
	
	private void init(final Context context) {
		
		LayoutInflater.from(context).inflate(R.layout.jdroid_rate_app_view, this, true);
		
		rateAppTitle = findViewById(R.id.rateAppTitle);
		positiveButton = findViewById(R.id.positive);
		negativeButton = findViewById(R.id.negative);

		enjoyingAppView(context);
	}

	private void enjoyingAppView(final Context context) {
		rateAppTitle.setText(context.getString(R.string.jdroid_rateAppEnjoying, context.getString(R.string.jdroid_appName)));
		positiveButton.setText(R.string.jdroid_rateAppYes);
		positiveButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				googlePlayView(context);
				AbstractApplication.get().getCoreAnalyticsSender().trackEnjoyingApp(true);
				RateAppStats.setEnjoyingApp(true);
			}

		});
		negativeButton.setText(R.string.jdroid_rateAppNotReally);
		negativeButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				feedbackView(context);
				AbstractApplication.get().getCoreAnalyticsSender().trackEnjoyingApp(false);
				RateAppStats.setEnjoyingApp(false);
			}
		});
	}

	private void feedbackView(final Context context) {
		rateAppTitle.setText(context.getString(R.string.jdroid_rateAppFeedback));
		positiveButton.setText(R.string.jdroid_rateAppOkSure);
		positiveButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				String contactUsEmailAddress = AbstractApplication.get().getAppContext().getContactUsEmail();
				if (ExternalAppsUtils.openEmail(context, contactUsEmailAddress, AbstractApplication.get().getAppName())) {
					AbstractApplication.get().getCoreAnalyticsSender().trackGiveFeedback(true);
					RateAppStats.setGiveFeedback(true);
				} else {
					// TODO Improve this adding a toast or something
				}
				setVisibility(View.GONE);
			}

		});
		negativeButton.setText(R.string.jdroid_rateAppNotThanks);
		negativeButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setVisibility(View.GONE);
				AbstractApplication.get().getCoreAnalyticsSender().trackGiveFeedback(false);
				RateAppStats.setGiveFeedback(false);
			}

		});
	}

	private void googlePlayView(final Context context) {
		rateAppTitle.setText(context.getString(R.string.jdroid_rateAppGooglePlay));
		positiveButton.setText(R.string.jdroid_rateAppOkSure);
		positiveButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				GooglePlayUtils.launchAppDetails(context);
				setVisibility(View.GONE);
				AbstractApplication.get().getCoreAnalyticsSender().trackRateOnGooglePlay(true);
				RateAppStats.setRateOnGooglePlay(true);
			}

		});
		negativeButton.setText(R.string.jdroid_rateAppNotThanks);
		negativeButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setVisibility(View.GONE);
				AbstractApplication.get().getCoreAnalyticsSender().trackRateOnGooglePlay(false);
				RateAppStats.setRateOnGooglePlay(false);
			}
		});
	}
}
