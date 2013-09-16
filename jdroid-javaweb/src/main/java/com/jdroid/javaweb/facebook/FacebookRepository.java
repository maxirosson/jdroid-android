package com.jdroid.javaweb.facebook;

import java.util.List;
import java.util.NoSuchElementException;
import com.google.common.collect.Lists;
import com.jdroid.javaweb.guava.function.PropertyFunction;
import com.restfb.Connection;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.exception.FacebookOAuthException;
import com.restfb.types.FacebookType;
import com.restfb.types.User;

public class FacebookRepository {
	
	private static String FRIEND_FQL = "SELECT uid,name FROM user WHERE uid in (SELECT uid1 FROM friend WHERE uid2 = me() and uid1 = #friendId#)";
	private static String FRIEND_FQL_REPLACEMENT = "#friendId#";
	private static String FRIENDS_FQL = "SELECT uid,first_name,last_name,is_app_user FROM user WHERE uid in (SELECT uid1 FROM friend WHERE uid2 = me()) order by name";
	private static String APP_FRIENDS_FQL = "SELECT uid,first_name,last_name FROM user WHERE is_app_user AND uid in (SELECT uid1 FROM friend WHERE uid2 = me()) order by name";
	private static String APP_FRIENDS_IDS_FQL = "SELECT uid FROM user WHERE is_app_user AND uid in (SELECT uid1 FROM friend WHERE uid2 = me()) order by name";
	private static String FB_ID = "id";
	private static String FB_ME = "me";
	private static String FB_FEED = "/feed";
	private static String FB_SEARCH = "search";
	private static String FB_MESSAGE = "message";
	private static String FB_CAPTION = "caption";
	private static String FB_LINK = "link";
	private static String FB_PICTURE = "picture";
	private static String FB_DESC = "description";
	
	private FacebookClient createFacebookClient(String accessToken) {
		return new DefaultFacebookClient(accessToken);
	}
	
	public Boolean exist(String accessToken, String facebookId) {
		FacebookClient fb = createFacebookClient(accessToken);
		Connection<User> connection = fb.fetchConnection(FB_SEARCH, User.class, Parameter.with(FB_ID, facebookId));
		return !connection.getData().isEmpty();
	}
	
	public User getProfile(String accessToken) {
		FacebookClient fb = createFacebookClient(accessToken);
		return fb.fetchObject(FB_ME, User.class);
	}
	
	public Boolean isFriend(String accessToken, String facebookId) {
		return getFriend(accessToken, facebookId) != null;
	}
	
	public User getFriend(String accessToken, String facebookId) throws FacebookOAuthException {
		FacebookClient fb = createFacebookClient(accessToken);
		List<User> users = fb.executeFqlQuery(FRIEND_FQL.replaceAll(FRIEND_FQL_REPLACEMENT, facebookId), User.class);
		try {
			return users.iterator().next();
		} catch (NoSuchElementException e) {
			return null;
		}
	}
	
	public List<FacebookUser> getFriends(String accessToken) throws FacebookOAuthException {
		FacebookClient fb = createFacebookClient(accessToken);
		return fb.executeFqlQuery(FRIENDS_FQL, FacebookUser.class);
	}
	
	public List<FacebookUser> getAppFriends(String accessToken) throws FacebookOAuthException {
		FacebookClient fb = createFacebookClient(accessToken);
		return fb.executeFqlQuery(APP_FRIENDS_FQL, FacebookUser.class);
	}
	
	public List<String> getAppFriendsIds(String accessToken) throws FacebookOAuthException {
		FacebookClient fb = createFacebookClient(accessToken);
		List<FacebookUser> facebookUsers = fb.executeFqlQuery(APP_FRIENDS_IDS_FQL, FacebookUser.class);
		return Lists.transform(facebookUsers, new PropertyFunction<FacebookUser, String>("facebookId"));
	}
	
	public void publish(String accessToken, String message) {
		publish(accessToken, FB_ME, message);
	}
	
	public void publish(String accessToken, String facebookId, String message) {
		FacebookClient fb = createFacebookClient(accessToken);
		fb.publish(facebookId + FB_FEED, FacebookType.class, Parameter.with(FB_MESSAGE, message));
	}
	
	public void publishLink(String accessToken, String link, String message, String image, String caption,
			String description) {
		publishLink(accessToken, FB_ME, link, message, image, caption, description);
	}
	
	public void publishLink(String accessToken, String facebookId, String link, String message, String image,
			String caption, String description) {
		FacebookClient fb = createFacebookClient(accessToken);
		fb.publish(facebookId + FB_FEED, FacebookType.class, Parameter.with(FB_LINK, link),
			Parameter.with(FB_MESSAGE, message), Parameter.with(FB_PICTURE, image),
			Parameter.with(FB_CAPTION, caption), Parameter.with(FB_DESC, description));
	}
}
