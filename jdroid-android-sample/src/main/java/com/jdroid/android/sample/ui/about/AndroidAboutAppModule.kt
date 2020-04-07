package com.jdroid.android.sample.ui.about

import com.jdroid.android.about.AboutAppModule
import com.jdroid.android.about.AboutContext

class AndroidAboutAppModule : AboutAppModule() {

    override fun createAboutContext(): AboutContext {
        return AndroidAboutContext()
    }
}
