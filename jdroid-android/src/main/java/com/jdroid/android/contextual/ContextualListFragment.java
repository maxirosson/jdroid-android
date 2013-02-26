package com.jdroid.android.contextual;

import java.util.List;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import com.google.ads.AdSize;
import com.jdroid.android.R;
import com.jdroid.android.fragment.AbstractListFragment;
import com.jdroid.android.fragment.OnItemSelectedListener;
import com.jdroid.android.tabs.TabAction;
import com.jdroid.android.utils.AndroidUtils;
import com.jdroid.java.collections.Lists;

/**
 * 
 * @author Maxi Rosson
 */
public class ContextualListFragment extends AbstractListFragment<TabAction> {
	
	private static final String ACTIONS_EXTRA = "actions";
	private static final String DEFAULT_INDEX_EXTRA = "defaultIndex";
	
	private List<TabAction> actions;
	private int defaultIndex;
	
	public ContextualListFragment() {
	}
	
	public ContextualListFragment(List<? extends TabAction> actions, TabAction defaultContextualItem) {
		Bundle bundle = new Bundle();
		bundle.putSerializable(ACTIONS_EXTRA, Lists.newArrayList(actions));
		bundle.putInt(DEFAULT_INDEX_EXTRA, actions.indexOf(defaultContextualItem));
		setArguments(bundle);
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractListFragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
		
		actions = getArgument(ACTIONS_EXTRA);
		defaultIndex = getArgument(DEFAULT_INDEX_EXTRA);
	}
	
	/**
	 * @see android.app.ListFragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup,
	 *      android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.contextual_list_fragment, container, false);
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractListFragment#onViewCreated(android.view.View, android.os.Bundle)
	 */
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		setListAdapter(new ContextualItemsAdapter(this.getActivity(), actions));
		
		if (AndroidUtils.isLargeScreenOrBigger()) {
			getListView().setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
			getListView().setItemChecked(defaultIndex, true);
		} else {
			getListView().setChoiceMode(AbsListView.CHOICE_MODE_NONE);
		}
	}
	
	@Override
	public void onListItemClick(ListView listView, View v, int position, long id) {
		super.onListItemClick(listView, v, position, id);
		getListView().setItemChecked(position, true);
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractListFragment#onItemSelected(java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void onItemSelected(TabAction action) {
		((OnItemSelectedListener<TabAction>)getActivity()).onItemSelected(action);
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#getAdSize()
	 */
	@Override
	public AdSize getAdSize() {
		return AndroidUtils.isLargeScreenOrBigger() ? null : AdSize.SMART_BANNER;
	}
}