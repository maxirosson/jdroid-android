package com.jdroid.android.search;

import android.annotation.SuppressLint;
import android.content.SearchRecentSuggestionsProvider;

import com.jdroid.android.utils.AppUtils;

/**
 * Declare on your manifest as:
 * 
 * <pre>
 * &lt;provider android:name=".android.common.search.TwoLinesSuggestionsProvider"
 * 	android:authorities="[PackageName].TwoLinesSuggestionsProvider" />
 * </pre>
 * 
 */
@SuppressLint("Registered")
public class TwoLinesSuggestionsProvider extends SearchRecentSuggestionsProvider {
	
	public final static String AUTHORITY = AppUtils.getApplicationId() + ".TwoLinesSuggestionsProvider";
	public final static int MODE = DATABASE_MODE_QUERIES | DATABASE_MODE_2LINES;
	
	public TwoLinesSuggestionsProvider() {
		setupSuggestions(AUTHORITY, MODE);
	}
}