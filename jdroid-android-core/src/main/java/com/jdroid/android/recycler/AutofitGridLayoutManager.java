package com.jdroid.android.recycler;

import android.content.Context;
import androidx.annotation.CallSuper;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * A {@link GridLayoutManager} subclass that automatically calculates the spanCount using the specified columnWidth.
 * The columnWidth can be provided in some of the constructors or calling the method {@link #setColumnWidth(int)}
 *
 * @see #setColumnWidth(int)
 */
public class AutofitGridLayoutManager extends GridLayoutManager {

	private int columnWidth;
	private boolean recalculateSpanCount = false;

	public AutofitGridLayoutManager(Context context, int columnWidth) {
		// Initially set spanCount to 1, will be changed later if needed in onLayoutChildren(...)
		super(context, 1);
		innerSetColumnWidth(columnWidth);
	}

	public AutofitGridLayoutManager(Context context, int columnWidth, int orientation, boolean reverseLayout) {
		// Initially set spanCount to 1, will be changed later if needed in onLayoutChildren(...)
		super(context, 1, orientation, reverseLayout);
		innerSetColumnWidth(columnWidth);
	}

	public AutofitGridLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
	}

	private void innerSetColumnWidth(int newColumnWidth) {
		if (newColumnWidth > 0 && newColumnWidth != columnWidth) {
			columnWidth = newColumnWidth;
			recalculateSpanCount = true;
		}
	}

	/**
	 * Set the width of columns in the grid.
	 *
	 * @param columnWidth The column width, in pixels.
	 */
	@CallSuper
	public void setColumnWidth(int columnWidth) {
		innerSetColumnWidth(columnWidth);
	}

	@Override
	public void setSpanCount(int spanCount) {
		columnWidth = 0;
		recalculateSpanCount = false;
		super.setSpanCount(spanCount);
	}

	@Override
	public void setOrientation(int orientation) {
		if (columnWidth > 0) {
			recalculateSpanCount = true;
		}
		super.setOrientation(orientation);
	}

	@Override
	public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
		int width = getWidth();
		int height = getHeight();
		if (recalculateSpanCount && columnWidth > 0 && width > 0 && height > 0) {
			int totalSpace;
			if (getOrientation() == VERTICAL) {
				totalSpace = width - getPaddingRight() - getPaddingLeft();
			} else {
				totalSpace = height - getPaddingTop() - getPaddingBottom();
			}
			int spanCount = Math.max(1, totalSpace / columnWidth);
			super.setSpanCount(spanCount);
			recalculateSpanCount = false;
		}
		super.onLayoutChildren(recycler, state);
	}

}
