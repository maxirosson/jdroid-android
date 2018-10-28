package com.jdroid.android.sample.ui.recyclerview;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.jdroid.android.recycler.AbstractRecyclerFragment;
import com.jdroid.android.recycler.FooterRecyclerViewType;
import com.jdroid.android.recycler.HeaderRecyclerViewType;
import com.jdroid.android.recycler.RecyclerViewAdapter;
import com.jdroid.android.recycler.RecyclerViewContainer;
import com.jdroid.android.recycler.RecyclerViewType;
import com.jdroid.android.sample.R;
import com.jdroid.android.sample.usecase.SampleItemsUseCase;
import com.jdroid.android.usecase.UseCaseHelper;
import com.jdroid.android.usecase.UseCaseTrigger;
import com.jdroid.java.collections.Lists;
import com.jdroid.java.utils.IdGenerator;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ComplexRecyclerFragment extends AbstractRecyclerFragment {

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
		List<RecyclerViewType> recyclerViewTypes = Lists.<RecyclerViewType>newArrayList(new StringRecyclerViewType(), new IntegerRecyclerViewType(), new BooleanRecyclerViewType());
		setAdapter(new RecyclerViewAdapter(recyclerViewTypes, sampleItemsUseCase.getComplexItems()));
		dismissLoading();
	}

	@Override
	public Integer getMenuResourceId() {
		return R.menu.recycler_menu;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.addItem:
				getRecyclerViewAdapter().addItem(IdGenerator.getIntId().toString());
				return true;
			case R.id.addItems:
				getRecyclerViewAdapter().addItems(Lists.newArrayList(IdGenerator.getIntId().toString(), IdGenerator.getIntId().toString(), IdGenerator.getIntId().toString()));
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
			return ComplexRecyclerFragment.this;
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

	public class IntegerRecyclerViewType extends RecyclerViewType<Integer, IntegerViewHolder> {

		@Override
		protected Integer getLayoutResourceId() {
			return R.layout.item;
		}

		@Override
		protected Class<Integer> getItemClass() {
			return Integer.class;
		}

		@Override
		public RecyclerView.ViewHolder createViewHolderFromView(View view) {
			IntegerViewHolder viewHolder = new IntegerViewHolder(view);
			viewHolder.textView = findView(view, R.id.name);
			return viewHolder;
		}

		@SuppressLint("SetTextI18n")
		@Override
		public void fillHolderFromItem(Integer item, IntegerViewHolder holder) {
			holder.textView.setText(item.toString());
		}

		@Override
		public boolean isSelectable() {
			return true;
		}

		@NonNull
		@Override
		public RecyclerViewContainer getRecyclerViewContainer() {
			return ComplexRecyclerFragment.this;
		}
	}

	public static class IntegerViewHolder extends RecyclerView.ViewHolder {

		public TextView textView;

		public IntegerViewHolder(View itemView) {
			super(itemView);
		}
	}

	public class BooleanRecyclerViewType extends RecyclerViewType<Boolean, BooleanViewHolder> {

		@Override
		protected Integer getLayoutResourceId() {
			return R.layout.item;
		}

		@Override
		protected Class<Boolean> getItemClass() {
			return Boolean.class;
		}

		@Override
		public RecyclerView.ViewHolder createViewHolderFromView(View view) {
			BooleanViewHolder viewHolder = new BooleanViewHolder(view);
			viewHolder.textView = findView(view, R.id.name);
			return viewHolder;
		}

		@Override
		public void fillHolderFromItem(Boolean item, BooleanViewHolder holder) {
			holder.textView.setText(item.toString());
		}

		@Override
		protected Boolean isClickable() {
			return false;
		}

		@NonNull
		@Override
		public RecyclerViewContainer getRecyclerViewContainer() {
			return ComplexRecyclerFragment.this;
		}
	}

	public static class BooleanViewHolder extends RecyclerView.ViewHolder {

		public TextView textView;

		public BooleanViewHolder(View itemView) {
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
			return ComplexRecyclerFragment.this;
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
			return ComplexRecyclerFragment.this;
		}
	}
}
