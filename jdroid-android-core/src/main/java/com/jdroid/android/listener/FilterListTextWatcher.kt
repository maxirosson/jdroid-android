package com.jdroid.android.listener

import android.text.Editable
import android.text.TextWatcher
import android.widget.ArrayAdapter

/**
 * Listener used to filter a list based on a text input.
 *
 * @param <T> The type of items in the list.
</T> */
abstract class FilterListTextWatcher<T> : TextWatcher {

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
        // Nothing here by default.
    }

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        // Nothing here by default.
    }

    override fun afterTextChanged(prefix: Editable) {
        getFilterableArrayAdapter()?.let {
            it.filter.filter(prefix)
            doAfterTextChanged(prefix.toString())
        }
    }

    fun doAfterTextChanged(prefix: String) {
        // Do Nothing
    }

    /**
     * @return The [ArrayAdapter] of the list to filter.
     */
    abstract fun getFilterableArrayAdapter(): ArrayAdapter<T>?
}
