package com.jdroid.android.recycler.pagination;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import com.jdroid.android.R;
import com.jdroid.android.exception.DialogErrorDisplayer;
import com.jdroid.android.exception.ErrorDisplayer;
import com.jdroid.android.listener.OnEnterKeyListener;
import com.jdroid.android.usecase.UseCaseHelper;
import com.jdroid.android.usecase.UseCaseTrigger;
import com.jdroid.android.utils.AppUtils;
import com.jdroid.android.utils.ToastUtils;
import com.jdroid.java.exception.AbstractException;
import com.jdroid.java.utils.StringUtils;

/**
 * Base search Fragment. It has a search text and a list with the results.
 */
public abstract class AbstractSearchPaginatedRecyclerFragment extends AbstractPaginatedRecyclerFragment {

	private int threshold = 1;
	private EditText searchText;
	private View loading;

	@Override
	public Integer getContentFragmentLayout() {
		return R.layout.jdroid_abstract_search_fragment;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		searchText = findView(R.id.searchText);
		searchText.setHint(getSearchEditTextHintResId());
		searchText.setOnFocusChangeListener(new View.OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					AppUtils.showSoftInput(getActivity());
				} else {
					AppUtils.hideSoftInput(v);
				}
			}
		});
		searchText.requestFocus();

		if (isInstantSearchEnabled()) {
			searchText.addTextChangedListener(getTextWatcher());
			searchText.setImeOptions(EditorInfo.IME_ACTION_DONE);
		} else {
			searchText.setOnKeyListener(new OnEnterKeyListener(false) {

				@Override
				public void onRun(View view) {
					search();
				}
			});
		}

		View searchButton = findView(R.id.searchButton);
		if (isInstantSearchEnabled()) {
			if (searchButton != null) {
				searchButton.setVisibility(View.GONE);
			}
		} else {
			searchButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					search();
				}
			});
		}

		View cancelButton = findView(R.id.cancelButton);
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

		loading = findView(R.id.loading);
	}

	@Override
	public void onPause() {
		super.onPause();
		AppUtils.hideSoftInput(getView());
	}

	public Boolean isInstantSearchEnabled() {
		return true;
	}

	/**
	 * @return <code>true</code> if the amount of text in the field meets or exceeds the {@link #getThreshold}
	 * 	requirement. You can override this to impose a different standard for when filtering will be triggered.
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
				AbstractSearchPaginatedRecyclerFragment.this.afterTextChanged(prefix.toString());
			}
		};
	}

	protected void afterTextChanged(String text) {
		if (enoughToFilter()) {
			search();
		} else {
			if (getAdapter() != null) {
				getAdapter().clear();
			}
			if (emptyViewContainer != null) {
				emptyViewContainer.setVisibility(View.GONE);
			}
		}
	}

	protected int getNoResultsResId() {
		return R.string.jdroid_noResultsSearch;
	}

	protected int getSearchEditTextHintResId() {
		return R.string.jdroid_typeHere;
	}

	@Override
	protected UseCaseTrigger getUseCaseTrigger() {
		return UseCaseTrigger.MANUAL;
	}

	private void search() {
		String searchValue = searchText.getText().toString();
		if (StringUtils.isNotEmpty(searchValue) || !isSearchValueRequired()) {
			doSearch(searchValue);
			if (!isInstantSearchEnabled()) {
				searchText.clearFocus();
			}
		} else {
			ToastUtils.showToast(R.string.jdroid_requiredSearchTerm);
		}
	}

	protected void doCancel() {
		searchText.setText(null);
		getSearchUseCase().setSearchValue(null);
		getSearchUseCase().reset();
		dismissLoading();

		if (getAdapter() != null) {
			getAdapter().clear();
		}
		if (emptyViewContainer != null) {
			emptyViewContainer.setVisibility(View.GONE);
		}
	}

	protected void doSearch(String searchValue) {
		getSearchUseCase().setSearchValue(searchValue);
		getSearchUseCase().reset();

		if (!isInstantSearchEnabled() && getAdapter() != null) {
			getAdapter().clear();
		}
		UseCaseHelper.executeUseCase(getSearchUseCase());
	}

	@Override
	public void showLoading() {
		executeOnUIThread(new Runnable() {

			@Override
			public void run() {
				if (loading != null) {
					loading.setVisibility(View.VISIBLE);
				}
			}
		});
	}

	@Override
	public ErrorDisplayer createErrorDisplayer(AbstractException abstractException) {
		DialogErrorDisplayer.markAsNotGoBackOnError(abstractException);
		return super.createErrorDisplayer(abstractException);
	}

	@Override
	public void dismissLoading() {
		executeOnUIThread(new Runnable() {

			@Override
			public void run() {
				if (loading != null) {
					loading.setVisibility(View.INVISIBLE);
				}
			}
		});
	}

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

	@Override
	protected SearchUseCase<Object> createPaginatedUseCase() {
		return null;
	}

	private SearchUseCase<Object> getSearchUseCase() {
		return (SearchUseCase<Object>)paginatedUseCase;
	}
}
