package com.jdroid.android.sample.database.room;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.jdroid.android.concurrent.AppExecutors;
import com.jdroid.android.fragment.AbstractFragment;
import com.jdroid.android.room.RoomHelper;
import com.jdroid.android.sample.R;
import com.jdroid.java.collections.Lists;
import com.jdroid.java.date.DateUtils;
import com.jdroid.java.utils.RandomUtils;

import java.util.List;


public class RoomFragment extends AbstractFragment {

	private static String lastId;

	@Override
	public Integer getContentFragmentLayout() {
		return R.layout.room_fragment;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		findView(R.id.add).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				AppExecutors.getDiskIOExecutor().execute(new Runnable() {
					@Override
					public void run() {
						SampleEntityDao sampleEntityDao = RoomHelper.getDefaultDatabase(AppDatabase.class).sampleEntityDao();
						SampleEntity entity = new SampleEntity();
						lastId = RandomUtils.getLong().toString();
						entity.setId(lastId);
						entity.setField(RandomUtils.getLong().toString());
						entity.setDate(DateUtils.now());
						entity.setStringList(Lists.newArrayList("a", "b", "c"));
						sampleEntityDao.insert(entity);
					}
				});
			}
		});
		findView(R.id.update).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				AppExecutors.getDiskIOExecutor().execute(new Runnable() {
					@Override
					public void run() {
						if (lastId != null) {
							SampleEntityDao sampleEntityDao = RoomHelper.getDefaultDatabase(AppDatabase.class).sampleEntityDao();
							SampleEntity entity = sampleEntityDao.get2(lastId);
							if (entity != null) {
								entity.setField(RandomUtils.getLong().toString());
								sampleEntityDao.update(entity);
							}
						}
					}
				});
			}
		});
		findView(R.id.remove).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				AppExecutors.getDiskIOExecutor().execute(new Runnable() {
					@Override
					public void run() {
						if (lastId != null) {
							SampleEntityDao sampleEntityDao = RoomHelper.getDefaultDatabase(AppDatabase.class).sampleEntityDao();
							sampleEntityDao.delete(lastId);
							lastId = null;
						}
					}
				});
			}
		});
		findView(R.id.removeAll).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				AppExecutors.getDiskIOExecutor().execute(new Runnable() {
					@Override
					public void run() {
						SampleEntityDao sampleEntityDao = RoomHelper.getDefaultDatabase(AppDatabase.class).sampleEntityDao();
						sampleEntityDao.deleteAll();
					}
				});
			}
		});
		findView(R.id.insertAll).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				AppExecutors.getDiskIOExecutor().execute(new Runnable() {
					@Override
					public void run() {
						SampleEntityDao sampleEntityDao = RoomHelper.getDefaultDatabase(AppDatabase.class).sampleEntityDao();

						List<SampleEntity> entities = Lists.newArrayList();

						SampleEntity entity = new SampleEntity();
						lastId = RandomUtils.getLong().toString();
						entity.setId(lastId);
						entity.setField(RandomUtils.getLong().toString());
						entities.add(entity);

						entity = new SampleEntity();
						lastId = RandomUtils.getLong().toString();
						entity.setId(lastId);
						entity.setField(RandomUtils.getLong().toString());
						entities.add(entity);

						sampleEntityDao.insertAll(entities);
					}
				});
			}
		});

		findView(R.id.getAll).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				AppExecutors.getDiskIOExecutor().execute(new Runnable() {
					@Override
					public void run() {
						SampleEntityDao sampleEntityDao = RoomHelper.getDefaultDatabase(AppDatabase.class).sampleEntityDao();
						final List<SampleEntity> results = sampleEntityDao.getAll();
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
