package com.jdroid.android.sample.ui.sqlite;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.jdroid.android.fragment.AbstractFragment;
import com.jdroid.android.sample.R;
import com.jdroid.android.sample.application.AndroidApplication;
import com.jdroid.java.collections.Lists;
import com.jdroid.java.concurrent.ExecutorUtils;
import com.jdroid.java.repository.Repository;
import com.jdroid.java.utils.RandomUtils;

import java.util.List;

public class SQLiteFragment extends AbstractFragment {

	private static String lastId;

	@Override
	public Integer getContentFragmentLayout() {
		return R.layout.sqlite_fragment;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		findView(R.id.sqliteAdd).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ExecutorUtils.execute(new Runnable() {
					@Override
					public void run() {
						Repository<SampleSQLiteEntity> repository = AndroidApplication.get().getRepositoryInstance(SampleSQLiteEntity.class);
						SampleSQLiteEntity entity = new SampleSQLiteEntity();
						lastId = RandomUtils.getLong().toString();
						entity.setId(lastId);
						entity.setField(RandomUtils.getLong().toString());
						repository.add(entity);
					}
				});
			}
		});
		findView(R.id.sqliteUpdate).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ExecutorUtils.execute(new Runnable() {
					@Override
					public void run() {
						if (lastId != null) {
							Repository<SampleSQLiteEntity> repository = AndroidApplication.get().getRepositoryInstance(SampleSQLiteEntity.class);
							SampleSQLiteEntity entity = repository.get(lastId);
							if (entity != null) {
								entity.setField(RandomUtils.getLong().toString());
								repository.update(entity);
							}
						}
					}
				});
			}
		});
		findView(R.id.sqliteRemove).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ExecutorUtils.execute(new Runnable() {
					@Override
					public void run() {
						if (lastId != null) {
							Repository<SampleSQLiteEntity> repository = AndroidApplication.get().getRepositoryInstance(SampleSQLiteEntity.class);
							repository.remove(lastId);
							lastId = null;
						}
					}
				});
			}
		});
		findView(R.id.sqliteRemoveAll).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ExecutorUtils.execute(new Runnable() {
					@Override
					public void run() {
						Repository<SampleSQLiteEntity> repository = AndroidApplication.get().getRepositoryInstance(SampleSQLiteEntity.class);
						repository.removeAll();
					}
				});
			}
		});
		findView(R.id.sqliteReplaceAll).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ExecutorUtils.execute(new Runnable() {
					@Override
					public void run() {
						Repository<SampleSQLiteEntity> repository = AndroidApplication.get().getRepositoryInstance(SampleSQLiteEntity.class);

						List<SampleSQLiteEntity> entities = Lists.newArrayList();

						SampleSQLiteEntity entity = new SampleSQLiteEntity();
						lastId = RandomUtils.getLong().toString();
						entity.setId(lastId);
						entity.setField(RandomUtils.getLong().toString());
						entities.add(entity);

						entity = new SampleSQLiteEntity();
						lastId = RandomUtils.getLong().toString();
						entity.setId(lastId);
						entity.setField(RandomUtils.getLong().toString());
						entities.add(entity);

						repository.replaceAll(entities);
					}
				});
			}
		});

		findView(R.id.sqliteGetAll).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ExecutorUtils.execute(new Runnable() {
					@Override
					public void run() {
						final Repository<SampleSQLiteEntity> repository = AndroidApplication.get().getRepositoryInstance(SampleSQLiteEntity.class);
						final List<SampleSQLiteEntity> results = repository.getAll();
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
	}
}
