package com.jdroid.javaweb.domain;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import com.jdroid.java.exception.UnexpectedException;
import com.jdroid.java.utils.DateUtils;

/**
 * This is a Date Range class to store a start date and an end date
 * 
 */
@Embeddable
public class DateRange implements Serializable, Comparable<DateRange> {
	
	@Temporal(TemporalType.DATE)
	private Date startDate;
	
	@Temporal(TemporalType.DATE)
	private Date endDate;
	
	/**
	 * The default constructor
	 */
	public DateRange() {
		// Default constructor
	}
	
	/**
	 * Constructor.<br>
	 * This constructor validates that the end date is not before the start date.
	 * 
	 * @param startDate Date range's start date.
	 * @param endDate Date range's end date.
	 */
	public DateRange(Date startDate, Date endDate) {
		
		if ((startDate != null) && (endDate != null) && DateUtils.isAfter(startDate, endDate)) {
			throw new UnexpectedException("The end date must be after start date.");
		}
		this.startDate = startDate;
		this.endDate = endDate;
	}
	
	/**
	 * @see java.lang.Object#clone()
	 */
	@Override
	public DateRange clone() {
		return new DateRange(startDate, endDate);
	}
	
	/**
	 * @see Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (prime * result) + ((endDate == null) ? 0 : endDate.hashCode());
		result = (prime * result) + ((startDate == null) ? 0 : startDate.hashCode());
		return result;
	}
	
	/**
	 * @see Object#equals(Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		DateRange other = (DateRange)obj;
		if (endDate == null) {
			if (other.endDate != null) {
				return false;
			}
		} else if (!endDate.equals(other.endDate)) {
			return false;
		}
		if (startDate == null) {
			if (other.startDate != null) {
				return false;
			}
		} else if (!startDate.equals(other.startDate)) {
			return false;
		}
		return true;
	}
	
	/**
	 * @return startDate Get the start date
	 */
	public Date getStartDate() {
		return startDate;
	}
	
	/**
	 * @return endDate Get the end date
	 */
	public Date getEndDate() {
		return endDate;
	}
	
	/**
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(DateRange dateRange) {
		int compareStart = getStartDate().compareTo(dateRange.getStartDate());
		if (compareStart == 0) {
			return getEndDate().compareTo(dateRange.getEndDate());
		}
		return compareStart;
	}
	
	/**
	 * Verifies if this {@link DateRange} is inside the dates
	 * 
	 * @param theStartDate The start date
	 * @param theEndDate The end date
	 * @return If this date range is inside the range
	 */
	public boolean isContainedInPeriod(Date theStartDate, Date theEndDate) {
		return DateUtils.containsPeriod(theStartDate, theEndDate, getStartDate(), getEndDate());
	}
	
	/**
	 * Compares this {@link DateRange} with another instance and validates that both overlap.<br>
	 * As an important note, having a null value in start or end date means that the range doesn't have a limit on that
	 * side.
	 * 
	 * @param dateRange The {@link DateRange} with which compare this one.
	 * @return <b>boolean</b> <code>true</code> if both ranges overlap.
	 */
	public boolean overlaps(DateRange dateRange) {
		return DateUtils.periodsOverlap(getStartDate(), getEndDate(), dateRange.getStartDate(), dateRange.getEndDate());
	}
	
	/**
	 * Calculates and returns the intersection of this and another {@link DateRange}.
	 * 
	 * @param dateRange The {@link DateRange} to be intersected with this one.
	 * @return {@link DateRange} The intersection.
	 * @throws IllegalArgumentException If this {@link DateRange} doesn't overlap with the other.
	 */
	public DateRange intersection(DateRange dateRange) {
		if (!overlaps(dateRange)) {
			throw new IllegalArgumentException();
		}
		;
		return new DateRange(startDate.after(dateRange.getStartDate()) ? startDate : dateRange.getStartDate(),
				endDate.before(dateRange.getEndDate()) ? endDate : dateRange.getEndDate());
	}
}
