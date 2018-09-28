package com.jdroid.android.recycler.pagination;

import androidx.annotation.WorkerThread;

import com.jdroid.android.search.SortingType;
import com.jdroid.android.usecase.AbstractUseCase;
import com.jdroid.java.search.PagedResult;
import com.jdroid.java.utils.LoggerUtils;

import org.slf4j.Logger;

import java.util.List;

/**
 * @param <T>
 */
public abstract class PaginatedUseCase<T> extends AbstractUseCase {

	private static final long serialVersionUID = 4321782756459414981L;

	public enum PaginatedUseCaseMode {
		INITIAL_LOAD,
		PAGINATION,
		SORTING;
	}

	private final static Logger LOGGER = LoggerUtils.getLogger(PaginatedUseCase.class);

	private PaginatedUseCaseMode paginatedUseCaseMode = PaginatedUseCaseMode.INITIAL_LOAD;
	private int page = 1;
	private int pageSize = 50;
	private SortingType sortingType;

	private PagedResult<T> pagedResult;

	@WorkerThread
	@Override
	protected final void doExecute() {

		if (paginatedUseCaseMode.equals(PaginatedUseCaseMode.PAGINATION)) {
			page++;
		} else {
			page = 1;
		}

		try {
			pagedResult = doPopulate(page, pageSize, sortingType);
			LOGGER.debug("Results: " + pagedResult.getResults().size() + " / Page: " + page + " / Sorting: "
				+ sortingType);
		} catch (RuntimeException e) {
			if (paginatedUseCaseMode.equals(PaginatedUseCaseMode.PAGINATION)) {
				page--;
			}
			throw e;
		}
	}

	protected abstract PagedResult<T> doPopulate(int page, int pageSize, SortingType sortingType);

	public void markAsPaginating() {
		paginatedUseCaseMode = PaginatedUseCaseMode.PAGINATION;
	}

	public void reset() {
		paginatedUseCaseMode = PaginatedUseCaseMode.INITIAL_LOAD;
	}

	public Boolean isInitialLoad() {
		return paginatedUseCaseMode.equals(PaginatedUseCaseMode.INITIAL_LOAD);
	}

	public Boolean isPaginating() {
		return paginatedUseCaseMode.equals(PaginatedUseCaseMode.PAGINATION);
	}

	/**
	 * @return Whether this list is on the last page or not
	 */
	public boolean isLastPage() {
		return pagedResult.isLastPage();
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	/**
	 * @return the results
	 */
	public List<T> getResults() {
		return pagedResult.getResults();
	}

	/**
	 * @param sortingType the sortingType to set
	 */
	public void setSortingType(SortingType sortingType) {
		this.sortingType = sortingType;
	}

}
