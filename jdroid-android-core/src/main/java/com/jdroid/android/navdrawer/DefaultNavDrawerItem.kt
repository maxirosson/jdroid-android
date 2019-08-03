package com.jdroid.android.navdrawer

import android.app.Activity
import android.content.Intent

import com.jdroid.android.activity.AbstractFragmentActivity

class DefaultNavDrawerItem @JvmOverloads constructor(private val itemId: Int, private val activityClass: Class<out Activity>? = null) : NavDrawerItem {

    override fun startActivity(currentActivity: AbstractFragmentActivity?) {
        if (currentActivity != null && currentActivity.javaClass != activityClass) {
            val intent = Intent(currentActivity, activityClass)
            currentActivity.startActivity(intent)
        }
    }

    override fun matchesActivity(activity: Activity): Boolean {
        return activity.javaClass == activityClass
    }

    override fun getItemId(): Int {
        return itemId
    }

    override fun getActivityClass(): Class<out Activity>? {
        return activityClass
    }
}
