package com.jdroid.android.exception;

import java.lang.Thread.UncaughtExceptionHandler;
import com.jdroid.java.exception.ApplicationException;
import com.jdroid.java.exception.BusinessException;
import com.jdroid.java.exception.ConnectionException;

/**
 * 
 * @author Maxi Rosson
 */
public interface ExceptionHandler extends UncaughtExceptionHandler {
	
	/**
	 * @param thread
	 * @param throwable
	 */
	public void handleMainThreadException(Thread thread, Throwable throwable);
	
	/**
	 * @param businessException
	 */
	public void handleException(BusinessException businessException);
	
	/**
	 * @param thread
	 * @param businessException
	 */
	public void handleException(Thread thread, BusinessException businessException);
	
	/**
	 * @param thread
	 * @param connectionException
	 */
	public void handleException(Thread thread, ConnectionException connectionException);
	
	/**
	 * @param thread
	 * @param applicationException
	 */
	public void handleException(Thread thread, ApplicationException applicationException);
	
	/**
	 * @param thread
	 * @param throwable
	 */
	public void handleException(Thread thread, Throwable throwable);
	
	public void logHandledException(String message, Throwable throwable);
	
	public void logHandledException(Throwable throwable);
	
}
