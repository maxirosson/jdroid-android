package com.jdroid.android.dialog;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import com.google.ads.AdSize;
import com.jdroid.android.AbstractApplication;
import com.jdroid.android.domain.User;
import com.jdroid.android.fragment.BaseFragment;
import com.jdroid.android.fragment.BaseFragment.UseCaseTrigger;
import com.jdroid.android.fragment.FragmentIf;
import com.jdroid.android.usecase.DefaultAbstractUseCase;
import com.jdroid.android.usecase.DefaultUseCase;
import com.jdroid.android.usecase.listener.DefaultUseCaseListener;

/**
 * 
 * @author Maxi Rosson
 */
public class AbstractDialogFragment extends DialogFragment implements FragmentIf {
	
	private BaseFragment baseFragment;
	
	protected FragmentIf getFragmentIf() {
		return (FragmentIf)this.getActivity();
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
	 * @see com.jdroid.android.fragment.FragmentIf#findView(int)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public <V extends View> V findView(int id) {
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
	 * @see com.jdroid.android.fragment.FragmentIf#showLoading(java.lang.Integer)
	 */
	@Override
	public void showLoading(Integer loadingResId) {
		getFragmentIf().showLoading(loadingResId);
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#showLoading(java.lang.Boolean)
	 */
	@Override
	public void showLoading(Boolean cancelable) {
		getFragmentIf().showLoading(cancelable);
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#showLoading(java.lang.Boolean, java.lang.Integer)
	 */
	@Override
	public void showLoading(Boolean cancelable, Integer loadingResId) {
		getFragmentIf().showLoading(cancelable, loadingResId);
		
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#showLoadingOnUIThread()
	 */
	@Override
	public void showLoadingOnUIThread() {
		getFragmentIf().showLoadingOnUIThread();
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#showLoadingOnUIThread(java.lang.Integer)
	 */
	@Override
	public void showLoadingOnUIThread(Integer loadingResId) {
		getFragmentIf().showLoadingOnUIThread(loadingResId);
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#showLoadingOnUIThread(java.lang.Boolean)
	 */
	@Override
	public void showLoadingOnUIThread(Boolean cancelable) {
		getFragmentIf().showLoadingOnUIThread(cancelable);
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#showLoadingOnUIThread(java.lang.Boolean, java.lang.Integer)
	 */
	@Override
	public void showLoadingOnUIThread(Boolean cancelable, Integer loadingResId) {
		getFragmentIf().showLoadingOnUIThread(cancelable, loadingResId);
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
