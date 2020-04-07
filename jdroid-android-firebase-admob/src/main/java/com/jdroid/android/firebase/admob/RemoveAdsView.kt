package com.jdroid.android.firebase.admob

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.annotation.WorkerThread
import com.jdroid.android.application.AbstractApplication
import com.jdroid.android.context.UsageStats
import java.util.concurrent.TimeUnit

class RemoveAdsView : RelativeLayout {

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
        val view = LayoutInflater.from(context).inflate(R.layout.jdroid_remove_ads_view, this, true)
        (view.findViewById<View>(R.id.icon) as ImageView).setImageResource(AbstractApplication.get().launcherIconResId)
    }

    override fun setOnClickListener(removeAdsClickListener: OnClickListener?) {
        super.setOnClickListener { v ->
            removeAdsClickListener?.onClick(v)
            AdMobAppModule.get().getModuleAnalyticsSender().trackRemoveAdsBannerClicked()
        }
    }

    companion object {

        @WorkerThread
        fun displayRemoveAdsView(): Boolean {
            return TimeUnit.MILLISECONDS.toDays(UsageStats.getFirstAppLoadTimestamp()) > 3
        }
    }
}
