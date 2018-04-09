package com.jdroid.android.listener;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;

/**
 * Listener used to filter a list based on a text input.
 * 
 * @param <T> The type of items in the list.
 */
public abstract class FilterListTextWatcher<T> implements TextWatcher {
	
	/**
	 * @see android.text.TextWatcher#beforeTextChanged(java.lang.CharSequence, int, int, int)
	 */
	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		// Nothing here by default.
	}
	
	/**
	 * @see android.text.TextWatcher#onTextChanged(java.lang.CharSequence, int, int, int)
	 */
	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// Nothing here by default.
	}
	
	/**
	 * @see android.text.TextWatcher#afterTextChanged(android.text.Editable)
	 */
	@Override
	public void afterTextChanged(Editable prefix) {
		if (getFilterableArrayAdapter() != null) {
			getFilterableArrayAdapter().getFilter().filter(prefix);
			doAfterTextChanged(prefix.toString());
		}
	}
	
	public void doAfterTextChanged(String prefix) {
		// Do Nothing
	}
	
	/**
	 * @return The {@link ArrayAdapter} of the list to filter.
	 */
	public abstract ArrayAdapter<T> getFilterableArrayAdapter();
}
