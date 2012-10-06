package com.jdroid.javaweb.domain;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import com.google.common.collect.Lists;
import com.jdroid.java.utils.DateUtils;
import com.jdroid.javaweb.domain.DateRange;

/**
 * Test the different methods of {@link DateRange}
 * 
 */
public class DateRangeTest {
	
	/**
	 * @return The different scenarios for test {@link DateRange#isContainedInPeriod(Date, Date)}
	 */
	@DataProvider
	public Iterator<Object[]> getForIsBiggerThan() {
		List<Object[]> cases = Lists.newArrayList();
		cases.add(new Object[] { DateUtils.getDate(2009, Calendar.JANUARY, 10),
				DateUtils.getDate(2009, Calendar.JANUARY, 12), DateUtils.getDate(2009, Calendar.JANUARY, 10),
				DateUtils.getDate(2009, Calendar.JANUARY, 12), true });
		cases.add(new Object[] { DateUtils.getDate(2009, Calendar.JANUARY, 10),
				DateUtils.getDate(2009, Calendar.JANUARY, 12), DateUtils.getDate(2009, Calendar.JANUARY, 9),
				DateUtils.getDate(2009, Calendar.JANUARY, 12), true });
		cases.add(new Object[] { DateUtils.getDate(2009, Calendar.JANUARY, 10),
				DateUtils.getDate(2009, Calendar.JANUARY, 12), DateUtils.getDate(2009, Calendar.JANUARY, 10),
				DateUtils.getDate(2009, Calendar.JANUARY, 13), true });
		cases.add(new Object[] { DateUtils.getDate(2009, Calendar.JANUARY, 10),
				DateUtils.getDate(2009, Calendar.JANUARY, 12), DateUtils.getDate(2009, Calendar.JANUARY, 9),
				DateUtils.getDate(2009, Calendar.JANUARY, 13), true });
		cases.add(new Object[] { DateUtils.getDate(2009, Calendar.JANUARY, 10),
				DateUtils.getDate(2009, Calendar.JANUARY, 12), DateUtils.getDate(2009, Calendar.JANUARY, 11),
				DateUtils.getDate(2009, Calendar.JANUARY, 12), false });
		cases.add(new Object[] { DateUtils.getDate(2009, Calendar.JANUARY, 10),
				DateUtils.getDate(2009, Calendar.JANUARY, 12), DateUtils.getDate(2009, Calendar.JANUARY, 10),
				DateUtils.getDate(2009, Calendar.JANUARY, 11), false });
		cases.add(new Object[] { DateUtils.getDate(2009, Calendar.JANUARY, 10),
				DateUtils.getDate(2009, Calendar.JANUARY, 12), DateUtils.getDate(2009, Calendar.JANUARY, 11),
				DateUtils.getDate(2009, Calendar.JANUARY, 11), false });
		cases.add(new Object[] { DateUtils.getDate(2009, Calendar.JANUARY, 10),
				DateUtils.getDate(2009, Calendar.JANUARY, 12), DateUtils.getDate(2009, Calendar.JANUARY, 8),
				DateUtils.getDate(2009, Calendar.JANUARY, 9), false });
		return cases.iterator();
	}
	
	/**
	 * @param startDate The start date
	 * @param endDate The end date
	 * @param theStartDate The start date to compare
	 * @param theEndDate The end date to compare
	 * @param expectedResult The expected result
	 */
	@Test(dataProvider = "getForIsBiggerThan")
	public void isBiggerThan(Date startDate, Date endDate, Date theStartDate, Date theEndDate, boolean expectedResult) {
		DateRange dateRange = new DateRange(startDate, endDate);
		Assert.assertEquals(dateRange.isContainedInPeriod(theStartDate, theEndDate), expectedResult);
	}
	
	/**
	 * Data provider for the {@link DateRangeTest#overlapsTest(DateRange, DateRange, boolean)} method.
	 * 
	 * @return {@link Iterator} Contains the test cases.
	 */
	@DataProvider
	public Iterator<Object[]> overlapsDataProvider() {
		List<Object[]> cases = Lists.newArrayList();
		Date firstOfJan = DateUtils.getDate(2010, Calendar.JANUARY, 1);
		Date tenthOfJan = DateUtils.getDate(2010, Calendar.JANUARY, 10);
		Date twentiethOfJan = DateUtils.getDate(2010, Calendar.JANUARY, 20);
		Date lastOfJan = DateUtils.getDate(2010, Calendar.JANUARY, 31);
		
		/**
		 * Case 1:<br>
		 * |----|<br>
		 * |----|
		 */
		DateRange dateRange = new DateRange(firstOfJan, lastOfJan);
		DateRange dateRangeToCompare = new DateRange(firstOfJan, lastOfJan);
		boolean expectedResult = true;
		cases.add(new Object[] { dateRange, dateRangeToCompare, expectedResult });
		
		/**
		 * Case 2:<br>
		 * |----|---<br>
		 * ---|----|
		 */
		dateRange = new DateRange(firstOfJan, twentiethOfJan);
		dateRangeToCompare = new DateRange(tenthOfJan, lastOfJan);
		expectedResult = true;
		cases.add(new Object[] { dateRange, dateRangeToCompare, expectedResult });
		
		/**
		 * Case 3:<br>
		 * ---|----|<br>
		 * |----|---
		 */
		dateRange = new DateRange(tenthOfJan, lastOfJan);
		dateRangeToCompare = new DateRange(firstOfJan, twentiethOfJan);
		expectedResult = true;
		cases.add(new Object[] { dateRange, dateRangeToCompare, expectedResult });
		
		/**
		 * Case 4:<br>
		 * |----|<br>
		 * ---|-|
		 */
		dateRange = new DateRange(firstOfJan, lastOfJan);
		dateRangeToCompare = new DateRange(twentiethOfJan, lastOfJan);
		expectedResult = true;
		cases.add(new Object[] { dateRange, dateRangeToCompare, expectedResult });
		
		/**
		 * Case 5:<br>
		 * |----|<br>
		 * |-|---
		 */
		dateRange = new DateRange(firstOfJan, lastOfJan);
		dateRangeToCompare = new DateRange(firstOfJan, tenthOfJan);
		expectedResult = true;
		cases.add(new Object[] { dateRange, dateRangeToCompare, expectedResult });
		
		/**
		 * Case 6:<br>
		 * |----|<br>
		 * -|--|-
		 */
		dateRange = new DateRange(firstOfJan, lastOfJan);
		dateRangeToCompare = new DateRange(tenthOfJan, twentiethOfJan);
		expectedResult = true;
		cases.add(new Object[] { dateRange, dateRangeToCompare, expectedResult });
		
		/**
		 * Case 7:<br>
		 * |----|--<br>
		 * |------|
		 */
		dateRange = new DateRange(firstOfJan, twentiethOfJan);
		dateRangeToCompare = new DateRange(firstOfJan, lastOfJan);
		expectedResult = true;
		cases.add(new Object[] { dateRange, dateRangeToCompare, expectedResult });
		
		/**
		 * Case 8:<br>
		 * --|----|<br>
		 * |------|
		 */
		dateRange = new DateRange(tenthOfJan, lastOfJan);
		dateRangeToCompare = new DateRange(firstOfJan, lastOfJan);
		expectedResult = true;
		cases.add(new Object[] { dateRange, dateRangeToCompare, expectedResult });
		
		/**
		 * Case 9:<br>
		 * -|--|-<br>
		 * |----|
		 */
		dateRange = new DateRange(tenthOfJan, twentiethOfJan);
		dateRangeToCompare = new DateRange(firstOfJan, lastOfJan);
		expectedResult = true;
		cases.add(new Object[] { dateRange, dateRangeToCompare, expectedResult });
		
		/**
		 * Case 10:<br>
		 * |----|------<br>
		 * ------|----|
		 */
		dateRange = new DateRange(firstOfJan, tenthOfJan);
		dateRangeToCompare = new DateRange(twentiethOfJan, lastOfJan);
		expectedResult = false;
		cases.add(new Object[] { dateRange, dateRangeToCompare, expectedResult });
		
		/**
		 * Case 11:<br>
		 * ------|----|<br>
		 * |----|------
		 */
		dateRange = new DateRange(twentiethOfJan, lastOfJan);
		dateRangeToCompare = new DateRange(firstOfJan, tenthOfJan);
		expectedResult = false;
		cases.add(new Object[] { dateRange, dateRangeToCompare, expectedResult });
		
		/**
		 * Case 12:<br>
		 * -----|----|<br>
		 * |----|-----
		 */
		dateRange = new DateRange(tenthOfJan, lastOfJan);
		dateRangeToCompare = new DateRange(firstOfJan, tenthOfJan);
		expectedResult = true;
		cases.add(new Object[] { dateRange, dateRangeToCompare, expectedResult });
		
		/**
		 * Case 13:<br>
		 * |----|-----<br>
		 * -----|----|
		 */
		dateRange = new DateRange(firstOfJan, tenthOfJan);
		dateRangeToCompare = new DateRange(tenthOfJan, lastOfJan);
		expectedResult = true;
		cases.add(new Object[] { dateRange, dateRangeToCompare, expectedResult });
		
		/**
		 * Case 14:<br>
		 * ----|--><br>
		 * <--|----
		 */
		dateRange = new DateRange(twentiethOfJan, null);
		dateRangeToCompare = new DateRange(null, tenthOfJan);
		expectedResult = false;
		cases.add(new Object[] { dateRange, dateRangeToCompare, expectedResult });
		
		return cases.iterator();
	}
	
	/**
	 * Test method for the {@link DateRange#overlaps(DateRange)} method.<br>
	 * Data provided by the {@link DateRangeTest#overlapsDataProvider()} method.
	 * 
	 * @param dateRange The {@link DateRange} to be compared.
	 * @param dateRangeToCompare The {@link DateRange} with which the first one is compared.
	 * @param expectedResult The expected result of the method.
	 */
	@Test(dataProvider = "overlapsDataProvider")
	public void overlapsTest(DateRange dateRange, DateRange dateRangeToCompare, boolean expectedResult) {
		boolean result = dateRange.overlaps(dateRangeToCompare);
		Assert.assertEquals(result, expectedResult);
	}
	
	/**
	 * Data provider for the {@link DateRangeTest#overlapsExceptionTest(DateRange, DateRange)} method.
	 * 
	 * @return {@link Iterator} Contains the test cases.
	 */
	@DataProvider
	public Iterator<Object[]> overlapsExceptionDataProvider() {
		List<Object[]> cases = Lists.newArrayList();
		Date firstOfJan = DateUtils.getDate(2010, Calendar.JANUARY, 1);
		Date tenthOfJan = DateUtils.getDate(2010, Calendar.JANUARY, 10);
		Date twentiethOfJan = DateUtils.getDate(2010, Calendar.JANUARY, 20);
		Date lastOfJan = DateUtils.getDate(2010, Calendar.JANUARY, 31);
		
		/**
		 * Case 2:<br>
		 * <-------><br>
		 * --|----|-
		 */
		DateRange dateRange = new DateRange();
		DateRange dateRangeToCompare = new DateRange(tenthOfJan, twentiethOfJan);
		cases.add(new Object[] { dateRange, dateRangeToCompare });
		
		/**
		 * Case 5:<br>
		 * --|----|-<br>
		 * <------->
		 */
		dateRange = new DateRange(firstOfJan, lastOfJan);
		dateRangeToCompare = new DateRange();
		cases.add(new Object[] { dateRange, dateRangeToCompare });
		
		/**
		 * Case 7:<br>
		 * <------><br>
		 * <------>
		 */
		dateRange = new DateRange();
		dateRangeToCompare = new DateRange();
		cases.add(new Object[] { dateRange, dateRangeToCompare });
		
		return cases.iterator();
	}
	
	/**
	 * Test method for the {@link DateRange#overlaps(DateRange)} method.<br>
	 * Tests the exception cases.<br>
	 * Data provided by the {@link DateRangeTest#overlapsExceptionDataProvider()} method.
	 * 
	 * @param dateRange The {@link DateRange} to be compared.
	 * @param dateRangeToCompare The {@link DateRange} with which the first one is compared.
	 */
	@Test(dataProvider = "overlapsExceptionDataProvider", expectedExceptions = NullPointerException.class)
	public void overlapsExceptionTest(DateRange dateRange, DateRange dateRangeToCompare) {
		dateRange.overlaps(dateRangeToCompare);
	}
	
	/**
	 * Data provider for the {@link DateRangeTest#intersectionTest(DateRange, DateRange, DateRange)} method.
	 * 
	 * @return {@link Iterator} Contains the test cases.
	 */
	@DataProvider
	public Iterator<Object[]> intersectionDataProvider() {
		List<Object[]> cases = Lists.newArrayList();
		Date firstOfJan = DateUtils.getDate(2010, Calendar.JANUARY, 1);
		Date tenthOfJan = DateUtils.getDate(2010, Calendar.JANUARY, 10);
		Date twentiethOfJan = DateUtils.getDate(2010, Calendar.JANUARY, 10);
		Date lastOfJan = DateUtils.getDate(2010, Calendar.JANUARY, 31);
		
		/**
		 * Case 1:<br>
		 * |--LIMIT--|<br>
		 * --|RANGE|--
		 */
		DateRange limit = new DateRange(firstOfJan, lastOfJan);
		DateRange toTruncate = new DateRange(tenthOfJan, twentiethOfJan);
		DateRange expectedResult = new DateRange(tenthOfJan, twentiethOfJan);
		cases.add(new Object[] { toTruncate, limit, expectedResult });
		
		/**
		 * Case 2:<br>
		 * |-LIMIT-|<br>
		 * --|RANGE|
		 */
		limit = new DateRange(firstOfJan, twentiethOfJan);
		toTruncate = new DateRange(tenthOfJan, twentiethOfJan);
		expectedResult = new DateRange(tenthOfJan, twentiethOfJan);
		cases.add(new Object[] { toTruncate, limit, expectedResult });
		
		/**
		 * Case 3:<br>
		 * |--LIMIT--|<br>
		 * |RANGE|----
		 */
		limit = new DateRange(tenthOfJan, lastOfJan);
		toTruncate = new DateRange(tenthOfJan, twentiethOfJan);
		expectedResult = new DateRange(tenthOfJan, twentiethOfJan);
		cases.add(new Object[] { toTruncate, limit, expectedResult });
		
		/**
		 * Case 4:<br>
		 * |LIMIT|<br>
		 * |RANGE|
		 */
		limit = new DateRange(firstOfJan, lastOfJan);
		toTruncate = new DateRange(firstOfJan, lastOfJan);
		expectedResult = new DateRange(firstOfJan, lastOfJan);
		cases.add(new Object[] { toTruncate, limit, expectedResult });
		
		/**
		 * Case 5:<br>
		 * --|LIMIT|--<br>
		 * |--RANGE--|
		 */
		limit = new DateRange(tenthOfJan, twentiethOfJan);
		toTruncate = new DateRange(firstOfJan, lastOfJan);
		expectedResult = new DateRange(tenthOfJan, twentiethOfJan);
		cases.add(new Object[] { toTruncate, limit, expectedResult });
		
		/**
		 * Case 6:<br>
		 * --|LIMIT|<br>
		 * |--RANGE|
		 */
		limit = new DateRange(tenthOfJan, twentiethOfJan);
		toTruncate = new DateRange(firstOfJan, twentiethOfJan);
		expectedResult = new DateRange(tenthOfJan, twentiethOfJan);
		cases.add(new Object[] { toTruncate, limit, expectedResult });
		
		/**
		 * Case 7:<br>
		 * |LIMIT|--<br>
		 * |RANGE--|
		 */
		limit = new DateRange(tenthOfJan, twentiethOfJan);
		toTruncate = new DateRange(tenthOfJan, lastOfJan);
		expectedResult = new DateRange(tenthOfJan, twentiethOfJan);
		cases.add(new Object[] { toTruncate, limit, expectedResult });
		
		/**
		 * Case 8:<br>
		 * |LIMIT|--<br>
		 * --|RANGE|
		 */
		limit = new DateRange(firstOfJan, twentiethOfJan);
		toTruncate = new DateRange(tenthOfJan, lastOfJan);
		expectedResult = new DateRange(tenthOfJan, twentiethOfJan);
		cases.add(new Object[] { toTruncate, limit, expectedResult });
		
		/**
		 * Case 9:<br>
		 * --|LIMIT|<br>
		 * |RANGE|--
		 */
		limit = new DateRange(tenthOfJan, lastOfJan);
		toTruncate = new DateRange(firstOfJan, twentiethOfJan);
		expectedResult = new DateRange(tenthOfJan, twentiethOfJan);
		cases.add(new Object[] { toTruncate, limit, expectedResult });
		
		/**
		 * Case 10:<br>
		 * ------|LIMIT|<br>
		 * |RANGE|------
		 */
		limit = new DateRange(tenthOfJan, lastOfJan);
		toTruncate = new DateRange(firstOfJan, tenthOfJan);
		expectedResult = new DateRange(tenthOfJan, tenthOfJan);
		cases.add(new Object[] { toTruncate, limit, expectedResult });
		
		/**
		 * Case 11:<br>
		 * |LIMIT|------<br>
		 * ------|RANGE|
		 */
		limit = new DateRange(firstOfJan, twentiethOfJan);
		toTruncate = new DateRange(twentiethOfJan, lastOfJan);
		expectedResult = new DateRange(twentiethOfJan, twentiethOfJan);
		cases.add(new Object[] { toTruncate, limit, expectedResult });
		
		return cases.iterator();
	}
	
	/**
	 * Test method for the {@link DateRange#intersection(DateRange)} method.<br>
	 * Data provided by the {@link DateRangeTest#intersectionDataProvider()} method.
	 * 
	 * @param toTruncate The {@link DateRange} to be truncated.
	 * @param limit The {@link DateRange} that defines the boundary by which the {@link DateRange} to be truncated must
	 *            be truncated.
	 * @param expectedResult The expected result.
	 */
	@Test(dataProvider = "intersectionDataProvider")
	public void intersectionTest(DateRange toTruncate, DateRange limit, DateRange expectedResult) {
		DateRange result = toTruncate.intersection(limit);
		Assert.assertEquals(result, expectedResult);
		Assert.assertFalse(result == toTruncate);
		Assert.assertFalse(result == limit);
	}
	
	/**
	 * Test method for the {@link DateRange#intersection(DateRange)} method.<br>
	 * Tests the exception cases.
	 */
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void intersectionExceptionTest() {
		
		// The only case that this happens is when the date ranges don't overlap.
		DateRange toTruncate = new DateRange(DateUtils.getDate(2010, Calendar.JANUARY, 1), DateUtils.getDate(2010,
			Calendar.JANUARY, 31));
		DateRange limit = new DateRange(DateUtils.getDate(2010, Calendar.FEBRUARY, 1), DateUtils.getDate(2010,
			Calendar.FEBRUARY, 28));
		toTruncate.intersection(limit);
	}
	
}
