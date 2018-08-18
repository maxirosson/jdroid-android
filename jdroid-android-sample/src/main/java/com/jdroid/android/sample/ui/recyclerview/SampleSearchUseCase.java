package com.jdroid.android.sample.ui.recyclerview;

import com.jdroid.android.recycler.pagination.SearchUseCase;
import com.jdroid.android.search.SortingType;
import com.jdroid.java.collections.Lists;
import com.jdroid.java.concurrent.ExecutorUtils;
import com.jdroid.java.search.PagedResult;

import java.util.List;

public class SampleSearchUseCase extends SearchUseCase<String> {

	private List<String> results = Lists.newArrayList();

	public SampleSearchUseCase() {
		for (int i = 1; i < 300; i++) {
			results.add("word" + i);
		}
	}

	@Override
	protected PagedResult<String> doSearch(String searchValue, int page, int pageSize, SortingType sortingType) {
		List<String> pagedResults = Lists.newArrayList();
		Boolean isLastPage = false;
		int from = (page - 1) * pageSize;
		int to = from + pageSize;
		if (to >= results.size()) {
			to = results.size();
			isLastPage = true;
		}

		for (String each : results.subList(from, to)) {
			if (each.contains(searchValue)) {
				pagedResults.add(each);
			}
		}
		PagedResult<String> pagedResult = new PagedResult<>(pagedResults, isLastPage);
		ExecutorUtils.sleep(5);
		return pagedResult;
	}
}
