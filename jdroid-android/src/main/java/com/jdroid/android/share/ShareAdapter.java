package com.jdroid.android.share;

import java.util.List;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.jdroid.android.R;
import com.jdroid.android.adapter.BaseHolderArrayAdapter;
import com.jdroid.android.share.ShareAdapter.ShareViewHolder;
import com.jdroid.android.social.facebook.FacebookShareLaunchable;
import com.jdroid.java.collections.Lists;

/**
 * 
 * @author Estefania Caravatti
 */
public class ShareAdapter extends BaseHolderArrayAdapter<ShareLaunchable, ShareViewHolder> {
	
	/** Pattern for the official Facebook activity */
	private static final String FACEBOOK_APP_PATTERN = "^com.facebook.*";
	
	/**
	 * @param context The {@link Activity} to use as context.
	 * @param apps The {@link ResolveInfo} for the apps to list.
	 * @param packageManager The {@link PackageManager}.
	 * @param facebookAppId
	 * @param accessToken
	 */
	public ShareAdapter(Activity context, List<ResolveInfo> apps, PackageManager packageManager, String facebookAppId,
			String accessToken) {
		super(context, R.layout.share_row);
		
		// The Facebook app is removed and a custom FacebookShareLaunchable is used.
		List<ShareLaunchable> launchables = Lists.newArrayList();
		for (ResolveInfo resolveInfo : apps) {
			
			if (!resolveInfo.activityInfo.name.matches(FACEBOOK_APP_PATTERN)) {
				launchables.add(new ShareLaunchable(resolveInfo, packageManager));
			}
		}
		launchables.add(new FacebookShareLaunchable(facebookAppId, accessToken));
		
		addAll(launchables);
	}
	
	/**
	 * @see com.jdroid.android.adapter.BaseHolderArrayAdapter#fillHolderFromItem(java.lang.Object, java.lang.Object)
	 */
	@Override
	protected void fillHolderFromItem(final ShareLaunchable item, ShareViewHolder holder) {
		holder.appName.setText(item.getName());
		holder.appIcon.setImageDrawable(item.getIcon());
	}
	
	/**
	 * @see com.jdroid.android.adapter.BaseHolderArrayAdapter#createViewHolderFromConvertView(android.view.View)
	 */
	@Override
	protected ShareViewHolder createViewHolderFromConvertView(View convertView) {
		ShareViewHolder holder = new ShareViewHolder();
		holder.appName = findView(convertView, R.id.name);
		holder.appIcon = findView(convertView, R.id.icon);
		return holder;
	}
	
	public static class ShareViewHolder {
		
		private TextView appName;
		private ImageView appIcon;
	}
	
}
