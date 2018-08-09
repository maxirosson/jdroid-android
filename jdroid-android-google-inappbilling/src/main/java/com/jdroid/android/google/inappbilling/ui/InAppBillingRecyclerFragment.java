package com.jdroid.android.google.inappbilling.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import com.jdroid.android.google.inappbilling.client.Product;
import com.jdroid.android.recycler.AbstractRecyclerFragment;
import com.jdroid.android.recycler.RecyclerViewAdapter;

import java.util.List;

public abstract class InAppBillingRecyclerFragment extends AbstractRecyclerFragment implements InAppBillingListener {
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		if (savedInstanceState == null) {
			InAppBillingHelperFragment.add(getActivity(), InAppBillingHelperFragment.class, false, this);
		}
		showLoading();
	}
	
	@Override
	public void onProductsLoaded(List<Product> products) {
		setAdapter(new RecyclerViewAdapter(new ProductViewType() {
			
			@NonNull
			@Override
			public AbstractRecyclerFragment getAbstractRecyclerFragment() {
				return InAppBillingRecyclerFragment.this;
			}
			
			@Override
			protected void onPriceClick(Product product) {
				launchPurchaseFlow(product);
			}
			
			
		}, products));
		dismissLoading();
	}
	
	public void launchPurchaseFlow(Product product) {
		InAppBillingHelperFragment inAppBillingHelperFragment = InAppBillingHelperFragment.get(getActivity());
		if (inAppBillingHelperFragment != null) {
			inAppBillingHelperFragment.launchPurchaseFlow(product);
		}
	}
	
	@Override
	@SuppressWarnings("rawtypes")
	public void onConsumed(Product product) {
		getAdapter().notifyDataSetChanged();
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		InAppBillingHelperFragment inAppBillingHelperFragment = InAppBillingHelperFragment.get(getActivity());
		if (inAppBillingHelperFragment != null) {
			inAppBillingHelperFragment.onActivityResult(requestCode, resultCode, data);
		}
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		InAppBillingHelperFragment.removeTarget(getActivity());
	}

	@Override
	protected Boolean isCardViewDecorationEnabled() {
		return true;
	}

	@Override
	protected Boolean isDividerItemDecorationEnabled() {
		return true;
	}
}
