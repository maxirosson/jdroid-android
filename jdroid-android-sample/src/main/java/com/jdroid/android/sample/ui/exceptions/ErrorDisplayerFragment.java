package com.jdroid.android.sample.ui.exceptions;

import android.os.Bundle;
import android.view.View;

import com.jdroid.android.androidx.lifecycle.Resource;
import com.jdroid.android.androidx.lifecycle.ResourceObserver;
import com.jdroid.android.exception.AbstractErrorDisplayer;
import com.jdroid.android.exception.DialogErrorDisplayer;
import com.jdroid.android.exception.ErrorDisplayer;
import com.jdroid.android.exception.SnackbarErrorDisplayer;
import com.jdroid.android.fragment.AbstractFragment;
import com.jdroid.android.sample.R;
import com.jdroid.android.sample.androidx.SampleRepository;
import com.jdroid.android.sample.androidx.SampleViewModel;
import com.jdroid.android.sample.database.room.SampleEntity;
import com.jdroid.java.exception.AbstractException;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;


public class ErrorDisplayerFragment extends AbstractFragment {

	private SampleViewModel sampleViewModel;
	private Observer<Resource<SampleEntity>> observer;

	private ErrorDisplayer errorDisplayer;
	private Boolean goBackOnError = true;

	@Override
	public Integer getContentFragmentLayout() {
		return R.layout.exception_handling_fragment;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		observer = new ResourceObserver<SampleEntity>() {

			@Override
			protected void onDataChanged(SampleEntity data) {
			}

			@Override
			protected void onStarting() {

			}

			@Override
			protected void onStartLoading(@Nullable SampleEntity data) {

			}

			@Override
			protected void onError(AbstractException exception, @Nullable SampleEntity data) {
				getFragment().createErrorDisplayer(exception).displayError(getFragment().getActivity(), exception);
			}

			@Override
			protected AbstractFragment getFragment() {
				return ErrorDisplayerFragment.this;
			}
		};
		sampleViewModel = ViewModelProviders.of(this).get(SampleViewModel.class);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		findView(R.id.defaultErrorDisplayer).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ErrorDisplayerFragment.this.errorDisplayer = null;
				ErrorDisplayerFragment.this.goBackOnError = true;
				execute();
			}
		});

		findView(R.id.defaultErrorDisplayerNotGoBack).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ErrorDisplayerFragment.this.errorDisplayer = null;
				ErrorDisplayerFragment.this.goBackOnError = false;
				execute();
			}
		});


		findView(R.id.snackbarErrorDisplayer).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				SnackbarErrorDisplayer errorDisplayer = new SnackbarErrorDisplayer();
				errorDisplayer.setParentLayoutId(R.id.container);
				errorDisplayer.setActionTextResId(R.string.jdroid_retry);
				errorDisplayer.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						execute();
					}
				});
				ErrorDisplayerFragment.this.errorDisplayer = errorDisplayer;
				execute();
			}
		});
	}

	private void execute() {
		if (sampleViewModel.getSampleEntity() != null) {
			sampleViewModel.getSampleEntity().removeObserver(observer);
		}
		sampleViewModel.setFailLoadFromNetwork(true);
		sampleViewModel.load(SampleRepository.ID, true).observe(this, observer);
	}

	@Override
	public ErrorDisplayer createErrorDisplayer(AbstractException abstractException) {
		AbstractErrorDisplayer.setErrorDisplayer(abstractException, errorDisplayer);
		if (errorDisplayer == null && !goBackOnError) {
			DialogErrorDisplayer.Companion.markAsNotGoBackOnError(abstractException);
		}
		return super.createErrorDisplayer(abstractException);
	}
}
