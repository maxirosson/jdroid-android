package com.jdroid.android.contextual;

import java.util.List;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import com.jdroid.android.ActivityLauncher;
import com.jdroid.android.R;
import com.jdroid.android.activity.AbstractFragmentActivity;
import com.jdroid.android.fragment.OnItemSelectedListener;
import com.jdroid.android.tabs.TabAction;
import com.jdroid.android.utils.AndroidUtils;

/**
 * 
 * @param <T>
 * @author Maxi Rosson
 */
public abstract class ContextualActivity<T extends TabAction> extends AbstractFragmentActivity implements
		OnItemSelectedListener<T> {
	
	public static final String DEFAULT_CONTEXTUAL_ITEM_EXTRA = "defaultContextualItem";
	
	/**
	 * @see com.jdroid.android.activity.ActivityIf#getContentView()
	 */
	@Override
	public int getContentView() {
		return AndroidUtils.isLargeScreenOrBigger() ? R.layout.contextual_activity
				: R.layout.fragment_container_activity;
	}
	
	/**
	 * @see android.preference.PreferenceActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		if (savedInstanceState == null) {
			
			T defaultContextualItem = getExtra(DEFAULT_CONTEXTUAL_ITEM_EXTRA);
			if (defaultContextualItem == null) {
				defaultContextualItem = getDefaultContextualItem();
			}
			
			FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
			
			int contextualListFragmentId = AndroidUtils.isLargeScreenOrBigger() ? R.id.contextualFragmentContainer
					: R.id.fragmentContainer;
			fragmentTransaction.add(contextualListFragmentId,
				newContextualListFragment(getContextualItems(), defaultContextualItem));
			
			if (AndroidUtils.isLargeScreenOrBigger()) {
				fragmentTransaction.add(R.id.detailsFragmentContainer, defaultContextualItem.createFragment(null),
					defaultContextualItem.getName());
			}
			fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
			fragmentTransaction.commit();
		}
	}
	
	protected Fragment newContextualListFragment(List<T> actions, T defaultContextualItem) {
		return new ContextualListFragment(getContextualItems(), defaultContextualItem);
	}
	
	protected abstract List<T> getContextualItems();
	
	protected abstract T getDefaultContextualItem();
	
	/**
	 * @see com.jdroid.android.fragment.OnItemSelectedListener#onItemSelected(java.lang.Object)
	 */
	@Override
	public void onItemSelected(T item) {
		
		Fragment oldFragment = getSupportFragmentManager().findFragmentById(R.id.detailsFragmentContainer);
		if (oldFragment == null) {
			ActivityLauncher.launchActivity(item.getActivityClass());
		} else {
			if (!oldFragment.getTag().equals(item.getName())) {
				FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
				fragmentTransaction.replace(R.id.detailsFragmentContainer, item.createFragment(null), item.getName());
				fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
				fragmentTransaction.commit();
			}
		}
	}
}