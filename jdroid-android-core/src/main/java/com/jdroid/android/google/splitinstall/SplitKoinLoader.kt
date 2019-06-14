package com.jdroid.android.google.splitinstall

import com.jdroid.android.application.AbstractApplication
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules
import org.koin.core.module.Module

abstract class SplitKoinLoader {

    companion object {

        fun loadKoinModule(moduleName: String) {
            executeKoinLoaderMethod(moduleName, "loadKoinModule")
        }

        fun unloadKoinModule(moduleName: String) {
            executeKoinLoaderMethod(moduleName, "unloadKoinModule")
        }

        private fun executeKoinLoaderMethod(moduleName: String, method: String) {
            val modulePackage = moduleName.toLowerCase().replace("_", "")
            val cls = Class.forName(AbstractApplication.get().manifestPackageName + ".$modulePackage.koin.SplitKoinLoaderImpl")
            cls.getMethod(method).invoke(cls.kotlin.objectInstance)
        }
    }

    fun loadKoinModule() {
        loadKoinModules(createKoinModule())
    }

    fun unloadKoinModule() {
        unloadKoinModules(createKoinModule())
    }

    protected abstract fun createKoinModule(): Module
}