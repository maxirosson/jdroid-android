package com.jdroid.android.sample.unit

import com.jdroid.android.application.AbstractApplication
import com.jdroid.android.debug.http.HttpDebugConfiguration
import com.jdroid.android.firebase.analytics.FirebaseAnalyticsHelper
import com.jdroid.android.leakcanary.LeakCanaryHelper
import com.jdroid.android.sample.AbstractUnitTest
import com.jdroid.android.sample.TestAndroidApplication
import com.jdroid.android.sample.TestAppContext
import com.jdroid.android.sample.TestExceptionHandler
import com.jdroid.android.strictmode.StrictModeHelper
import com.jdroid.android.utils.AppUtils
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertFalse
import junit.framework.Assert.assertNull
import junit.framework.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest : AbstractUnitTest() {

    @Test
    fun example() {
        assertTrue(HttpDebugConfiguration.isHttpMockEnabled())
        assertEquals(TestAndroidApplication::class.java, AbstractApplication.get().javaClass)
        assertNull(AbstractApplication.get().homeActivityClass)
        assertEquals(TestAppContext::class.java, AbstractApplication.get().appContext.javaClass)
        assertFalse(LeakCanaryHelper.isLeakCanaryEnabled())
        assertFalse(StrictModeHelper.isStrictModeEnabled())
        assertFalse(AppUtils.isReleaseBuildType)
        assertTrue(FirebaseAnalyticsHelper.isFirebaseAnalyticsEnabled())
        assertEquals(TestExceptionHandler::class.java, AbstractApplication.get().exceptionHandler.javaClass)
    }
}
