package com.jdroid.android.sample.ui.firebase.fcm

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.EditText

import com.google.firebase.messaging.FirebaseMessaging
import com.jdroid.android.concurrent.AppExecutors
import com.jdroid.android.firebase.fcm.AbstractFcmAppModule
import com.jdroid.android.firebase.fcm.DefaultFcmMessageResolver
import com.jdroid.android.firebase.fcm.FcmRegistrationCommand
import com.jdroid.android.firebase.fcm.FcmRegistrationWorker
import com.jdroid.android.firebase.fcm.instanceid.InstanceIdHelper
import com.jdroid.android.firebase.fcm.notification.NotificationFcmMessage
import com.jdroid.android.fragment.AbstractFragment
import com.jdroid.android.sample.R
import com.jdroid.android.sample.api.SampleApiService
import com.jdroid.java.collections.Maps

class FcmFragment : AbstractFragment() {

    override fun getContentFragmentLayout(): Int? {
        return R.layout.fcm_fragment
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        findView<View>(R.id.registerDevice).setOnClickListener { AbstractFcmAppModule.get().startFcmRegistration(false) }
        findView<View>(R.id.registerDeviceAndUpdateLastActiveTimestamp).setOnClickListener {
            AbstractFcmAppModule.get().startFcmRegistration(true)
        }

        findView<View>(R.id.removeInstanceId).setOnClickListener { AppExecutors.networkIOExecutor.execute { InstanceIdHelper.removeInstanceId() } }

        findView<View>(R.id.removeDevice).setOnClickListener { AppExecutors.networkIOExecutor.execute { SampleApiService().removeDevice() } }

        val topicToSubscribeEditText = findView<EditText>(R.id.topicToSubscribe)

        findView<View>(R.id.subscribeToTopic).setOnClickListener {
            AppExecutors.networkIOExecutor.execute {
                val topic = if (topicToSubscribeEditText.text.isNotEmpty()) topicToSubscribeEditText.text.toString() else null
                if (topic != null) {
                    FirebaseMessaging.getInstance().subscribeToTopic(topic)
                }
            }
        }

        findView<View>(R.id.unsubscribeFromTopic).setOnClickListener {
            AppExecutors.networkIOExecutor.execute {
                val topic = if (topicToSubscribeEditText.text.isNotEmpty()) topicToSubscribeEditText.text.toString() else null
                if (topic != null) {
                    FirebaseMessaging.getInstance().unsubscribeFromTopic(topic)
                }
            }
        }

        val topicEditText = findView<EditText>(R.id.topic)

        val messageKeyEditText = findView<EditText>(R.id.messageKey)
        messageKeyEditText.setText("sampleMessage")

        val minAppVersionCode = findView<EditText>(R.id.minAppVersionCode)
        minAppVersionCode.setText("0")

        val minDeviceOsVersion = findView<EditText>(R.id.minDeviceOsVersion)
        minDeviceOsVersion.setText("0")

        val senderId = findView<EditText>(R.id.senderId)
        senderId.setText(AbstractFcmAppModule.get().getFcmSenders()[0].getSenderId())

        findView<View>(R.id.sendPush).setOnClickListener {
            AppExecutors.networkIOExecutor.execute {
                val topic = if (topicEditText.text.isNotEmpty()) topicEditText.text.toString() else null
                val registrationToken = FcmRegistrationWorker.getRegistrationToken(senderId.text.toString())

                val params = Maps.newHashMap<String, String>()
                if (minAppVersionCode.text.isNotEmpty()) {
                    params[DefaultFcmMessageResolver.MIN_APP_VERSION_CODE_KEY] = minAppVersionCode.text.toString()
                }
                if (minDeviceOsVersion.text.isNotEmpty()) {
                    params[DefaultFcmMessageResolver.MIN_DEVICE_OS_VERSION_KEY] = minDeviceOsVersion.text.toString()
                }

                val messageKey = messageKeyEditText.text.toString()
                if (NotificationFcmMessage.MESSAGE_KEY == messageKey) {
                    params[NotificationFcmMessage.CONTENT_TITLE] = "Sample Content Title"
                    params[NotificationFcmMessage.CONTENT_TEXT] = "Sample Content Text"
                    params[NotificationFcmMessage.LIGHT_ENABLED] = "true"
                    params[NotificationFcmMessage.SOUND_ENABLED] = "false"
                    params[NotificationFcmMessage.VIBRATION_ENABLED] = "true"
                    params[NotificationFcmMessage.URL] = "https://jdroidtools.com/uri/noflags?a=1"
                    params[NotificationFcmMessage.LARGE_ICON_URL] = "https://jdroidtools.com/images/gradle.png"
                }

                SampleApiService().sendPush(topic, registrationToken!!, messageKey, params)
            }
        }
    }
}
