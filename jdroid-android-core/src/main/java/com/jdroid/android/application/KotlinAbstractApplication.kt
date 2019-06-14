package com.jdroid.android.application

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

abstract class KotlinAbstractApplication: Application() {

    protected fun initKoin() {
        startKoin {
            androidLogger()
            androidContext(this@KotlinAbstractApplication)
        }
    }

}