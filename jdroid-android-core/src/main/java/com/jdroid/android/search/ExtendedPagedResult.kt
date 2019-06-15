package com.jdroid.android.search

import com.jdroid.java.collections.Lists
import com.jdroid.java.search.PagedResult

/**
 * @param <T> The list item.
</T> */
class ExtendedPagedResult<T> : PagedResult<T>() {

    private val extraResults = Lists.newArrayList<T>()

    /**
     * Adds a result item to the extra list.
     *
     * @param result The result to add.
     */
    fun addExtraResult(result: T) {
        extraResults.add(result)
    }

    /**
     * Adds result items to the extra list.
     *
     * @param results The results to add.
     */
    fun addExtraResults(results: Collection<T>) {
        extraResults.addAll(results)
    }

    /**
     * @return the extraResults
     */
    fun getExtraResults(): List<T> {
        return extraResults
    }
}
