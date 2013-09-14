package com.jdroid.android.adapter;

import java.util.Collection;
import java.util.List;
import android.content.Context;
import android.view.View;
import android.widget.ArrayAdapter;
import com.jdroid.java.collections.Lists;

/**
 * 
 * @author Maxi Rosson
 * @param <T>
 */
public class BaseArrayAdapter<T> extends ArrayAdapter<T> {
	
	/**
	 * @param context
	 * @param objects
	 */
	public BaseArrayAdapter(Context context, List<T> objects) {
		super(context, 0, Lists.safeArrayList(objects));
	}
	
	/**
	 * @param context
	 */
	public BaseArrayAdapter(Context context) {
		super(context, 0, Lists.<T>newArrayList());
	}
	
	public void replaceAll(Collection<? extends T> items) {
		setNotifyOnChange(false);
		clear();
		if (items != null) {
			for (T item : items) {
				add(item);
			}
		}
		notifyDataSetChanged();
	}
	
	/**
	 * Finds a view that was identified by the id attribute from the {@link View} view.
	 * 
	 * @param containerView The view that contains the view to find.
	 * @param id The id to search for.
	 * @param <V> The {@link View} class.
	 * 
	 * @return The view if found or null otherwise.
	 */
	@SuppressWarnings("unchecked")
	public <V extends View> V findView(View containerView, int id) {
		return (V)containerView.findViewById(id);
	}
	
}
