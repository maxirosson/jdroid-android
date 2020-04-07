package com.jdroid.android.utils

object TagUtils {

    fun getTag(clazz: Class<*>): String {
        return clazz.enclosingClass?.simpleName ?: clazz.simpleName
    }
}
