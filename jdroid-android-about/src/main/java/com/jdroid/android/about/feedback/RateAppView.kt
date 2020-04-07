package com.jdroid.android.about.feedback

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TextView
import com.jdroid.android.application.AbstractApplication
import com.jdroid.android.google.GooglePlayUtils
import com.jdroid.android.utils.ExternalAppsUtils
import com.jdroid.android.utils.LocalizationUtils

class RateAppView : RelativeLayout {

    private lateinit var rateAppTitle: TextView
    private lateinit var positiveButton: Button
    private lateinit var negativeButton: Button

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context) : super(context) {
        init(context)
    }

    private fun init(context: Context) {

        LayoutInflater.from(context).inflate(getLayoutResId(), this, true)

        rateAppTitle = findViewById(com.jdroid.android.about.R.id.rateAppTitle)
        positiveButton = findViewById(com.jdroid.android.about.R.id.positive)
        negativeButton = findViewById(com.jdroid.android.about.R.id.negative)

        enjoyingAppView()
    }

    protected fun getLayoutResId(): Int {
        return com.jdroid.android.about.R.layout.jdroid_rate_app_view
    }

    private fun enjoyingAppView() {
        rateAppTitle.text = LocalizationUtils.getString(com.jdroid.android.about.R.string.jdroid_rateAppEnjoying, LocalizationUtils.getRequiredString(com.jdroid.android.about.R.string.jdroid_appName))
        positiveButton.setText(com.jdroid.android.about.R.string.jdroid_rateAppYes)
        positiveButton.setOnClickListener {
            googlePlayView()
            AbstractApplication.get().coreAnalyticsSender.trackEnjoyingApp(true)
            RateAppStats.setEnjoyingApp(true)
        }
        negativeButton.setText(com.jdroid.android.about.R.string.jdroid_rateAppNotReally)
        negativeButton.setOnClickListener {
            feedbackView()
            AbstractApplication.get().coreAnalyticsSender.trackEnjoyingApp(false)
            RateAppStats.setEnjoyingApp(false)
        }
    }

    private fun feedbackView() {
        rateAppTitle.text = LocalizationUtils.getString(com.jdroid.android.about.R.string.jdroid_rateAppFeedback)
        positiveButton.setText(com.jdroid.android.about.R.string.jdroid_rateAppOkSure)
        positiveButton.setOnClickListener {
            val contactUsEmailAddress = AbstractApplication.get().appContext.contactUsEmail
            if (ExternalAppsUtils.openEmail(contactUsEmailAddress, AbstractApplication.get().appName)) {
                AbstractApplication.get().coreAnalyticsSender.trackGiveFeedback(true)
                RateAppStats.setGiveFeedback(true)
            } else {
                // TODO Improve this adding a toast or something
            }
            visibility = View.GONE
        }
        negativeButton.setText(com.jdroid.android.about.R.string.jdroid_rateAppNotThanks)
        negativeButton.setOnClickListener {
            visibility = View.GONE
            AbstractApplication.get().coreAnalyticsSender.trackGiveFeedback(false)
            RateAppStats.setGiveFeedback(false)
        }
    }

    private fun googlePlayView() {
        rateAppTitle.text = LocalizationUtils.getString(com.jdroid.android.about.R.string.jdroid_rateAppGooglePlay)
        positiveButton.setText(com.jdroid.android.about.R.string.jdroid_rateAppOkSure)
        positiveButton.setOnClickListener {
            GooglePlayUtils.launchAppDetails()
            visibility = View.GONE
            AbstractApplication.get().coreAnalyticsSender.trackRateOnGooglePlay(true)
            RateAppStats.setRateOnGooglePlay(true)
        }
        negativeButton.setText(com.jdroid.android.about.R.string.jdroid_rateAppNotThanks)
        negativeButton.setOnClickListener {
            visibility = View.GONE
            AbstractApplication.get().coreAnalyticsSender.trackRateOnGooglePlay(false)
            RateAppStats.setRateOnGooglePlay(false)
        }
    }
}
