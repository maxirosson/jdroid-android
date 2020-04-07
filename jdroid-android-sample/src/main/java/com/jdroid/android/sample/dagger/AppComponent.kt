package com.jdroid.android.sample.dagger

import android.content.Context
import com.jdroid.android.dagger.CoreModule
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Component(
    modules = [
        CoreModule::class
    ]
)
@Singleton
interface AppComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance applicationContext: Context): AppComponent
    }

    // @Singleton
    // @Provides
    // fun firebaseAnalyticsFacade(): FirebaseAnalyticsFacade
}
