package com.jdroid.android.search;

import java.util.Collection;
import java.util.List;
import org.slf4j.Logger;
import com.jdroid.java.exception.AbstractException;
import com.jdroid.java.search.PagedResult;
import com.jdroid.java.utils.ExecutorUtils;
import com.jdroid.java.utils.LoggerUtils;

/**
 * 
 * @author Maxi Rosson
 * @param <E>
 */
public abstract class SearchResult<E> {
	
	private final static Logger LOGGER = LoggerUtils.getLogger(SearchResult.class);
	
	private final static int PAGE_SIZE = 20;
	private SortingListener<E> sortingListener;
	private PaginationListener<E> paginationListener;
	private int pageOffset = 1;
	private SortingType sortingType;
	private PagedResult<E> pagedResult;
	
	public static <T> SearchResult<T> getInstance(Collection<T> items) {
		final PagedResult<T> pagedResult = new PagedResult<T>();
		pagedResult.addResults(items);
		return new SearchResult<T>() {
			
			@Override
			protected PagedResult<T> doPopulate(int pageOffset, int pageSize, SortingType sortingType) {
				return pagedResult;
			}
		};
	}
	
	/**
	 * @param defaultSortingType
	 */
	public SearchResult(SortingType defaultSortingType) {
		sortingType = defaultSortingType;
		populate();
	}
	
	public SearchResult() {
		this(null);
	}
	
	private List<E> populate() {
		pagedResult = doPopulate(pageOffset, PAGE_SIZE, sortingType);
		LOGGER.debug("Results: " + pagedResult.getResults().size() + " / Page Offset: " + pageOffset + " / Sorting: "
				+ sortingType);
		return pagedResult.getResults();
	}
	
	public void nextPage() {
		ExecutorUtils.execute(new Runnable() {
			
			@Override
			public void run() {
				synchronized (SearchResult.class) {
					paginationListener.onStartPagination();
					pageOffset++;
					try {
						List<E> items = populate();
						paginationListener.onFinishSuccessfulPagination(items);
					} catch (AbstractException e) {
						pageOffset--;
						paginationListener.onFinishInvalidPagination(e);
					}
				}
			}
		});
	}
	
	public void sortBy(SortingType sortingType) {
		sortingListener.onStartSorting();
		pageOffset = 1;
		this.sortingType = sortingType;
		
		ExecutorUtils.execute(new Runnable() {
			
			@Override
			public void run() {
				synchronized (SearchResult.class) {
					try {
						List<E> items = populate();
						sortingListener.onFinishSuccessfulSorting(items);
					} catch (AbstractException e) {
						sortingListener.onFinishInvalidSorting(e);
					}
				}
			}
		});
	}
	
	/**
	 * @return Whether this list is on the last page or not
	 */
	public boolean isLastPage() {
		return pagedResult.isLastPage();
	}
	
	protected abstract PagedResult<E> doPopulate(int pageOffset, int pageSize, SortingType sortingType);
	
	/**
	 * @param sortingListener the sortingListener to set
	 */
	public void setSortingListener(SortingListener<E> sortingListener) {
		this.sortingListener = sortingListener;
	}
	
	/**
	 * @param paginationListener the paginationListener to set
	 */
	public void setPaginationListener(PaginationListener<E> paginationListener) {
		this.paginationListener = paginationListener;
	}
	
	/**
	 * @return the results
	 */
	public List<E> getResults() {
		return pagedResult.getResults();
	}
	
	public interface SortingListener<E> {
		
		/**
		 * Called before the call to sorting the list starts
		 */
		public void onStartSorting();
		
		/**
		 * Called after the successful call to sorting the list
		 * 
		 * @param items
		 */
		public void onFinishSuccessfulSorting(List<E> items);
		
		/**
		 * Called after an invalid call to sorting the list
		 * 
		 * @param androidException The {@link AbstractException} with the error
		 */
		public void onFinishInvalidSorting(AbstractException androidException);
		
	}
	
	public interface PaginationListener<E> {
		
		/**
		 * Called before the call to paginate the list starts
		 */
		public void onStartPagination();
		
		/**
		 * Called after the successful call to paginate the list
		 * 
		 * @param items
		 */
		public void onFinishSuccessfulPagination(List<E> items);
		
		/**
		 * Called after an invalid call to paginate the list
		 * 
		 * @param androidException The {@link AbstractException} with the error
		 */
		public void onFinishInvalidPagination(AbstractException androidException);
		
	}
	
	/**
	 * @return the pagedResult
	 */
	public PagedResult<E> getPagedResult() {
		return pagedResult;
	}
}
