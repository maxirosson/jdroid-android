package com.jdroid.android.sample

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
