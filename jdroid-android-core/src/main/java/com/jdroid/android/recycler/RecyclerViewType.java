package com.jdroid.android.recycler;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jdroid.java.utils.LoggerUtils;

import org.slf4j.Logger;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public abstract class RecyclerViewType<ITEM, VIEWHOLDER extends RecyclerView.ViewHolder> implements View.OnClickListener {

	private final static Logger LOGGER = LoggerUtils.getLogger(RecyclerViewType.class);

	private RecyclerViewTypeListener recyclerViewTypeListener;

	public View inflateView(LayoutInflater inflater, ViewGroup parent) {
		return inflater.inflate(getLayoutResourceId(), parent, false);
	}

	@NonNull
	protected abstract Integer getLayoutResourceId();

	/**
	 * Creates a VIEWHOLDER from the given view. Please declare the VIEWHOLDER class as static when possible
	 *
	 * @param view The view from the list.
	 * @return The new VIEWHOLDER.
	 */
	public abstract RecyclerView.ViewHolder createViewHolderFromView(View view);

	/**
	 * Fills the VIEWHOLDER with the Object's data.
	 *
	 * @param item The Object.
	 * @param holder The VIEWHOLDER.
	 */
	public abstract void fillHolderFromItem(ITEM item, VIEWHOLDER holder);

	@NonNull
	public abstract RecyclerViewContainer getRecyclerViewContainer();

	protected boolean isClickable() {
		return true;
	}

	@Override
	@SuppressWarnings("unchecked")
	public void onClick(View view) {
		int itemPosition = getRecyclerViewContainer().getRecyclerView().getChildAdapterPosition(view);
		if (itemPosition != RecyclerView.NO_POSITION) {
			if (recyclerViewTypeListener != null) {
				recyclerViewTypeListener.onItemSelected(itemPosition);
			}
			onItemSelected((ITEM)getRecyclerViewContainer().getRecyclerViewAdapter().getItem(itemPosition), view);
		} else {
			LOGGER.warn("Ignored onClick for item with no position");
		}
	}

	public void onItemSelected(ITEM item, View view) {
		// Do Nothing
	}

	/**
	 * Finds a view that was identified by the id attribute from the {@link View} view.
	 *
	 * @param containerView The view that contains the view to find.
	 * @param id The id to search for.
	 * @param <V> The {@link View} class.
	 * @return The view if found or null otherwise.
	 */
	@SuppressWarnings("unchecked")
	public <V extends View> V findView(View containerView, int id) {
		return (V)containerView.findViewById(id);
	}

	protected Activity getActivity() {
		return getRecyclerViewContainer().getActivity();
	}

	public Boolean matchViewType(Object item) {
		Class itemClass = item.getClass();
		while (itemClass != null) {
			if (getItemClass().equals(itemClass)) {
				return true;
			}
			itemClass = itemClass.getSuperclass();
		}
		return false;
	}

	protected abstract Class<ITEM> getItemClass();

	void setRecyclerViewTypeListener(RecyclerViewType.RecyclerViewTypeListener recyclerViewTypeListener) {
		this.recyclerViewTypeListener = recyclerViewTypeListener;
	}

	public boolean isSelectable() {
		return false;
	}

	interface RecyclerViewTypeListener {
		void onItemSelected(int position);
	}

}
