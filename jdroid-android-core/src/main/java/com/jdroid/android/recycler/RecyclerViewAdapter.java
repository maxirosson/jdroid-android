package com.jdroid.android.recycler;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jdroid.java.collections.Lists;
import com.jdroid.java.collections.Maps;
import com.jdroid.java.utils.LoggerUtils;

import org.slf4j.Logger;

import java.util.List;
import java.util.Map;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

	private static final Logger LOGGER = LoggerUtils.getLogger(RecyclerViewAdapter.class);

	private Map<Integer, RecyclerViewType> recyclerViewTypeMap = Maps.newHashMap();

	private HeaderRecyclerViewType.HeaderItem headerItem;
	private List<Object> items;
	private FooterRecyclerViewType.FooterItem footerItem;

	private int selectedItemPosition = RecyclerView.NO_POSITION;
	private RecyclerViewType.RecyclerViewTypeListener recyclerViewTypeListener = new RecyclerViewType.RecyclerViewTypeListener() {
		@Override
		public void onItemSelected(int position) {
			setSelectedItemPosition(position);
		}
	};

	public RecyclerViewAdapter(RecyclerViewType recyclerViewType) {
		this(Lists.newArrayList(recyclerViewType), Lists.newArrayList());
	}

	public RecyclerViewAdapter(RecyclerViewType recyclerViewType, List<? extends Object> items) {
		this(Lists.newArrayList(recyclerViewType), items);
	}

	public RecyclerViewAdapter(List<RecyclerViewType> recyclerViewTypes) {
		this(recyclerViewTypes, Lists.newArrayList());
	}

	public RecyclerViewAdapter(List<RecyclerViewType> recyclerViewTypes, List<? extends Object> items) {
		this.items = (List<Object>)items;
		for (RecyclerViewType each : recyclerViewTypes) {
			addRecyclerViewType(each);
		}
	}

	public void setHeader(final @LayoutRes int headerResId) {
		setHeader(new HeaderRecyclerViewType() {
			@Override
			protected Integer getLayoutResourceId() {
				return headerResId;
			}

			@Override
			@Nullable
			@SuppressWarnings("NullableProblems")
			public AbstractRecyclerFragment getRecyclerViewContainer() {
				return null;
			}
		});
	}

	public void setHeader(HeaderRecyclerViewType headerRecyclerViewType) {
		Boolean add = headerItem == null;

		for (Map.Entry<Integer, RecyclerViewType> entry : recyclerViewTypeMap.entrySet()) {
			if (entry.getValue() instanceof HeaderRecyclerViewType) {
				recyclerViewTypeMap.put(entry.getKey(), null);
				break;
			}
		}
		addRecyclerViewType(headerRecyclerViewType);

		headerItem = new HeaderRecyclerViewType.HeaderItem();

		if (add) {
			if (selectedItemPosition != RecyclerView.NO_POSITION) {
				selectedItemPosition++;
			}
			notifyItemInserted(0);
		} else {
			notifyItemChanged(0);
		}

	}

	public void removeHeader() {
		for (Map.Entry<Integer, RecyclerViewType> entry : recyclerViewTypeMap.entrySet()) {
			if (entry.getValue() instanceof HeaderRecyclerViewType) {
				recyclerViewTypeMap.put(entry.getKey(), null);
				break;
			}
		}
		if (headerItem != null) {
			headerItem = null;
			if (selectedItemPosition != RecyclerView.NO_POSITION) {
				selectedItemPosition--;
			}
			notifyItemRemoved(0);
		}
	}

	public void setFooter(final @LayoutRes int footerResId) {
		setFooter(new FooterRecyclerViewType() {
			@Override
			protected Integer getLayoutResourceId() {
				return footerResId;
			}

			@NonNull
			@Override
			@SuppressWarnings("NullableProblems")
			public RecyclerViewContainer getRecyclerViewContainer() {
				return null;
			}
		});
	}

	public void setFooter(FooterRecyclerViewType footerRecyclerViewType) {
		Boolean add = footerItem == null;

		for (Map.Entry<Integer, RecyclerViewType> entry : recyclerViewTypeMap.entrySet()) {
			if (entry.getValue() instanceof FooterRecyclerViewType) {
				recyclerViewTypeMap.put(entry.getKey(), null);
				break;
			}
		}
		addRecyclerViewType(footerRecyclerViewType);

		footerItem = new FooterRecyclerViewType.FooterItem();
		if (add) {
			notifyItemInserted(getItemCount() - 1);
		} else {
			notifyItemChanged(getItemCount() - 1);
		}
	}

	public void removeFooter() {
		for (Map.Entry<Integer, RecyclerViewType> entry : recyclerViewTypeMap.entrySet()) {
			if (entry.getValue() instanceof FooterRecyclerViewType) {
				recyclerViewTypeMap.put(entry.getKey(), null);
				break;
			}
		}
		if (footerItem != null) {
			if (selectedItemPosition == getItemCount() - 1) {
				selectedItemPosition = RecyclerView.NO_POSITION;
			}
			footerItem = null;
			notifyItemRemoved(getItemCount() - 1);
		}
	}

	public int addRecyclerViewType(RecyclerViewType recyclerViewType) {
		recyclerViewType.setRecyclerViewTypeListener(recyclerViewTypeListener);
		int viewType = recyclerViewTypeMap.size() + 1;
		recyclerViewTypeMap.put(viewType, recyclerViewType);
		return viewType;
	}

	@Override
	public int getItemViewType(int position) {
		Object item = getItem(position);
		for (Map.Entry<Integer, RecyclerViewType> entry : recyclerViewTypeMap.entrySet()) {
			if (entry.getValue() != null && entry.getValue().matchViewType(item)) {
				return entry.getKey();
			}
		}
		LOGGER.warn("ViewType not found for item " + item);
		return -1;
	}

	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		LayoutInflater inflater = LayoutInflater.from(parent.getContext());
		RecyclerViewType recyclerViewType = recyclerViewTypeMap.get(viewType);
		View view = recyclerViewType.inflateView(inflater, parent);
		return recyclerViewType.createViewHolderFromView(view);
	}

	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
		Integer itemViewType = holder.getItemViewType();
		Object item = getItem(position);
		RecyclerViewType recyclerViewType = recyclerViewTypeMap.get(itemViewType);

		if (recyclerViewType.isClickable()) {
			holder.itemView.setOnClickListener(recyclerViewType);
		} else {
			holder.itemView.setOnClickListener(null);
		}

		if (recyclerViewType.isSelectable()) {
			holder.itemView.setSelected(holder.getLayoutPosition() == selectedItemPosition);
		}

		recyclerViewType.fillHolderFromItem(item, holder);
	}

	@Override
	public void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
		super.onViewDetachedFromWindow(holder);

		// The view could be reused while an animation is been happening.
		// In order to avoid that is recommendable to clear the animation when is detached.
		holder.itemView.clearAnimation();
	}

	@Override
	public int getItemCount() {
		return items.size() + (headerItem != null ? 1 : 0) + (footerItem != null ? 1 : 0);
	}

	public <T> void addItem(T item) {
		items.add(item);
		notifyItemInserted(getItemCount() - 1);
	}

	public <T> void addItems(List<T> newItems) {
		items.addAll(newItems);
		notifyItemRangeInserted(getItemCount() - newItems.size(), newItems.size());
	}

	public <T> void removeItem(T item) {
		if (item.equals(headerItem)) {
			removeHeader();
		} else if (item.equals(footerItem)) {
			removeFooter();
		} else {
			int position = getPosition(item);
			items.remove(item);
			if (selectedItemPosition == position) {
				selectedItemPosition = RecyclerView.NO_POSITION;
			} else if (selectedItemPosition > position) {
				selectedItemPosition--;
			}
			notifyItemRemoved(position);
		}
	}

	public void removeItemByPosition(int position) {
		Object item = getItem(position);
		if (item.equals(headerItem)) {
			removeHeader();
		} else if (item.equals(footerItem)) {
			removeFooter();
		} else {
			int pos = items.indexOf(item);
			pos = headerItem != null ? pos + 1 : pos;
			items.remove(item);
			if (selectedItemPosition == position) {
				selectedItemPosition = RecyclerView.NO_POSITION;
			} else if (selectedItemPosition > position) {
				selectedItemPosition--;
			}
			notifyItemRemoved(pos);
		}
	}

	public void clear() {
		int size = getItemCount();
		selectedItemPosition = RecyclerView.NO_POSITION;
		headerItem = null;
		footerItem = null;
		items.clear();
		notifyItemRangeRemoved(0, size);
	}

	public List<Object> getItems() {
		return items;
	}

	public Object getItem(Integer position) {
		Object item;
		if (headerItem != null && position == 0) {
			item = headerItem;
		} else if (footerItem != null && position == getItemCount() - 1) {
			item = footerItem;
		} else {
			if (headerItem != null && position != 0) {
				position = position - 1;
			}
			item = items.get(position);
		}
		return item;
	}

	public <T> int getPosition(T item) {
		if (item.equals(headerItem)) {
			return 0;
		} else if (item.equals(footerItem)) {
			return getItemCount() - 1;
		} else {
			int pos = items.indexOf(item);
			return headerItem != null ? pos + 1 : pos;
		}
	}

	public <T> void setSelectedItem(T item) {
		setSelectedItemPosition(getPosition(item));
	}

	public void setSelectedItemPosition(int position) {
		if (position != RecyclerView.NO_POSITION) {
			if (position < getItemCount()) {
				int itemViewType = getItemViewType(position);
				RecyclerViewType recyclerViewType = recyclerViewTypeMap.get(itemViewType);
				if (recyclerViewType.isSelectable()) {
					int previousSelectedItemPosition = selectedItemPosition;
					selectedItemPosition = position;
					notifyItemChanged(previousSelectedItemPosition);
					notifyItemChanged(selectedItemPosition);
				}
			}
		} else {
			int oldPosition = selectedItemPosition;
			selectedItemPosition = position;
			notifyItemChanged(oldPosition);
		}
	}

	public int getSelectedItemPosition() {
		return selectedItemPosition;
	}
}
