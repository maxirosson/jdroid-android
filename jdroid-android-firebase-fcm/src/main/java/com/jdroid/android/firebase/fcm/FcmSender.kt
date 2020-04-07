package com.jdroid.android.firebase.fcm

import android.os.Bundle

import com.jdroid.java.http.Server

interface FcmSender : Server {

    /**
     * @return A unique numerical value created when you configure your API project (given as "Project Number" in the Google
     * Developers Console). The sender ID is used in the registration process to identify an app server that is permitted
     * to send messages to the client app.
     */
    fun getSenderId(): String

    fun onRegisterOnServer(registrationToken: String, updateLastActiveTimestamp: Boolean, parameters: Map<String, Object>)
}
