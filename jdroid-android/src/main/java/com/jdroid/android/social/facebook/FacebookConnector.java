package com.jdroid.android.social.facebook;

import java.util.Arrays;
import java.util.List;
import org.slf4j.Logger;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import com.facebook.FacebookRequestError.Category;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.Session.Builder;
import com.facebook.SessionDefaultAudience;
import com.facebook.SessionLoginBehavior;
import com.facebook.SessionState;
import com.facebook.SharedPreferencesTokenCachingStrategy;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import com.jdroid.android.AbstractApplication;
import com.jdroid.android.exception.CommonErrorCode;
import com.jdroid.java.exception.UnexpectedException;
import com.jdroid.java.utils.LoggerUtils;

/**
 * This class handles all Facebook session events and requests.
 * <p>
 * When using this class, clients MUST call all the public methods from the respective methods in either an Activity or
 * Fragment. Failure to call all the methods can result in improperly initialized or uninitialized Sessions.
 */
public class FacebookConnector {
	
	private static final Logger LOGGER = LoggerUtils.getLogger(FacebookConnector.class);
	
	private static final List<String> PERMISSIONS = Arrays.asList("email");
	private static final List<String> PUBLISH_PERMISSION = Arrays.asList("publish_stream");
	private static final SessionLoginBehavior LOGIN_BEHAVIOR = SessionLoginBehavior.SSO_WITH_FALLBACK;
	private SessionDefaultAudience DEFAULT_AUDIENCE = SessionDefaultAudience.FRIENDS;
	
	private UiLifecycleHelper uiHelper;
	private SessionStateListener sessionStateListener;
	private String facebookAppId;
	private FacebookAuthenticationListener facebookAuthenticationListener;
	
	private Session.StatusCallback callback = new Session.StatusCallback() {
		
		@Override
		public void call(Session session, SessionState state, Exception exception) {
			onSessionStateChange(session, state, exception);
		}
	};
	
	public FacebookConnector() {
		this(null, null, null);
	}
	
	public FacebookConnector(Activity activity) {
		this(activity, null, null);
	}
	
	/**
	 * Creates a new {@link FacebookConnector}, building and activating a {@link Session}.
	 * 
	 * @param activity The {@link Activity}
	 * @param sessionStateListener is a callback for session events.
	 * @param facebookAuthenticationListener is a callback for Facebook login/logout event.
	 */
	public FacebookConnector(Activity activity, SessionStateListener sessionStateListener,
			FacebookAuthenticationListener facebookAuthenticationListener) {
		facebookAppId = AbstractApplication.get().getAndroidApplicationContext().getFacebookAppId();
		buildSession(facebookAppId);
		this.sessionStateListener = sessionStateListener;
		if (activity != null) {
			uiHelper = new UiLifecycleHelper(activity, callback);
		}
		this.facebookAuthenticationListener = facebookAuthenticationListener;
	}
	
	private Session buildSession(String facebookAppId) {
		SharedPreferencesTokenCachingStrategy tokenCachingStrategy = new SharedPreferencesTokenCachingStrategy(
				AbstractApplication.get());
		Builder sessionBuilder = new Builder(AbstractApplication.get());
		sessionBuilder.setTokenCachingStrategy(tokenCachingStrategy);
		sessionBuilder.setApplicationId(facebookAppId);
		Session session = sessionBuilder.build();
		Session.setActiveSession(session);
		
		return session;
	}
	
	public void onCreate(Bundle savedInstanceState) {
		if (uiHelper != null) {
			uiHelper.onCreate(savedInstanceState);
		}
	}
	
	public void onResume() {
		if (uiHelper != null) {
			uiHelper.onResume();
		}
	}
	
	public void onPause() {
		if (uiHelper != null) {
			uiHelper.onPause();
		}
	}
	
	public void onDestroy() {
		if (uiHelper != null) {
			uiHelper.onDestroy();
		}
	}
	
	public void onSaveInstanceState(Bundle outState) {
		if (uiHelper != null) {
			uiHelper.onSaveInstanceState(outState);
		}
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (uiHelper != null) {
			uiHelper.onActivityResult(requestCode, resultCode, data);
		}
		if ((requestCode == Session.DEFAULT_AUTHORIZE_ACTIVITY_CODE) && (resultCode == Activity.RESULT_OK)) {
			notifyFacebookAuthenticationListener();
		}
	}
	
	/**
	 * Initializes {@link LoginButton} with email permissions.
	 * 
	 * @param fragment
	 * @param facebookLoginButton
	 */
	public void initLoginButtonWithEmailPermissions(Fragment fragment, LoginButton facebookLoginButton) {
		facebookLoginButton.setFragment(fragment);
		facebookLoginButton.setReadPermissions(PERMISSIONS);
		facebookLoginButton.setApplicationId(facebookAppId);
	}
	
	/**
	 * 
	 * @param session FB's {@link Session}
	 * @param state The new state for FB's {@link Session}.
	 * @param exception The exception that may have been thrown when trying to change the {@link Session}'s state.
	 */
	private void onSessionStateChange(Session session, SessionState state, Exception exception) {
		LOGGER.debug("Facebook session state changed to " + state.name());
		
		if (sessionStateListener != null) {
			if (SessionState.OPENED_TOKEN_UPDATED.equals(state)) {
				sessionStateListener.onSessionOpenedWithUpdatedToken();
			} else if (SessionState.CLOSED.equals(state)) {
				sessionStateListener.onSessionClosed();
			} else if (SessionState.OPENED.equals(state)) {
				sessionStateListener.onSessionOpened();
			} else if (SessionState.CLOSED_LOGIN_FAILED.equals(state)) {
				sessionStateListener.onSessionClosedLoginFailed();
			}
		}
		
	}
	
	public void login(Fragment fragment) {
		Session currentSession = getCurrentSession();
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Facebook currentSession currentSession.isOpened() = " + currentSession.isOpened());
			LOGGER.debug("Facebook currentSession currentSession.isClosed() = " + currentSession.isClosed());
			LOGGER.debug("Facebook currentSession currentSession state = " + currentSession.getState().name());
		}
		
		if (currentSession.isClosed()) {
			// Discard the current session and build a new one to avoid an exception
			Session.setActiveSession(null);
			currentSession = getCurrentSession();
		}
		
		if (currentSession.isOpened()) {
			notifyFacebookAuthenticationListener();
		} else {
			Session.OpenRequest openRequest = new Session.OpenRequest(fragment);
			
			openRequest.setDefaultAudience(DEFAULT_AUDIENCE);
			openRequest.setPermissions(PERMISSIONS);
			openRequest.setLoginBehavior(LOGIN_BEHAVIOR);
			
			currentSession.openForRead(openRequest);
		}
	}
	
	public void loginForPublish(Fragment fragment) {
		Session currentSession = getCurrentSession();
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Facebook currentSession currentSession.isOpened() = " + currentSession.isOpened());
			LOGGER.debug("Facebook currentSession currentSession.isClosed() = " + currentSession.isClosed());
			LOGGER.debug("Facebook currentSession currentSession state = " + currentSession.getState().name());
		}
		
		if (currentSession.isClosed()) {
			// Discard the current session and build a new one to avoid an exception
			Session.setActiveSession(null);
			currentSession = getCurrentSession();
		}
		
		if (currentSession.isOpened()) {
			notifyFacebookAuthenticationListener();
		} else {
			Session.OpenRequest openRequest = new Session.OpenRequest(fragment);
			
			openRequest.setDefaultAudience(DEFAULT_AUDIENCE);
			openRequest.setPermissions(PUBLISH_PERMISSION);
			openRequest.setLoginBehavior(LOGIN_BEHAVIOR);
			
			currentSession.openForPublish(openRequest);
		}
	}
	
	public void logout() {
		Session currentSession = getCurrentSession();
		currentSession.closeAndClearTokenInformation();
		if (facebookAuthenticationListener != null) {
			facebookAuthenticationListener.onFacebookLogoutCompleted();
		}
	}
	
	public String getAccessToken() {
		return getCurrentSession().getAccessToken();
	}
	
	public void verifyToken() {
		executeMeRequest();
	}
	
	/**
	 * This method execute a Facebook "MeRequest" to get user profile info for the current user. This method should not
	 * be called from the UI thread.
	 * 
	 * @return GraphUser for the logged user
	 */
	public GraphUser executeMeRequest() {
		
		Response response = null;
		try {
			response = Request.executeAndWait(Request.newMeRequest(Session.getActiveSession(), null));
		} catch (Exception e) {
			throw CommonErrorCode.SERVER_ERROR.newApplicationException(e);
		}
		
		if (response.getError() != null) {
			if (Category.AUTHENTICATION_RETRY.equals(response.getError().getCategory())
					|| Category.AUTHENTICATION_REOPEN_SESSION.equals(response.getError().getCategory())
					|| Category.PERMISSION.equals(response.getError().getCategory())) {
				throw new FacebookExpiredSessionException(response.getError().getErrorMessage());
			} else if (Category.CLIENT.equals(response.getError().getCategory())) {
				LOGGER.warn(response.getError().getErrorMessage());
				throw CommonErrorCode.CONNECTION_ERROR.newApplicationException(response.getError().getErrorMessage());
			} else {
				throw CommonErrorCode.SERVER_ERROR.newApplicationException(response.getError().getErrorMessage());
			}
		}
		
		GraphUser fbUser = null;
		try {
			fbUser = response.getGraphObjectAs(GraphUser.class);
		} catch (Exception e) {
			throw CommonErrorCode.SERVER_ERROR.newApplicationException(e);
		}
		
		if (fbUser == null) {
			throw new UnexpectedException("Failed to get GraphUser from Facebook");
		}
		return fbUser;
	}
	
	private Session getCurrentSession() {
		Session currentSession = Session.getActiveSession();
		if (currentSession == null) {
			currentSession = buildSession(facebookAppId);
		}
		return currentSession;
	}
	
	private void notifyFacebookAuthenticationListener() {
		if (facebookAuthenticationListener != null) {
			facebookAuthenticationListener.onFacebookLoginCompleted(this);
		}
	}
}
