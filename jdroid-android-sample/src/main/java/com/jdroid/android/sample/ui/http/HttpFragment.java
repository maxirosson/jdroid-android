package com.jdroid.android.sample.ui.http;

import android.os.Bundle;
import android.view.View;

import com.jdroid.android.exception.DialogErrorDisplayer;
import com.jdroid.android.exception.ErrorDisplayer;
import com.jdroid.android.fragment.AbstractFragment;
import com.jdroid.android.sample.R;
import com.jdroid.android.sample.api.SampleApiService;
import com.jdroid.android.sample.api.SampleResponse;
import com.jdroid.java.concurrent.ExecutorUtils;
import com.jdroid.java.exception.AbstractException;
import com.jdroid.java.utils.LoggerUtils;

import org.slf4j.Logger;

public class HttpFragment extends AbstractFragment {

	private static final Logger LOGGER = LoggerUtils.getLogger(HttpFragment.class);

	@Override
	public Integer getContentFragmentLayout() {
		return R.layout.http_fragment;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		findView(R.id.httpGet).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ExecutorUtils.execute(new Runnable() {
					@Override
					public void run() {
						SampleResponse sampleResponse = new SampleApiService().httpGetSample();
						LOGGER.debug("Sample response key: " + sampleResponse.getSampleKey());
					}
				});
			}
		});

		findView(R.id.httpPost).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ExecutorUtils.execute(new Runnable() {
					@Override
					public void run() {
						new SampleApiService().httpPostSample();
					}
				});
			}
		});

		findView(R.id.httpPut).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ExecutorUtils.execute(new Runnable() {
					@Override
					public void run() {
						new SampleApiService().httpPutSample();
					}
				});
			}
		});

		findView(R.id.httpDelete).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ExecutorUtils.execute(new Runnable() {
					@Override
					public void run() {
						new SampleApiService().httpDeleteSample();
					}
				});
			}
		});

		findView(R.id.httpPatch).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ExecutorUtils.execute(new Runnable() {
					@Override
					public void run() {
						new SampleApiService().httpPatchSample();
					}
				});
			}
		});

		findView(R.id.connectionExceptionParser).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ExecutorUtils.execute(new Runnable() {
					@Override
					public void run() {
						new SampleApiService().connectionExceptionParser();
					}
				});
			}
		});

		findView(R.id.unexpectedExceptionParser).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ExecutorUtils.execute(new Runnable() {
					@Override
					public void run() {
						new SampleApiService().unexpectedExceptionParser();
					}
				});
			}
		});

	}

	@Override
	public ErrorDisplayer createErrorDisplayer(AbstractException abstractException) {
		DialogErrorDisplayer.markAsNotGoBackOnError(abstractException);
		return super.createErrorDisplayer(abstractException);
	}
}
