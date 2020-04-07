package com.jdroid.android.firebase.fcm.instanceid

import com.jdroid.java.http.HttpResponseWrapper
import com.jdroid.java.http.HttpService
import com.jdroid.java.http.HttpServiceProcessor

object InstanceIdHeaderAppender : HttpServiceProcessor {

    private const val INSTANCE_ID_HEADER = "instanceId"

    override fun onInit(httpService: HttpService) {
        // Do Nothing
    }

    override fun beforeExecute(httpService: HttpService) {
        httpService.addHeader(INSTANCE_ID_HEADER, InstanceIdHelper.getInstanceId())
    }

    override fun afterExecute(httpService: HttpService, httpResponse: HttpResponseWrapper) {
        // Do Nothing
    }
}
