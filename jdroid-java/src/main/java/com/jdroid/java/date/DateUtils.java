package com.jdroid.java.date;

import com.jdroid.java.exception.UnexpectedException;
import com.jdroid.java.utils.NumberUtils;
import com.jdroid.java.utils.StringUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

/**
 * Utilities for Dates and Calendars
 */
public abstract class DateUtils {

	private DateUtils(){}
	
	/** Seconds in a minute */
	public static final int SECONDS_PER_MINUTE = 60;
	
	/** Seconds in an hour */
	public static final int SECONDS_PER_HOUR = SECONDS_PER_MINUTE * 60;
	
	/** Seconds in a day */
	public static final int SECONDS_PER_DAY = SECONDS_PER_HOUR * 24;
	
	/** Seconds in a week */
	public static final int WEEK = SECONDS_PER_DAY * 7;
	
	/** Number of milliseconds in a second. */
	public static final long MILLIS_PER_SECOND = 1000;
	
	/** Number of milliseconds in a minute. */
	public static final long MILLIS_PER_MINUTE = 60 * MILLIS_PER_SECOND;
	
	/** Number of milliseconds in a hour. */
	public static final long MILLIS_PER_HOUR = 60 * MILLIS_PER_MINUTE;
	
	/** Number of milliseconds in a day. */
	public static final long MILLIS_PER_DAY = 24 * MILLIS_PER_HOUR;

	/** Number of milliseconds in a week. */
	public static final long MILLIS_PER_WEEK = 7 * MILLIS_PER_DAY;

	/** Number of hours in a day **/
	public static final int HOURS_PER_DAY = 24;

	public static String DEFAULT_DATE_TIME_FORMAT = DateTimeFormat.YYYYMMDDHHMMSSZ;

	private static Date fakeNow;
	
	public static void init() {
		// nothing...
	}
	
	/**
	 * @param dateFormatted The formatted string to parse
	 * @param dateFormat
	 * @return A date that represents the formatted string
	 */
	public static Date parse(String dateFormatted, String dateFormat) {
		return parse(dateFormatted, dateFormat, false);
	}

	public static Date parse(String dateFormatted, String dateFormat, boolean useUtc) {
		return parse(dateFormatted, new SimpleDateFormat(dateFormat), useUtc);
	}

	/**
	 * @param dateFormatted The formatted string to parse
	 * @param dateFormat
	 * @return A date that represents the formatted string
	 */
	public static Date parse(String dateFormatted, SimpleDateFormat dateFormat) {
		return parse(dateFormatted, dateFormat, false);
	}

	public static Date parse(String dateFormatted, SimpleDateFormat dateFormat, boolean useUtc) {
		Date date = null;
		if (StringUtils.isNotEmpty(dateFormatted)) {
			try {
				if (useUtc) {
					dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
				}
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
		int day = DateUtils.getDayOfMonth(date);
		String ordinalSuffix = NumberUtils.getOrdinalSuffix(day);
		StringBuilder builder = new StringBuilder();
		builder.append(format(date, DateTimeFormat.EEEEMMMM));
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
		return format(date, dateFormat, false);
	}

	public static String format(Date date, String dateFormat, boolean useUtc) {
		return format(date, new SimpleDateFormat(dateFormat), useUtc);
	}

	/**
	 * Transform the {@link Date} to a {@link String} using the received {@link SimpleDateFormat}
	 *
	 * @param date The {@link Date} to be formatted
	 * @param dateFormat The {@link DateFormat} used to format the {@link Date}
	 * @return A String that represent the date with the pattern
	 */
	public static String format(Date date, DateFormat dateFormat) {
		return format(date, dateFormat, false);
	}

	public static String format(Date date, DateFormat dateFormat, boolean useUtc) {
		if (useUtc) {
			dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
		}
		return date != null ? dateFormat.format(date) : null;
	}

	public static String formatDateTime(Date date) {
		return format(date, DateFormat.getDateTimeInstance());
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
		truncateTime(calendar);
		return calendar.getTime();
	}
	
	public static Date getDate(Date date, Date time, Boolean is24Hour) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		if (time != null) {
			calendar.set(Calendar.HOUR_OF_DAY, DateUtils.getHour(time, is24Hour));
			calendar.set(Calendar.MINUTE, DateUtils.getMinute(time));
			calendar.set(Calendar.SECOND, 0);
		} else {
			truncateTime(calendar);
		}
		return calendar.getTime();
	}
	
	public static Date getDate(Date date, Date time) {
		return getDate(date, time, true);
	}
	
	public static Date getDate(long milliseconds) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(milliseconds);
		return calendar.getTime();
	}
	
	public static Date getTime(int hour, int minutes) {
		return getTime(hour, minutes, true);
	}
	
	public static Date getTime(int hour, int minutes, Boolean is24Hour) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(is24Hour ? Calendar.HOUR_OF_DAY : Calendar.HOUR, hour);
		calendar.set(Calendar.MINUTE, minutes);
		calendar.set(Calendar.SECOND, 0);
		truncateDate(calendar);
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
	
	public static int getDayOfMonth(Date date) {
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
	
	public static DayOfWeek getDayOfWeek() {
		return getDayOfWeek(DateUtils.now());
	}
	
	public static DayOfWeek getDayOfWeek(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
		return DayOfWeek.findByNumber(dayOfWeek);
	}

	public static boolean isDateOnWeekend(Date date) {
		return getDayOfWeek(date).isWeekend();
	}

	public static Date setHour(Date date, int hours, Boolean is24Hour) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(is24Hour ? Calendar.HOUR_OF_DAY : Calendar.HOUR, hours);
		return calendar.getTime();
	}
	
	public static Date addSeconds(Date date, int seconds) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.SECOND, seconds);
		return calendar.getTime();
	}

	public static Date addHours(Date date, int hours) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.HOUR, hours);
		return calendar.getTime();
	}
	
	public static Date addDays(Date date, int days) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_MONTH, days);
		return calendar.getTime();
	}
	
	public static Date addMonths(Date date, int days) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, days);
		return calendar.getTime();
	}
	
	public static Date addYears(Date date, int years) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.YEAR, years);
		return calendar.getTime();
	}
	
	/**
	 * Truncate the date asigning it to 1st of January of 1980
	 *
	 * @param date The {@link Date} to truncate
	 * @return The truncated {@link Date}
	 */
	public static Date truncateDate(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		truncateDate(calendar);
		return calendar.getTime();
	}

	/**
	 * Truncate the {@link Calendar} date asigning it to 1st of January of 1980
	 * 
	 * @param calendar The {@link Calendar} to truncate
	 */
	public static void truncateDate(Calendar calendar) {
		calendar.set(Calendar.MONTH, 0);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.YEAR, 1980);
	}

	/**
	 * Truncate the date removing hours, minutes, seconds and milliseconds
	 *
	 * @param date The {@link Date} to truncate
	 * @return The truncated {@link Date}
	 */
	public static Date truncateTime(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		truncateTime(calendar);
		return calendar.getTime();
	}

	/**
	 * Truncate the {@link Calendar} removing hours, minutes, seconds and milliseconds
	 *
	 * @param calendar The {@link Calendar} to truncate
	 */
	public static void truncateTime(Calendar calendar) {
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
	}

	/**
	 * @return the current moment
	 */
	public static Date now() {
		return fakeNow != null ? fakeNow : new Date();
	}

	public static long nowMillis() {
		return fakeNow != null ? fakeNow.getTime() : System.currentTimeMillis();
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
	
	public static Calendar todayCalendar() {
		Calendar calendar = Calendar.getInstance();
		DateUtils.truncateTime(calendar);
		return calendar;
	}
	
	/**
	 * @return a day after today
	 */
	public static Date tomorrow() {
		Calendar calendar = DateUtils.todayCalendar();
		return DateUtils.addDays(calendar.getTime(), 1);
	}
	
	public static Date today() {
		Calendar calendar = DateUtils.todayCalendar();
		return calendar.getTime();
	}
	
	/**
	 * @return a day before today
	 */
	public static Date yesterday() {
		Calendar calendar = DateUtils.todayCalendar();
		return DateUtils.addDays(calendar.getTime(), -1);
	}
	
	/**
	 * @param months amount of months to move the calendar
	 * @return a date that is <code>months</code> in the future/past. Use negative values for past dates.
	 */
	public static Date monthsAway(int months) {
		return DateUtils.addMonths(DateUtils.todayCalendar().getTime(), months);
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
		DateUtils.truncateTime(calendar);
		return calendar.getTime();
	}

	/**
	 * @param fromDate the start date
	 * @param toDate the end date
	 * @return an integer representing the amount of days between fromDate and toDate
	 */
	public static Integer differenceInDays(Date fromDate, Date toDate) {
		Long diff = toDate.getTime() - fromDate.getTime();
		diff = diff / (DateUtils.MILLIS_PER_DAY);
		return diff.intValue();
	}

	/**
	 * @param fromDate the start date
	 * @param toDate the end date
	 * @return an double representing the amount of hours between fromDate and toDate
	 */
	public static double differenceInHours(Date fromDate, Date toDate) {
		double diff = toDate.getTime() - fromDate.getTime();
		diff = diff / (DateUtils.MILLIS_PER_HOUR);
		return diff;
	}

	/**
	 * @param fromDate the start date
	 * @param toDate the end date
	 * @return an integer representing the amount of minutes between fromDate and toDate
	 */
	public static Integer differenceInMinutes(Date fromDate, Date toDate) {
		Long diff = toDate.getTime() - fromDate.getTime();
		diff = diff / (DateUtils.MILLIS_PER_MINUTE);
		return diff.intValue();
	}
	
	public static String formatDuration(long duration) {
		
		long hours = TimeUnit.MILLISECONDS.toHours(duration);
		long minutes = TimeUnit.MILLISECONDS.toMinutes(duration) - (hours * 60);
		long seconds = TimeUnit.MILLISECONDS.toSeconds(duration) - (hours * 60 * 60) - (minutes * 60);
		long milliseconds = TimeUnit.MILLISECONDS.toMillis(duration) - (hours * 60 * 60 * 1000) - (minutes * 60 * 1000)
				- (seconds * 1000);
		
		StringBuilder builder = new StringBuilder();
		if (hours > 0) {
			builder.append(hours);
			builder.append("h, ");
		}
		if ((minutes > 0) || (builder.length() > 0)) {
			builder.append(minutes);
			builder.append("m, ");
		}
		if ((seconds > 0) || (builder.length() > 0)) {
			builder.append(seconds);
			builder.append("s, ");
		}
		if (milliseconds >= 0) {
			builder.append(milliseconds);
			builder.append("ms");
		}
		
		return builder.toString();
	}

	public static Long millisecondsToDays(Long timestamp) {
		if (timestamp != null) {
			return (DateUtils.nowMillis() - timestamp) / MILLIS_PER_DAY;
		} else {
			return 0L;
		}
	}
	
	public static void setFakeNow(Date fakeNow) {
		DateUtils.fakeNow = fakeNow;
	}
}
