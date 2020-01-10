package com.jdroid.android.search

import android.annotation.SuppressLint
import android.content.SearchRecentSuggestionsProvider

import com.jdroid.android.utils.AppUtils

/**
 * Declare on your manifest as:
 *
 * <pre>
 * &lt;provider android:name=".android.common.search.TwoLinesSuggestionsProvider"
 * android:authorities="[applicationId].TwoLinesSuggestionsProvider" />
</pre> *
 */
@SuppressLint("Registered")
class TwoLinesSuggestionsProvider : SearchRecentSuggestionsProvider() {

    init {
        setupSuggestions(AUTHORITY, MODE)
    }

    companion object {
        val AUTHORITY = AppUtils.getApplicationId() + ".TwoLinesSuggestionsProvider"
        const val MODE = DATABASE_MODE_QUERIES or DATABASE_MODE_2LINES
    }
}
