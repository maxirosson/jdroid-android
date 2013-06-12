package com.jdroid.android.fragment;

import android.os.Bundle;
import android.os.Handler;
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
	
	private ListAdapter adapter;
	private GridView gridView;
	private View emptyView;
	private boolean listShown;
	
	private Handler handler = new Handler();
	private Runnable requestFocus = new Runnable() {
		
		@Override
		public void run() {
			gridView.focusableViewAvailable(gridView);
		}
	};
	private AdapterView.OnItemClickListener onClickListener = new AdapterView.OnItemClickListener() {
		
		@Override
		public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
			onGridItemClick((GridView)parent, v, position, id);
		}
	};
	
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
		ensureGrid();
		
		View emptyView = gridView.getEmptyView();
		if (emptyView != null) {
			if (emptyView instanceof TextView) {
				((TextView)emptyView).setText(getNoResultsText());
			}
		}
	}
	
	/**
	 * Detach from list view.
	 */
	@Override
	public void onDestroyView() {
		handler.removeCallbacks(requestFocus);
		gridView = null;
		listShown = false;
		emptyView = null;
		super.onDestroyView();
	}
	
	public void setListAdapter(ListAdapter adapter) {
		boolean hadAdapter = adapter != null;
		this.adapter = adapter;
		if (gridView != null) {
			gridView.setAdapter(adapter);
			if (!listShown && !hadAdapter) {
				// The list was hidden, and previously didn't have an
				// adapter. It is now time to show it.
				setListShown(true, getView().getWindowToken() != null);
			}
		}
	}
	
	/**
	 * Set the currently selected grid item to the specified position with the adapter's data
	 * 
	 * @param position
	 */
	public void setSelection(int position) {
		ensureGrid();
		gridView.setSelection(position);
	}
	
	/**
	 * @return the position of the currently selected grid item.
	 */
	public int getSelectedItemPosition() {
		ensureGrid();
		return gridView.getSelectedItemPosition();
	}
	
	/**
	 * @return the cursor row ID of the currently selected grid item.
	 */
	public long getSelectedItemId() {
		ensureGrid();
		return gridView.getSelectedItemId();
	}
	
	public GridView getGridView() {
		ensureGrid();
		return gridView;
	}
	
	/**
	 * Control whether the list is being displayed. You can make it not displayed if you are waiting for the initial
	 * data to show in it. During this time an indeterminant progress indicator will be shown instead.
	 * 
	 * <p>
	 * Applications do not normally need to use this themselves. The default behavior of ListFragment is to start with
	 * the list not being shown, only showing it once an adapter is given with {@link #setListAdapter(ListAdapter)}. If
	 * the list at that point had not been shown, when it does get shown it will be do without the user ever seeing the
	 * hidden state.
	 * 
	 * @param shown If true, the list view is shown; if false, the progress indicator. The initial value is true.
	 */
	public void setListShown(boolean shown) {
		setListShown(shown, true);
	}
	
	/**
	 * Like {@link #setListShown(boolean)}, but no animation is used when transitioning from the previous state.
	 * 
	 * @param shown
	 */
	public void setListShownNoAnimation(boolean shown) {
		setListShown(shown, false);
	}
	
	/**
	 * Control whether the list is being displayed. You can make it not displayed if you are waiting for the initial
	 * data to show in it. During this time an indeterminant progress indicator will be shown instead.
	 * 
	 * @param shown If true, the list view is shown; if false, the progress indicator. The initial value is true.
	 * @param animate If true, an animation will be used to transition to the new state.
	 */
	private void setListShown(boolean shown, boolean animate) {
		ensureGrid();
		if (listShown == shown) {
			return;
		}
		listShown = shown;
	}
	
	public ListAdapter getListAdapter() {
		return adapter;
	}
	
	private void ensureGrid() {
		if (gridView != null) {
			return;
		}
		View root = getView();
		if (root == null) {
			throw new IllegalStateException("Content view not yet created");
		}
		if (root instanceof GridView) {
			gridView = (GridView)root;
		} else {
			emptyView = root.findViewById(android.R.id.empty);
			View rawGridView = root.findViewById(R.id.grid);
			if (!(rawGridView instanceof GridView)) {
				throw new RuntimeException("Content has view with id attribute 'R.id.grid' "
						+ "that is not a GridView class");
			}
			gridView = (GridView)rawGridView;
			if (gridView == null) {
				throw new RuntimeException("Your content must have a GridView whose id attribute is "
						+ "'android.R.id.grid'");
			}
			if (emptyView != null) {
				gridView.setEmptyView(emptyView);
			}
		}
		listShown = true;
		gridView.setOnItemClickListener(onClickListener);
		if (adapter != null) {
			ListAdapter adapter = this.adapter;
			this.adapter = null;
			setListAdapter(adapter);
		}
		handler.post(requestFocus);
	}
	
	protected int getNoResultsText() {
		return R.string.noResults;
	}
	
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
	
	@SuppressWarnings("unchecked")
	public T getMenuItem(MenuItem item) {
		AdapterView.AdapterContextMenuInfo info = (AdapterContextMenuInfo)item.getMenuInfo();
		return (T)gridView.getItemAtPosition(info.position);
	}
}
