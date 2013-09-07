package com.jdroid.android.social.facebook;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import com.jdroid.android.R;
import com.jdroid.android.activity.AbstractFragmentActivity;
import com.jdroid.android.utils.ToastUtils;

/**
 * 
 * @author Maxi Rosson
 */
public class FacebookLoginButton extends Button {
	
	private Boolean loggedOnFacebook;
	
	public FacebookLoginButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	public FacebookLoginButton(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public FacebookLoginButton(Context context) {
		super(context);
	}
	
	public void init() {
		loggedOnFacebook = FacebookPreferencesUtils.verifyFacebookAccesToken();
		setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (loggedOnFacebook) {
					getFacebookAuthenticationFragment().startLogoutProcess();
				} else {
					getFacebookAuthenticationFragment().startLoginProcess();
				}
			}
		});
		
		if (loggedOnFacebook) {
			setText(R.string.disconnectFromFacebook);
		} else {
			setText(R.string.connectToFacebook);
		}
	}
	
	private FacebookAuthenticationFragment<?> getFacebookAuthenticationFragment() {
		return ((AbstractFragmentActivity)getContext()).getFragment(FacebookAuthenticationFragment.class);
	}
	
	public void markAsLogged() {
		loggedOnFacebook = true;
		setText(R.string.disconnectFromFacebook);
		ToastUtils.showInfoToast(R.string.accountConnected);
	}
	
	public void markAsNotLogged() {
		loggedOnFacebook = false;
		setText(R.string.connectToFacebook);
		ToastUtils.showInfoToast(R.string.accountDisconnected);
	}
	
}
