package com.jdroid.android.sample.ui.firebase.dynamiclinks

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import com.jdroid.android.concurrent.AppExecutors
import com.jdroid.android.firebase.dynamiclink.FirebaseDynamicLinksAppContext
import com.jdroid.android.fragment.AbstractFragment
import com.jdroid.android.sample.R
import com.jdroid.android.utils.AppUtils
import com.jdroid.java.firebase.dynamiclinks.ShortDynamicLinkService
import com.jdroid.java.firebase.dynamiclinks.domain.AnalyticsInfo
import com.jdroid.java.firebase.dynamiclinks.domain.AndroidInfo
import com.jdroid.java.firebase.dynamiclinks.domain.DynamicLink
import com.jdroid.java.firebase.dynamiclinks.domain.DynamicLinkInfo
import com.jdroid.java.firebase.dynamiclinks.domain.GooglePlayAnalytics
import com.jdroid.java.firebase.dynamiclinks.domain.SuffixOption
import com.jdroid.java.utils.TypeUtils

class DynamicLinksFragment : AbstractFragment() {

    private lateinit var linkUrlTextView: TextView
    private lateinit var minVersionCodeTextView: TextView
    private lateinit var fallbackLinkTextView: TextView
    private lateinit var customAppLocationTextView: TextView
    private lateinit var utmSourceTextView: TextView
    private lateinit var utmMediumTextView: TextView
    private lateinit var utmCampaignTextView: TextView
    private lateinit var utmTermTextView: TextView
    private lateinit var utmContentTextView: TextView
    private lateinit var unguessableCheckBox: CheckBox

    private lateinit var shortLinkTextView: TextView
    private lateinit var longLinkTextView: TextView

    override fun getContentFragmentLayout(): Int? {
        return R.layout.dynamic_links_fragment
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        linkUrlTextView = findView(R.id.linkUrl)
        linkUrlTextView.text = "http://www.jdroidtools.com/"

        minVersionCodeTextView = findView(R.id.minVersionCode)
        fallbackLinkTextView = findView(R.id.fallbackLink)
        customAppLocationTextView = findView(R.id.customAppLocation)
        utmSourceTextView = findView(R.id.utmSource)
        utmMediumTextView = findView(R.id.utmMedium)
        utmCampaignTextView = findView(R.id.utmCampaign)
        utmTermTextView = findView(R.id.utmTerm)
        utmContentTextView = findView(R.id.utmContent)
        unguessableCheckBox = findView(R.id.unguessable)

        shortLinkTextView = findView(R.id.shortLink)
        longLinkTextView = findView(R.id.longLink)

        findView<View>(R.id.buildLink).setOnClickListener {
            AppExecutors.networkIOExecutor.execute {
                val dynamicLink = DynamicLink()

                val dynamicLinkInfo = DynamicLinkInfo()
                dynamicLinkInfo.dynamicLinkDomain = FirebaseDynamicLinksAppContext.getDynamicLinksDomain()
                dynamicLinkInfo.link = linkUrlTextView.text.toString()

                val androidInfo = AndroidInfo()
                androidInfo.androidPackageName = AppUtils.getApplicationId()
                if (minVersionCodeTextView.text.isNotEmpty()) {
                    androidInfo.setAndroidMinPackageVersionCode(TypeUtils.getInteger(minVersionCodeTextView.text.toString())!!)
                }
                androidInfo.androidFallbackLink = fallbackLinkTextView.text.toString()
                androidInfo.androidLink = customAppLocationTextView.text.toString()
                dynamicLinkInfo.androidInfo = androidInfo

                val analyticsInfo = AnalyticsInfo()
                val googlePlayAnalytics = GooglePlayAnalytics()
                googlePlayAnalytics.utmSource = utmSourceTextView.text.toString()
                googlePlayAnalytics.utmMedium = utmMediumTextView.text.toString()
                googlePlayAnalytics.utmCampaign = utmCampaignTextView.text.toString()
                googlePlayAnalytics.utmTerm = utmTermTextView.text.toString()
                googlePlayAnalytics.utmContent = utmContentTextView.text.toString()
                analyticsInfo.googlePlayAnalytics = googlePlayAnalytics
                dynamicLinkInfo.analyticsInfo = analyticsInfo

                dynamicLink.dynamicLinkInfo = dynamicLinkInfo

                executeOnUIThread(Runnable { longLinkTextView.text = dynamicLink.build() })
            }
        }

        findView<View>(R.id.shortenLink).setOnClickListener {
            val longLink = longLinkTextView.text.toString()
            if (longLink.isNotEmpty()) {
                AppExecutors.networkIOExecutor.execute {
                    val dynamicLinkResponse = ShortDynamicLinkService().getShortDynamicLink(
                        FirebaseDynamicLinksAppContext.getWebApiKey(), longLink,
                        if (unguessableCheckBox.isChecked) SuffixOption.UNGUESSABLE else SuffixOption.SHORT
                    )
                    executeOnUIThread(Runnable { shortLinkTextView.text = dynamicLinkResponse.shortLink })
                }
            }
        }
    }
}
