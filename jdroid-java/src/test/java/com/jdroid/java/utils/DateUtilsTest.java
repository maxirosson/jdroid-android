package com.jdroid.java.utils;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import com.jdroid.java.collections.Lists;

/**
 * Test class for the {@link DateUtils} class.
 * 
 */
public class DateUtilsTest {
	
	/**
	 * @return The different cases
	 */
	@DataProvider
	public Iterator<Object[]> formatDataProvider() {
		List<Object[]> cases = Lists.newArrayList();
		// TODO Fix this tests
		// cases.add(new Object[] { DateUtils.getDate(2010, Calendar.AUGUST, 10), DateUtils.MMDDYYYY_DATE_FORMAT,
		// "08/10/2010" });
		// cases.add(new Object[] { DateUtils.getDate(2010, Calendar.AUGUST, 10), DateUtils.MMDDYYYY_DATE_FORMAT,
		// "08/10/10" });
		// cases.add(new Object[] { DateUtils.getDate(2010, Calendar.AUGUST, 10), DateUtils.DATE_PATTERN_SIMPLE,
		// "8/10/2010" });
		// cases.add(new Object[] { DateUtils.getDate(2010, Calendar.JANUARY, 8), DateUtils.DATE_PATTERN_SIMPLE,
		// "1/8/2010" });
		// cases.add(new Object[] { DateUtils.getDate(2010, Calendar.DECEMBER, 10), DateUtils.DATE_PATTERN_SIMPLE,
		// "12/10/2010" });
		// cases.add(new Object[] { DateUtils.getDate(2010, Calendar.DECEMBER, 10), DateUtils.DATE_TIME_PATTERN,
		// "12/10/2010 00:00:00" });
		return cases.iterator();
	}
	
	/**
	 * @param date The {@link Date} to format
	 * @param pattern The pattern to format the {@link Date}
	 * @param expected The expected string
	 */
	@Test(dataProvider = "formatDataProvider")
	public void format(Date date, String pattern, String expected) {
		String result = DateUtils.format(date, pattern);
		Assert.assertEquals(result, expected);
	}
	
	/**
	 * Test method for the {@link DateUtils#isBetween(Date, Date, Date)} method.
	 * 
	 * @return The different scenarios
	 */
	@DataProvider
	public Iterator<Object[]> getForIsBetween() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(2009, Calendar.JANUARY, 20);
		Date date = calendar.getTime();
		
		List<Object[]> cases = Lists.newArrayList();
		calendar.set(2009, Calendar.JANUARY, 20);
		Date startDate = calendar.getTime();
		calendar.set(2009, Calendar.JANUARY, 20);
		Date endDate = calendar.getTime();
		cases.add(new Object[] { date, startDate, endDate, true });
		
		calendar.set(2009, Calendar.JANUARY, 19);
		startDate = calendar.getTime();
		calendar.set(2009, Calendar.JANUARY, 21);
		endDate = calendar.getTime();
		cases.add(new Object[] { date, startDate, endDate, true });
		
		calendar.set(2009, Calendar.JANUARY, 19);
		startDate = calendar.getTime();
		calendar.set(2009, Calendar.JANUARY, 19);
		endDate = calendar.getTime();
		cases.add(new Object[] { date, startDate, endDate, false });
		
		return cases.iterator();
	}
	
	/**
	 * Test method for the {@link DateUtils#isBetween(Date, Date, Date)} method.
	 * 
	 * @param date The date
	 * @param startDate The start date
	 * @param endDate The end date
	 * @param expectedResult The expected result
	 */
	@Test(dataProvider = "getForIsBetween")
	public void isBetween(Date date, Date startDate, Date endDate, boolean expectedResult) {
		Assert.assertEquals(DateUtils.isBetween(date, startDate, endDate), expectedResult);
	}
	
	/**
	 * Tests the {@link DateUtils#getDate(int, int, int)} method.
	 */
	@Test
	public void getDate() {
		Calendar calendar = Calendar.getInstance();
		int year = 2009;
		int month = Calendar.JANUARY;
		int date = 1;
		calendar.set(year, month, date);
		calendar = org.apache.commons.lang.time.DateUtils.truncate(calendar, Calendar.DATE);
		Date createdDate = DateUtils.getDate(year, month, date);
		Assert.assertEquals(createdDate, calendar.getTime());
	}
	
	/**
	 * So given the start date is 10-Jun and stop date is 20-Jun there are several scenarios to check:
	 * 
	 * <pre>
	 * Input:  --------|-----|---------
	 * Case1:  --------|-----|---------  Yes
	 * Case2:  ----------|-|-----------  Yes
	 * Case3:  --------|-|-------------  Yes
	 * Case4:  ------------|-|---------  Yes
	 * Case5:  -----|--|---------------  Yes
	 * Case6:  -----|----|-------------  Yes
	 * Case7:  --------------|---|-----  Yes
	 * Case8:  ----------- |-----|-----  Yes
	 * Case9:  -----|------------|-----  Yes
	 * Case10: --|--|------------------  No
	 * Case11: ------------------|--|--  No
	 * </pre>
	 * 
	 * In other words, ONLY if one range touches the other, it's an overlap.
	 * 
	 * @return {@link Iterator} Contains the test cases.
	 */
	@DataProvider
	public Iterator<Object[]> periodsOverlapDataProvider() {
		List<Object[]> cases = Lists.newArrayList();
		Integer year = Calendar.getInstance().get(Calendar.YEAR);
		Integer month = Calendar.JANUARY;
		cases.add(new Object[] { DateUtils.getDate(year, month, 10), DateUtils.getDate(year, month, 20), true });
		cases.add(new Object[] { DateUtils.getDate(year, month, 12), DateUtils.getDate(year, month, 18), true });
		cases.add(new Object[] { DateUtils.getDate(year, month, 10), DateUtils.getDate(year, month, 12), true });
		cases.add(new Object[] { DateUtils.getDate(year, month, 18), DateUtils.getDate(year, month, 20), true });
		cases.add(new Object[] { DateUtils.getDate(year, month, 8), DateUtils.getDate(year, month, 10), true });
		cases.add(new Object[] { DateUtils.getDate(year, month, 8), DateUtils.getDate(year, month, 12), true });
		cases.add(new Object[] { DateUtils.getDate(year, month, 20), DateUtils.getDate(year, month, 22), true });
		cases.add(new Object[] { DateUtils.getDate(year, month, 18), DateUtils.getDate(year, month, 22), true });
		cases.add(new Object[] { DateUtils.getDate(year, month, 8), DateUtils.getDate(year, month, 22), true });
		cases.add(new Object[] { DateUtils.getDate(year, month, 6), DateUtils.getDate(year, month, 8), false });
		cases.add(new Object[] { DateUtils.getDate(year, month, 22), DateUtils.getDate(year, month, 24), false });
		return cases.iterator();
	}
	
	/**
	 * @param start2 The second rage start date
	 * @param end2 The second rage end date
	 * @param expected the expected overlap result between the current rage and the scenario
	 */
	@Test(dataProvider = "periodsOverlapDataProvider")
	public void periodsOverlapDateTest(Date start2, Date end2, boolean expected) {
		
		Integer year = Calendar.getInstance().get(Calendar.YEAR);
		Integer month = Calendar.JANUARY;
		
		Date start = DateUtils.getDate(year, month, 10);
		Date end = DateUtils.getDate(year, month, 20);
		
		Assert.assertEquals(DateUtils.periodsOverlap(start, end, start2, end2), expected);
	}
	
	/**
	 * @return The different scenarios of periods
	 */
	@DataProvider
	public Iterator<Object[]> getForIsAfterEquals() {
		List<Object[]> cases = Lists.newArrayList();
		cases.add(new Object[] { 2009, Calendar.JANUARY, 11, 2009, Calendar.JANUARY, 11, true });
		cases.add(new Object[] { 2009, Calendar.JANUARY, 11, 2009, Calendar.JANUARY, 12, false });
		cases.add(new Object[] { 2009, Calendar.JANUARY, 11, 2009, Calendar.JANUARY, 10, true });
		return cases.iterator();
	}
	
	/**
	 * @param year The year
	 * @param month The month
	 * @param date The date
	 * @param yearToCompare The year to compare with
	 * @param monthToCompare The month to compare with
	 * @param dateToCompare The date to compare with
	 * @param expectedResult The expectedResult
	 */
	@Test(dataProvider = "getForIsAfterEquals")
	public void isAfterEquals(int year, int month, int date, int yearToCompare, int monthToCompare, int dateToCompare,
			boolean expectedResult) {
		Assert.assertEquals(
			DateUtils.isAfterEquals(DateUtils.getDate(year, month, date),
				DateUtils.getDate(yearToCompare, monthToCompare, dateToCompare)), expectedResult);
	}
	
	/**
	 * @return The different scenarios of periods
	 */
	@DataProvider
	public Iterator<Object[]> getForIsAfter() {
		List<Object[]> cases = Lists.newArrayList();
		cases.add(new Object[] { 2009, Calendar.JANUARY, 11, 2009, Calendar.JANUARY, 11, false });
		cases.add(new Object[] { 2009, Calendar.JANUARY, 11, 2009, Calendar.JANUARY, 12, false });
		cases.add(new Object[] { 2009, Calendar.JANUARY, 11, 2009, Calendar.JANUARY, 10, true });
		return cases.iterator();
	}
	
	/**
	 * @param year The year
	 * @param month The month
	 * @param date The date
	 * @param yearToCompare The year to compare with
	 * @param monthToCompare The month to compare with
	 * @param dateToCompare The date to compare with
	 * @param expectedResult The expectedResult
	 */
	@Test(dataProvider = "getForIsAfter")
	public void isAfter(int year, int month, int date, int yearToCompare, int monthToCompare, int dateToCompare,
			boolean expectedResult) {
		Assert.assertEquals(
			DateUtils.isAfter(DateUtils.getDate(year, month, date),
				DateUtils.getDate(yearToCompare, monthToCompare, dateToCompare)), expectedResult);
	}
	
	/**
	 * @return The different scenarios of periods
	 */
	@DataProvider
	public Iterator<Object[]> getForIsBeforeEquals() {
		List<Object[]> cases = Lists.newArrayList();
		cases.add(new Object[] { 2009, Calendar.JANUARY, 11, 2009, Calendar.JANUARY, 11, true });
		cases.add(new Object[] { 2009, Calendar.JANUARY, 11, 2009, Calendar.JANUARY, 12, true });
		cases.add(new Object[] { 2009, Calendar.JANUARY, 11, 2009, Calendar.JANUARY, 10, false });
		return cases.iterator();
	}
	
	/**
	 * @param year The year
	 * @param month The month
	 * @param date The date
	 * @param yearToCompare The year to compare with
	 * @param monthToCompare The month to compare with
	 * @param dateToCompare The date to compare with
	 * @param expectedResult The expectedResult
	 */
	@Test(dataProvider = "getForIsBeforeEquals")
	public void isBeforeEquals(int year, int month, int date, int yearToCompare, int monthToCompare, int dateToCompare,
			boolean expectedResult) {
		Assert.assertEquals(
			DateUtils.isBeforeEquals(DateUtils.getDate(year, month, date),
				DateUtils.getDate(yearToCompare, monthToCompare, dateToCompare)), expectedResult);
	}
	
	/**
	 * @return The different scenarios of periods
	 */
	@DataProvider
	public Iterator<Object[]> getForIsBefore() {
		List<Object[]> cases = Lists.newArrayList();
		cases.add(new Object[] { 2009, Calendar.JANUARY, 11, 2009, Calendar.JANUARY, 11, false });
		cases.add(new Object[] { 2009, Calendar.JANUARY, 11, 2009, Calendar.JANUARY, 12, true });
		cases.add(new Object[] { 2009, Calendar.JANUARY, 11, 2009, Calendar.JANUARY, 10, false });
		return cases.iterator();
	}
	
	/**
	 * @param year The year
	 * @param month The month
	 * @param date The date
	 * @param yearToCompare The year to compare with
	 * @param monthToCompare The month to compare with
	 * @param dateToCompare The date to compare with
	 * @param expectedResult The expectedResult
	 */
	@Test(dataProvider = "getForIsBefore")
	public void isBefore(int year, int month, int date, int yearToCompare, int monthToCompare, int dateToCompare,
			boolean expectedResult) {
		Assert.assertEquals(
			DateUtils.isBefore(DateUtils.getDate(year, month, date),
				DateUtils.getDate(yearToCompare, monthToCompare, dateToCompare)), expectedResult);
	}
	
	/**
	 * @param date the date to be tested
	 * @param expectedResult The expected result
	 */
	@Test(dataProvider = "getLastDayOfTheMonthData", dependsOnMethods = "getDate")
	public void lastDayOfTheMonth(Date date, Date expectedResult) {
		Date lastDay = DateUtils.getLastDayOfMonth(date);
		Assert.assertEquals(lastDay, expectedResult);
	}
	
	/**
	 * @return the data to tests the lastDayOfTheMonth method
	 */
	@DataProvider
	protected Iterator<Object[]> getLastDayOfTheMonthData() {
		List<Object[]> cases = Lists.newArrayList();
		
		// 31-day month
		Date date1 = DateUtils.getDate(2010, 0, 10);
		Date expectedDate1 = DateUtils.getDate(2010, 0, 31);
		cases.add(new Object[] { date1, expectedDate1 });
		
		// 31-day month but in the 31th day
		Date date2 = DateUtils.getDate(2010, 0, 31);
		Date expectedDate2 = DateUtils.getDate(2010, 0, 31);
		cases.add(new Object[] { date2, expectedDate2 });
		
		// 30-day month
		Date date3 = DateUtils.getDate(2010, 3, 1);
		Date expectedDate3 = DateUtils.getDate(2010, 3, 30);
		cases.add(new Object[] { date3, expectedDate3 });
		
		// February special case in a leap year
		Date date4 = DateUtils.getDate(2008, 1, 1);
		Date expectedDate4 = DateUtils.getDate(2008, 1, 29);
		cases.add(new Object[] { date4, expectedDate4 });
		
		// February special case in a normal year
		Date date5 = DateUtils.getDate(2009, 1, 1);
		Date expectedDate5 = DateUtils.getDate(2009, 1, 28);
		cases.add(new Object[] { date5, expectedDate5 });
		
		return cases.iterator();
	}
}
