package com.jdroid.android.sample.ui.firebase.database;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.jdroid.android.concurrent.AppExecutors;
import com.jdroid.android.fragment.AbstractFragment;
import com.jdroid.android.sample.R;
import com.jdroid.java.utils.LoggerUtils;
import com.jdroid.java.utils.RandomUtils;

import org.slf4j.Logger;

import java.util.List;

public class FirebaseDatabaseFragment extends AbstractFragment {

	private static final Logger LOGGER = LoggerUtils.getLogger(FirebaseDatabaseFragment.class);

	private SampleFirebaseRepository repository;

	private static String lastId;

	@Override
	public Integer getContentFragmentLayout() {
		return R.layout.firebase_database_fragment;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		repository = new SampleFirebaseRepository();

		findView(R.id.firebaseCreate).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				AppExecutors.getNetworkIOExecutor().execute(new Runnable() {
					@Override
					public void run() {
						SampleFirebaseEntity entity = new SampleFirebaseEntity();
						lastId = RandomUtils.getLong().toString();
						entity.setId(lastId);
						entity.setField(RandomUtils.getLong().toString());
						repository.add(entity);
					}
				});
			}
		});

		findView(R.id.firebaseUpdate).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				AppExecutors.getNetworkIOExecutor().execute(new Runnable() {
					@Override
					public void run() {
						SampleFirebaseEntity entity = new SampleFirebaseEntity();
						entity.setId(lastId);
						entity.setField(RandomUtils.getLong().toString());
						repository.update(entity);
					}
				});
			}
		});

		findView(R.id.firebaseGetAll).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				AppExecutors.getNetworkIOExecutor().execute(new Runnable() {
					@Override
					public void run() {
						final List<SampleFirebaseEntity> results = repository.getAll();
						executeOnUIThread(new Runnable() {
							@Override
							public void run() {
								((TextView)findView(R.id.results)).setText(results.toString());
							}
						});
					}
				});
			}
		});

		findView(R.id.firebaseRemove).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				AppExecutors.getNetworkIOExecutor().execute(new Runnable() {
					@Override
					public void run() {
						repository.remove(lastId);
						lastId = null;
					}
				});
			}
		});
	}
}
