package com.jdroid.android.fragment;

import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import com.jdroid.android.fragment.BaseFragment.UseCaseTrigger;
import com.jdroid.android.listener.FilterListTextWatcher;

/**
 * Search fragment that performs the search only once, when the fragment is created, and has the ability to filter
 * thereafter.
 * 
 * @param <T> An item in the list. The filtering will be done based on {@link T#toString()}.
 * 
 * @author Estefania Caravatti
 */
public abstract class AbstractLocalSearchFragment<T> extends AbstractSearchFragment<T> {
	
	/**
	 * @see com.jdroid.android.fragment.AbstractSearchFragment#getUseCaseTrigger()
	 */
	@Override
	protected UseCaseTrigger getUseCaseTrigger() {
		return UseCaseTrigger.ONCE;
	}
	
	@Override
	protected TextWatcher getTextWatcher() {
		return new FilterListTextWatcher<T>() {
			
			@Override
			public void doAfterTextChanged(String prefix) {
				AbstractLocalSearchFragment.this.afterTextChanged(prefix.toString());
			}
			
			@Override
			public ArrayAdapter<T> getFilterableArrayAdapter() {
				return AbstractLocalSearchFragment.this.getFilterableArrayAdapter();
			}
		};
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractSearchFragment#doCancel()
	 */
	@Override
	public void doCancel() {
		getSearchText().setText(null);
	}
	
	/**
	 * @see com.jdroid.android.activity.AbstractSearchActivity#doSearch()
	 */
	@Override
	protected void doSearch() {
		// Do Nothing
	}
	
	/**
	 * @return The {@link ArrayAdapter} used to filter the results.
	 */
	protected abstract ArrayAdapter<T> getFilterableArrayAdapter();
	
	@Override
	protected void afterTextChanged(String text) {
		// Do Nothing
	}
}
