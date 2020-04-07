package com.jdroid.android.provider

import android.content.Context

import com.jdroid.android.R

class RefreshActionProvider(context: Context) : TwoStateActionProvider(context) {

    override fun getFirstStateImageResId(): Int? {
        return R.drawable.jdroid_ic_refresh_white_24dp
    }

    override fun getFirstStateCheatSheetResId(): Int? {
        return R.string.jdroid_refresh
    }
}
