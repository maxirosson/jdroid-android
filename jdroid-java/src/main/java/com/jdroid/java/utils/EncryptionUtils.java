package com.jdroid.java.utils;

import com.jdroid.java.exception.UnexpectedException;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class EncryptionUtils {

	private EncryptionUtils(){}
	
	private static final String AES = "AES";
	
	public static String encrypt(String seed, String cleartext) {
		if (cleartext != null) {
			byte[] rawKey = getRawKey(seed.getBytes());
			byte[] result = doFinal(rawKey, Cipher.ENCRYPT_MODE, cleartext.getBytes());
			return toHex(result);
		}
		return null;
	}
	
	public static String decrypt(String seed, String encrypted) {
		if (encrypted != null) {
			byte[] rawKey = getRawKey(seed.getBytes());
			byte[] enc = toBytes(encrypted);
			byte[] result = doFinal(rawKey, Cipher.DECRYPT_MODE, enc);
			return new String(result);
		}
		return null;
	}
	
	private static byte[] getRawKey(byte[] seed) {
		try {
			SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
			random.setSeed(seed);
			
			KeyGenerator kgen = KeyGenerator.getInstance(AES);
			kgen.init(128, random); // 192 and 256 bits may not be available
			SecretKey skey = kgen.generateKey();
			return skey.getEncoded();
		} catch (NoSuchAlgorithmException e) {
			throw new UnexpectedException(e);
		}
	}
	
	private static byte[] doFinal(byte[] raw, int opMode, byte[] input) {
		try {
			Cipher cipher = Cipher.getInstance(AES);
			SecretKeySpec skeySpec = new SecretKeySpec(raw, AES);
			cipher.init(opMode, skeySpec);
			return cipher.doFinal(input);
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException e) {
			throw new UnexpectedException(e);
		} catch (InvalidKeyException e) {
			throw new UnexpectedException(e);
		} catch (BadPaddingException e) {
			throw new UnexpectedException(e);
		}
	}
	
	public static byte[] toBytes(String hexString) {
		int len = hexString.length() / 2;
		byte[] result = new byte[len];
		for (int i = 0; i < len; i++) {
			result[i] = Integer.valueOf(hexString.substring(2 * i, (2 * i) + 2), 16).byteValue();
		}
		return result;
	}
	
	public static String toHex(byte[] bytes) {
		StringBuilder hexBuilder = new StringBuilder();
		for (byte aByte : bytes) {
			hexBuilder.append(Character.forDigit((aByte >>> 4) & 0xf, 16));
			hexBuilder.append(Character.forDigit(aByte & 0xf, 16));
		}
		return hexBuilder.toString();
	}
}
