package com.jdroid.android.loading

import com.jdroid.android.activity.ActivityIf

interface ActivityLoading {

    fun show(activityIf: ActivityIf)

    fun dismiss(activityIf: ActivityIf)
}
