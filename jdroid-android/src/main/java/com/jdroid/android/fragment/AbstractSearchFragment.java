package com.jdroid.android.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.jdroid.android.R;
import com.jdroid.android.adapter.BaseArrayAdapter;
import com.jdroid.android.fragment.BaseFragment.UseCaseTrigger;
import com.jdroid.android.listener.OnEnterKeyListener;
import com.jdroid.android.search.PagedResult;
import com.jdroid.android.search.SearchResult;
import com.jdroid.android.usecase.SearchUseCase;
import com.jdroid.android.utils.ToastUtils;
import com.jdroid.java.utils.StringUtils;

/**
 * Base search Fragment. It has a search text and a list with the results.
 * 
 * @param <T> An item in the list.
 * 
 * @author Maxi Rosson
 */
public abstract class AbstractSearchFragment<T> extends AbstractListFragment<T> {
	
	private int threshold = 1;
	private EditText searchText;
	private ImageView searchButton;
	private ImageView cancelButton;
	private TextView emptyLegend;
	
	/**
	 * @see android.support.v4.app.ListFragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup,
	 *      android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.abstract_search_fragment, container, false);
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractListFragment#onViewCreated(android.view.View, android.os.Bundle)
	 */
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		getListView().getEmptyView().setVisibility(View.GONE);
		
		searchText = findView(R.id.searchText);
		if (isInstantSearchEnabled()) {
			searchText.addTextChangedListener(getTextWatcher());
		} else {
			searchText.setOnKeyListener(new OnEnterKeyListener(false) {
				
				@Override
				public void onRun(View view) {
					search();
				}
			});
		}
		
		searchButton = findView(R.id.searchButton);
		if (isInstantSearchEnabled()) {
			searchButton.setVisibility(View.GONE);
		} else {
			searchButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					search();
				}
			});
		}
		
		cancelButton = findView(R.id.cancelButton);
		if (displayCancelButton()) {
			cancelButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					doCancel();
				}
			});
		} else {
			cancelButton.setVisibility(View.GONE);
		}
		
		emptyLegend = findView(android.R.id.empty);
		emptyLegend.setText(getNoResultsResId());
		
		setListAdapter(createBaseArrayAdapter());
	}
	
	public Boolean isInstantSearchEnabled() {
		return true;
	}
	
	/**
	 * @return <code>true</code> if the amount of text in the field meets or exceeds the {@link #getThreshold}
	 *         requirement. You can override this to impose a different standard for when filtering will be triggered.
	 */
	public boolean enoughToFilter() {
		return searchText.getText().length() >= threshold;
	}
	
	protected TextWatcher getTextWatcher() {
		return new TextWatcher() {
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
			
			@Override
			public void afterTextChanged(Editable prefix) {
				AbstractSearchFragment.this.afterTextChanged(prefix.toString());
			}
		};
	}
	
	protected void afterTextChanged(String text) {
		if (enoughToFilter()) {
			search();
		} else {
			getBaseArrayAdapter().clear();
		}
	}
	
	protected int getNoResultsResId() {
		return R.string.noResultsSearch;
	}
	
	protected UseCaseTrigger getUseCaseTrigger() {
		return UseCaseTrigger.MANUAL;
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractListFragment#onResume()
	 */
	@Override
	public void onResume() {
		super.onResume();
		onResumeUseCase(getSearchUseCase(), this, getUseCaseTrigger());
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractListFragment#onPause()
	 */
	@Override
	public void onPause() {
		super.onPause();
		onPauseUseCase(getSearchUseCase(), this);
	}
	
	private void search() {
		if (StringUtils.isNotEmpty(searchText.getText().toString()) || !isSearchValueRequired()) {
			doSearch();
		} else {
			ToastUtils.showToast(R.string.requiredSearchTerm);
		}
	}
	
	protected void doCancel() {
		searchText.setText(null);
		getSearchUseCase().setSearchValue(null);
		getBaseArrayAdapter().clear();
	}
	
	protected void doSearch() {
		getSearchUseCase().setSearchValue(searchText.getText().toString());
		executeUseCase(getSearchUseCase());
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractListFragment#onStartUseCase()
	 */
	@Override
	public void onStartUseCase() {
		// Do nothing
	}
	
	/**
	 * @see com.jdroid.android.activity.AbstractListActivity#onFinishUseCase()
	 */
	@Override
	public void onFinishUseCase() {
		executeOnUIThread(new Runnable() {
			
			@Override
			public void run() {
				SearchResult<T> searchResult = getSearchUseCase().getSearchResult();
				PagedResult<T> pagedResult = searchResult != null ? searchResult.getPagedResult()
						: new PagedResult<T>();
				getBaseArrayAdapter().replaceAll(pagedResult.getResults());
			}
		});
	}
	
	protected abstract BaseArrayAdapter<T> createBaseArrayAdapter();
	
	@SuppressWarnings("unchecked")
	private BaseArrayAdapter<T> getBaseArrayAdapter() {
		return (BaseArrayAdapter<T>)getListAdapter();
	}
	
	/**
	 * @return The {@link SearchUseCase} to use.
	 */
	protected abstract SearchUseCase<T> getSearchUseCase();
	
	protected boolean displayCancelButton() {
		return true;
	}
	
	/**
	 * @return Whether a search can be performed without a search value or not.
	 */
	public boolean isSearchValueRequired() {
		return false;
	}
	
	/**
	 * @return the threshold
	 */
	public int getThreshold() {
		return threshold;
	}
	
	/**
	 * @param threshold the threshold to set
	 */
	public void setThreshold(int threshold) {
		if (threshold <= 0) {
			threshold = 1;
		}
		this.threshold = threshold;
	}
	
	/**
	 * @return the searchText
	 */
	public EditText getSearchText() {
		return searchText;
	}
}
