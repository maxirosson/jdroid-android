package com.jdroid.android.utils;

import android.content.SharedPreferences;
import android.util.Base64;

import com.jdroid.java.exception.UnexpectedException;

import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class AndroidEncryptionUtils {

	private AndroidEncryptionUtils(){}
	
	private static final String BASE64_KEY = "base64Key";
	
	private static final String TRANSFORMATION = "AES/ECB/PKCS5Padding";
	private static final String ALGORITHM = "AES";
	private static final String SHA_ALGORITHM = "SHA-1";
	private static final String UTF_8 = "UTF-8";
	
	private static String base64Key;
	
	/**
	 * Returns the data encrypted. Avoid calling this method on the UI thread if possible, since it may access to shared
	 * preferences.
	 * 
	 * @param cleartext
	 * @return encrypted data
	 */
	public static String encrypt(String cleartext) {
		if (cleartext != null) {
			byte[] result = doFinal(Base64.decode(getBase64Key(), Base64.DEFAULT), Cipher.ENCRYPT_MODE,
				cleartext.getBytes());
			return Base64.encodeToString(result, Base64.DEFAULT);
		}
		return null;
	}
	
	/**
	 * Returns the original data. Avoid calling this method on the UI thread if possible, since it may access to shared
	 * preferences.
	 * 
	 * @param base64Encrypted
	 * @return the original data
	 */
	public static String decrypt(String base64Encrypted) {
		if (base64Encrypted != null) {
			byte[] enc = Base64.decode(base64Encrypted, Base64.DEFAULT);
			byte[] result = doFinal(Base64.decode(getBase64Key(), Base64.DEFAULT), Cipher.DECRYPT_MODE, enc);
			return new String(result);
		}
		return null;
	}
	
	private static byte[] doFinal(byte[] raw, int opMode, byte[] input) {
		try {
			Cipher cipher = Cipher.getInstance(TRANSFORMATION);
			SecretKeySpec skeySpec = new SecretKeySpec(raw, ALGORITHM);
			cipher.init(opMode, skeySpec);
			return cipher.doFinal(input);
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | BadPaddingException | IllegalBlockSizeException e) {
			throw new UnexpectedException(e);
		} catch (InvalidKeyException e) {
			throw new UnexpectedException(e);
		}
	}

	/**
	 * Returns the encryption key stored on {@link SharedPreferences}. If the key is already on memory, it doesn't access the file system.
	 * You shouldn't call this method in the UI thread.
	 */
	private static String getBase64Key() {
		if (base64Key == null) {
			base64Key = SharedPreferencesHelper.get().loadPreference(BASE64_KEY);
			if (base64Key == null) {
				base64Key = generateBase64Key();
				SharedPreferencesHelper.get().savePreference(BASE64_KEY, base64Key);
			}
		}
		return base64Key;
	}
	
	private static String generateBase64Key() {
		final int outputKeyLength = 128;
		try {
			SecureRandom secureRandom = new SecureRandom();
			KeyGenerator keyGenerator;
			keyGenerator = KeyGenerator.getInstance(ALGORITHM);
			keyGenerator.init(outputKeyLength, secureRandom);
			SecretKey key = keyGenerator.generateKey();
			return Base64.encodeToString(key.getEncoded(), Base64.DEFAULT);
		} catch (NoSuchAlgorithmException e) {
			throw new UnexpectedException(e);
		}
	}
	
	private static String toHexEncode(byte[] bytes) {
		StringBuilder sb = new StringBuilder();
		for (byte aByte : bytes) {
			int low = aByte & 0xF;
			int high = (aByte >>> 4) & 0xF;
			sb.append(Character.forDigit(high, 16));
			sb.append(Character.forDigit(low, 16));
		}
		return sb.toString();
	}
	
	/**
	 * Generates the SHA hash for the input string.
	 * 
	 * @param text the input string to hash
	 * @return the hash for the input string in hexadecimal encoding
	 */
	public static String generateShaHash(String text) {
		try {
			MessageDigest digest = MessageDigest.getInstance(SHA_ALGORITHM);
			byte[] bytes = text.getBytes(UTF_8);
			bytes = digest.digest(bytes);
			return toHexEncode(bytes);
		} catch (Exception e) {
			throw new UnexpectedException(e);
		}
	}
}
