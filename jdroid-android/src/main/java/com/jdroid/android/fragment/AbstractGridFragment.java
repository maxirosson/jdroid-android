package com.jdroid.android.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.TextView;
import com.jdroid.android.R;

/**
 * 
 * @param <T>
 * @author Maxi Rosson
 */
public class AbstractGridFragment<T> extends AbstractFragment implements OnItemSelectedListener<T> {
	
	private GridView gridView;
	
	final private AdapterView.OnItemClickListener onClickListener = new AdapterView.OnItemClickListener() {
		
		@Override
		public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
			onGridItemClick((GridView)parent, v, position, id);
		}
	};
	
	@SuppressWarnings("unchecked")
	public void onGridItemClick(GridView parent, View view, int position, long id) {
		onItemSelected((T)parent.getAdapter().getItem(position));
	}
	
	/**
	 * @see com.jdroid.android.fragment.OnItemSelectedListener#onItemSelected(java.lang.Object)
	 */
	@Override
	public void onItemSelected(T item) {
		// Do Nothing
	}
	
	/**
	 * Provide the {@link ListAdapter} for the {@link GridView}.
	 * 
	 * @param adapter
	 */
	public void setListAdapter(ListAdapter adapter) {
		gridView.setAdapter(adapter);
		gridView.requestFocus();
	}
	
	/**
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup,
	 *      android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.grid_fragment, container, false);
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#onViewCreated(android.view.View, android.os.Bundle)
	 */
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		View rawGridView = view.findViewById(R.id.grid);
		if (rawGridView == null) {
			throw new RuntimeException("Your content must have a GridView whose id attribute is "
					+ "'android.R.id.list'");
		}
		if (!(rawGridView instanceof GridView)) {
			throw new RuntimeException("Content has view with id attribute 'android.R.id.list' "
					+ "that is not a GridView class");
		}
		gridView = (GridView)rawGridView;
		gridView.setOnItemClickListener(onClickListener);
		
		View emptyView = view.findViewById(android.R.id.empty);
		if (emptyView != null) {
			gridView.setEmptyView(emptyView);
			emptyView.setVisibility(View.GONE);
			if (emptyView instanceof TextView) {
				((TextView)emptyView).setText(getNoResultsText());
			}
		}
	}
	
	protected int getNoResultsText() {
		return R.string.noResults;
	}
	
	public GridView getGridView() {
		return gridView;
	}
	
	@SuppressWarnings("unchecked")
	public T getMenuItem(MenuItem item) {
		AdapterView.AdapterContextMenuInfo info = (AdapterContextMenuInfo)item.getMenuInfo();
		return (T)gridView.getItemAtPosition(info.position);
	}
}
