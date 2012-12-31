package com.jdroid.android.wizard;

import java.util.List;
import android.os.Build;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import com.jdroid.android.R;
import com.jdroid.android.activity.AbstractFragmentActivity;
import com.jdroid.android.utils.AndroidUtils;

/**
 * 
 * @author Maxi Rosson
 */
public abstract class WizardActivity extends AbstractFragmentActivity {
	
	private ViewPager pager;
	private Button previous;
	private Button next;
	
	/**
	 * @see com.jdroid.android.activity.ActivityIf#getContentView()
	 */
	@Override
	public int getContentView() {
		return R.layout.wizard_activity;
	}
	
	protected void loadWizard() {
		WizardStepFragmentAdapter pagerAdapter = new WizardStepFragmentAdapter(getSupportFragmentManager(),
				getWizardSteps());
		
		pager = findView(R.id.pager);
		pager.setAdapter(pagerAdapter);
		pager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			
			@Override
			public void onPageSelected(int position) {
				updateBottomBar();
			}
		});
		
		next = findView(R.id.next);
		next.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View view) {
				if (isOnFinishStep()) {
					onfinishWizard();
				} else {
					pager.setCurrentItem(pager.getCurrentItem() + 1);
				}
			}
		});
		
		previous = findView(R.id.previous);
		previous.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View view) {
				pager.setCurrentItem(pager.getCurrentItem() - 1);
			}
		});
		
		updateBottomBar();
	}
	
	public abstract List<? extends WizardStep> getWizardSteps();
	
	public WizardStep getCurrentWizardStep() {
		return getWizardSteps().get(pager.getCurrentItem());
	}
	
	private void updateBottomBar() {
		int position = pager.getCurrentItem();
		if (isOnFinishStep()) {
			next.setText(R.string.finish);
			if (AndroidUtils.getApiLevel() >= Build.VERSION_CODES.JELLY_BEAN) {
				next.setBackgroundResource(R.drawable.finish_background);
				next.setTextAppearance(this, R.style.finishWizardText);
			}
		} else {
			next.setText(R.string.next);
			if (AndroidUtils.getApiLevel() >= Build.VERSION_CODES.JELLY_BEAN) {
				next.setBackgroundResource(R.drawable.selectable_item_background);
				TypedValue v = new TypedValue();
				getTheme().resolveAttribute(android.R.attr.textAppearanceMedium, v, true);
				next.setTextAppearance(this, v.resourceId);
			}
		}
		
		previous.setVisibility(position <= 0 ? View.INVISIBLE : View.VISIBLE);
	}
	
	private Boolean isOnFinishStep() {
		return pager.getCurrentItem() == (getWizardSteps().size() - 1);
	}
	
	protected abstract void onfinishWizard();
	
}
