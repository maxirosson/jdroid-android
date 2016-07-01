package com.jdroid.java.utils;

public class IdGenerator {

	private IdGenerator(){}
	
	private static Integer ID = 10000;
	
	public static synchronized Long getLongId() {
		ID++;
		return ID.longValue();
	}
	
	public static synchronized Integer getIntId() {
		return ID++;
	}
	

}
