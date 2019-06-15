package com.jdroid.android.http.parser

import android.util.Xml

import com.jdroid.java.http.exception.ConnectionException
import com.jdroid.java.http.parser.Parser
import com.jdroid.java.utils.StreamUtils
import com.jdroid.java.utils.TypeUtils

import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException

import java.io.ByteArrayInputStream
import java.io.IOException
import java.io.InputStream

abstract class AbstractXmlPullParser : Parser {

    override fun parse(input: String): Any {
        return parse(ByteArrayInputStream(input.toByteArray()))
    }

    override fun parse(inputStream: InputStream): Any {
        try {
            val parser = Xml.newPullParser()
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
            parser.setInput(inputStream, null)
            parser.nextTag()
            return readFeed(parser)
        } catch (e: XmlPullParserException) {
            throw ConnectionException(e)
        } catch (e: IOException) {
            throw ConnectionException(e)
        } finally {
            StreamUtils.safeClose(inputStream)
        }
    }

    @Throws(XmlPullParserException::class, IOException::class)
    protected abstract fun readFeed(parser: XmlPullParser): Any

    protected fun readStringAttribute(parser: XmlPullParser, attributeName: String): String {
        return parser.getAttributeValue(null, attributeName)
    }

    @Throws(IOException::class, XmlPullParserException::class)
    protected fun readLongValue(parser: XmlPullParser, name: String): Long? {
        return TypeUtils.getLong(readStringValue(parser, name))
    }

    @Throws(IOException::class, XmlPullParserException::class)
    protected fun readStringValue(parser: XmlPullParser, name: String): String {
        parser.require(XmlPullParser.START_TAG, null, name)
        var value = ""
        if (parser.next() == XmlPullParser.TEXT) {
            value = parser.text
            parser.nextTag()
        }
        parser.require(XmlPullParser.END_TAG, null, name)
        return value
    }

    @Throws(XmlPullParserException::class, IOException::class)
    protected fun skip(parser: XmlPullParser) {
        if (parser.eventType != XmlPullParser.START_TAG) {
            throw IllegalStateException()
        }
        var depth = 1
        while (depth != 0) {
            when (parser.next()) {
                XmlPullParser.END_TAG -> depth--
                XmlPullParser.START_TAG -> depth++
            }
        }
    }
}
