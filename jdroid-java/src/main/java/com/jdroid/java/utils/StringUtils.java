package com.jdroid.java.utils;

import com.jdroid.java.collections.Lists;
import com.jdroid.java.collections.Sets;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class contains functions for managing Strings
 */
public abstract class StringUtils {
	
	public static final String EMPTY = "";
	public static final String ELLIPSIS = "...";
	public static final String COMMA = ",";
	public static final String SPACE = " ";
	public static final String DASH = "-";
	public static final String SLASH = "/";
	public static final String DOT = ".";
	public static final String NEW_LINE = "\n";
	public static final String UNDERSCORE = "_";
	public static final String BANG = "!";
	public static final String PIPE = "|";
	
	private static final String PLACEHOLDER_PATTERN = "\\$\\{(.*?)\\}";
	private static final String ALPHANUMERIC_PATTERN = "([^\\w\\s])*";
	
	public static String getNotEmptyString(String text) {
		return StringUtils.isEmpty(text) ? null : text;
	}
	
	public static Boolean equal(String text1, String text2) {
		return defaultString(text1).equals(defaultString(text2));
	}
	
	public static Boolean isEmpty(String text) {
		return text != null ? text.length() == 0 : true;
	}
	
	public static Boolean isNotEmpty(String text) {
		return !isEmpty(text);
	}
	
	/**
	 * <p>
	 * Checks if a String is whitespace, empty ("") or null.
	 * </p>
	 * 
	 * <pre>
	 * StringUtils.isBlank(null)      = true
	 * StringUtils.isBlank("")        = true
	 * StringUtils.isBlank(" ")       = true
	 * StringUtils.isBlank("bob")     = false
	 * StringUtils.isBlank("  bob  ") = false
	 * </pre>
	 * 
	 * @param str the String to check, may be null
	 * @return <code>true</code> if the String is null, empty or whitespace
	 */
	public static boolean isBlank(String str) {
		int strLen;
		if ((str == null) || ((strLen = str.length()) == 0)) {
			return true;
		}
		for (int i = 0; i < strLen; i++) {
			if ((!Character.isWhitespace(str.charAt(i)))) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * <p>
	 * Checks if a String is not empty (""), not null and not whitespace only.
	 * </p>
	 * 
	 * <pre>
	 * StringUtils.isNotBlank(null)      = false
	 * StringUtils.isNotBlank("")        = false
	 * StringUtils.isNotBlank(" ")       = false
	 * StringUtils.isNotBlank("bob")     = true
	 * StringUtils.isNotBlank("  bob  ") = true
	 * </pre>
	 * 
	 * @param str the String to check, may be null
	 * @return <code>true</code> if the String is not empty and not null and not whitespace
	 */
	public static boolean isNotBlank(String str) {
		return !StringUtils.isBlank(str);
	}
	
	/**
	 * <p>
	 * Returns either the passed in String, or if the String is <code>null</code>, an empty String ("").
	 * </p>
	 * 
	 * <pre>
	 * StringUtils.defaultString(null)  = ""
	 * StringUtils.defaultString("")    = ""
	 * StringUtils.defaultString("bat") = "bat"
	 * </pre>
	 * 
	 * @param str the String to check, may be null
	 * @return the passed in String, or the empty String if it was <code>null</code>
	 */
	public static String defaultString(String str) {
		return defaultString(str, EMPTY);
	}
	
	public static String defaultString(String str, String defaultString) {
		return str == null ? defaultString : str;
	}
	
	public static String capitalize(String text) {
		if (isEmpty(text) || (text.length() == 1)) {
			return text.toUpperCase();
		}
		return text.substring(0, 1).toUpperCase() + text.substring(1).toLowerCase();
	}
	
	/**
	 * <p>
	 * Capitalizes all the whitespace separated words in a String. Only the first letter of each word is changed. To
	 * convert the rest of each word to lowercase at the same time, use {@link #capitalize(String)}.
	 * </p>
	 * 
	 * <p>
	 * Whitespace is defined by {@link Character#isWhitespace(char)}. A <code>null</code> input String returns
	 * <code>null</code>. Capitalization uses the unicode title case, normally equivalent to upper case.
	 * </p>
	 * 
	 * <pre>
	 * WordUtils.capitalize(null)        = null
	 * WordUtils.capitalize("")          = ""
	 * WordUtils.capitalize("i am FINE") = "I Am FINE"
	 * </pre>
	 * 
	 * @param str the String to capitalize, may be null
	 * @return capitalized String, <code>null</code> if null String input
	 */
	public static String capitalizeWords(String str) {
		return capitalizeWords(str, null);
	}
	
	/**
	 * <p>
	 * Capitalizes all the delimiter separated words in a String. Only the first letter of each word is changed.
	 * </p>
	 * 
	 * <p>
	 * The delimiters represent a set of characters understood to separate words. The first string character and the
	 * first non-delimiter character after a delimiter will be capitalized.
	 * </p>
	 * 
	 * <p>
	 * A <code>null</code> input String returns <code>null</code>. Capitalization uses the unicode title case, normally
	 * equivalent to upper case.
	 * </p>
	 * 
	 * <pre>
	 * WordUtils.capitalize(null, *)            = null
	 * WordUtils.capitalize("", *)              = ""
	 * WordUtils.capitalize(*, new char[0])     = *
	 * WordUtils.capitalize("i am fine", null)  = "I Am Fine"
	 * WordUtils.capitalize("i aM.fine", {'.'}) = "I aM.Fine"
	 * </pre>
	 * 
	 * @param str the String to capitalize, may be null
	 * @param delimiters set of characters to determine capitalization, null means whitespace
	 * @return capitalized String, <code>null</code> if null String input
	 */
	public static String capitalizeWords(String str, char[] delimiters) {
		int delimLen = (delimiters == null ? -1 : delimiters.length);
		if ((str == null) || (str.length() == 0) || (delimLen == 0)) {
			return str;
		}
		int strLen = str.length();
		StringBuilder builder = new StringBuilder(strLen);
		boolean capitalizeNext = true;
		for (int i = 0; i < strLen; i++) {
			char ch = str.charAt(i);
			
			if (isDelimiter(ch, delimiters)) {
				builder.append(ch);
				capitalizeNext = true;
			} else if (capitalizeNext) {
				builder.append(Character.toTitleCase(ch));
				capitalizeNext = false;
			} else {
				builder.append(ch);
			}
		}
		return builder.toString();
	}
	
	/**
	 * Is the character a delimiter.
	 * 
	 * @param ch the character to check
	 * @param delimiters the delimiters
	 * @return true if it is a delimiter
	 */
	private static boolean isDelimiter(char ch, char[] delimiters) {
		if (delimiters == null) {
			return Character.isWhitespace(ch);
		}
		for (char delimiter : delimiters) {
			if (ch == delimiter) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Joins all the strings in the list in a single one separated by the separator sequence.
	 * 
	 * @param objectsToJoin The objects to join.
	 * @param separator The separator sequence.
	 * @return The joined strings.
	 */
	public static String join(Collection<?> objectsToJoin, String separator) {
		if ((objectsToJoin != null) && !objectsToJoin.isEmpty()) {
			StringBuilder builder = new StringBuilder();
			for (Object object : objectsToJoin) {
				builder.append(object);
				builder.append(separator);
			}
			// Remove the last separator
			return builder.substring(0, builder.length() - separator.length());
		} else {
			return EMPTY;
		}
	}
	
	/**
	 * Joins all the strings in the list in a single one separated by ','.
	 * 
	 * @param objectsToJoin The objects to join.
	 * @return The joined strings.
	 */
	public static String join(Collection<?> objectsToJoin) {
		return join(objectsToJoin, COMMA);
	}
	
	/**
	 * Truncate the text adding "..." if the truncateWords parameter is true. The ellipsis will be taken into account
	 * when counting the amount of characters.
	 * 
	 * @param text The text to truncate
	 * @param maxCharacters The maximum amount of characters allowed for the returned text
	 * @param truncateWords True if the words should be truncated
	 * @return The truncated text
	 */
	public static String truncate(String text, Integer maxCharacters, Boolean truncateWords) {
		
		if (isNotBlank(text)) {
			StringBuilder truncatedTextBuilder = new StringBuilder();
			if (text.length() > maxCharacters) {
				if (truncateWords) {
					
					// The words are truncated and the ellipsis is added when is possible
					if (maxCharacters <= StringUtils.ELLIPSIS.length()) {
						truncatedTextBuilder.append(text.substring(0, maxCharacters));
					} else {
						truncatedTextBuilder.append(text.substring(0, maxCharacters - StringUtils.ELLIPSIS.length()));
						truncatedTextBuilder.append(StringUtils.ELLIPSIS);
					}
				} else {
					
					// The words are not truncated and the ellipsis is not added
					List<String> words = Lists.newArrayList(text.split(SPACE));
					Iterator<String> it = words.iterator();
					int usedChars = 0;
					Boolean exit = false;
					while (it.hasNext() && !exit) {
						String word = it.next();
						int increment = usedChars == 0 ? word.length() : word.length() + 1;
						if ((usedChars + increment) <= maxCharacters) {
							truncatedTextBuilder.append(usedChars == 0 ? word : SPACE + word);
							usedChars += increment;
						} else {
							exit = true;
						}
					}
				}
			} else {
				truncatedTextBuilder.append(text);
			}
			return truncatedTextBuilder.toString();
		}
		return text;
	}
	
	/**
	 * Truncate the text adding "...". The ellipsis will be taken into account when counting the amount of characters.
	 * 
	 * @param text The text to truncate
	 * @param maxCharacters The maximum amount of characters allowed for the returned text
	 * @return The truncated text
	 */
	public static String truncate(String text, Integer maxCharacters) {
		return StringUtils.truncate(text, maxCharacters, true);
	}
	
	/**
	 * Extract all the placeholder's names of the string
	 * 
	 * @param string The whole string with placeholders
	 * @return A set with all the placeholder's names
	 */
	public static Set<String> extractPlaceHolders(String string) {
		Matcher matcher = Pattern.compile(StringUtils.PLACEHOLDER_PATTERN).matcher(string);
		Set<String> placeHolders = Sets.newHashSet();
		while (matcher.find()) {
			placeHolders.add(matcher.group(1));
		}
		return placeHolders;
	}
	
	/**
	 * Transform the received value removing all the not alphanumeric or spaces characters
	 * 
	 * @param value The string to transform
	 * @return The transformed string
	 */
	public static String toAlphanumeric(String value) {
		return Pattern.compile(StringUtils.ALPHANUMERIC_PATTERN).matcher(value).replaceAll("");
	}
	
	public static Collection<String> splitToCollectionWithCommaSeparator(String text) {
		return splitToCollectionWithCommaSeparator(text, COMMA);
	}
	
	public static Collection<String> splitToCollectionWithCommaSeparator(String text, String separator) {
		Collection<String> values = Lists.newArrayList();
		if (isNotEmpty(text)) {
			values = Lists.newArrayList(text.split(separator));
		}
		return values;
	}
	
	/**
	 * Returns the first token of the string if that string can be split by the token, else return the unmodified input
	 * string
	 * 
	 * @param string The string to split
	 * @param token The token to use as splitter
	 * @return The resultant string
	 */
	public static String getFirstToken(String string, String token) {
		if ((string != null) && string.contains(token)) {
			return string.split(token)[0];
		}
		return string;
	}
	
	/**
	 * Returns the first token of the string if that string can be split by a ",", else return the unmodified input
	 * string
	 * 
	 * @param string The string to split
	 * @return The resultant string
	 */
	public static String getFirstToken(String string) {
		return getFirstToken(string, COMMA);
	}
	
	/**
	 * This method word wrap a text to two lines if the text has multiple words and its length is greater than the
	 * specified minLength. To do the word wrap, the white space closest to the middle of the string is replaced by a
	 * "\n" character
	 * 
	 * @param text A Sting, the text to word wrap to two lines.
	 * @param minLength The min text length to apply word wrap.
	 * @return The input text word wrapped to two lines or the original text.
	 */
	public static String wordWrapToTwoLines(String text, int minLength) {
		String wordWrapText = text != null ? text.trim() : null;
		if ((wordWrapText != null) && (wordWrapText.length() > minLength)) {
			int middle = wordWrapText.length() / 2;
			int leftSpaceIndex = wordWrapText.substring(0, middle).lastIndexOf(SPACE);
			int rightSpaceIndex = wordWrapText.indexOf(SPACE, middle);
			int wordWrapIndex = rightSpaceIndex;
			if ((leftSpaceIndex >= 0)
					&& ((rightSpaceIndex < 0) || ((middle - leftSpaceIndex) < (rightSpaceIndex - middle)))) {
				wordWrapIndex = leftSpaceIndex;
			}
			if (wordWrapIndex >= 0) {
				wordWrapText = wordWrapText.substring(0, wordWrapIndex) + "\n"
						+ wordWrapText.substring(wordWrapIndex + 1);
			}
		}
		
		return wordWrapText;
	}
	
	public static String replaceValues(final String template, final Map<String, String> values) {
		
		final StringBuffer sb = new StringBuffer();
		final Pattern pattern = Pattern.compile("\\$\\{(.*?)\\}", Pattern.DOTALL);
		final Matcher matcher = pattern.matcher(template);
		while (matcher.find()) {
			final String key = matcher.group(1);
			final String replacement = values.get(key);
			if (replacement == null) {
				throw new IllegalArgumentException("Template contains unmapped key: " + key);
			}
			matcher.appendReplacement(sb, replacement);
		}
		matcher.appendTail(sb);
		return sb.toString();
	}
	
	public static boolean hasOnlyCharacters(String name) {
		return !isEmpty(name) && name.matches("[A-Za-z\\s]*");
	}

	/**
	 * @param charSequence a CharSequence, the charSequence to trim.
	 * @return a CharSequence with whitespace characters removed from the end, including non-breaking spaces (&nbsp; or
	 *         &#160;) and line separators.
	 */
	public static CharSequence removeTrailingWhitespaces(CharSequence charSequence) {
		CharSequence result = charSequence;
		if ((charSequence != null) && (charSequence.length() > 0)) {
			int lastIndex = charSequence.length() - 1;
			int index = lastIndex;
			while ((index >= 0) && isAnySpaceChar(charSequence.charAt(index))) {
				index--;
			}
			if (index < lastIndex) {
				result = charSequence.subSequence(0, index + 1);
			}
		}
		return result;
	}

	/**
	 * @param character a char
	 * @return true if the character is a whitespace (including non-breaking spaces and line separators), false
	 *         otherwise.
	 */
	private static boolean isAnySpaceChar(char character) {
		return Character.isSpaceChar(character) || Character.isWhitespace(character);
	}
}
