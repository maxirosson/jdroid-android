package com.jdroid.android.sample.api

import com.jdroid.java.http.parser.json.JsonParser
import com.jdroid.java.json.JSONObject

class SampleJsonParser : JsonParser<JSONObject>() {

    override fun parse(json: JSONObject): Any {
        return SampleResponse(json.getString("sampleKey"))
    }
}
