package com.jdroid.android.sample.usecase;

import com.jdroid.android.usecase.AbstractUseCase;
import com.jdroid.java.collections.Lists;
import com.jdroid.java.concurrent.ExecutorUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class SampleItemsUseCase extends AbstractUseCase {

	private static final long serialVersionUID = -3206803568176386530L;

	private List<String> items;
	private List<Object> complexItems;

	@Override
	protected void doExecute() {

		// Wait 3 seconds to simulate a request
		ExecutorUtils.sleep(3, TimeUnit.SECONDS);

		items = Lists.newArrayList("one", "two", "three", "four", "five", "six", "seven", "eight", "nine", "ten", "eleven", "twelve", "thirteen", "fourteen", "fifteen", "sixteen");
		complexItems = Lists.<Object>newArrayList("one", "two", true, "three", 1, 2, "four", true, "five", "six", "seven", "eight", 3, "nine", "ten", "eleven", "twelve", 4, "thirteen", false, false, "fourteen", "fifteen", "sixteen");
	}

	public List<String> getItems() {
		return items;
	}

	public List<Object> getComplexItems() {
		return complexItems;
	}

	@Override
	protected Boolean timingTrackingEnabled() {
		return false;
	}
}
