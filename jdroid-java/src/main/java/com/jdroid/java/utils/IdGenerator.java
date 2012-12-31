package com.jdroid.java.utils;

import java.util.Random;

/**
 * 
 * @author Maxi Rosson
 */
public class IdGenerator {
	
	private static int ID = 10000;
	
	public static synchronized long getLongId() {
		return ID++;
	}
	
	public static synchronized int getIntId() {
		return ID++;
	}
	
	public static long getRandomLongId() {
		return Math.abs(new Random().nextLong());
	}
	
	public static int getRandomIntId() {
		return Math.abs(new Random().nextInt());
	}
}
