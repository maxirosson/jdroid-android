package com.jdroid.android.recycler.pagination;

import android.support.annotation.WorkerThread;

import com.jdroid.android.search.SortingType;
import com.jdroid.java.collections.Sets;
import com.jdroid.java.search.PagedResult;

import java.util.Set;

/**
 * @param <T> item to search for
 */
public abstract class SearchUseCase<T> extends PaginatedUseCase<T> {
	
	private static final long serialVersionUID = -6921635595717987983L;
	
	private String searchValue;
	private Set<T> selectedItems = Sets.newHashSet();
	
	@Override
	protected final PagedResult<T> doPopulate(int page, int pageSize, SortingType sortingType) {
		return doSearch(searchValue, page, pageSize, sortingType);
	}

	@WorkerThread
	protected abstract PagedResult<T> doSearch(String searchValue, int page, int pageSize, SortingType sortingType);
	
	/**
	 * @param searchValue the searchValue to set
	 */
	public void setSearchValue(String searchValue) {
		this.searchValue = searchValue;
	}
	
	public Set<T> getSelectedItems() {
		return selectedItems;
	}
	
	public void setSelectedItems(Set<T> selectedItems) {
		this.selectedItems = selectedItems;
	}
	
	/**
	 * @return the searchValue
	 */
	public String getSearchValue() {
		return searchValue;
	}
}
