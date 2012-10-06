package com.jdroid.javaweb.search;

import java.util.Iterator;
import java.util.List;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import com.google.common.collect.Lists;
import com.jdroid.java.exception.UnexpectedException;
import com.jdroid.javaweb.search.Pager;

public class PagerTest {
	
	@DataProvider(name = "wrongPageScenarios")
	protected Iterator<Object[]> wrongPageScenarios() {
		List<Object[]> scenarios = Lists.newArrayList();
		scenarios.add(new Object[] { -100 });
		scenarios.add(new Object[] { 0 });
		return scenarios.iterator();
	}
	
	@DataProvider(name = "wrongPageSizeScenarios")
	protected Iterator<Object[]> wrongPageSizeScenarios() {
		List<Object[]> scenarios = Lists.newArrayList();
		scenarios.add(new Object[] { -100 });
		scenarios.add(new Object[] { 0 });
		return scenarios.iterator();
	}
	
	@DataProvider(name = "totalPagesScenarios")
	protected Iterator<Object[]> totalPagesScenarios() {
		List<Object[]> scenarios = Lists.newArrayList();
		scenarios.add(new Object[] { 20, 20L, 1 });
		scenarios.add(new Object[] { 20, 21L, 2 });
		scenarios.add(new Object[] { 20, 19L, 1 });
		return scenarios.iterator();
	}
	
	@DataProvider(name = "offsetScenarios")
	protected Iterator<Object[]> offsetScenarios() {
		List<Object[]> scenarios = Lists.newArrayList();
		scenarios.add(new Object[] { 1, 20, 0 });
		scenarios.add(new Object[] { 2, 20, 20 });
		scenarios.add(new Object[] { 3, 20, 40 });
		return scenarios.iterator();
	}
	
	@Test(dataProvider = "wrongPageScenarios", expectedExceptions = { UnexpectedException.class })
	public void wrongPage(Integer page) {
		new Pager(page, 10);
	}
	
	@Test(dataProvider = "wrongPageSizeScenarios", expectedExceptions = { UnexpectedException.class })
	public void wrongPageSize(Integer size) {
		new Pager(1, size);
	}
	
	@Test(dataProvider = "totalPagesScenarios")
	public void totalPages(Integer size, Long total, Integer pages) {
		Pager pager = new Pager(1, size);
		Assert.assertEquals(pager.getMaxPages(total), pages);
	}
	
	@Test(dataProvider = "offsetScenarios")
	public void offset(Integer page, Integer size, Integer offset) {
		Pager pager = new Pager(page, size);
		Assert.assertEquals(pager.getOffset(), offset);
		Assert.assertNotNull(pager);
	}
	
}
