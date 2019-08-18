package com.jdroid.android.about

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.MainThread
import androidx.recyclerview.widget.RecyclerView
import com.jdroid.android.about.feedback.RateAppStats
import com.jdroid.android.activity.ActivityLauncher
import com.jdroid.android.application.AbstractApplication
import com.jdroid.android.recycler.AbstractRecyclerFragment
import com.jdroid.android.recycler.FooterRecyclerViewType
import com.jdroid.android.recycler.RecyclerViewAdapter
import com.jdroid.android.recycler.RecyclerViewContainer
import com.jdroid.android.recycler.RecyclerViewType
import com.jdroid.android.utils.AppUtils
import com.jdroid.android.utils.ExternalAppsUtils

open class AboutFragment : AbstractRecyclerFragment() {

    private val aboutItems = mutableListOf<Any>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Header
        aboutItems.add("")

        val website = getWebsite()
        if (website != null) {
            aboutItems.add(object : AboutItem(R.drawable.jdroid_ic_website_black_24dp, R.string.jdroid_website) {
                override fun onSelected(activity: Activity) {
                    ExternalAppsUtils.openUrl(website)
                }
            })
        }

        val contactUsEmailAddress = getContactUsEmail()
        if (contactUsEmailAddress != null) {
            aboutItems.add(object : AboutItem(R.drawable.jdroid_ic_contact_us_black_24dp, R.string.jdroid_contactUs) {

                override fun onSelected(activity: Activity) {
                    if (ExternalAppsUtils.openEmail(contactUsEmailAddress)) {
                        AboutAppModule.get().getModuleAnalyticsSender().trackContactUs()
                    } else {
                        // TODO Improve this adding a toast or something
                    }
                }
            })
        }

        if (AboutAppModule.get().aboutContext.getSpreadTheLoveFragmentClass() != null) {
            aboutItems.add(object : AboutItem(R.drawable.jdroid_ic_spread_the_love_black_24dp, R.string.jdroid_spreadTheLove) {

                override fun onSelected(activity: Activity) {
                    ActivityLauncher.startActivity(activity, SpreadTheLoveActivity::class.java)
                }
            })
        }
        aboutItems.add(object : AboutItem(R.drawable.jdroid_ic_libraries_black_24dp, R.string.jdroid_libraries) {

            override fun onSelected(activity: Activity) {
                ActivityLauncher.startActivity(activity, LibrariesActivity::class.java)
            }
        })

        val privacyPolicyUrlRemoteConfigParameter = AbstractApplication.get().appContext.privacyPolicyUrl
        if (privacyPolicyUrlRemoteConfigParameter != null) {
            val privacyPolicyUrl = AbstractApplication.get().remoteConfigLoader!!.getString(privacyPolicyUrlRemoteConfigParameter)!!
            aboutItems.add(object : AboutItem(R.drawable.jdroid_ic_privacy_policy_black_24dp, R.string.jdroid_privacyPolicy) {

                override fun onSelected(activity: Activity) {
                    ExternalAppsUtils.openUrl(privacyPolicyUrl)
                }
            })
        }

        if (AboutAppModule.get().aboutContext.isBetaTestingEnabled()) {
            aboutItems.add(object : AboutItem(R.drawable.jdroid_ic_beta_black_24dp, R.string.jdroid_beta) {

                override fun onSelected(activity: Activity) {
                    ExternalAppsUtils.openUrl(AboutAppModule.get().aboutContext.getBetaTestingUrl())
                }
            })
        }
        aboutItems.addAll(getCustomAboutItems())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerViewAdapter = RecyclerViewAdapter(listOf(HeaderRecyclerViewType(), AboutRecyclerViewType()), aboutItems)
        if (rateAppViewEnabled()) {
            recyclerViewAdapter.setFooter(AboutFooterRecyclerViewType())
        }
        setAdapter(recyclerViewAdapter)
    }

    protected open fun getWebsite(): String? {
        return AbstractApplication.get().appContext.website
    }

    protected open fun getContactUsEmail(): String? {
        return AbstractApplication.get().appContext.contactUsEmail
    }

    protected open fun getCustomAboutItems(): List<AboutItem> {
        return listOf()
    }

    @MainThread
    protected fun rateAppViewEnabled(): Boolean {
        return RateAppStats.displayRateAppView()
    }

    override fun isDividerItemDecorationEnabled(): Boolean {
        return true
    }

    inner class HeaderRecyclerViewType : RecyclerViewType<String, HeaderItemHolder>() {

        override fun getItemClass(): Class<String> {
            return String::class.java
        }

        override fun getLayoutResourceId(): Int {
            return R.layout.jdroid_about_header_view
        }

        override fun createViewHolderFromView(view: View): RecyclerView.ViewHolder {
            val holder = HeaderItemHolder(view)
            holder.appIcon = findView(view, R.id.appIcon)
            holder.appName = findView(view, R.id.appName)
            holder.version = findView(view, R.id.version)
            return holder
        }

        override fun fillHolderFromItem(item: String, holder: HeaderItemHolder) {
            holder.appIcon.setImageResource(AbstractApplication.get().launcherIconResId)
            holder.appName.text = AbstractApplication.get().appName
            holder.version.text = getString(R.string.jdroid_version, AppUtils.getVersionName())
            if (AbstractApplication.get().appContext.isDebugSettingsEnabled) {
                holder.appIcon.setOnClickListener { AbstractApplication.get().debugContext.launchActivityDebugSettingsActivity(activity) }
            }
        }

        override fun getRecyclerViewContainer(): RecyclerViewContainer {
            return this@AboutFragment
        }
    }

    class HeaderItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        lateinit var appIcon: ImageView
        lateinit var appName: TextView
        lateinit var version: TextView
    }

    inner class AboutFooterRecyclerViewType : FooterRecyclerViewType() {

        override fun getLayoutResourceId(): Int {
            return R.layout.jdroid_about_footer_view
        }

        override fun getRecyclerViewContainer(): RecyclerViewContainer {
            return this@AboutFragment
        }
    }

    inner class AboutRecyclerViewType : RecyclerViewType<AboutItem, AboutItemHolder>() {

        override fun getItemClass(): Class<AboutItem> {
            return AboutItem::class.java
        }

        override fun getLayoutResourceId(): Int {
            return R.layout.jdroid_default_item
        }

        override fun createViewHolderFromView(view: View): RecyclerView.ViewHolder {
            val holder = AboutItemHolder(view)
            holder.image = findView(view, R.id.image)
            holder.name = findView(view, R.id.name)
            return holder
        }

        override fun fillHolderFromItem(item: AboutItem, holder: AboutItemHolder) {
            holder.image.setImageResource(item.iconResId)
            holder.name.setText(item.nameResId)
        }

        public override fun onItemSelected(item: AboutItem, view: View) {
            item.onSelected(activity)
        }

        override fun getRecyclerViewContainer(): RecyclerViewContainer {
            return this@AboutFragment
        }
    }

    class AboutItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        lateinit var image: ImageView
        lateinit var name: TextView
    }
}
