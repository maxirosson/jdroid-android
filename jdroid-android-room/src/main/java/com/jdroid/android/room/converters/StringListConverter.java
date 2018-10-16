package com.jdroid.android.room.converters;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import androidx.room.TypeConverter;

public class StringListConverter {

	@TypeConverter
	public static List<String> fromString(String value) {
		Type listType = new TypeToken<List<String>>() {}.getType();
		return new Gson().fromJson(value, listType);
	}

	@TypeConverter
	public static String fromList(List<String> list) {
		Gson gson = new Gson();
		return gson.toJson(list);
	}

}
