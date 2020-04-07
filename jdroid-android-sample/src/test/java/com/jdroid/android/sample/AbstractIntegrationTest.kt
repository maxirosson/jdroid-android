package com.jdroid.android.sample

abstract class AbstractIntegrationTest : AbstractTest() {

    override fun isHttpMockEnabled(): Boolean {
        return false
    }
}
