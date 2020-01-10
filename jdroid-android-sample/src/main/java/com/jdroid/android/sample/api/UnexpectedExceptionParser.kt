package com.jdroid.android.sample.api

import com.jdroid.java.exception.UnexpectedException
import com.jdroid.java.http.parser.Parser

import java.io.InputStream

class UnexpectedExceptionParser : Parser {

    override fun parse(inputStream: InputStream): Any? {
        throw UnexpectedException("Error")
    }

    override fun parse(input: String): Any? {
        throw UnexpectedException("Error")
    }
}
