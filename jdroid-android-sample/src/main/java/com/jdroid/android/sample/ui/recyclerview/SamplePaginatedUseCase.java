package com.jdroid.android.sample.ui.recyclerview;

import com.jdroid.android.recycler.pagination.PaginatedUseCase;
import com.jdroid.android.search.SortingType;
import com.jdroid.java.collections.Lists;
import com.jdroid.java.concurrent.ExecutorUtils;
import com.jdroid.java.search.PagedResult;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class SamplePaginatedUseCase extends PaginatedUseCase<String> {

	private List<String> results = Lists.newArrayList();

	public SamplePaginatedUseCase() {
		for (int i = 1; i < 300; i++) {
			results.add(i + "");
		}
	}

	@Override
	protected PagedResult<String> doPopulate(int page, int pageSize, SortingType sortingType) {
		List<String> pagedResults = Lists.newArrayList();
		Boolean isLastPage = false;
		int from = (page - 1) * pageSize;
		int to = from + pageSize;
		if (to >= results.size()) {
			to = results.size();
			isLastPage = true;
		}
		pagedResults.addAll(results.subList(from, to));
		PagedResult<String> pagedResult = new PagedResult<>(pagedResults, isLastPage);
		ExecutorUtils.sleep(5, TimeUnit.SECONDS);
		return pagedResult;
	}

}
