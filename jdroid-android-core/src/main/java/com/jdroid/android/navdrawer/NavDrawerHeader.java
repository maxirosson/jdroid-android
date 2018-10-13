package com.jdroid.android.navdrawer;

import androidx.annotation.DrawableRes;
import androidx.annotation.LayoutRes;
import com.google.android.material.navigation.NavigationView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jdroid.android.R;
import com.jdroid.android.images.loader.ImageViewLoader;

public class NavDrawerHeader {

	private View navDrawerHeader;

	public NavDrawerHeader(NavigationView navigationView) {
		navDrawerHeader = navigationView.inflateHeaderView(getNavDrawerHeaderLayout());
	}

	@LayoutRes
	protected int getNavDrawerHeaderLayout() {
		return R.layout.jdroid_nav_drawer_header;
	}

	public void setTitle(String title) {
		((TextView)navDrawerHeader.findViewById(R.id.title)).setText(title);
	}

	public void setSubTitle(String subTitle) {
		((TextView)navDrawerHeader.findViewById(R.id.subTitle)).setText(subTitle);
	}

	public void setBackground(@DrawableRes Integer imageResId) {
		((ImageView)navDrawerHeader.findViewById(R.id.cover)).setImageResource(imageResId);
	}

	public void setBackground(String imageUrl, Long ttl, ImageViewLoader imageViewLoader) {
		if (imageUrl != null) {
			imageViewLoader.displayImage(imageUrl, (ImageView)navDrawerHeader.findViewById(R.id.cover), null, ttl);
		}
	}

	public void setMainImage(@DrawableRes Integer imageResId) {
		((ImageView)navDrawerHeader.findViewById(R.id.photo)).setImageResource(imageResId);
	}

	public void setMainImage(String imageUrl, Long ttl, ImageViewLoader imageViewLoader) {
		if (imageUrl != null) {
			imageViewLoader.displayImage(imageUrl, (ImageView)navDrawerHeader.findViewById(R.id.photo), R.drawable.jdroid_person_default, ttl);
		}
	}
}
