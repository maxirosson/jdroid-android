package com.jdroid.java.logger;

import org.slf4j.helpers.MarkerIgnoringBase;

/**
 * A logger wrapper that filter all log requests.
 * 
 */
public class MuteLogger extends MarkerIgnoringBase {
	
	public MuteLogger() {
		
	}
	
	/**
	 * @see org.slf4j.Logger#isTraceEnabled()
	 */
	@Override
	public boolean isTraceEnabled() {
		return false;
	}
	
	/**
	 * @see org.slf4j.Logger#trace(java.lang.String)
	 */
	@Override
	public void trace(final String msg) {
		// Ignore
	}
	
	/**
	 * @see org.slf4j.Logger#trace(java.lang.String, java.lang.Object)
	 */
	@Override
	public void trace(final String format, final Object param1) {
		// Ignore
	}
	
	/**
	 * @see org.slf4j.Logger#trace(java.lang.String, java.lang.Object, java.lang.Object)
	 */
	@Override
	public void trace(final String format, final Object param1, final Object param2) {
		// Ignore
	}
	
	/**
	 * @see org.slf4j.Logger#trace(java.lang.String, java.lang.Object[])
	 */
	@Override
	public void trace(final String format, final Object[] argArray) {
		// Ignore
	}
	
	/**
	 * @see org.slf4j.Logger#trace(java.lang.String, java.lang.Throwable)
	 */
	@Override
	public void trace(final String msg, final Throwable t) {
		// Ignore
	}
	
	/**
	 * @see org.slf4j.Logger#isDebugEnabled()
	 */
	@Override
	public boolean isDebugEnabled() {
		return false;
	}
	
	/**
	 * @see org.slf4j.Logger#debug(java.lang.String)
	 */
	@Override
	public void debug(final String msg) {
		// Ignore
	}
	
	/**
	 * @see org.slf4j.Logger#debug(java.lang.String, java.lang.Object)
	 */
	@Override
	public void debug(final String format, final Object arg1) {
		// Ignore
	}
	
	/**
	 * @see org.slf4j.Logger#debug(java.lang.String, java.lang.Object, java.lang.Object)
	 */
	@Override
	public void debug(final String format, final Object param1, final Object param2) {
		// Ignore
	}
	
	/**
	 * @see org.slf4j.Logger#debug(java.lang.String, java.lang.Object[])
	 */
	@Override
	public void debug(final String format, final Object[] argArray) {
		// Ignore
	}
	
	/**
	 * @see org.slf4j.Logger#debug(java.lang.String, java.lang.Throwable)
	 */
	@Override
	public void debug(final String msg, final Throwable t) {
		// Ignore
	}
	
	/**
	 * @see org.slf4j.Logger#isInfoEnabled()
	 */
	@Override
	public boolean isInfoEnabled() {
		return false;
	}
	
	/**
	 * @see org.slf4j.Logger#info(java.lang.String)
	 */
	@Override
	public void info(final String msg) {
		// Ignore
	}
	
	/**
	 * @see org.slf4j.Logger#info(java.lang.String, java.lang.Object)
	 */
	@Override
	public void info(final String format, final Object arg) {
		// Ignore
	}
	
	/**
	 * @see org.slf4j.Logger#info(java.lang.String, java.lang.Object, java.lang.Object)
	 */
	@Override
	public void info(final String format, final Object arg1, final Object arg2) {
		// Ignore
	}
	
	/**
	 * @see org.slf4j.Logger#info(java.lang.String, java.lang.Object[])
	 */
	@Override
	public void info(final String format, final Object[] argArray) {
		// Ignore
	}
	
	/**
	 * @see org.slf4j.Logger#info(java.lang.String, java.lang.Throwable)
	 */
	@Override
	public void info(final String msg, final Throwable t) {
		// Ignore
	}
	
	/**
	 * @see org.slf4j.Logger#isWarnEnabled()
	 */
	@Override
	public boolean isWarnEnabled() {
		return false;
	}
	
	/**
	 * @see org.slf4j.Logger#warn(java.lang.String)
	 */
	@Override
	public void warn(final String msg) {
		// Ignore
	}
	
	/**
	 * @see org.slf4j.Logger#warn(java.lang.String, java.lang.Object)
	 */
	@Override
	public void warn(final String format, final Object arg) {
		// Ignore
	}
	
	/**
	 * @see org.slf4j.Logger#warn(java.lang.String, java.lang.Object, java.lang.Object)
	 */
	@Override
	public void warn(final String format, final Object arg1, final Object arg2) {
		// Ignore
	}
	
	/**
	 * @see org.slf4j.Logger#warn(java.lang.String, java.lang.Object[])
	 */
	@Override
	public void warn(final String format, final Object[] argArray) {
		// Ignore
	}
	
	/**
	 * @see org.slf4j.Logger#warn(java.lang.String, java.lang.Throwable)
	 */
	@Override
	public void warn(final String msg, final Throwable t) {
		// Ignore
	}
	
	/**
	 * @see org.slf4j.Logger#isErrorEnabled()
	 */
	@Override
	public boolean isErrorEnabled() {
		return false;
	}
	
	/**
	 * @see org.slf4j.Logger#error(java.lang.String)
	 */
	@Override
	public void error(final String msg) {
		// Ignore
	}
	
	/**
	 * @see org.slf4j.Logger#error(java.lang.String, java.lang.Object)
	 */
	@Override
	public void error(final String format, final Object arg) {
		// Ignore
	}
	
	/**
	 * @see org.slf4j.Logger#error(java.lang.String, java.lang.Object, java.lang.Object)
	 */
	@Override
	public void error(final String format, final Object arg1, final Object arg2) {
		// Ignore
	}
	
	/**
	 * @see org.slf4j.Logger#error(java.lang.String, java.lang.Object[])
	 */
	@Override
	public void error(final String format, final Object[] argArray) {
		// Ignore
	}
	
	/**
	 * @see org.slf4j.Logger#error(java.lang.String, java.lang.Throwable)
	 */
	@Override
	public void error(final String msg, final Throwable t) {
		// Ignore
	}
	
}
