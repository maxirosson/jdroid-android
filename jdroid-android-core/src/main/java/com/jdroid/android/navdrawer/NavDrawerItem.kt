package com.jdroid.android.navdrawer

import android.app.Activity

import com.jdroid.android.activity.AbstractFragmentActivity

interface NavDrawerItem {

    fun getItemId(): Int

    fun startActivity(currentActivity: AbstractFragmentActivity?)

    fun matchesActivity(activity: Activity): Boolean

    fun getActivityClass(): Class<out Activity>?
}
