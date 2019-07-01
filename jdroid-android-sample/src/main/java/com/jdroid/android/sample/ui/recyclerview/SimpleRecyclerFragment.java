package com.jdroid.android.sample.ui.recyclerview;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.jdroid.android.androidx.lifecycle.Resource;
import com.jdroid.android.androidx.lifecycle.ResourceObserver;
import com.jdroid.android.fragment.AbstractFragment;
import com.jdroid.android.recycler.AbstractRecyclerFragment;
import com.jdroid.android.recycler.FooterRecyclerViewType;
import com.jdroid.android.recycler.HeaderRecyclerViewType;
import com.jdroid.android.recycler.RecyclerViewAdapter;
import com.jdroid.android.recycler.RecyclerViewContainer;
import com.jdroid.android.recycler.RecyclerViewType;
import com.jdroid.android.sample.R;
import com.jdroid.android.sample.androidx.SampleListViewModel;
import com.jdroid.java.collections.Lists;
import com.jdroid.java.utils.IdGenerator;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

public class SimpleRecyclerFragment extends AbstractRecyclerFragment {

	private SampleListViewModel sampleListViewModel;
	private Observer<Resource<List<String>>> observer;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		observer = new ResourceObserver<List<String>>() {

			@Override
			protected void onDataChanged(List<String> data) {
				setAdapter(new RecyclerViewAdapter(new StringRecyclerViewType(), data));
			}

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
				return SimpleRecyclerFragment.this;
			}
		};
		sampleListViewModel = ViewModelProviders.of(this).get(SampleListViewModel.class);
		sampleListViewModel.loadStrings().observe(this, observer);
	}

	@Override
	public Integer getMenuResourceId() {
		return R.menu.recycler_menu;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.addItem:
				getRecyclerViewAdapter().addItem(IdGenerator.INSTANCE.getIntId().toString());
				return true;
			case R.id.addItems:
				getRecyclerViewAdapter().addItems(Lists.newArrayList(IdGenerator.INSTANCE.getIntId().toString(), IdGenerator.INSTANCE.getIntId().toString(), IdGenerator.INSTANCE.getIntId().toString()));
				return true;
			case R.id.replaceWithSameItems:
				List<Object> items = Lists.newArrayList(getRecyclerViewAdapter().getItems());
				getRecyclerViewAdapter().replaceItems(items, new DiffUtil.Callback() {
					@Override
					public int getOldListSize() {
						return items.size();
					}

					@Override
					public int getNewListSize() {
						return items.size();
					}

					@Override
					public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
						return items.get(oldItemPosition).toString().equals(items.get(newItemPosition).toString());
					}

					@Override
					public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
						return items.get(oldItemPosition).equals(items.get(newItemPosition));
					}
				});
				return true;
			case R.id.replaceWithDifferentItems:
				List<Object> oldItems = Lists.newArrayList(getRecyclerViewAdapter().getItems());
				List<Object> newItems = Lists.newArrayList(IdGenerator.INSTANCE.getIntId().toString(), IdGenerator.INSTANCE.getIntId().toString(), IdGenerator.INSTANCE.getIntId().toString());
				getRecyclerViewAdapter().replaceItems(newItems, new DiffUtil.Callback() {
					@Override
					public int getOldListSize() {
						return oldItems.size();
					}

					@Override
					public int getNewListSize() {
						return newItems.size();
					}

					@Override
					public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
						return oldItems.get(oldItemPosition).toString().equals(newItems.get(newItemPosition).toString());
					}

					@Override
					public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
						return oldItems.get(oldItemPosition).equals(newItems.get(newItemPosition));
					}
				});
				return true;
			case R.id.clearItems:
				getRecyclerViewAdapter().clear();
				return true;
			case R.id.removeFirstItem:
				getRecyclerViewAdapter().removeItemByPosition(0);
				return true;
			case R.id.removeSecondItem:
				getRecyclerViewAdapter().removeItemByPosition(1);
				return true;
			case R.id.addHeader:
				getRecyclerViewAdapter().setHeader(R.layout.header_item);
				return true;
			case R.id.addClickableHeader:
				getRecyclerViewAdapter().setHeader(new SampleHeaderRecyclerViewType());
				return true;
			case R.id.removeHeader:
				getRecyclerViewAdapter().removeHeader();
				return true;
			case R.id.addFooter:
				getRecyclerViewAdapter().setFooter(R.layout.footer_item);
				return true;
			case R.id.addClickableFooter:
				getRecyclerViewAdapter().setFooter(new SampleFooterRecyclerViewType());
				return true;
			case R.id.removeFooter:
				getRecyclerViewAdapter().removeFooter();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected Boolean isDividerItemDecorationEnabled() {
		return true;
	}

	public class StringRecyclerViewType extends RecyclerViewType<String, StringViewHolder> {

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
			StringViewHolder viewHolder = new StringViewHolder(view);
			viewHolder.textView = findView(view, R.id.name);
			return viewHolder;
		}

		@Override
		public void fillHolderFromItem(String item, StringViewHolder holder) {
			holder.textView.setText(item);
		}

		@NonNull
		@Override
		public RecyclerViewContainer getRecyclerViewContainer() {
			return SimpleRecyclerFragment.this;
		}

		@Override
		public void onItemSelected(String item, View view) {
			getRecyclerViewAdapter().removeItem(item);
		}
	}

	public static class StringViewHolder extends RecyclerView.ViewHolder {

		public TextView textView;

		public StringViewHolder(View itemView) {
			super(itemView);
		}
	}

	public class SampleHeaderRecyclerViewType extends HeaderRecyclerViewType {

		@Override
		protected Integer getLayoutResourceId() {
			return R.layout.clickable_header_item;
		}

		@Override
		protected Boolean isClickable() {
			return true;
		}

		@Override
		public void onItemSelected(HeaderItem headerItem, View view) {
			getRecyclerViewAdapter().removeItem(headerItem);
		}

		@NonNull
		@Override
		public RecyclerViewContainer getRecyclerViewContainer() {
			return SimpleRecyclerFragment.this;
		}
	}

	public class SampleFooterRecyclerViewType extends FooterRecyclerViewType {

		@Override
		protected Integer getLayoutResourceId() {
			return R.layout.clickable_footer_item;
		}

		@Override
		protected Boolean isClickable() {
			return true;
		}

		@Override
		public void onItemSelected(FooterItem footerItem, View view) {
			getRecyclerViewAdapter().removeItem(footerItem);
		}

		@NonNull
		@Override
		public RecyclerViewContainer getRecyclerViewContainer() {
			return SimpleRecyclerFragment.this;
		}
	}
}
