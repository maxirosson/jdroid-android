package com.jdroid.android

import java.io.InputStream
import java.net.URL

fun ClassLoader.getRequiredResource(name: String): URL {
    return this.getResource(name)!!
}

fun ClassLoader.getRequiredResourcePath(name: String): String {
    return this.getResource(name)!!.file
}

fun ClassLoader.getRequiredResourceAsStream(name: String): InputStream {
    return this.getResourceAsStream(name)!!
}