package com.jdroid.android

abstract class AbstractUnitTest : AbstractTest() {

    override fun isHttpMockEnabled(): Boolean {
        return true
    }
}
