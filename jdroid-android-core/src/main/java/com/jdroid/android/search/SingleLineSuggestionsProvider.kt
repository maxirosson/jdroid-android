package com.jdroid.android.search

import android.annotation.SuppressLint
import android.content.SearchRecentSuggestionsProvider

import com.jdroid.android.utils.AppUtils

/**
 * Declare on your manifest as:
 *
 * <pre>
 * &lt;provider android:name=".android.common.search.SingleLineSuggestionsProvider"
 * android:authorities="[applicationId].SingleLineSuggestionsProvider" />
</pre> *
 */
@SuppressLint("Registered")
class SingleLineSuggestionsProvider : SearchRecentSuggestionsProvider() {

    init {
        setupSuggestions(AUTHORITY, MODE)
    }

    companion object {
        val AUTHORITY = AppUtils.getApplicationId() + ".SingleLineSuggestionsProvider"
        const val MODE = DATABASE_MODE_QUERIES
    }
}