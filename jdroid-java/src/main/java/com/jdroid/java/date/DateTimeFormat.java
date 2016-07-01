package com.jdroid.java.date;

public class DateTimeFormat {

	private DateTimeFormat(){}

	/** Date format like 2013-01-11T20:30:00.000 */
	public static final String YYYYMMDDTHHMMSSSSS = "yyyy-MM-dd'T'HH:mm:ss.SSS";

	/** Date format like yyyy-MM-ddTHH:mm:ss ZZZZ */
	public static final String YYYYMMDDTHHMMSSZ = "yyyy-MM-dd'T'HH:mm:ssZZZZ";

	/** Date format like yyyy-MM-dd HH:mm:ss Z */
	public static final String YYYYMMDDHHMMSSZ = "yyyy-MM-dd HH:mm:ss Z";

	/** Date format like 2010-10-25 21:30:00.123 */
	public static final String YYYYMMDDHHMMSSSSS = "yyyy-MM-dd HH:mm:ss.SSS";

	/** Date format like 2010-10-25 21:30:00 */
	public static final String YYYYMMDDHHMMSS = "yyyy-MM-dd HH:mm:ss";

	/** Date format like 10/25/2010 21:30 */
	public static final String MMDDYYYYHHMM = "MM/dd/yyyy HH:mm";

	/** Date format like Nov 5 1985 3:45 PM */
	public static final String MMMDYYYYHHMMAA = "MMM d yyyy hh:mm aa";

	/** Date format like Nov 5 3:45 PM */
	public static final String MMMDHHMMAA = "MMM d hh:mm aa";

	/** Date format like 10/25/2010 */
	public static final String MMDDYYYY = "MM/dd/yyyy";

	/** Date format like 10-25-2010 */
	public static final String MMDDYYYY_SLASH = "MM-dd-yyyy";

	/** Date format like 2010-10-25 */
	public static final String YYYYMMDD = "yyyy-MM-dd";

	/** Date format like Nov 5 1985 */
	public static final String MMMDYYYY = "MMM d yyyy";

	/** Date format like Sat, Dec 15, 2012 */
	public static final String EEMMMDYYYY = "EE, MMM d, yyyy";

	/** Date format like Monday Nov 5 1985 */
	public static final String EEEE_MMMDYYYY = "EEEE MMM d yyyy";

	/** Date format like Friday 5 November 2013 */
	public static final String EEEEDMMMMYYYY = "EEEE d MMMM yyyy";

	/** Date format like Fri 5 November */
	public static final String EEDMMMM = "EE d MMMM";

	/** Date format like Fri 5 November 2013 */
	public static final String EEDMMMMYYYY = "EE d MMMM yyyy";

	/** Date format like Monday Nov 5 */
	public static final String EEEE_MMMD = "EEEE MMM d";

	/** Date format like Friday 5 November */
	public static final String EEEEDMMMM = "EEEE d MMMM";

	/** Date format like Friday November */
	public static final String EEEEMMMM = "EEEE MMMM";

	/** Date format like November 5 */
	public static final String MMMMD = "MMMM d";

	/** Date format like Nov 5 */
	public static final String MMMD = "MMM d";

	/** Date format like Friday */
	public static final String EEEE = "EEEE";

	/** Date format like Fri */
	public static final String E = "E";

	/** Date format like Fri 5:15 AM */
	public static final String EHHMMAA = "E hh:mm aa";

	/** Date format like 21:45:34 */
	public static final String HHMMSS = "HH:mm:ss";

	/** Date format like 03:45 PM */
	public static final String HHMMAA = "hh:mm aa";

	/** Date format like 21:45 */
	public static final String HHMM = "HH:mm";

}
