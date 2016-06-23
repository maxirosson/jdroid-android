package com.jdroid.android.sqlite;

import android.content.ContentValues;
import android.database.Cursor;

import com.jdroid.android.utils.AndroidEncryptionUtils;
import com.jdroid.java.collections.Lists;
import com.jdroid.java.json.JSONObject;
import com.jdroid.java.http.parser.json.JsonParser;
import com.jdroid.java.date.DateTimeFormat;
import com.jdroid.java.date.DateUtils;
import com.jdroid.java.utils.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;

public enum DataType {
	TEXT("TEXT") {
		
		@Override
		public <T> void writeValue(ContentValues values, String columnName, T value) {
			if (value != null) {
				values.put(columnName, (String)value);
			} else {
				values.putNull(columnName);
			}
		}
		
		@SuppressWarnings("unchecked")
		@Override
		public String readValue(Cursor cursor, String columnName) {
			int columnIndex = cursor.getColumnIndex(columnName);
			if (cursor.isNull(columnIndex)) {
				return null;
			}
			return cursor.getString(columnIndex);
		}
	},
	INTEGER("INTEGER") {
		
		@Override
		public <T> void writeValue(ContentValues values, String columnName, T value) {
			if (value != null) {
				values.put(columnName, ((Number)value).intValue());
			} else {
				values.putNull(columnName);
			}
		}
		
		@SuppressWarnings("unchecked")
		@Override
		public Integer readValue(Cursor cursor, String columnName) {
			int columnIndex = cursor.getColumnIndex(columnName);
			if (cursor.isNull(columnIndex)) {
				return null;
			}
			return cursor.getInt(columnIndex);
		}
	},
	LONG("INTEGER") {
		
		@Override
		public <T> void writeValue(ContentValues values, String columnName, T value) {
			if (value != null) {
				values.put(columnName, ((Number) value).longValue());
			} else {
				values.putNull(columnName);
			}
		}
		
		@SuppressWarnings("unchecked")
		@Override
		public Long readValue(Cursor cursor, String columnName) {
			int columnIndex = cursor.getColumnIndex(columnName);
			if (cursor.isNull(columnIndex)) {
				return null;
			}
			return cursor.getLong(columnIndex);
		}
	},
	REAL("REAL") {
		
		@Override
		public <T> void writeValue(ContentValues values, String columnName, T value) {
			if (value != null) {
				values.put(columnName, ((Number) value).doubleValue());
			} else {
				values.putNull(columnName);
			}
		}
		
		@SuppressWarnings("unchecked")
		@Override
		public Double readValue(Cursor cursor, String columnName) {
			int columnIndex = cursor.getColumnIndex(columnName);
			if (cursor.isNull(columnIndex)) {
				return null;
			}
			return cursor.getDouble(columnIndex);
		}
	},
	FLOAT("REAL") {
		
		@Override
		public <T> void writeValue(ContentValues values, String columnName, T value) {
			if (value != null) {
				values.put(columnName, ((Number) value).floatValue());
			} else {
				values.putNull(columnName);
			}
		}
		
		@SuppressWarnings("unchecked")
		@Override
		public Float readValue(Cursor cursor, String columnName) {
			int columnIndex = cursor.getColumnIndex(columnName);
			if (cursor.isNull(columnIndex)) {
				return null;
			}
			return cursor.getFloat(columnIndex);
		}
	},
	BLOB("BLOB") {
		
		@Override
		public <T> void writeValue(ContentValues values, String columnName, T value) {
			if (value != null) {
				values.put(columnName, (byte[])value);
			} else {
				values.putNull(columnName);
			}
		}
		
		@SuppressWarnings("unchecked")
		@Override
		public byte[] readValue(Cursor cursor, String columnName) {
			int columnIndex = cursor.getColumnIndex(columnName);
			if (cursor.isNull(columnIndex)) {
				return null;
			}
			return cursor.getBlob(columnIndex);
		}
	},
	BOOLEAN("INTEGER") {
		
		@Override
		public <T> void writeValue(ContentValues values, String columnName, T value) {
			if (value != null) {
				values.put(columnName, ((Boolean) value) ? 1 : 0);
			} else {
				values.putNull(columnName);
			}
		}
		
		@SuppressWarnings("unchecked")
		@Override
		public Boolean readValue(Cursor cursor, String columnName) {
			int columnIndex = cursor.getColumnIndex(columnName);
			if (cursor.isNull(columnIndex)) {
				return null;
			}
			return cursor.getInt(columnIndex) == 0 ? Boolean.FALSE : Boolean.TRUE;
		}
	},
	DATE("TEXT") {
		
		@Override
		public <T> void writeValue(ContentValues values, String columnName, T value) {
			if (value != null) {
				values.put(columnName, DateUtils.format((Date)value, DateTimeFormat.YYYYMMDDHHMMSS));
			} else {
				values.putNull(columnName);
			}
		}
		
		@SuppressWarnings("unchecked")
		@Override
		public Date readValue(Cursor cursor, String columnName) {
			int columnIndex = cursor.getColumnIndex(columnName);
			if (cursor.isNull(columnIndex)) {
				return null;
			}
			String date = cursor.getString(columnIndex);
			return DateUtils.parse(date, DateTimeFormat.YYYYMMDDHHMMSS);
		}
		
	},
	DATE_MILLISECONDS("TEXT") {
		
		@Override
		public <T> void writeValue(ContentValues values, String columnName, T value) {
			if (value != null) {
				values.put(columnName, DateUtils.format((Date)value, DateTimeFormat.YYYYMMDDHHMMSSSSS));
			} else {
				values.putNull(columnName);
			}
		}
		
		@SuppressWarnings("unchecked")
		@Override
		public Date readValue(Cursor cursor, String columnName) {
			int columnIndex = cursor.getColumnIndex(columnName);
			if (cursor.isNull(columnIndex)) {
				return null;
			}
			String date = cursor.getString(columnIndex);
			return DateUtils.parse(date, DateTimeFormat.YYYYMMDDHHMMSSSSS);
		}
		
	},
	DATE_TZ("TEXT") {
		
		@Override
		public <T> void writeValue(ContentValues values, String columnName, T value) {
			if (value != null) {
				values.put(columnName, DateUtils.format((Date)value, DateTimeFormat.YYYYMMDDHHMMSSZ));
			} else {
				values.putNull(columnName);
			}
		}
		
		@SuppressWarnings("unchecked")
		@Override
		public Date readValue(Cursor cursor, String columnName) {
			int columnIndex = cursor.getColumnIndex(columnName);
			if (cursor.isNull(columnIndex)) {
				return null;
			}
			String date = cursor.getString(columnIndex);
			return DateUtils.parse(date, DateTimeFormat.YYYYMMDDHHMMSSZ);
		}
		
	},
	ENCRYPTED_TEXT("TEXT") {
		
		@Override
		public <T> void writeValue(ContentValues values, String columnName, T value) {
			if (value != null) {
				values.put(columnName, AndroidEncryptionUtils.encrypt((String)value));
			} else {
				values.putNull(columnName);
			}
		}
		
		@SuppressWarnings("unchecked")
		@Override
		public String readValue(Cursor cursor, String columnName) {
			int columnIndex = cursor.getColumnIndex(columnName);
			if (cursor.isNull(columnIndex)) {
				return null;
			}
			return AndroidEncryptionUtils.decrypt(cursor.getString(columnIndex));
		}
	},
	CSV_TEXT("TEXT") {
		
		@Override
		public <T> void writeValue(ContentValues values, String columnName, T value) {
			if (value != null) {
				values.put(columnName, StringUtils.join((List<?>)value));
			} else {
				values.putNull(columnName);
			}
		}
		
		@SuppressWarnings("unchecked")
		@Override
		public List<String> readValue(Cursor cursor, String columnName) {
			int columnIndex = cursor.getColumnIndex(columnName);
			if (cursor.isNull(columnIndex)) {
				return null;
			}
			return Lists.newArrayList(StringUtils.splitToCollectionWithCommaSeparator(cursor.getString(columnIndex)));
		}
	},
	MAP("TEXT") {
		
		@Override
		public <T> void writeValue(ContentValues values, String columnName, T value) {
			if (value != null) {
				values.put(columnName, new JSONObject((Map<?, ?>)value).toString());
			} else {
				values.putNull(columnName);
			}
		}
		
		@SuppressWarnings("unchecked")
		@Override
		public JSONObject readValue(Cursor cursor, String columnName) {
			int columnIndex = cursor.getColumnIndex(columnName);
			if (cursor.isNull(columnIndex)) {
				return null;
			}
			String value = cursor.getString(columnIndex);
			JsonParser<JSONObject> parser = new JsonParser<JSONObject>() {
				
				@Override
				public Object parse(JSONObject json) {
					return json;
				}
			};
			
			return (JSONObject)parser.parse(value);
		}
	};
	
	private String type;
	
	DataType(String type) {
		this.type = type;
	}
	
	public String getType() {
		return type;
	}
	
	public abstract <T> void writeValue(ContentValues values, String columnName, T value);
	
	public abstract <T> T readValue(Cursor cursor, String columnName);
}
