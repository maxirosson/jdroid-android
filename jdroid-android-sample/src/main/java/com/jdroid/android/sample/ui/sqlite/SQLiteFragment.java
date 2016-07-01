package com.jdroid.android.sample.ui.sqlite;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.jdroid.android.fragment.AbstractFragment;
import com.jdroid.android.sample.R;
import com.jdroid.android.sample.application.AndroidApplication;
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

	/**
	 * @see AbstractFragment#onViewCreated(View, Bundle)
	 */
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		findView(R.id.sqliteAdd).setOnClickListener(v ->
				ExecutorUtils.execute(() -> {
					Repository<SampleSQLiteEntity> repository = AndroidApplication.get().getRepositoryInstance(SampleSQLiteEntity.class);
					SampleSQLiteEntity entity = new SampleSQLiteEntity();
					lastId = RandomUtils.getLong().toString();
					entity.setId(lastId);
					entity.setField(RandomUtils.getLong().toString());
					repository.add(entity);
				}
			));

		findView(R.id.sqliteUpdate).setOnClickListener(v ->
				ExecutorUtils.execute(() -> {
						if (lastId != null) {
							Repository<SampleSQLiteEntity> repository = AndroidApplication.get().getRepositoryInstance(SampleSQLiteEntity.class);
							SampleSQLiteEntity entity = repository.get(lastId);
							if (entity != null) {
								entity.setField(RandomUtils.getLong().toString());
								repository.update(entity);
							}
						}
					}
				)
			);

		findView(R.id.sqliteRemove).setOnClickListener(v ->
				ExecutorUtils.execute(() -> {
					if (lastId != null) {
						Repository<SampleSQLiteEntity> repository = AndroidApplication.get().getRepositoryInstance(SampleSQLiteEntity.class);
						repository.remove(lastId);
						lastId = null;
					}
				}
			));

		findView(R.id.sqliteRemoveAll).setOnClickListener(v ->
				ExecutorUtils.execute(() -> {
					Repository<SampleSQLiteEntity> repository = AndroidApplication.get().getRepositoryInstance(SampleSQLiteEntity.class);
					repository.removeAll();
				}
			));

		findView(R.id.sqliteGetAll).setOnClickListener(v ->
				ExecutorUtils.execute(() -> {
					final Repository<SampleSQLiteEntity> repository = AndroidApplication.get().getRepositoryInstance(SampleSQLiteEntity.class);
					final List<SampleSQLiteEntity> results = repository.getAll();
					executeOnUIThread(() -> ((TextView)findView(R.id.results)).setText(results.toString()));
				}
			));
	}
}
