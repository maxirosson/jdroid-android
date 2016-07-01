package com.jdroid.java.utils;

import java.text.Normalizer;

public class Sanitizer {

	private Sanitizer(){}
	
	public static String plainString(String text) {
		String plain = null;
		if (text != null) {
			String nfdNormalizedString = Normalizer.normalize(text, Normalizer.Form.NFD);
			plain = nfdNormalizedString.replaceAll("[^a-zA-Z0-9\\s]", "");
			plain = plain.replaceAll("\\s{2,}", " ").trim();
		}
		return plain;
	}

	public static String plainStringWithoutExtraSpaces(String text) {
		String plain = plainString(text);
		if (text != null) {
			plain = plain.replaceAll("\\s{2,}", " ").trim();
		}
		return plain;
	}
	
	public static String plainStringWithoutNumbers(String text) {
		String withoutNumbers = removeNumbers(text);
		return plainString(withoutNumbers);
	}
	
	public static String justNumbers(String text) {
		return text != null ? text.replaceAll("[^\\d]", "") : null;
	}
	
	public static String removeNumbers(String text) {
		return text != null ? text.replaceAll("\\d", "") : null;
	}

	public static String removeDashes(String text) {
		return text != null ? text.replaceAll(StringUtils.DASH, "") : null;
	}
}
