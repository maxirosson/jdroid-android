package com.jdroid.android.sample.ui.google.admob;

import android.os.Bundle;
import android.view.View;

import com.google.android.gms.ads.AdSize;
import com.jdroid.android.firebase.admob.AdViewType;
import com.jdroid.android.firebase.admob.helpers.AdViewHelper;
import com.jdroid.android.firebase.admob.helpers.BaseAdViewHelper;
import com.jdroid.android.recycler.AbstractRecyclerFragment;
import com.jdroid.android.recycler.RecyclerViewAdapter;
import com.jdroid.android.recycler.RecyclerViewContainer;
import com.jdroid.android.recycler.RecyclerViewType;
import com.jdroid.android.sample.R;
import com.jdroid.android.sample.application.AndroidAppContext;
import com.jdroid.android.sample.ui.recyclerview.SimpleRecyclerFragment;
import com.jdroid.android.sample.usecase.SampleItemsUseCase;
import com.jdroid.android.usecase.UseCaseHelper;
import com.jdroid.android.usecase.UseCaseTrigger;
import com.jdroid.java.collections.Lists;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdRecyclerFragment extends AbstractRecyclerFragment {

	private SampleItemsUseCase sampleItemsUseCase;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		sampleItemsUseCase = new SampleItemsUseCase();
	}

	@Override
	public void onStart() {
		super.onStart();
		UseCaseHelper.registerUseCase(sampleItemsUseCase, this, UseCaseTrigger.ONCE);
	}

	@Override
	public void onStop() {
		super.onStop();
		UseCaseHelper.unregisterUseCase(sampleItemsUseCase, this);
	}

	@Override
	public void onFinishUseCase() {
		List<RecyclerViewType> recyclerViewTypes = Lists.<RecyclerViewType>newArrayList(new StringRecyclerViewType(), new MyAdViewType());

		List<Object> items = Lists.newArrayList();
		for (String each : sampleItemsUseCase.getItems()) {
			items.add(each);
			if (each.equals("three")) {
				BaseAdViewHelper baseAdViewHelper = new AdViewHelper();
				baseAdViewHelper.setAdSize(new AdSize(AdSize.FULL_WIDTH, 80));
				baseAdViewHelper.setAdUnitId(AndroidAppContext.SAMPLE_BANNER_AD_UNIT_ID);
				items.add(baseAdViewHelper);
			}
		}
		setAdapter(new RecyclerViewAdapter(recyclerViewTypes, items));
		dismissLoading();
	}

	public class StringRecyclerViewType extends RecyclerViewType<String, SimpleRecyclerFragment.StringViewHolder> {

		@Override
		protected Integer getLayoutResourceId() {
			return R.layout.item;
		}

		@Override
		protected Class<String> getItemClass() {
			return String.class;
		}

		@Override
		public RecyclerView.ViewHolder createViewHolderFromView(View view) {
			SimpleRecyclerFragment.StringViewHolder viewHolder = new SimpleRecyclerFragment.StringViewHolder(view);
			viewHolder.textView = findView(view, R.id.name);
			return viewHolder;
		}

		@Override
		public void fillHolderFromItem(String item, SimpleRecyclerFragment.StringViewHolder holder) {
			holder.textView.setText(item);
		}

		@NonNull
		@Override
		public RecyclerViewContainer getRecyclerViewContainer() {
			return AdRecyclerFragment.this;
		}

		@Override
		public void onItemSelected(String item, View view) {
			getRecyclerViewAdapter().removeItem(item);
		}
	}

	public class MyAdViewType extends AdViewType {

		@Override
		protected Integer getLayoutResourceId() {
			return R.layout.ad;
		}

		@NonNull
		@Override
		public RecyclerViewContainer getRecyclerViewContainer() {
			return AdRecyclerFragment.this;
		}
	}
}
