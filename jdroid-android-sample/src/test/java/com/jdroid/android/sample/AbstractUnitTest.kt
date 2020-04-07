package com.jdroid.android.sample

abstract class AbstractUnitTest : AbstractTest() {

    override fun isHttpMockEnabled(): Boolean {
        return true
    }
}
