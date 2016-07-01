package com.jdroid.java.utils;

import java.util.Locale;

public class LocaleUtils {

	private LocaleUtils(){}

	public static String getAcceptLanguage() {
		String language = Locale.getDefault().getLanguage();
		String country = Locale.getDefault().getCountry();
		if (StringUtils.isNotBlank(country)) {
			language = language + "-" + country;
		}
		return language;
	}

	public static String getLanguage(String acceptLanguage) {
		return acceptLanguage.split("-")[0];
	}
}
