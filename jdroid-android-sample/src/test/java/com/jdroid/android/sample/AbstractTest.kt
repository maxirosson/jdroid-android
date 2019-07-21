package com.jdroid.android.sample

import android.os.Build

import com.jdroid.android.debug.http.HttpDebugConfiguration

import org.junit.Before
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE, application = TestAndroidApplication::class, sdk = [Build.VERSION_CODES.N_MR1])
abstract class AbstractTest {

    @Before
    @Throws(Exception::class)
    fun setUp() {
        HttpDebugConfiguration.setHttpMockEnabled(isHttpMockEnabled())
        onSetup()
    }

    protected fun onSetup() {
        // Do nothing
    }

    protected abstract fun isHttpMockEnabled(): Boolean
}
