package com.jdroid.android.sample.ui.home;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jdroid.android.recycler.AbstractRecyclerFragment;
import com.jdroid.android.recycler.RecyclerViewAdapter;
import com.jdroid.android.recycler.RecyclerViewContainer;
import com.jdroid.android.recycler.RecyclerViewType;
import com.jdroid.android.sample.R;
import com.jdroid.java.collections.Lists;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class HomeFragment extends AbstractRecyclerFragment {

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		setAdapter(new RecyclerViewAdapter(new HomeRecyclerViewType(), Lists.newArrayList(HomeItem.values())));
	}

	@Override
	protected Boolean isCardViewDecorationEnabled() {
		return true;
	}

	@Override
	protected boolean isDividerItemDecorationEnabled() {
		return true;
	}

	public class HomeRecyclerViewType extends RecyclerViewType<HomeItem, HomeHolder> {

		@Override
		protected Class getItemClass() {
			return HomeItem.class;
		}

		@Override
		protected Integer getLayoutResourceId() {
			return R.layout.jdroid_default_item;
		}

		@Override
		public RecyclerView.ViewHolder createViewHolderFromView(View view) {
			HomeHolder holder = new HomeHolder(view);
			holder.image = findView(view, com.jdroid.android.R.id.image);
			holder.name = findView(view, com.jdroid.android.R.id.name);
			return holder;
		}

		@Override
		public void fillHolderFromItem(HomeItem item, HomeHolder holder) {
			holder.image.setImageResource(item.getIconResource());
			holder.name.setText(item.getNameResource());
		}

		@Override
		public void onItemSelected(@NonNull HomeItem item, View view) {
			item.startActivity(HomeFragment.this.getActivity());
		}

		@NonNull
		@Override
		public RecyclerViewContainer getRecyclerViewContainer() {
			return HomeFragment.this;
		}
	}

	public static class HomeHolder extends RecyclerView.ViewHolder {

		protected ImageView image;
		protected TextView name;

		public HomeHolder(View itemView) {
			super(itemView);
		}
	}

}
