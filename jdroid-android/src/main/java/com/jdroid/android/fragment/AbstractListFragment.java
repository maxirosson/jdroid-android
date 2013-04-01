package com.jdroid.android.fragment;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockListFragment;
import com.google.ads.AdSize;
import com.jdroid.android.AbstractApplication;
import com.jdroid.android.R;
import com.jdroid.android.context.DefaultApplicationContext;
import com.jdroid.android.domain.User;
import com.jdroid.android.fragment.BaseFragment.UseCaseTrigger;
import com.jdroid.android.loading.LoadingDialogBuilder;
import com.jdroid.android.usecase.DefaultAbstractUseCase;
import com.jdroid.android.usecase.DefaultUseCase;
import com.jdroid.android.usecase.listener.DefaultUseCaseListener;

/**
 * Base {@link ListFragment}
 * 
 * @param <T>
 * @author Maxi Rosson
 */
public abstract class AbstractListFragment<T> extends SherlockListFragment implements FragmentIf, OnItemClickListener,
		OnItemSelectedListener<T> {
	
	private BaseFragment baseFragment;
	
	protected FragmentIf getFragmentIf() {
		return (FragmentIf)this.getActivity();
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#getAndroidApplicationContext()
	 */
	@Override
	public DefaultApplicationContext getAndroidApplicationContext() {
		return getFragmentIf().getAndroidApplicationContext();
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#shouldRetainInstance()
	 */
	@Override
	public Boolean shouldRetainInstance() {
		return baseFragment.shouldRetainInstance();
	}
	
	/**
	 * @see android.support.v4.app.Fragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		baseFragment = AbstractApplication.get().createBaseFragment(this);
		baseFragment.onCreate(savedInstanceState);
	}
	
	/**
	 * @see android.support.v4.app.Fragment#onViewCreated(android.view.View, android.os.Bundle)
	 */
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		baseFragment.onViewCreated(view, savedInstanceState);
		View emptyView = getListView().getEmptyView();
		if (emptyView != null) {
			emptyView.setVisibility(View.GONE);
			if (emptyView instanceof TextView) {
				((TextView)emptyView).setText(getNoResultsText());
			}
		}
	}
	
	protected int getNoResultsText() {
		return R.string.noResults;
	}
	
	/**
	 * @see android.support.v4.app.Fragment#onActivityCreated(android.os.Bundle)
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		baseFragment.onActivityCreated(savedInstanceState);
	}
	
	/**
	 * @see android.support.v4.app.Fragment#onStart()
	 */
	@Override
	public void onStart() {
		super.onStart();
		baseFragment.onStart();
	}
	
	/**
	 * @see android.support.v4.app.Fragment#onResume()
	 */
	@Override
	public void onResume() {
		super.onResume();
		baseFragment.onResume();
	}
	
	/**
	 * @see android.support.v4.app.Fragment#onPause()
	 */
	@Override
	public void onPause() {
		super.onPause();
		baseFragment.onPause();
	}
	
	/**
	 * @see android.support.v4.app.Fragment#onStop()
	 */
	@Override
	public void onStop() {
		super.onStop();
		baseFragment.onStop();
	}
	
	/**
	 * @see android.support.v4.app.Fragment#onDestroyView()
	 */
	@Override
	public void onDestroyView() {
		super.onDestroyView();
		baseFragment.onDestroyView();
	}
	
	/**
	 * @see android.support.v4.app.Fragment#onDestroy()
	 */
	@Override
	public void onDestroy() {
		super.onDestroy();
		baseFragment.onDestroy();
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
	 * @see android.app.ListFragment#onListItemClick(android.widget.ListView, android.view.View, int, long)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public void onListItemClick(ListView listView, View v, int position, long id) {
		int headersCount = listView.getHeaderViewsCount();
		int pos = position - headersCount;
		if ((pos >= 0) && (pos < listView.getAdapter().getCount())) {
			T t = (T)listView.getAdapter().getItem(pos);
			onItemSelected(t);
		}
	}
	
	/**
	 * @param item
	 */
	@Override
	public void onItemSelected(T item) {
		// Do Nothing
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#findView(int)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public <V extends View> V findView(int id) {
		return (V)getView().findViewById(id);
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#findViewOnActivity(int)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public <V extends View> V findViewOnActivity(int id) {
		return (V)getActivity().findViewById(id);
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#inflate(int)
	 */
	@Override
	public View inflate(int resource) {
		return getFragmentIf().inflate(resource);
	}
	
	/**
	 * @see com.jdroid.android.usecase.listener.DefaultUseCaseListener#onStartUseCase()
	 */
	@Override
	public void onStartUseCase() {
		getFragmentIf().onStartUseCase();
	}
	
	/**
	 * @see com.jdroid.android.usecase.listener.DefaultUseCaseListener#onUpdateUseCase()
	 */
	@Override
	public void onUpdateUseCase() {
		getFragmentIf().onUpdateUseCase();
	}
	
	/**
	 * @see com.jdroid.android.usecase.listener.DefaultUseCaseListener#onFinishFailedUseCase(java.lang.RuntimeException)
	 */
	@Override
	public void onFinishFailedUseCase(RuntimeException runtimeException) {
		getFragmentIf().onFinishFailedUseCase(runtimeException);
	}
	
	/**
	 * @see com.jdroid.android.usecase.listener.DefaultUseCaseListener#onFinishUseCase()
	 */
	@Override
	public void onFinishUseCase() {
		getFragmentIf().onFinishUseCase();
	}
	
	/**
	 * @see com.jdroid.android.usecase.listener.DefaultUseCaseListener#onFinishCanceledUseCase()
	 */
	@Override
	public void onFinishCanceledUseCase() {
		getFragmentIf().onFinishCanceledUseCase();
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#executeOnUIThread(java.lang.Runnable)
	 */
	@Override
	public void executeOnUIThread(Runnable runnable) {
		getFragmentIf().executeOnUIThread(runnable);
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#showLoading()
	 */
	@Override
	public void showLoading() {
		getFragmentIf().showLoading();
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#showLoading(com.jdroid.android.loading.LoadingDialogBuilder)
	 */
	@Override
	public void showLoading(LoadingDialogBuilder builder) {
		getFragmentIf().showLoading(builder);
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#showLoadingOnUIThread()
	 */
	@Override
	public void showLoadingOnUIThread() {
		getFragmentIf().showLoadingOnUIThread();
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#showLoadingOnUIThread(com.jdroid.android.loading.LoadingDialogBuilder)
	 */
	@Override
	public void showLoadingOnUIThread(LoadingDialogBuilder builder) {
		getFragmentIf().showLoadingOnUIThread(builder);
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#dismissLoading()
	 */
	@Override
	public void dismissLoading() {
		getFragmentIf().dismissLoading();
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#dismissLoadingOnUIThread()
	 */
	@Override
	public void dismissLoadingOnUIThread() {
		getFragmentIf().dismissLoadingOnUIThread();
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#getInstance(java.lang.Class)
	 */
	@Override
	public <I> I getInstance(Class<I> clazz) {
		return getFragmentIf().<I>getInstance(clazz);
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#getExtra(java.lang.String)
	 */
	@Override
	public <E> E getExtra(String key) {
		return getFragmentIf().<E>getExtra(key);
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#getArgument(java.lang.String, java.lang.Object)
	 */
	@Override
	public <E> E getArgument(String key, E defaultValue) {
		return baseFragment.<E>getArgument(key, defaultValue);
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#getArgument(java.lang.String)
	 */
	@Override
	public <E> E getArgument(String key) {
		return baseFragment.<E>getArgument(key);
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#executeUseCase(com.jdroid.android.usecase.DefaultUseCase)
	 */
	@Override
	public void executeUseCase(DefaultUseCase<?> useCase) {
		getFragmentIf().executeUseCase(useCase);
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#getUser()
	 */
	@Override
	public User getUser() {
		return getFragmentIf().getUser();
	}
	
	@SuppressWarnings("unchecked")
	protected <M> M getSelectedItem(MenuItem item) {
		AdapterView.AdapterContextMenuInfo info = (AdapterContextMenuInfo)item.getMenuInfo();
		return (M)getListView().getItemAtPosition(info.position);
	}
	
	@SuppressWarnings("unchecked")
	public T getMenuItem(MenuItem item) {
		AdapterView.AdapterContextMenuInfo info = (AdapterContextMenuInfo)item.getMenuInfo();
		return (T)getListView().getItemAtPosition(info.position);
	}
	
	public ActionBar getSupportActionBar() {
		return getSherlockActivity().getSupportActionBar();
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#getAdSize()
	 */
	@Override
	public AdSize getAdSize() {
		return AdSize.SMART_BANNER;
	}
	
	public void onResumeUseCase(DefaultAbstractUseCase useCase, DefaultUseCaseListener listener) {
		baseFragment.onResumeUseCase(useCase, listener);
	}
	
	public void onResumeUseCase(DefaultAbstractUseCase useCase, DefaultUseCaseListener listener,
			UseCaseTrigger useCaseTrigger) {
		baseFragment.onResumeUseCase(useCase, listener, useCaseTrigger);
	}
	
	public void onPauseUseCase(DefaultAbstractUseCase useCase, DefaultUseCaseListener listener) {
		baseFragment.onPauseUseCase(useCase, listener);
	}
}
