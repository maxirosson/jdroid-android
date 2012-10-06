package com.jdroid.java.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import com.jdroid.java.exception.UnexpectedException;

/**
 * Utilities for Dates and Calendars
 * 
 * @author Maxi Rosson
 */
public abstract class DateUtils {
	
	/** Seconds in a minute */
	public static final int MINUTE = 60;
	
	/** Seconds in an hour */
	public static final int HOUR = MINUTE * 60;
	
	/** Seconds in a day */
	public static final int DAY = HOUR * 24;
	
	/** Seconds in a week */
	public static final int WEEK = DAY * 7;
	
	/**
	 * Date format like yyyy-MM-ddTHH:mm:ss Z
	 */
	public static final String YYYYMMDDTHHMMSSZ_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZZZZ";
	
	/**
	 * Date format like yyyy-MM-dd HH:mm:ss Z
	 */
	public static final String YYYYMMDDHHMMSSZ_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss Z";
	
	/**
	 * Date format like 2010-10-25 21:30:00
	 */
	public static final String YYYYMMDDHHMMSS_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	
	/**
	 * Date format like 10/25/2010 21:30
	 */
	public static final String MMDDYYYYHHMM_DATE_FORMAT = "MM/dd/yyyy HH:mm";
	
	/**
	 * Date format like 10/25/2010
	 */
	public static final String MMDDYYYY_DATE_FORMAT = "MM/dd/yyyy";
	
	/**
	 * Date format like 10-25-2010
	 */
	public static final String MMDDYYYY_SLASH_DATE_FORMAT = "MM-dd-yyyy";
	
	/**
	 * Date format like 2010-10-25
	 */
	public static final String YYYYMMDD_DATE_FORMAT = "yyyy-MM-dd";
	
	/**
	 * Date format like Fri 5:15 AM
	 */
	public static final String EHHMMAA_DATE_FORMAT = "E hh:mm aa";
	
	/**
	 * Date format like Nov 5 3:45 PM
	 */
	public static final String MMMDHHMMAA_DATE_FORMAT = "MMM d hh:mm aa";
	
	/**
	 * Date format like Nov 5 1985 3:45 PM
	 */
	public static final String MMMDYYYYHHMMAA_DATE_FORMAT = "MMM d yyyy hh:mm aa";
	
	/**
	 * Date format like Nov 5
	 */
	public static final String MMMD_DATE_FORMAT = "MMM d";
	
	/**
	 * Date format like November 5
	 */
	public static final String MMMMD_DATE_FORMAT = "MMMM d";
	
	/**
	 * Date format like 03:45 PM
	 */
	public static final String HHMMAA_DATE_FORMAT = "hh:mm aa";
	
	/**
	 * Date format like Friday 5 November
	 */
	public static final String EEEEDMMMM_DATE_FORMAT = "EEEE d MMMM";
	
	/**
	 * Date format like Friday November
	 */
	public static final String EEEEMMMM_DATE_FORMAT = "EEEE MMMM";
	
	public enum DayOfWeek {
		
		SUNDAY("Sunday", true),
		MONDAY("Monday", false),
		TUESDAY("Tuesday", false),
		WEDNESDAY("Wednesday", false),
		THURSDAY("Thursday", false),
		FRIDAY("Friday", false),
		SATURDAY("Saturday", true);
		
		private String name;
		private Boolean weekend;
		
		private DayOfWeek(String name, Boolean weekend) {
			this.name = name;
			this.weekend = weekend;
		}
		
		public Boolean isWeekend() {
			return weekend;
		}
		
		@Override
		public String toString() {
			return name;
		}
	}
	
	public static final void init() {
		// nothing...
	}
	
	/**
	 * @param dateFormatted The formatted string to parse
	 * @param dateFormat
	 * @return A date that represents the formatted string
	 */
	public static Date parse(String dateFormatted, String dateFormat) {
		return DateUtils.parse(dateFormatted, new SimpleDateFormat(dateFormat));
	}
	
	/**
	 * @param dateFormatted The formatted string to parse
	 * @param dateFormat
	 * @return A date that represents the formatted string
	 */
	public static Date parse(String dateFormatted, SimpleDateFormat dateFormat) {
		Date date = null;
		if (StringUtils.isNotEmpty(dateFormatted)) {
			try {
				date = dateFormat.parse(dateFormatted);
			} catch (ParseException e) {
				throw new UnexpectedException("Error parsing the dateFormatted: " + dateFormatted + " pattern: "
						+ dateFormat.toPattern(), e);
			}
		}
		return date;
	}
	
	/**
	 * Transform the {@link Date} to a {@link String} with a format like Friday November 5th
	 * 
	 * @param date The {@link Date} to be formatted
	 * @return A String that represent the date with the pattern
	 */
	public static String formatToCardinal(Date date) {
		int day = DateUtils.getDay(date);
		String ordinalSuffix = NumberUtils.getOrdinalSuffix(day);
		StringBuilder builder = new StringBuilder();
		builder.append(format(date, EEEEMMMM_DATE_FORMAT));
		builder.append(" ");
		builder.append(day);
		builder.append(ordinalSuffix);
		return builder.toString();
	}
	
	/**
	 * @param date The {@link Date} to be formatted
	 * @param dateFormat The {@link DateFormat} used to format the {@link Date}
	 * @return A String that represent the date with the pattern
	 */
	public static String format(Date date, String dateFormat) {
		return DateUtils.format(date, new SimpleDateFormat(dateFormat));
	}
	
	/**
	 * Transform the {@link Date} to a {@link String} using the received {@link SimpleDateFormat}
	 * 
	 * @param date The {@link Date} to be formatted
	 * @param dateFormat The {@link DateFormat} used to format the {@link Date}
	 * @return A String that represent the date with the pattern
	 */
	public static String format(Date date, DateFormat dateFormat) {
		return date != null ? dateFormat.format(date) : null;
	}
	
	public static String formatDate(Date date) {
		return format(date, DateFormat.getDateInstance());
	}
	
	public static String formatTime(Date date) {
		return format(date, DateFormat.getTimeInstance(DateFormat.SHORT));
	}
	
	/**
	 * Creates a {@link Date} for the specified day
	 * 
	 * @param year The year
	 * @param monthOfYear The month number (starting on 0)
	 * @param dayOfMonth The day of the month
	 * @return The {@link Date}
	 */
	public static Date getDate(int year, int monthOfYear, int dayOfMonth) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(year, monthOfYear, dayOfMonth);
		truncate(calendar);
		return calendar.getTime();
	}
	
	public static Date getTime(int hour, int minutes) {
		return getTime(hour, minutes, true);
	}
	
	public static Date getTime(int hour, int minutes, Boolean is24Hour) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(is24Hour ? Calendar.HOUR_OF_DAY : Calendar.HOUR, hour);
		calendar.set(Calendar.MINUTE, minutes);
		return calendar.getTime();
	}
	
	public static int getYear() {
		return getYear(now());
	}
	
	public static int getYear(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.YEAR);
	}
	
	public static int getMonth(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.MONTH);
	}
	
	public static int getDay(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.DAY_OF_MONTH);
	}
	
	public static int getHour(Date date, Boolean is24Hour) {
		return DateUtils.getHour(date, TimeZone.getDefault(), is24Hour);
	}
	
	public static int getHour(Date date, TimeZone timeZone, Boolean is24Hour) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.setTimeZone(timeZone);
		return calendar.get(is24Hour ? Calendar.HOUR_OF_DAY : Calendar.HOUR);
	}
	
	public static int getMinute(Date date) {
		return DateUtils.getMinute(date, TimeZone.getDefault());
	}
	
	public static int getMinute(Date date, TimeZone timeZone) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.setTimeZone(timeZone);
		return calendar.get(Calendar.MINUTE);
	}
	
	public static DayOfWeek getWeekDay(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
		return DayOfWeek.values()[dayOfWeek - 1];
	}
	
	public static Date addSeconds(Date date, int seconds) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.SECOND, seconds);
		return calendar.getTime();
	}
	
	/**
	 * Truncate the date removing hours, minutes, seconds and milliseconds
	 * 
	 * @param date The {@link Date} to truncate
	 * @return The truncated {@link Date}
	 */
	public static Date truncate(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		truncate(calendar);
		return calendar.getTime();
	}
	
	/**
	 * Truncate the {@link Calendar} removing hours, minutes, seconds and milliseconds
	 * 
	 * @param calendar The {@link Calendar} to truncate
	 */
	public static void truncate(Calendar calendar) {
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
	}
	
	/**
	 * @return the current moment
	 */
	public static Date now() {
		return new Date();
	}
	
	/**
	 * @param date The date to compare
	 * @param startDate The left between' side
	 * @param endDate The right between's side
	 * @return <code>true</code> if the date is in the middle of startDate and endDate
	 */
	public static boolean isBetween(Date date, Date startDate, Date endDate) {
		return DateUtils.isBeforeEquals(startDate, date) && DateUtils.isAfterEquals(endDate, date);
	}
	
	/**
	 * Tests if the date is before than the specified dateToCompare.
	 * 
	 * @param date the date to compare with the dateToCompare.
	 * @param dateToCompare the date to compare with the date.
	 * @return <code>true</code> if the instant of time represented by <code>date</code> object is earlier than the
	 *         instant represented by <tt>dateToCompare</tt>; <code>false</code> otherwise.
	 */
	public static boolean isBefore(Date date, Date dateToCompare) {
		return date.compareTo(dateToCompare) < 0;
	}
	
	/**
	 * Tests if the date is before or equals than the specified dateToCompare.
	 * 
	 * @param date the date to compare with the dateToCompare.
	 * @param dateToCompare the date to compare with the date.
	 * @return <code>true</code> if the instant of time represented by <code>date</code> object is earlier or equal than
	 *         the instant represented by <tt>dateToCompare</tt>; <code>false</code> otherwise.
	 */
	public static boolean isBeforeEquals(Date date, Date dateToCompare) {
		return date.compareTo(dateToCompare) <= 0;
	}
	
	/**
	 * Tests if the date is after or equals than the specified dateToCompare.
	 * 
	 * @param date the date to compare with the dateToCompare.
	 * @param dateToCompare the date to compare with the date.
	 * @return <code>true</code> if the instant of time represented by <code>date</code> object is later or equal than
	 *         the instant represented by <tt>dateToCompare</tt>; <code>false</code> otherwise.
	 */
	public static boolean isAfterEquals(Date date, Date dateToCompare) {
		return date.compareTo(dateToCompare) >= 0;
	}
	
	/**
	 * Tests if the date is after than the specified dateToCompare.
	 * 
	 * @param date the date to compare with the dateToCompare.
	 * @param dateToCompare the date to compare with the date.
	 * @return <code>true</code> if the instant of time represented by <code>date</code> object is later than the
	 *         instant represented by <tt>dateToCompare</tt>; <code>false</code> otherwise.
	 */
	public static boolean isAfter(Date date, Date dateToCompare) {
		return date.compareTo(dateToCompare) > 0;
	}
	
	/**
	 * Returns true if two periods overlap
	 * 
	 * @param startDate1 the period one start date
	 * @param endDate1 the period one end date
	 * @param startDate2 the period two start date
	 * @param endDate2 the period two end date
	 * @return true if overlap
	 */
	public static boolean periodsOverlap(Date startDate1, Date endDate1, Date startDate2, Date endDate2) {
		return (startDate1.before(endDate2) || startDate1.equals(endDate2))
				&& (endDate1.after(startDate2) || endDate1.equals(startDate2));
	}
	
	/**
	 * Returns true if the first period contains the second periods
	 * 
	 * @param startDate1 the period one start date
	 * @param endDate1 the period one end date
	 * @param startDate2 the period two start date
	 * @param endDate2 the period two end date
	 * @return true if the first period contains the second period
	 */
	public static boolean containsPeriod(Date startDate1, Date endDate1, Date startDate2, Date endDate2) {
		return DateUtils.isBeforeEquals(startDate1, startDate2) && DateUtils.isAfterEquals(endDate1, endDate2);
	}
	
	private static Calendar todayCalendar() {
		Calendar calendar = Calendar.getInstance();
		return DateUtils.resetTime(calendar);
	}
	
	private static Calendar resetTime(Calendar calendar) {
		return org.apache.commons.lang.time.DateUtils.truncate(calendar, Calendar.DATE);
	}
	
	/**
	 * @return a day after today
	 */
	public static Date tomorrow() {
		Calendar calendar = DateUtils.todayCalendar();
		return org.apache.commons.lang.time.DateUtils.addDays(calendar.getTime(), 1);
	}
	
	/**
	 * @return a day before today
	 */
	public static Date yesterday() {
		Calendar calendar = DateUtils.todayCalendar();
		return org.apache.commons.lang.time.DateUtils.addDays(calendar.getTime(), -1);
	}
	
	/**
	 * @param months amount of months to move the calendar
	 * @return a date that is <code>months</code> in the future/past. Use negative values for past dates.
	 */
	public static Date monthsAway(int months) {
		return org.apache.commons.lang.time.DateUtils.addMonths(DateUtils.todayCalendar().getTime(), months);
	}
	
	/**
	 * @return a date that is one month in the future
	 */
	public static Date oneMonthInFuture() {
		return DateUtils.monthsAway(1);
	}
	
	/**
	 * @return a date that is one month in the past
	 */
	public static Date oneMonthInPast() {
		return DateUtils.monthsAway(-1);
	}
	
	/**
	 * @param date Date that includes the desired month in order to calculate the last day of that month
	 * @return the date of the last day of the month
	 */
	public static Date getLastDayOfMonth(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		return DateUtils.resetTime(calendar).getTime();
	}
	
}
