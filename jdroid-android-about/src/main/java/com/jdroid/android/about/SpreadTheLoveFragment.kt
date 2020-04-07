package com.jdroid.android.about

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.jdroid.android.application.AbstractApplication
import com.jdroid.android.facebook.FacebookHelper
import com.jdroid.android.fragment.AbstractFragment
import com.jdroid.android.google.GooglePlayUtils
import com.jdroid.android.instagram.InstagramHelper
import com.jdroid.android.linkedin.LinkedInHelper
import com.jdroid.android.share.HangoutsSharingItem
import com.jdroid.android.share.MoreSharingItem
import com.jdroid.android.share.ShareView
import com.jdroid.android.share.SharingData
import com.jdroid.android.share.SharingDataItem
import com.jdroid.android.share.SharingItem
import com.jdroid.android.share.SharingMedium
import com.jdroid.android.share.SmsSharingItem
import com.jdroid.android.share.TelegramSharingItem
import com.jdroid.android.share.TwitterSharingItem
import com.jdroid.android.share.WhatsAppSharingItem
import com.jdroid.android.social.twitter.TwitterHelper
import com.jdroid.java.collections.Maps

abstract class SpreadTheLoveFragment : AbstractFragment() {

    override fun getContentFragmentLayout(): Int? {
        return R.layout.jdroid_spread_the_love_fragment
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val followUsItems = getFollowUsItems()

        val followUsContainer = findView<ViewGroup>(R.id.followUsContainer)

        for (item in followUsItems) {
            val itemViewGroup = LayoutInflater.from(activity).inflate(R.layout.jdroid_spread_the_love_follow_us_item, followUsContainer, false) as ViewGroup
            itemViewGroup.setOnClickListener { item.onSelected(requireActivity()) }
            (itemViewGroup.findViewById<View>(R.id.image) as ImageView).setImageResource(item.imageResId)
            (itemViewGroup.findViewById<View>(R.id.title) as TextView).setText(item.titleResId)
            followUsContainer.addView(itemViewGroup)
        }
        findView<View>(R.id.followUs).visibility = if (followUsItems.isEmpty()) View.GONE else View.VISIBLE

        val sharingDataItemsMap = Maps.newHashMap<String, SharingDataItem>()
        sharingDataItemsMap[SharingMedium.TWITTER.mediumName] = SharingDataItem(getTwitterShareText())
        sharingDataItemsMap[SharingMedium.WHATSAPP.mediumName] = SharingDataItem(getWhatsAppShareText())
        sharingDataItemsMap[SharingMedium.TELEGRAM.mediumName] = SharingDataItem(getTelegramShareText())
        sharingDataItemsMap[SharingMedium.HANGOUTS.mediumName] = SharingDataItem(getHangoutsShareText())
        sharingDataItemsMap[SharingMedium.SMS.mediumName] = SharingDataItem(getSmsShareText())
        val sharingData = SharingData(getShareKey(), sharingDataItemsMap, SharingDataItem(getDefaultShareSubject(), getDefaultShareText()))

        val sharingItems = mutableListOf<SharingItem>()
        sharingItems.add(TwitterSharingItem(sharingData))
        sharingItems.add(WhatsAppSharingItem(sharingData))
        sharingItems.add(TelegramSharingItem(sharingData))
        sharingItems.add(HangoutsSharingItem(sharingData))
        sharingItems.add(SmsSharingItem(sharingData))

        val displayShareTitle = ShareView.initShareSection(requireActivity(), sharingItems, MoreSharingItem(sharingData))
        if (!displayShareTitle) {
            findView<View>(R.id.shareSectionTitle).visibility = View.GONE
        }
    }

    protected open fun getFacebookPageId(): String? {
        return AbstractApplication.get().appContext.facebookPageId
    }

    protected open fun getTwitterAccount(): String? {
        return AbstractApplication.get().appContext.twitterAccount
    }

    protected open fun getInstagramAccount(): String? {
        return AbstractApplication.get().appContext.instagramAccount
    }

    protected open fun getLinkedInCompanyPageId(): String? {
        return AbstractApplication.get().appContext.linkedInCompanyPageId
    }

    protected open fun getShareKey(): String {
        return GooglePlayUtils.getGooglePlayLink()
    }

    protected open fun getDefaultShareSubject(): String {
        return getString(R.string.jdroid_appName)
    }

    protected abstract fun getDefaultShareText(): String

    protected open fun getTwitterShareText(): String {
        return getDefaultShareText()
    }

    protected open fun getWhatsAppShareText(): String {
        return getDefaultShareText()
    }

    protected open fun getTelegramShareText(): String {
        return getDefaultShareText()
    }

    protected open fun getHangoutsShareText(): String {
        return getDefaultShareText()
    }

    protected open fun getSmsShareText(): String {
        return getDefaultShareText()
    }

    protected open fun getFollowUsItems(): List<FollowUsItem> {
        val items = mutableListOf<FollowUsItem>()
        if (getFacebookPageId() != null) {
            items.add(object : FollowUsItem(R.drawable.jdroid_ic_facebook_24dp, R.string.jdroid_facebook) {
                override fun onSelected(activity: Activity) {
                    FacebookHelper.openPage(getFacebookPageId()!!)
                }
            })
        }

        if (getTwitterAccount() != null) {
            items.add(object : FollowUsItem(R.drawable.jdroid_ic_twitter_24dp, R.string.jdroid_twitter) {
                override fun onSelected(activity: Activity) {
                    TwitterHelper.openProfile(getTwitterAccount()!!)
                }
            })
        }

        if (getInstagramAccount() != null) {
            items.add(object : FollowUsItem(R.drawable.jdroid_ic_instagram_24dp, R.string.jdroid_instagram) {
                override fun onSelected(activity: Activity) {
                    InstagramHelper.openProfile(getInstagramAccount()!!)
                }
            })
        }

        if (getLinkedInCompanyPageId() != null) {
            items.add(object : FollowUsItem(R.drawable.jdroid_ic_linkedin_24dp, R.string.jdroid_linkedin) {
                override fun onSelected(activity: Activity) {
                    LinkedInHelper.openCompanyPage(getLinkedInCompanyPageId()!!)
                }
            })
        }
        return items
    }
}
