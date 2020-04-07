package com.jdroid.android

abstract class AbstractIntegrationTest : AbstractTest() {

    override fun isHttpMockEnabled(): Boolean {
        return false
    }
}
