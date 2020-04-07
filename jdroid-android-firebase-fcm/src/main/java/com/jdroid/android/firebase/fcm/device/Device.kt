package com.jdroid.android.firebase.fcm.device

import com.jdroid.android.utils.AndroidUtils
import com.jdroid.android.utils.AppUtils
import com.jdroid.android.utils.DeviceUtils

class Device(val registrationToken: String, val deviceGroupId: String?) {

    val deviceBrandName: String?
    val deviceModelName: String?
    val deviceOsVersion: String
    val appVersionCode: String

    init {
        deviceBrandName = DeviceUtils.getDeviceManufacturer()
        deviceModelName = DeviceUtils.getDeviceModel()
        deviceOsVersion = AndroidUtils.getPlatformVersion()
        appVersionCode = AppUtils.getVersionCode().toString()
    }
}
