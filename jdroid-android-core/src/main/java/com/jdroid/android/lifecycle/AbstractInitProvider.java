package com.jdroid.android.lifecycle;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

abstract class AbstractInitProvider extends ContentProvider {

	protected abstract void init();

	@Override
	public final boolean onCreate() {
		init();
		return false;
	}

	@Nullable
	@Override
	public final Cursor query(@NonNull Uri uri, String[] strings, String s, String[] strings1, String s1) {
		return null;
	}

	@Nullable
	@Override
	public final String getType(@NonNull Uri uri) {
		return null;
	}

	@Nullable
	@Override
	public final Uri insert(@NonNull Uri uri, ContentValues contentValues) {
		return null;
	}

	@Override
	public final int delete(@NonNull Uri uri, String s, String[] strings) {
		return 0;
	}

	@Override
	public final int update(@NonNull Uri uri, ContentValues contentValues, String s, String[] strings) {
		return 0;
	}
}
