package com.jdroid.android.search;

import com.jdroid.java.collections.Lists;
import com.jdroid.java.search.PagedResult;

import java.util.Collection;
import java.util.List;

/**
 * @param <T> The list item.
 */
public class ExtendedPagedResult<T> extends PagedResult<T> {

	private List<T> extraResults = Lists.newArrayList();

	/**
	 * Adds a result item to the extra list.
	 *
	 * @param result The result to add.
	 */
	public void addExtraResult(T result) {
		extraResults.add(result);
	}

	/**
	 * Adds result items to the extra list.
	 *
	 * @param results The results to add.
	 */
	public void addExtraResults(Collection<T> results) {
		extraResults.addAll(results);
	}

	/**
	 * @return the extraResults
	 */
	public List<T> getExtraResults() {
		return extraResults;
	}
}
