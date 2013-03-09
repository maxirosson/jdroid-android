package com.jdroid.android.facebook;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;
import com.facebook.android.Util;
import com.jdroid.android.exception.CommonErrorCode;
import com.jdroid.java.collections.Lists;
import com.jdroid.java.parser.json.JsonObjectWrapper;

/**
 * TODO FB
 * 
 * @author Maxi Rosson
 */
public class FacebookConnector {
	
	private static final String TAG = FacebookConnector.class.getSimpleName();
	
	private Facebook facebook;
	
	public FacebookConnector(String appId) {
		this(appId, null);
	}
	
	public FacebookConnector(String appId, String accessToken) {
		facebook = new Facebook(appId);
		facebook.setAccessToken(accessToken);
	}
	
	/**
	 * @param accessToken The access token to use.
	 * @param accessExpiresIn The time until the token expires (in milliseconds since Unix epoch).
	 */
	public void setAccessToken(String accessToken, Long accessExpiresIn) {
		facebook.setAccessToken(accessToken);
		facebook.setAccessExpires(accessExpiresIn);
	}
	
	public void post(String message, String description, String name, String picure, String link) {
		try {
			Bundle parameters = new Bundle();
			parameters.putString("message", message);
			parameters.putString("description", description);
			parameters.putString("name", name);
			parameters.putString("picture", picure);
			parameters.putString("link", link);
			String response = facebook.request("me/feed", parameters, "POST");
			com.facebook.android.Util.parseJson(response);
		} catch (FacebookError e) {
			handleFacebookError(e);
		} catch (FileNotFoundException e) {
			throw CommonErrorCode.INTERNAL_ERROR.newApplicationException(e);
		} catch (MalformedURLException e) {
			throw CommonErrorCode.INTERNAL_ERROR.newApplicationException(e);
		} catch (IOException e) {
			throw CommonErrorCode.CONNECTION_ERROR.newApplicationException(e);
		} catch (JSONException e) {
			throw CommonErrorCode.INTERNAL_ERROR.newApplicationException(e);
		}
	}
	
	/**
	 * @return The id of the logged Facebook user.
	 */
	public String getFacebookUserId() {
		try {
			return getFacebookUser().getString("id");
		} catch (JSONException e) {
			throw CommonErrorCode.INTERNAL_ERROR.newApplicationException(e);
		}
	}
	
	/**
	 * @return A {@link JsonObjectWrapper} containing the logged Facebook user.
	 */
	private JsonObjectWrapper getFacebookUser() {
		try {
			return new JsonObjectWrapper(facebook.request("me"));
		} catch (JSONException e) {
			throw CommonErrorCode.INTERNAL_ERROR.newApplicationException(e);
		} catch (Exception e) {
			throw CommonErrorCode.FACEBOOK_ERROR.newApplicationException(e);
		}
	}
	
	public List<String> getFriendsIds() {
		List<String> facebookIds = Lists.newArrayList();
		try {
			Bundle parameters = new Bundle();
			parameters.putString("fields", "id");
			String response = facebook.request("me/friends", parameters);
			JSONArray jArray = Util.parseJson(response).getJSONArray("data");
			for (int i = 0; i < jArray.length(); i++) {
				JSONObject array_element = jArray.getJSONObject(i);
				facebookIds.add(array_element.getString("id"));
			}
			
		} catch (FacebookError e) {
			handleFacebookError(e);
		} catch (JSONException e) {
			throw CommonErrorCode.INTERNAL_ERROR.newApplicationException(e);
		} catch (MalformedURLException e) {
			throw CommonErrorCode.INTERNAL_ERROR.newApplicationException(e);
		} catch (IOException e) {
			throw CommonErrorCode.CONNECTION_ERROR.newApplicationException(e);
		}
		return facebookIds;
	}
	
	private void handleFacebookError(FacebookError e) {
		if (FacebookErrorType.find(e).equals(FacebookErrorType.OAUTH_ERROR)) {
			throw new InvalidFacebookTokenException();
		} else {
			throw CommonErrorCode.FACEBOOK_ERROR.newApplicationException(e);
		}
	}
	
	/**
	 * Connects to facebook granting only basic permissions. {@link FacebookConnector#connectCallback(int, int, Intent)}
	 * must be called on the {@link Activity#onActivityResult(int, int, Intent)} to complete login.
	 * 
	 * @param activity The {@link Activity} where the authorization dialog will be shown.
	 */
	public void connect(Activity activity) {
		Log.d(TAG, "Attempting to connect to Facebook.");
		facebook.authorize(activity, new DefaultFacebookDialogListener());
	}
	
	public void disconnect(Context context) {
		try {
			Log.d(TAG, "Logging out from Facebook.");
			facebook.logout(context);
			Log.d(TAG, "Log out from Facebook completed successfully.");
		} catch (MalformedURLException e) {
			Log.d(TAG, "Error while logging out from Facebook.", e);
			throw CommonErrorCode.INTERNAL_ERROR.newApplicationException(e);
		} catch (IOException e) {
			Log.d(TAG, "Error while logging out from Facebook.", e);
			throw CommonErrorCode.CONNECTION_ERROR.newApplicationException(e);
		}
	}
	
	/**
	 * IMPORTANT: This method must be invoked at the top of the calling activity's onActivityResult() function or
	 * Facebook authentication will not function properly! If you're connecting from a fragment, you should call the
	 * fragment's {@link Fragment#onActivityResult(int, int, Intent)} manually from your activity.
	 * 
	 * @param requestCode The request code for the {@link Activity#onActivityResult(int, int, Intent)}.
	 * @param resultCode The result code obtained on {@link Activity#onActivityResult(int, int, Intent)}.
	 * @param data The data intent obtained on {@link Activity#onActivityResult(int, int, Intent)}.
	 * @return The access token required to connect to Facebook.
	 */
	public String connectCallback(int requestCode, int resultCode, Intent data) {
		facebook.authorizeCallback(requestCode, resultCode, data);
		Log.d(TAG, "Facebook connection callback completed successfully.");
		return facebook.getAccessToken();
	}
	
	/**
	 * @return Whether this connector is connected to a valid Facebook session.
	 */
	public Boolean isConnected() {
		return facebook.isSessionValid();
	}
	
	public Long getAccessExpires() {
		return facebook.getAccessExpires();
	}
	
	public String getAccessToken() {
		return facebook.getAccessToken();
	}
}
