package com.jdroid.android.activity;

import java.util.List;
import java.util.Map;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import com.actionbarsherlock.app.SherlockListActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.google.ads.AdSize;
import com.google.inject.Key;
import com.jdroid.android.AbstractApplication;
import com.jdroid.android.R;
import com.jdroid.android.adapter.BaseArrayAdapter;
import com.jdroid.android.domain.User;
import com.jdroid.android.search.SearchResult;
import com.jdroid.android.search.SearchResult.PaginationListener;
import com.jdroid.android.search.SearchResult.SortingListener;
import com.jdroid.android.usecase.DefaultUseCase;
import com.jdroid.android.view.PaginationFooter;
import com.jdroid.java.exception.AbstractException;

/**
 * Base {@link ListActivity}
 * 
 * @author Maxi Rosson
 * @param <T>
 */
public abstract class AbstractListActivity<T> extends SherlockListActivity implements SortingListener<T>,
		OnItemClickListener, ActivityIf {
	
	private BaseActivity baseActivity;
	private PaginationFooter paginationFooter;
	
	/**
	 * @see com.jdroid.android.activity.ActivityIf#onBeforeSetContentView()
	 */
	@Override
	public Boolean onBeforeSetContentView() {
		return baseActivity.onBeforeSetContentView();
	}
	
	/**
	 * @see com.jdroid.android.activity.ActivityIf#onAfterSetContentView(android.os.Bundle)
	 */
	@Override
	public void onAfterSetContentView(Bundle savedInstanceState) {
		baseActivity.onAfterSetContentView(savedInstanceState);
	}
	
	/**
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		baseActivity = AbstractApplication.get().createBaseActivity(this);
		baseActivity.beforeOnCreate();
		super.onCreate(savedInstanceState);
		baseActivity.onCreate(savedInstanceState);
	}
	
	/**
	 * @see android.app.Activity#onContentChanged()
	 */
	@Override
	public void onContentChanged() {
		super.onContentChanged();
		baseActivity.onContentChanged();
	}
	
	/**
	 * @see roboguice.util.ScopedObjectMapProvider#getScopedObjectMap()
	 */
	@Override
	public Map<Key<?>, Object> getScopedObjectMap() {
		return baseActivity.getScopedObjectMap();
	}
	
	/**
	 * @see android.app.Activity#onSaveInstanceState(android.os.Bundle)
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		baseActivity.onSaveInstanceState(outState);
	}
	
	/**
	 * @see android.app.Activity#onRestoreInstanceState(android.os.Bundle)
	 */
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		baseActivity.onRestoreInstanceState(savedInstanceState);
	}
	
	/**
	 * @see roboguice.activity.GuiceActivity#onStart()
	 */
	@Override
	protected void onStart() {
		super.onStart();
		baseActivity.onStart();
	}
	
	/**
	 * @see roboguice.activity.GuiceActivity#onResume()
	 */
	@Override
	protected void onResume() {
		super.onResume();
		baseActivity.onResume();
	}
	
	/**
	 * @see roboguice.activity.RoboActivity#onPause()
	 */
	@Override
	protected void onPause() {
		super.onPause();
		baseActivity.onPause();
	}
	
	/**
	 * @see roboguice.activity.RoboActivity#onStop()
	 */
	@Override
	protected void onStop() {
		super.onStop();
		baseActivity.onStop();
	}
	
	/**
	 * @see roboguice.activity.RoboActivity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		baseActivity.onDestroy();
	}
	
	/**
	 * @see android.app.Activity#onActivityResult(int, int, android.content.Intent)
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		baseActivity.onActivityResult(requestCode, resultCode, data);
	}
	
	/**
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return baseActivity.onCreateOptionsMenu(menu);
	}
	
	/**
	 * @see com.jdroid.android.activity.ActivityIf#getMenuResourceId()
	 */
	@Override
	public int getMenuResourceId() {
		return baseActivity.getMenuResourceId();
	}
	
	/**
	 * @see com.jdroid.android.activity.ActivityIf#doOnCreateOptionsMenu(com.actionbarsherlock.view.Menu)
	 */
	@Override
	public void doOnCreateOptionsMenu(Menu menu) {
		baseActivity.doOnCreateOptionsMenu(menu);
	}
	
	/**
	 * @see com.jdroid.android.activity.ActivityIf#doOnCreateOptionsMenu(com.actionbarsherlock.view.Menu)
	 */
	@Override
	public void doOnCreateOptionsMenu(android.view.Menu menu) {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// REVIEW See if this is the correct approach
		return baseActivity.onOptionsItemSelected(item) ? true : super.onOptionsItemSelected(item);
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#findView(int)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public <V extends View> V findView(int id) {
		return (V)findViewById(id);
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#showLoadingOnUIThread()
	 */
	@Override
	public void showLoadingOnUIThread() {
		baseActivity.showLoadingOnUIThread();
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#showLoadingOnUIThread(java.lang.Integer)
	 */
	@Override
	public void showLoadingOnUIThread(Integer loadingResId) {
		baseActivity.showLoadingOnUIThread(loadingResId);
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#showLoadingOnUIThread(java.lang.Boolean)
	 */
	@Override
	public void showLoadingOnUIThread(Boolean cancelable) {
		baseActivity.showLoadingOnUIThread(cancelable);
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#showLoadingOnUIThread(java.lang.Boolean, java.lang.Integer)
	 */
	@Override
	public void showLoadingOnUIThread(Boolean cancelable, Integer loadingResId) {
		baseActivity.showLoadingOnUIThread(cancelable, loadingResId);
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#showLoading()
	 */
	@Override
	public void showLoading() {
		baseActivity.showLoading();
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#showLoading(java.lang.Integer)
	 */
	@Override
	public void showLoading(Integer loadingResId) {
		baseActivity.showLoading(loadingResId);
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#showLoading(java.lang.Boolean)
	 */
	@Override
	public void showLoading(Boolean cancelable) {
		baseActivity.showLoading(cancelable);
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#showLoading(java.lang.Boolean, java.lang.Integer)
	 */
	@Override
	public void showLoading(Boolean cancelable, Integer loadingResId) {
		baseActivity.showLoading(cancelable, loadingResId);
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#dismissLoading()
	 */
	@Override
	public void dismissLoading() {
		baseActivity.dismissLoading();
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#dismissLoadingOnUIThread()
	 */
	@Override
	public void dismissLoadingOnUIThread() {
		baseActivity.dismissLoadingOnUIThread();
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#executeOnUIThread(java.lang.Runnable)
	 */
	@Override
	public void executeOnUIThread(Runnable runnable) {
		baseActivity.executeOnUIThread(runnable);
	}
	
	@SuppressWarnings("unchecked")
	public T getMenuItem(MenuItem item) {
		AdapterView.AdapterContextMenuInfo info = (AdapterContextMenuInfo)item.getMenuInfo();
		return (T)getListView().getItemAtPosition(info.position);
	}
	
	/**
	 * @see android.widget.AdapterView.OnItemClickListener#onItemClick(android.widget.AdapterView, android.view.View,
	 *      int, long)
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
		onListItemClick((ListView)parent, v, position, id);
	}
	
	/**
	 * @see android.app.ListActivity#onListItemClick(android.widget.ListView, android.view.View, int, long)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public void onListItemClick(ListView listView, View v, int position, long id) {
		int headersCount = listView.getHeaderViewsCount();
		int pos = position - headersCount;
		if ((pos >= 0) && (pos < listView.getAdapter().getCount())) {
			T t = (T)listView.getAdapter().getItem(pos);
			onListItemClick(t);
		}
	}
	
	protected void onListItemClick(T item) {
		// Do Nothing
	}
	
	@SuppressWarnings("unchecked")
	protected <M> M getSelectedItem(MenuItem item) {
		AdapterView.AdapterContextMenuInfo info = (AdapterContextMenuInfo)item.getMenuInfo();
		return (M)getListView().getItemAtPosition(info.position);
	}
	
	protected boolean hasPagination() {
		return false;
	}
	
	public SearchResult<T> getSearchResult() {
		return null;
	}
	
	/**
	 * @see android.app.ListActivity#setListAdapter(android.widget.ListAdapter)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void setListAdapter(ListAdapter adapter) {
		
		if (hasPagination()) {
			if (paginationFooter == null) {
				paginationFooter = (PaginationFooter)LayoutInflater.from(this).inflate(R.layout.pagination_footer, null);
				paginationFooter.setAbstractListActivity(this);
				getListView().addFooterView(paginationFooter, null, false);
			}
			getSearchResult().setPaginationListener((PaginationListener<T>)paginationFooter);
			paginationFooter.refresh();
		}
		super.setListAdapter(adapter);
	}
	
	/**
	 * @see com.jdroid.android.search.SearchResult.SortingListener#onStartSorting()
	 */
	@Override
	public void onStartSorting() {
		executeOnUIThread(new Runnable() {
			
			@Override
			public void run() {
				if (hasPagination()) {
					paginationFooter.hide();
				}
				showLoading();
			}
		});
	}
	
	/**
	 * @see com.jdroid.android.search.SearchResult.SortingListener#onFinishSuccessfulSorting(java.util.List)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void onFinishSuccessfulSorting(final List<T> items) {
		executeOnUIThread(new Runnable() {
			
			@Override
			public void run() {
				BaseArrayAdapter<T> baseArrayAdapter = (BaseArrayAdapter<T>)getListAdapter();
				baseArrayAdapter.replaceAll(items);
				getListView().setSelectionAfterHeaderView();
				if (hasPagination()) {
					paginationFooter.refresh();
				}
				showLoading();
			}
		});
	}
	
	/**
	 * @see com.jdroid.android.search.SearchResult.SortingListener#onFinishInvalidSorting(com.jdroid.java.exception.AbstractException)
	 */
	@Override
	public void onFinishInvalidSorting(AbstractException androidException) {
		dismissLoadingOnUIThread();
		throw androidException;
	}
	
	protected void addHeaderView(int resource) {
		getListView().addHeaderView(inflate(resource));
	}
	
	protected void addFooterView(int resource) {
		getListView().addFooterView(inflate(resource));
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#inflate(int)
	 */
	@Override
	public View inflate(int resource) {
		return baseActivity.inflate(resource);
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#getInstance(java.lang.Class)
	 */
	@Override
	public <I> I getInstance(Class<I> clazz) {
		return baseActivity.getInstance(clazz);
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#getExtra(java.lang.String)
	 */
	@Override
	public <E> E getExtra(String key) {
		return baseActivity.<E>getExtra(key);
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#executeUseCase(com.jdroid.android.usecase.DefaultUseCase)
	 */
	@Override
	public void executeUseCase(DefaultUseCase<?> useCase) {
		baseActivity.executeUseCase(useCase);
	}
	
	/**
	 * @see com.jdroid.android.usecase.listener.DefaultUseCaseListener#onStartUseCase()
	 */
	@Override
	public void onStartUseCase() {
		baseActivity.onStartUseCase();
	}
	
	/**
	 * @see com.jdroid.android.usecase.listener.DefaultUseCaseListener#onUpdateUseCase()
	 */
	@Override
	public void onUpdateUseCase() {
		baseActivity.onUpdateUseCase();
	}
	
	/**
	 * @see com.jdroid.android.usecase.listener.DefaultUseCaseListener#onFinishUseCase()
	 */
	@Override
	public void onFinishUseCase() {
		baseActivity.onFinishUseCase();
	}
	
	/**
	 * @see com.jdroid.android.usecase.listener.DefaultUseCaseListener#onFinishFailedUseCase(java.lang.RuntimeException)
	 */
	@Override
	public void onFinishFailedUseCase(RuntimeException runtimeException) {
		baseActivity.onFinishFailedUseCase(runtimeException);
	}
	
	/**
	 * @see com.jdroid.android.usecase.listener.DefaultUseCaseListener#onFinishCanceledUseCase()
	 */
	@Override
	public void onFinishCanceledUseCase() {
		baseActivity.onFinishCanceledUseCase();
	}
	
	/**
	 * @see com.jdroid.android.activity.ActivityIf#requiresAuthentication()
	 */
	@Override
	public Boolean requiresAuthentication() {
		return baseActivity.requiresAuthentication();
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#getUser()
	 */
	@Override
	public User getUser() {
		return baseActivity.getUser();
	}
	
	public Boolean isAuthenticated() {
		return baseActivity.isAuthenticated();
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#getAdSize()
	 */
	@Override
	public AdSize getAdSize() {
		return baseActivity.getAdSize();
	}
}