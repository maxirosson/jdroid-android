package com.jdroid.android

import com.jdroid.android.context.AppContext

class TestAppContext : AppContext() {

    override fun getServerName(): String? {
        return null
    }

    override fun getInstallationSource(): String {
        return "GooglePlay"
    }

    override fun getLocalIp(): String? {
        return null
    }
}
