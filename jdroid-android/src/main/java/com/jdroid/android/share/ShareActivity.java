package com.jdroid.android.share;

import java.util.Collections;
import java.util.List;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import com.jdroid.android.activity.AbstractListActivity;

/**
 * Activity with the list of applications that accept a share intent. This activity overrides facebook default option
 * with a custom behavior.
 * 
 * @author Estefania Caravatti
 */
public abstract class ShareActivity extends AbstractListActivity<ShareLaunchable> {
	
	public static final String SUBJECT_EXTRA = "subject";
	public static final String MESSAGE_EXTRA = "message";
	public static final String LINK_EXTRA = "link";
	public static final String LINK_NAME_EXTRA = "linkName";
	public static final String LINK_IMAGE_URL_EXTRA = "linkImage";
	public static final String CAPTION_EXTRA = "caption";
	
	private String shareSubject;
	private String shareMessage;
	private String link;
	private String linkName;
	private String linkImageURL;
	private String linkCaption;
	
	/**
	 * @see com.jdroid.android.activity.AbstractListActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		shareSubject = getExtra(SUBJECT_EXTRA);
		shareMessage = getExtra(MESSAGE_EXTRA);
		link = getExtra(LINK_EXTRA);
		linkName = getExtra(LINK_NAME_EXTRA);
		linkImageURL = getExtra(LINK_IMAGE_URL_EXTRA);
		linkCaption = getExtra(CAPTION_EXTRA);
		
		// Get the list of activities that can handle the share intent.
		PackageManager packageManager = getPackageManager();
		List<ResolveInfo> launchables = packageManager.queryIntentActivities(ShareLaunchable.getShareIntent(), 0);
		Collections.sort(launchables, new ResolveInfo.DisplayNameComparator(packageManager));
		
		setListAdapter(new ShareAdapter(this, launchables, packageManager, getFacebookAppId(), getAccessToken()));
	}
	
	/**
	 * @see com.jdroid.android.activity.AbstractListActivity#onListItemClick(java.lang.Object)
	 */
	@Override
	protected void onListItemClick(ShareLaunchable shareLaunchable) {
		shareLaunchable.share(this, shareSubject, shareMessage, link, linkName, linkCaption, linkImageURL);
	}
	
	protected abstract String getFacebookAppId();
	
	protected abstract String getAccessToken();
	
}
