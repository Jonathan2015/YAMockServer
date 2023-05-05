package de.brenner.it.server.response

import de.brenner.it.server.handler.HttpHandlerEntry
import java.io.File
import java.io.FileInputStream
import java.nio.charset.Charset
import java.util.logging.Logger

class ResponseParser() {

    companion object {
        private val LOGGER = Logger.getLogger(ResponseParser::class.java.name)
    }

    fun getResponseBody(parentPath: String, httpHandlerEntry: HttpHandlerEntry): ByteArray {
        return if (httpHandlerEntry.responseBody != null) {
            httpHandlerEntry.responseBody!!.toByteArray(Charset.defaultCharset())
        } else if (httpHandlerEntry.responseBodyFileReference != null) {
            val path = parentPath + "/" + httpHandlerEntry.responseBodyFileReference
            LOGGER.info("Read content for response body from file with path=$path")
            FileInputStream(File(path)).use { it.readAllBytes() }
        } else {
            byteArrayOf()
        }
    }
}