package com.jdroid.java.utils;

import java.util.Random;

public class RandomUtils {

	private RandomUtils(){}

	private static final Random RANDOM = new Random();

	public static Long getLong() {
		return Math.abs(RANDOM.nextLong());
	}

	public static Integer getInt() {
		return Math.abs(RANDOM.nextInt());
	}

	public static Integer get16BitsInt() {
		return Math.abs(RANDOM.nextInt(16));
	}

	public static Integer getInt(int bound) {
		return Math.abs(RANDOM.nextInt(bound));
	}
}
