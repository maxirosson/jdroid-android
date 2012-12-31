package com.jdroid.java.parser.json;

import java.io.InputStream;
import java.util.List;
import org.json.JSONException;
import org.slf4j.Logger;
import com.jdroid.java.collections.Lists;
import com.jdroid.java.exception.UnexpectedException;
import com.jdroid.java.parser.Parser;
import com.jdroid.java.utils.FileUtils;
import com.jdroid.java.utils.LoggerUtils;
import com.jdroid.java.utils.StringUtils;

/**
 * JSON input streams parser
 * 
 * @param <T>
 */
public abstract class JsonParser<T> implements Parser {
	
	private static final Logger LOGGER = LoggerUtils.getLogger(JsonParser.class);
	private static final String ARRAY_PREFIX = "[";
	
	/**
	 * @see com.jdroid.java.parser.Parser#parse(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Object parse(String input) {
		
		LOGGER.debug("Parsing started.");
		try {
			LOGGER.debug(input);
			
			// Create a wrapped JsonObjectWrapper or JsonArrayWrapper
			T json = null;
			if (input.startsWith(ARRAY_PREFIX)) {
				json = (T)new JsonArrayWrapper(input);
			} else {
				json = (T)new JsonObjectWrapper(input);
			}
			
			// Parse the JSONObject
			return parse(json);
		} catch (JSONException e) {
			throw new UnexpectedException(e);
		} finally {
			LOGGER.debug("Parsing finished.");
		}
	}
	
	/**
	 * @see com.jdroid.java.parser.Parser#parse(java.io.InputStream)
	 */
	@Override
	public Object parse(InputStream inputStream) {
		String content = FileUtils.toString(inputStream);
		return StringUtils.isNotBlank(content) ? parse(content) : null;
	}
	
	/**
	 * @param json
	 * @return The parsed object
	 * @throws JSONException
	 */
	public abstract Object parse(T json) throws JSONException;
	
	/**
	 * Parses a list of items.
	 * 
	 * @param <ITEM> The item's type.
	 * 
	 * @param jsonObject The {@link JsonObjectWrapper} to parse.
	 * @param jsonKey The key for the Json array.
	 * @param parser The {@link JsonParser} to parse each list item.
	 * @return The parsed list.
	 * @throws JSONException
	 */
	protected <ITEM> List<ITEM> parseList(JsonObjectWrapper jsonObject, String jsonKey,
			JsonParser<JsonObjectWrapper> parser) throws JSONException {
		return parseList(jsonObject.getJSONArray(jsonKey), parser);
	}
	
	/**
	 * Parses a list of items.
	 * 
	 * @param <ITEM> The item's type.
	 * 
	 * @param json The {@link JsonArrayWrapper} to parse.
	 * @param parser The {@link JsonParser} to parse each list item.
	 * @return The parsed list.
	 * @throws JSONException
	 */
	@SuppressWarnings("unchecked")
	protected <ITEM> List<ITEM> parseList(JsonArrayWrapper jsonArray, JsonParser<JsonObjectWrapper> parser)
			throws JSONException {
		List<ITEM> list = Lists.newArrayList();
		if (jsonArray != null) {
			int length = jsonArray.length();
			for (int i = 0; i < length; i++) {
				list.add((ITEM)parser.parse(jsonArray.getJSONObject(i)));
			}
		}
		return list;
	}
	
	protected List<String> parseListString(JsonArrayWrapper jsonArray) throws JSONException {
		List<String> list = Lists.newArrayList();
		if (jsonArray != null) {
			int length = jsonArray.length();
			for (int i = 0; i < length; i++) {
				list.add(jsonArray.getString(i));
			}
		}
		return list;
	}
	
	protected List<Long> parseListLong(JsonArrayWrapper jsonArray) throws JSONException {
		List<Long> list = Lists.newArrayList();
		if (jsonArray != null) {
			int length = jsonArray.length();
			for (int i = 0; i < length; i++) {
				list.add(jsonArray.getLong(i));
			}
		}
		return list;
	}
}
