package com.jdroid.java.utils;

import com.jdroid.java.exception.UnexpectedException;

import java.io.UnsupportedEncodingException;

public class EncodingUtils {
	
	public static final String UTF8 = "UTF-8";
	private static final char[] HEX_DIGITS = "0123456789ABCDEF".toCharArray();
	/** Index of a component which was not found. */
	private static final int NOT_FOUND = -1;
	
	/**
	 * Encodes characters in the given string as '%'-escaped octets using the UTF-8 scheme. Leaves letters ("A-Z",
	 * "a-z"), numbers ("0-9"), and unreserved characters ("_-!.~'()*") intact.
	 * 
	 * @param s string to encode
	 * @return an encoded version of s suitable for use as a URI component, or null if s is null
	 */
	public static String encodeURL(String s) {
		if (s == null) {
			return null;
		}
		
		// Lazily-initialized buffers.
		StringBuilder encoded = null;
		
		int oldLength = s.length();
		
		// This loop alternates between copying over allowed characters and
		// encoding in chunks. This results in fewer method calls and
		// allocations than encoding one character at a time.
		int current = 0;
		while (current < oldLength) {
			// Start in "copying" mode where we copy over allowed chars.
			
			// Find the next character which needs to be encoded.
			int nextToEncode = current;
			while ((nextToEncode < oldLength) && isAllowed(s.charAt(nextToEncode), null)) {
				nextToEncode++;
			}
			
			// If there's nothing more to encode...
			if (nextToEncode == oldLength) {
				if (current == 0) {
					// We didn't need to encode anything!
					return s;
				} else {
					// Presumably, we've already done some encoding.
					encoded.append(s, current, oldLength);
					return encoded.toString();
				}
			}
			
			if (encoded == null) {
				encoded = new StringBuilder();
			}
			
			if (nextToEncode > current) {
				// Append allowed characters leading up to this point.
				encoded.append(s, current, nextToEncode);
			}
			
			// Switch to "encoding" mode.
			
			// Find the next allowed character.
			current = nextToEncode;
			int nextAllowed = current + 1;
			while ((nextAllowed < oldLength) && !isAllowed(s.charAt(nextAllowed), null)) {
				nextAllowed++;
			}
			
			// Convert the substring to bytes and encode the bytes as
			// '%'-escaped octets.
			String toEncode = s.substring(current, nextAllowed);
			try {
				byte[] bytes = toEncode.getBytes(UTF8);
				for (byte aByte : bytes) {
					encoded.append('%');
					encoded.append(HEX_DIGITS[(aByte & 0xf0) >> 4]);
					encoded.append(HEX_DIGITS[aByte & 0xf]);
				}
			} catch (UnsupportedEncodingException e) {
				throw new UnexpectedException(e);
			}
			
			current = nextAllowed;
		}
		
		// Encoded could still be null at this point if s is empty.
		return encoded == null ? s : encoded.toString();
	}
	
	/**
	 * Returns true if the given character is allowed.
	 * 
	 * @param c character to check
	 * @param allow characters to allow
	 * @return true if the character is allowed or false if it should be encoded
	 */
	private static boolean isAllowed(char c, String allow) {
		return ((c >= 'A') && (c <= 'Z')) || ((c >= 'a') && (c <= 'z')) || ((c >= '0') && (c <= '9'))
				|| ("_-!.~'()*/".indexOf(c) != NOT_FOUND) || ((allow != null) && (allow.indexOf(c) != NOT_FOUND));
	}

	/**
	 * Decode a UTF-8 encoded String.
	 *
	 * @param url
	 * @return decoded String.
	 */
	public static String decodeURL(String url) {
		String decodedString = null;
		try {
			decodedString = java.net.URLDecoder.decode(url, UTF8);
		} catch (UnsupportedEncodingException e) {
			throw new UnexpectedException(e);
		}
		return decodedString;
	}
}
