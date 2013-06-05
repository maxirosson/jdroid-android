package com.jdroid.java.parser.json;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.jdroid.java.collections.Lists;
import com.jdroid.java.utils.DateUtils;

/**
 * {@link JSONObject} Wrapper
 * 
 * @author Maxi Rosson
 */
public class JsonObjectWrapper {
	
	private JSONObject jsonObject;
	
	public JsonObjectWrapper() {
		this(new JSONObject());
	}
	
	public JsonObjectWrapper(String string) throws JSONException {
		this(new JSONObject(string));
	}
	
	public JsonObjectWrapper(JSONObject jsonObject) {
		this.jsonObject = jsonObject;
	}
	
	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object o) {
		return jsonObject.equals(o);
	}
	
	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return jsonObject.hashCode();
	}
	
	/**
	 * @param key
	 * @param value
	 * @return the {@link JsonObjectWrapper}
	 * @throws JSONException
	 * @see org.json.JSONObject#accumulate(java.lang.String, java.lang.Object)
	 */
	public JsonObjectWrapper accumulate(String key, Object value) throws JSONException {
		return new JsonObjectWrapper(jsonObject.accumulate(key, value));
	}
	
	/**
	 * @param key
	 * @return the {@link Object}
	 * @throws JSONException
	 * @see org.json.JSONObject#get(java.lang.String)
	 */
	public Object get(String key) throws JSONException {
		return jsonObject.get(key);
	}
	
	/**
	 * @param key
	 * @return the boolean
	 * @throws JSONException
	 * @see org.json.JSONObject#getBoolean(java.lang.String)
	 */
	public boolean getBoolean(String key) throws JSONException {
		return jsonObject.getBoolean(key);
	}
	
	/**
	 * @param key
	 * @return the double
	 * @throws JSONException
	 * @see org.json.JSONObject#getDouble(java.lang.String)
	 */
	public double getDouble(String key) throws JSONException {
		return jsonObject.getDouble(key);
	}
	
	public float getFloat(String key) throws JSONException {
		return (float)jsonObject.getDouble(key);
	}
	
	/**
	 * @param key
	 * @return the int
	 * @throws JSONException
	 * @see org.json.JSONObject#getInt(java.lang.String)
	 */
	public int getInt(String key) throws JSONException {
		return jsonObject.getInt(key);
	}
	
	/**
	 * @param key
	 * @return the {@link JsonArrayWrapper}
	 * @throws JSONException
	 * @see org.json.JSONObject#getJSONArray(java.lang.String)
	 */
	public JsonArrayWrapper getJSONArray(String key) throws JSONException {
		return new JsonArrayWrapper(jsonObject.getJSONArray(key));
	}
	
	/**
	 * @param key
	 * @return the {@link JsonObjectWrapper}
	 * @throws JSONException
	 * @see org.json.JSONObject#getJSONObject(java.lang.String)
	 */
	public JsonObjectWrapper getJSONObject(String key) throws JSONException {
		return new JsonObjectWrapper(jsonObject.getJSONObject(key));
	}
	
	/**
	 * @param key
	 * @return the long
	 * @throws JSONException
	 * @see org.json.JSONObject#getLong(java.lang.String)
	 */
	public long getLong(String key) throws JSONException {
		return jsonObject.getLong(key);
	}
	
	/**
	 * @param key
	 * @return the {@link String}
	 * @throws JSONException
	 * @see org.json.JSONObject#getString(java.lang.String)
	 */
	public String getString(String key) throws JSONException {
		String value = null;
		if (!jsonObject.isNull(key)) {
			value = jsonObject.getString(key);
		}
		return value;
	}
	
	public Date getDate(String key) throws JSONException {
		return getDate(key, DateUtils.YYYYMMDDHHMMSSZ_DATE_FORMAT);
	}
	
	public Date getDate(String key, String dateFormat) throws JSONException {
		Date date = null;
		if (!jsonObject.isNull(key)) {
			String value = jsonObject.getString(key);
			date = DateUtils.parse(value, dateFormat);
		}
		return date;
	}
	
	public Date optDate(String key) throws JSONException {
		return optDate(key, DateUtils.YYYYMMDDHHMMSSZ_DATE_FORMAT);
	}
	
	public Date optDate(String key, String dateFormat) throws JSONException {
		return optDate(key, dateFormat, null);
	}
	
	public Date optDate(String key, String dateFormat, Date defaultDate) throws JSONException {
		if (has(key) && !jsonObject.isNull(key)) {
			String value = jsonObject.getString(key);
			return DateUtils.parse(value, dateFormat);
		}
		return defaultDate;
	}
	
	/**
	 * @param key
	 * @return the boolean
	 * @see org.json.JSONObject#has(java.lang.String)
	 */
	public boolean has(String key) {
		return jsonObject.has(key);
	}
	
	/**
	 * @param key
	 * @return the boolean
	 * @see org.json.JSONObject#isNull(java.lang.String)
	 */
	public boolean isNull(String key) {
		return jsonObject.isNull(key);
	}
	
	/**
	 * @return the {@link Iterator}
	 * @see org.json.JSONObject#keys()
	 */
	public Iterator<?> keys() {
		return jsonObject.keys();
	}
	
	/**
	 * @return the int
	 * @see org.json.JSONObject#length()
	 */
	public int length() {
		return jsonObject.length();
	}
	
	/**
	 * @return the {@link JsonArrayWrapper}
	 * @see org.json.JSONObject#names()
	 */
	public JsonArrayWrapper names() {
		return new JsonArrayWrapper(jsonObject.names());
	}
	
	/**
	 * @param key
	 * @return the {@link Object}
	 * @throws JSONException
	 * @see org.json.JSONObject#opt(java.lang.String)
	 */
	public Object opt(String key) throws JSONException {
		if (has(key) && !jsonObject.isNull(key)) {
			return jsonObject.get(key);
		}
		return null;
	}
	
	/**
	 * @param key
	 * @return the boolean
	 * @throws JSONException
	 * @see org.json.JSONObject#optBoolean(java.lang.String)
	 */
	public Boolean optBoolean(String key) throws JSONException {
		return optBoolean(key, null);
	}
	
	/**
	 * @param key
	 * @param defaultValue
	 * @return the boolean
	 * @throws JSONException
	 * @see org.json.JSONObject#optBoolean(java.lang.String, boolean)
	 */
	public Boolean optBoolean(String key, Boolean defaultValue) throws JSONException {
		if (has(key) && !jsonObject.isNull(key)) {
			return jsonObject.getBoolean(key);
		}
		return defaultValue;
	}
	
	/**
	 * @param key
	 * @return the double
	 * @throws JSONException
	 * @see org.json.JSONObject#optDouble(java.lang.String)
	 */
	public Double optDouble(String key) throws JSONException {
		return optDouble(key, null);
	}
	
	/**
	 * @param key
	 * @param defaultValue
	 * @return the double
	 * @throws JSONException
	 * @see org.json.JSONObject#optDouble(java.lang.String, double)
	 */
	public Double optDouble(String key, Double defaultValue) throws JSONException {
		if (has(key) && !jsonObject.isNull(key)) {
			return jsonObject.getDouble(key);
		}
		return defaultValue;
	}
	
	/**
	 * @param key
	 * @return the float
	 * @throws JSONException
	 * @see org.json.JSONObject#optDouble(java.lang.String)
	 */
	public Float optFloat(String key) throws JSONException {
		return optFloat(key, null);
	}
	
	/**
	 * @param key
	 * @param defaultValue
	 * @return the float
	 * @throws JSONException
	 * @see org.json.JSONObject#optDouble(java.lang.String, double)
	 */
	public Float optFloat(String key, Float defaultValue) throws JSONException {
		if (has(key) && !jsonObject.isNull(key)) {
			return (float)jsonObject.getDouble(key);
		}
		return defaultValue;
	}
	
	/**
	 * @param key
	 * @return the int
	 * @throws JSONException
	 * @see org.json.JSONObject#optInt(java.lang.String)
	 */
	public Integer optInt(String key) throws JSONException {
		return optInt(key, null);
	}
	
	/**
	 * @param key
	 * @param defaultValue
	 * @return the int
	 * @throws JSONException
	 * @see org.json.JSONObject#optInt(java.lang.String, int)
	 */
	public Integer optInt(String key, Integer defaultValue) throws JSONException {
		if (has(key) && !jsonObject.isNull(key)) {
			return jsonObject.getInt(key);
		}
		return defaultValue;
	}
	
	/**
	 * @param key
	 * @return the {@link JsonArrayWrapper}
	 * @see org.json.JSONObject#optJSONArray(java.lang.String)
	 */
	public JsonArrayWrapper optJSONArray(String key) {
		JSONArray optJsonArray = jsonObject.optJSONArray(key);
		return optJsonArray != null ? new JsonArrayWrapper(optJsonArray) : null;
	}
	
	public List<String> optList(String key) throws JSONException {
		JsonArrayWrapper jsonArray = optJSONArray(key);
		List<String> list = null;
		if (jsonArray != null) {
			int length = jsonArray.length();
			list = Lists.newArrayList();
			for (int i = 0; i < length; i++) {
				list.add(jsonArray.getString(i));
			}
		}
		return list;
	}
	
	/**
	 * @param key
	 * @return the {@link JsonObjectWrapper}
	 * @see org.json.JSONObject#optJSONObject(java.lang.String)
	 */
	public JsonObjectWrapper optJSONObject(String key) {
		JSONObject optJsonObject = jsonObject.optJSONObject(key);
		return optJsonObject != null ? new JsonObjectWrapper(optJsonObject) : null;
	}
	
	/**
	 * @param key
	 * @return the long
	 * @throws JSONException
	 * @see org.json.JSONObject#optLong(java.lang.String)
	 */
	public Long optLong(String key) throws JSONException {
		return optLong(key, null);
	}
	
	/**
	 * @param key
	 * @param defaultValue
	 * @return the long
	 * @throws JSONException
	 * @see org.json.JSONObject#optLong(java.lang.String, long)
	 */
	public Long optLong(String key, Long defaultValue) throws JSONException {
		if (has(key) && !jsonObject.isNull(key)) {
			return jsonObject.getLong(key);
		}
		return defaultValue;
	}
	
	/**
	 * @param key
	 * @return the {@link String}
	 * @throws JSONException
	 * @see org.json.JSONObject#optString(java.lang.String)
	 */
	public String optString(String key) throws JSONException {
		return optString(key, null);
	}
	
	/**
	 * @param key
	 * @param defaultValue
	 * @return the {@link String}
	 * @throws JSONException
	 * @see org.json.JSONObject#optString(java.lang.String, java.lang.String)
	 */
	public String optString(String key, String defaultValue) throws JSONException {
		if (has(key) && !jsonObject.isNull(key)) {
			return jsonObject.getString(key);
		}
		return defaultValue;
	}
	
	/**
	 * @param key
	 * @param value
	 * @return the {@link JsonObjectWrapper}
	 * @throws JSONException
	 * @see org.json.JSONObject#put(java.lang.String, boolean)
	 */
	public JsonObjectWrapper put(String key, boolean value) throws JSONException {
		return new JsonObjectWrapper(jsonObject.put(key, value));
	}
	
	/**
	 * @param key
	 * @param value
	 * @return the {@link JsonObjectWrapper}
	 * @throws JSONException
	 * @see org.json.JSONObject#put(java.lang.String, double)
	 */
	public JsonObjectWrapper put(String key, double value) throws JSONException {
		return new JsonObjectWrapper(jsonObject.put(key, value));
	}
	
	/**
	 * @param key
	 * @param value
	 * @return the {@link JsonObjectWrapper}
	 * @throws JSONException
	 * @see org.json.JSONObject#put(java.lang.String, int)
	 */
	public JsonObjectWrapper put(String key, int value) throws JSONException {
		return new JsonObjectWrapper(jsonObject.put(key, value));
	}
	
	/**
	 * @param key
	 * @param value
	 * @return the {@link JsonObjectWrapper}
	 * @throws JSONException
	 * @see org.json.JSONObject#put(java.lang.String, long)
	 */
	public JsonObjectWrapper put(String key, long value) throws JSONException {
		return new JsonObjectWrapper(jsonObject.put(key, value));
	}
	
	/**
	 * @param key
	 * @param value
	 * @return the {@link JsonObjectWrapper}
	 * @throws JSONException
	 * @see org.json.JSONObject#put(java.lang.String, java.lang.Object)
	 */
	public JsonObjectWrapper put(String key, Object value) throws JSONException {
		return new JsonObjectWrapper(jsonObject.put(key, value));
	}
	
	/**
	 * @param key
	 * @param value
	 * @return the {@link JsonObjectWrapper}
	 * @throws JSONException
	 * @see org.json.JSONObject#putOpt(java.lang.String, java.lang.Object)
	 */
	public JsonObjectWrapper putOpt(String key, Object value) throws JSONException {
		return new JsonObjectWrapper(jsonObject.putOpt(key, value));
	}
	
	/**
	 * @param key
	 * @return the {@link Object}
	 * @see org.json.JSONObject#remove(java.lang.String)
	 */
	public Object remove(String key) {
		return jsonObject.remove(key);
	}
	
	/**
	 * @param names
	 * @return the {@link JsonArrayWrapper}
	 * @throws JSONException
	 * @see org.json.JSONObject#toJSONArray(org.json.JSONArray)
	 */
	public JsonArrayWrapper toJSONArray(JSONArray names) throws JSONException {
		return new JsonArrayWrapper(jsonObject.toJSONArray(names));
	}
	
	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return jsonObject.toString();
	}
	
	/**
	 * @param indentFactor
	 * @return the {@link String}
	 * @throws JSONException
	 * @see org.json.JSONObject#toString(int)
	 */
	public String toString(int indentFactor) throws JSONException {
		return jsonObject.toString(indentFactor);
	}
	
}
