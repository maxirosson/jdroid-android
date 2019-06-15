package com.jdroid.android.application

import android.app.Application
import com.jdroid.android.firebase.analytics.FirebaseAnalyticsFacade
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin
import org.koin.dsl.module

abstract class KotlinAbstractApplication : Application() {

    protected open fun initKoin() {
        startKoin {
            androidLogger()
            androidContext(this@KotlinAbstractApplication)

            // TODO See if its a good idea to let each lifecycle call back to initialize its own dependencies
            val coreModule = module {
                single { FirebaseAnalyticsFacade() }
            }
            loadKoinModules(arrayListOf(coreModule))
        }
    }
}