package com.jdroid.android.loading;

import android.app.Dialog;
import android.content.Context;
import com.jdroid.android.activity.ActivityIf;

public class DefaultBlockingLoading extends BlockingLoading {
	
	private Dialog loadingDialog;
	
	/**
	 * @see com.jdroid.android.loading.ActivityLoading#show(com.jdroid.android.activity.ActivityIf)
	 */
	@Override
	public void show(final ActivityIf activityIf) {
		if (!activityIf.isActivityDestroyed() && ((loadingDialog == null) || (!loadingDialog.isShowing()))) {
			loadingDialog = new LoadingDialog((Context)activityIf);
			loadingDialog.setCancelable(isCancelable());
			loadingDialog.show();
		}
	}
	
	/**
	 * @see com.jdroid.android.loading.ActivityLoading#dismiss(com.jdroid.android.activity.ActivityIf)
	 */
	@Override
	public void dismiss(ActivityIf activityIf) {
		if (loadingDialog != null) {
			loadingDialog.dismiss();
			loadingDialog = null;
		}
	}
}
