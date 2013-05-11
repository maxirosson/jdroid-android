package com.jdroid.android.cookie;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.slf4j.Logger;
import android.content.SharedPreferences.Editor;
import com.jdroid.android.utils.SharedPreferencesUtils;
import com.jdroid.java.collections.Lists;
import com.jdroid.java.collections.Maps;
import com.jdroid.java.utils.DateUtils;
import com.jdroid.java.utils.LoggerUtils;

/**
 * {@link CookieRepository} implementation.
 * 
 * @author Estefania Caravatti
 */
public class CookieRepositoryImpl implements CookieRepository {
	
	private final static Logger LOGGER = LoggerUtils.getLogger(CookieRepositoryImpl.class);
	
	private static final String NAME = ".cookie.name";
	private static final String VALUE = ".cookie.value";
	private static final String DOMAIN = ".cookie.domain";
	private static final String EXPIRY_DATE = ".cookie.expirationdate";
	
	private Map<String, Cookie> cookiesMap = Maps.newHashMap();
	
	/**
	 * @see com.jdroid.android.cookie.CookieRepository#add(org.apache.http.cookie.Cookie)
	 */
	@Override
	public void add(Cookie cookie) {
		String cookieName = cookie.getName();
		LOGGER.debug("Saving cookie " + cookieName + "[" + cookie.getValue() + "]");
		
		Editor editor = SharedPreferencesUtils.getEditor();
		editor.putString(generateCookieNameKey(cookieName), cookieName);
		editor.putString(generateCookieValueKey(cookieName), cookie.getValue());
		editor.putString(generateCookieDomainKey(cookieName), cookie.getDomain());
		if (cookie.getExpiryDate() != null) {
			editor.putString(generateCookieExiryDateKey(cookieName),
				DateUtils.format(cookie.getExpiryDate(), DateUtils.MMDDYYYY_SLASH_DATE_FORMAT));
		}
		editor.commit();
		
		cookiesMap.put(cookieName, cookie);
	}
	
	/**
	 * @see com.jdroid.android.cookie.CookieRepository#addAll(java.util.List)
	 */
	@Override
	public void addAll(List<Cookie> cookies) {
		for (Cookie cookie : cookies) {
			add(cookie);
		}
	}
	
	/**
	 * @see com.jdroid.android.cookie.CookieRepository#remove(org.apache.http.cookie.Cookie)
	 */
	@Override
	public void remove(Cookie cookie) {
		String cookieName = cookie.getName();
		remove(cookieName);
	}
	
	/**
	 * @see com.jdroid.android.cookie.CookieRepository#removeAll()
	 */
	@Override
	public void removeAll() {
		for (Entry<String, ?> entry : SharedPreferencesUtils.loadAllPreferences().entrySet()) {
			if (entry.getKey().endsWith(NAME)) {
				remove(String.class.cast(entry.getValue()));
			}
		}
		cookiesMap.clear();
	}
	
	/**
	 * Removes the {@link Cookie} with the given name.
	 * 
	 * @param cookieName The {@link Cookie}'s name.
	 */
	private void remove(String cookieName) {
		LOGGER.debug("Removing cookie " + cookieName);
		
		Editor editor = SharedPreferencesUtils.getEditor();
		editor.remove(generateCookieNameKey(cookieName));
		editor.remove(generateCookieValueKey(cookieName));
		editor.remove(generateCookieDomainKey(cookieName));
		editor.remove(generateCookieExiryDateKey(cookieName));
		editor.commit();
		
		cookiesMap.remove(cookieName);
	}
	
	/**
	 * Gets a {@link Cookie} from the repository if it exists.
	 * 
	 * @param cookieName The name of the {@link Cookie} to get.
	 * @return The {@link Cookie}.
	 */
	private Cookie get(String cookieName) {
		
		Cookie cookie = cookiesMap.get(cookieName);
		
		if (cookie == null) {
			BasicClientCookie basicClientCookie = new BasicClientCookie(cookieName,
					SharedPreferencesUtils.loadPreference(generateCookieValueKey(cookieName)));
			basicClientCookie.setDomain(SharedPreferencesUtils.loadPreference(generateCookieDomainKey(cookieName)));
			
			String expiryDate = SharedPreferencesUtils.loadPreference(generateCookieExiryDateKey(cookieName));
			if (expiryDate != null) {
				basicClientCookie.setExpiryDate(DateUtils.parse(expiryDate, DateUtils.MMDDYYYY_SLASH_DATE_FORMAT));
			}
			cookie = basicClientCookie;
			cookiesMap.put(cookieName, cookie);
		}
		
		LOGGER.debug("Retrieving cookie " + cookieName + "[" + cookie.getValue() + "]");
		return cookie;
	}
	
	/**
	 * @see com.jdroid.android.cookie.CookieRepository#getAll()
	 */
	@Override
	public List<Cookie> getAll() {
		List<Cookie> cookies = Lists.newArrayList();
		for (Entry<String, ?> entry : SharedPreferencesUtils.loadAllPreferences().entrySet()) {
			
			// Get all the preferences whose key ends with CookieRepositoryImpl#NAME.
			if (entry.getKey().endsWith(NAME)) {
				cookies.add(get(String.class.cast(entry.getValue())));
			}
		}
		return cookies;
	}
	
	/**
	 * @param cookieName The {@link Cookie}'s name.
	 * @return The key used to save the {@link Cookie}'s name as shared preference.
	 */
	private String generateCookieNameKey(String cookieName) {
		return cookieName + NAME;
	}
	
	/**
	 * @param cookieName The {@link Cookie}'s name.
	 * @return The key used to save the {@link Cookie}'s expiration date as shared preference.
	 */
	private String generateCookieExiryDateKey(String cookieName) {
		return cookieName + EXPIRY_DATE;
	}
	
	/**
	 * @param cookieName The {@link Cookie}'s name.
	 * @return The key used to save the {@link Cookie}'s domain as shared preference.
	 */
	private String generateCookieDomainKey(String cookieName) {
		return cookieName + DOMAIN;
	}
	
	/**
	 * @param cookieName The {@link Cookie}'s name.
	 * @return The key used to save the {@link Cookie}'s value as shared preference.
	 */
	private String generateCookieValueKey(String cookieName) {
		return cookieName + VALUE;
	}
}
