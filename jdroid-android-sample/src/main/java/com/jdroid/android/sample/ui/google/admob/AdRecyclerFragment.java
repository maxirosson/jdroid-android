package com.jdroid.android.sample.ui.google.admob;

import android.os.Bundle;
import android.view.View;

import com.google.android.gms.ads.AdSize;
import com.jdroid.android.androidx.lifecycle.Resource;
import com.jdroid.android.androidx.lifecycle.ResourceObserver;
import com.jdroid.android.firebase.admob.AdViewType;
import com.jdroid.android.firebase.admob.helpers.AdViewHelper;
import com.jdroid.android.firebase.admob.helpers.BaseAdViewHelper;
import com.jdroid.android.fragment.AbstractFragment;
import com.jdroid.android.recycler.AbstractRecyclerFragment;
import com.jdroid.android.recycler.RecyclerViewAdapter;
import com.jdroid.android.recycler.RecyclerViewContainer;
import com.jdroid.android.recycler.RecyclerViewType;
import com.jdroid.android.sample.R;
import com.jdroid.android.sample.androidx.SampleListViewModel;
import com.jdroid.android.sample.application.AndroidAppContext;
import com.jdroid.android.sample.ui.recyclerview.SimpleRecyclerFragment;
import com.jdroid.java.collections.Lists;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

public class AdRecyclerFragment extends AbstractRecyclerFragment {

	private SampleListViewModel sampleListViewModel;
	private Observer<Resource<List<String>>> observer;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		observer = new ResourceObserver<List<String>>() {

			@Override
			protected void onDataChanged(List<String> data) {
				List<RecyclerViewType> recyclerViewTypes = Lists.newArrayList(new StringRecyclerViewType(), new MyAdViewType());

				List<Object> items = Lists.newArrayList();
				for (String each : data) {
					items.add(each);
					if (each.equals("three")) {
						BaseAdViewHelper baseAdViewHelper = new AdViewHelper();
						baseAdViewHelper.setAdSize(new AdSize(AdSize.FULL_WIDTH, 80));
						baseAdViewHelper.setAdUnitId(AndroidAppContext.SAMPLE_BANNER_AD_UNIT_ID);
						items.add(baseAdViewHelper);
					}
				}
				setAdapter(new RecyclerViewAdapter(recyclerViewTypes, items));			}

			@Override
			protected void onStarting() {
				showLoading();
			}

			@Override
			protected void onStartLoading(@Nullable List<String> data) {
				showLoading();
			}

			@Override
			protected AbstractFragment getFragment() {
				return AdRecyclerFragment.this;
			}
		};
		sampleListViewModel = ViewModelProviders.of(this).get(SampleListViewModel.class);
		sampleListViewModel.loadStrings().observe(this, observer);
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
