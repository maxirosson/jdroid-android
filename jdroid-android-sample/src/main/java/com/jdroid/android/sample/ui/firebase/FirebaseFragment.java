package com.jdroid.android.sample.ui.firebase;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.jdroid.android.fragment.AbstractFragment;
import com.jdroid.android.sample.R;
import com.jdroid.java.concurrent.ExecutorUtils;
import com.jdroid.java.utils.LoggerUtils;
import com.jdroid.java.utils.RandomUtils;

import org.slf4j.Logger;

import java.util.List;

import static javafx.scene.input.KeyCode.R;

public class FirebaseFragment extends AbstractFragment {

	private static final Logger LOGGER = LoggerUtils.getLogger(FirebaseFragment.class);

	private SampleFirebaseRepository repository;

	private static String lastId;

	@Override
	public Integer getContentFragmentLayout() {
		return R.layout.firebase_fragment;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		repository = new SampleFirebaseRepository();

		findView(R.id.firebaseCreate).setOnClickListener(v ->
				ExecutorUtils.execute(() -> {
					SampleFirebaseEntity entity = new SampleFirebaseEntity();
					lastId = RandomUtils.getLong().toString();
					entity.setId(lastId);
					entity.setField(RandomUtils.getLong().toString());
					repository.add(entity);
				}
			));

		findView(R.id.firebaseUpdate).setOnClickListener(v ->
				ExecutorUtils.execute(() -> {
						SampleFirebaseEntity entity = new SampleFirebaseEntity();
						entity.setId(lastId);
						entity.setField(RandomUtils.getLong().toString());
						repository.update(entity);
					}
			));

		findView(R.id.firebaseGetAll).setOnClickListener(v ->
				ExecutorUtils.execute(() -> {
					final List<SampleFirebaseEntity> results = repository.getAll();
					executeOnUIThread(() -> ((TextView)findView(R.id.results)).setText(results.toString()));
				}
			));

		findView(R.id.firebaseRemove).setOnClickListener(v ->
				ExecutorUtils.execute(() -> {
					repository.remove(lastId);
					lastId = null;
				}
			));
	}
}
