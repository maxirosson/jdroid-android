package com.jdroid.android.about;

import android.app.Activity;

import com.jdroid.android.utils.ExternalAppsUtils;

public class Library {
	
	private String libraryKey;
	private String name;
	private String author;
	private String url;
	
	public Library(String libraryKey, String name, String author, String url) {
		this.libraryKey = libraryKey;
		this.name = name;
		this.author = author;
		this.url = url;
	}
	
	public String getName() {
		return name;
	}
	
	public String getUrl() {
		return url;
	}
	
	public void onSelected(Activity activity) {
		ExternalAppsUtils.openUrl(url);
	}
	
	public String getLibraryKey() {
		return libraryKey;
	}

	public String getAuthor() {
		return author;
	}
}
