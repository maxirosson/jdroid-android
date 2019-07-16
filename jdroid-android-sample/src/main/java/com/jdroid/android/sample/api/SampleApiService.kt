package com.jdroid.android.sample.api

import com.jdroid.android.api.AndroidApiService
import com.jdroid.android.firebase.fcm.AbstractFcmMessage
import com.jdroid.android.firebase.fcm.device.Device

class SampleApiService : AndroidApiService() {

    fun httpGetSample(): SampleResponse {
        val httpService = newGetService("sample", "get")
        httpService.addQueryParameter("param1", "value1")
        httpService.addHeader("header1", "value1")
        httpService.setUserAgent("sampleUserAgent")
        return httpService.execute(SampleJsonParser())
    }

    fun httpPostSample() {
        val httpService = newPostService("sample", "post")
        httpService.addQueryParameter("param1", "value1")
        httpService.setBody("{}")
        httpService.addHeader("header1", "value1")
        httpService.setUserAgent("sampleUserAgent")
        httpService.execute()
    }

    fun httpPutSample() {
        val httpService = newPutService("sample", "put")
        httpService.addQueryParameter("param1", "value1")
        httpService.setBody("{}")
        httpService.addHeader("header1", "value1")
        httpService.setUserAgent("sampleUserAgent")
        httpService.execute()
    }

    fun httpDeleteSample() {
        val httpService = newDeleteService("sample", "delete")
        httpService.addQueryParameter("param1", "value1")
        httpService.addHeader("header1", "value1")
        httpService.setUserAgent("sampleUserAgent")
        httpService.execute()
    }

    fun httpPatchSample() {
        val httpService = newPatchService("sample", "patch")
        httpService.addQueryParameter("param1", "value1")
        httpService.setBody("{}")
        httpService.addHeader("header1", "value1")
        httpService.setUserAgent("sampleUserAgent")
        httpService.execute()
    }

    fun connectionExceptionParser() {
        val httpService = newGetService("sample", "get")
        httpService.addQueryParameter("param1", "value1")
        httpService.addHeader("header1", "value1")
        httpService.setUserAgent("sampleUserAgent")
        httpService.execute<Any>(ConnectionExceptionParser())
    }

    fun unexpectedExceptionParser() {
        val httpService = newGetService("sample", "get")
        httpService.addQueryParameter("param1", "value1")
        httpService.addHeader("header1", "value1")
        httpService.setUserAgent("sampleUserAgent")
        httpService.execute<Any>(UnexpectedExceptionParser())
    }

    fun addDevice(device: Device, updateLastActiveTimestamp: Boolean) {
        val httpService = newPostService("fcm", "device")
        httpService.addQueryParameter("updateLastActiveTimestamp", updateLastActiveTimestamp)
        autoMarshall(httpService, device)
        httpService.execute()
    }

    fun removeDevice() {
        val httpService = newDeleteService("fcm", "device")
        httpService.execute()
    }

    fun sendPush(topic: String?, registrationToken: String, messageKey: String, params: Map<String, String>) {
        val httpService = newGetService("fcm", "send")
        httpService.addQueryParameter("topic", topic)
        if (topic == null) {
            httpService.addQueryParameter("registrationToken", registrationToken)
        }
        httpService.addQueryParameter("messageKeyExtraName", AbstractFcmMessage.MESSAGE_KEY_EXTRA)
        httpService.addQueryParameter(AbstractFcmMessage.MESSAGE_KEY_EXTRA, messageKey)
        httpService.addQueryParameter("timestampEnabled", "true")

        val stringBuilder = StringBuilder()
        stringBuilder.append("[")
        var firstItem = true
        for ((key, value) in params) {
            if (firstItem) {
                firstItem = false
            } else {
                stringBuilder.append(",")
            }
            stringBuilder.append(key)
            stringBuilder.append("|")
            stringBuilder.append(value)
        }
        stringBuilder.append("]")
        httpService.addQueryParameter("params", stringBuilder.toString())
        httpService.execute()
    }
}
