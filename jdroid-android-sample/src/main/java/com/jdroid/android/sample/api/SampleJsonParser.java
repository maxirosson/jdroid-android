package com.jdroid.android.sample.api;

import com.jdroid.java.http.parser.json.JsonParser;
import com.jdroid.java.json.JSONObject;

public class SampleJsonParser extends JsonParser<JSONObject> {

	@Override
	public Object parse(JSONObject json) {
		return new SampleResponse(json.getString("sampleKey"));
	}
}