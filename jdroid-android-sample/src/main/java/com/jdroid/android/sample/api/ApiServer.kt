package com.jdroid.android.sample.api

import com.jdroid.android.application.AbstractApplication
import com.jdroid.android.firebase.fcm.FcmSender
import com.jdroid.android.firebase.fcm.device.Device
import com.jdroid.android.firebase.fcm.instanceid.InstanceIdHeaderAppender
import com.jdroid.android.http.DefaultHeaderAppender
import com.jdroid.java.http.HttpServiceProcessor
import com.jdroid.java.http.Server

enum class ApiServer private constructor(
    private val domain: String?,
    private val port: String?,
    private val segment: String?,
    private val supportsSsl: Boolean,
    private val isProduction: Boolean
) : FcmSender {

    PROD("url", ":8080", "/api", true, true) {
        override fun getSenderId(): String {
            // TODO Return production sender id here
            return ""
        }
    },
    UAT("staging-jdroid.rhcloud.com", null, "/api", true, false),
    DEV(null, ":8080", "/jdroid-java-webapp-sample/api", true, false),
    MOCKEY_LOCAL(null, ":8081", "/service/api", false, false);

    override fun getServerName(): String {
        return name
    }

    override fun getBaseUrl(): String {
        if (domain == null) {
            return AbstractApplication.get().appContext.localIp + port + segment
        } else {
            val urlBuilder = StringBuilder()
            urlBuilder.append(domain)
            if (port != null) {
                urlBuilder.append(port)
            }
            if (segment != null) {
                urlBuilder.append(segment)
            }
            return urlBuilder.toString()
        }
    }

    override fun supportsSsl(): Boolean {
        return supportsSsl
    }

    override fun isProduction(): Boolean {
        return isProduction
    }

    override fun getHttpServiceProcessors(): List<HttpServiceProcessor> {
        return listOf(DefaultHeaderAppender(), InstanceIdHeaderAppender, HttpResponseValidator)
    }

    override fun instance(name: String): Server {
        return valueOf(name)
    }

    override fun getSenderId(): String {
        return "527857945512"
    }

    override fun onRegisterOnServer(registrationToken: String, updateLastActiveTimestamp: Boolean, parameters: Map<String, Object>) {
        val service = SampleApiService()
        val device = Device(registrationToken, null)
        service.addDevice(device, updateLastActiveTimestamp)
    }
}
