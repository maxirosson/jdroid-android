package com.jdroid.android.view;

import android.content.Context;
import android.graphics.Rect;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 
 * @author Maxi Rosson
 */
public class CustomViewPager extends ViewPager {
	
	private boolean enabled = true;
	
	public CustomViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	/**
	 * @see android.support.v4.view.ViewPager#onTouchEvent(android.view.MotionEvent)
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (enabled) {
			return super.onTouchEvent(event);
		}
		return false;
	}
	
	/**
	 * @see android.support.v4.view.ViewPager#onInterceptTouchEvent(android.view.MotionEvent)
	 */
	@Override
	public boolean onInterceptTouchEvent(MotionEvent event) {
		if (enabled) {
			return super.onInterceptTouchEvent(event);
		}
		return false;
	}
	
	/**
	 * @see android.view.View#onFocusChanged(boolean, int, android.graphics.Rect)
	 */
	@Override
	protected void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect) {
	}
	
	public void setPagingEnabled(boolean enabled) {
		this.enabled = enabled;
	}
}