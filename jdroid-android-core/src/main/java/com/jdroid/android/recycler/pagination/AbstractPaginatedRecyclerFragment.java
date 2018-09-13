package com.jdroid.android.recycler.pagination;

import android.os.Bundle;
import android.support.annotation.MainThread;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.jdroid.android.R;
import com.jdroid.android.exception.DialogErrorDisplayer;
import com.jdroid.android.recycler.AbstractRecyclerFragment;
import com.jdroid.android.recycler.FooterRecyclerViewType;
import com.jdroid.android.recycler.RecyclerViewAdapter;
import com.jdroid.android.usecase.UseCaseHelper;
import com.jdroid.android.usecase.UseCaseTrigger;
import com.jdroid.java.exception.AbstractException;

import java.util.List;

public abstract class AbstractPaginatedRecyclerFragment extends AbstractRecyclerFragment {

	private static final int ITEMS_REMAINING_TO_START_PAGINATION = 4;

	private Boolean paginationInProgress = false;
	protected PaginatedUseCase<Object> paginatedUseCase;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		paginatedUseCase = createPaginatedUseCase();
	}

	@Override
	public void onStart() {
		super.onStart();
		UseCaseHelper.registerUseCase(paginatedUseCase, this, getUseCaseTrigger());
	}

	protected UseCaseTrigger getUseCaseTrigger() {
		return UseCaseTrigger.ONCE;
	}

	@Override
	public void onStop() {
		super.onStop();
		UseCaseHelper.unregisterUseCase(paginatedUseCase, this);
	}

	protected abstract PaginatedUseCase<Object> createPaginatedUseCase();

	protected abstract RecyclerViewAdapter createAdapter(List<Object> items);

	private void initAdapter() {
		if (getAdapter() != null) {
			getAdapter().clear();
		}
		setAdapter(createAdapter(paginatedUseCase.getResults()));
	}

	@MainThread
	@Override
	public void onStartUseCase() {
		if (paginatedUseCase.isPaginating()) {
			getAdapter().setFooter(new LoadingRecyclerViewType());
		} else {
			super.onStartUseCase();
		}
	}

	@MainThread
	@Override
	public void onFinishFailedUseCase(AbstractException abstractException) {
		if (paginatedUseCase.isPaginating()) {
			DialogErrorDisplayer.markAsNotGoBackOnError(abstractException);
		}
		super.onFinishFailedUseCase(abstractException);
		paginationInProgress = false;
		if (paginatedUseCase.isPaginating()) {
			dismissPaginationLoading();
		}
	}

	private void dismissPaginationLoading() {
		getAdapter().removeFooter();
	}

	@MainThread
	@Override
	public void onFinishUseCase() {
		paginationInProgress = false;
		if (paginatedUseCase.isPaginating()) {

			dismissPaginationLoading();

			getAdapter().addItems(paginatedUseCase.getResults());
			dismissLoading();
		} else {

			if ((getItemsToAutoPaginate() != null) && (paginatedUseCase.getResults().size() <= getItemsToAutoPaginate())) {
				if (paginatedUseCase.getResults().isEmpty()) {
					initAdapter();
				} else {
					initAdapter();
					dismissLoading();
				}
				paginatedUseCase.markAsPaginating();
				UseCaseHelper.executeUseCase(paginatedUseCase);
			} else {
				initAdapter();
				dismissLoading();
			}
		}
	}

	@Override
	public void setAdapter(RecyclerViewAdapter adapter) {
		super.setAdapter(adapter);

		RecyclerView recyclerView = getRecyclerView();
		if (recyclerView != null) {
			paginationInProgress = false;
			recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

				@Override
				public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
					if (!paginationInProgress && !paginatedUseCase.isLastPage()) {

						int visibleItemCount = getLayoutManager().getChildCount();
						int totalItemCount = getLayoutManager().getItemCount();
						int firstVisibleItemPosition = ((LinearLayoutManager)getLayoutManager()).findFirstVisibleItemPosition();

						if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount - getItemsRemainingToStartPagination()
							&& firstVisibleItemPosition >= 0) {
							paginatedUseCase.markAsPaginating();
							UseCaseHelper.executeUseCase(paginatedUseCase);
							paginationInProgress = true;
						}
					}
				}
			});
		}
	}

	protected int getItemsRemainingToStartPagination() {
		return ITEMS_REMAINING_TO_START_PAGINATION;
	}

	protected Integer getItemsToAutoPaginate() {
		return null;
	}

	protected int getPaginationFooterResId() {
		return R.layout.jdroid_pagination_footer;
	}

	public class LoadingRecyclerViewType extends FooterRecyclerViewType {

		@Override
		protected Integer getLayoutResourceId() {
			return AbstractPaginatedRecyclerFragment.this.getPaginationFooterResId();
		}

		@Override
		public AbstractRecyclerFragment getAbstractRecyclerFragment() {
			return AbstractPaginatedRecyclerFragment.this;
		}
	}
}
