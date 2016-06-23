package com.jdroid.android.feedback;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jdroid.android.R;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.google.GooglePlayUtils;
import com.jdroid.android.intent.IntentUtils;
import com.jdroid.android.share.ShareUtils;

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
		
		LayoutInflater.from(context).inflate(R.layout.rate_app_view, this, true);
		
		rateAppTitle = (TextView)findViewById(R.id.rateAppTitle);
		positiveButton = (Button)findViewById(R.id.positive);
		negativeButton = (Button)findViewById(R.id.negative);

		enjoyingAppView(context);
	}

	private void enjoyingAppView(final Context context) {
		rateAppTitle.setText(context.getString(R.string.rateAppEnjoying, context.getString(R.string.appName)));
		positiveButton.setText(R.string.rateAppYes);
		positiveButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				googlePlayView(context);
				AbstractApplication.get().getAnalyticsSender().trackEnjoyingApp(true);
				RateAppStats.setEnjoyingApp(true);
			}

		});
		negativeButton.setText(R.string.rateAppNotReally);
		negativeButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				feedbackView(context);
				AbstractApplication.get().getAnalyticsSender().trackEnjoyingApp(false);
				RateAppStats.setEnjoyingApp(false);
			}
		});
	}

	private void feedbackView(final Context context) {
		rateAppTitle.setText(context.getString(R.string.rateAppFeedback));
		positiveButton.setText(R.string.rateAppOkSure);
		positiveButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String contactUsEmailAddress = AbstractApplication.get().getAppContext().getContactUsEmail();
				Intent intent = ShareUtils.createOpenMail(contactUsEmailAddress,
						AbstractApplication.get().getAppName());
				if (IntentUtils.isIntentAvailable(intent)) {
					context.startActivity(intent);
				} else {
					// TODO Improve this adding a toast or something
					AbstractApplication.get().getExceptionHandler().logWarningException("Error when sending email intent");
				}
				setVisibility(View.GONE);
				AbstractApplication.get().getAnalyticsSender().trackGiveFeedback(true);
				RateAppStats.setGiveFeedback(true);
			}

		});
		negativeButton.setText(R.string.rateAppNotThanks);
		negativeButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setVisibility(View.GONE);
				AbstractApplication.get().getAnalyticsSender().trackGiveFeedback(false);
				RateAppStats.setGiveFeedback(false);
			}

		});
	}

	private void googlePlayView(final Context context) {
		rateAppTitle.setText(context.getString(R.string.rateAppGooglePlay));
		positiveButton.setText(R.string.rateAppOkSure);
		positiveButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				GooglePlayUtils.launchAppDetails(context);
				setVisibility(View.GONE);
				AbstractApplication.get().getAnalyticsSender().trackRateOnGooglePlay(true);
				RateAppStats.setRateOnGooglePlay(true);
			}

		});
		negativeButton.setText(R.string.rateAppNotThanks);
		negativeButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setVisibility(View.GONE);
				AbstractApplication.get().getAnalyticsSender().trackRateOnGooglePlay(false);
				RateAppStats.setRateOnGooglePlay(false);
			}
		});
	}
}
