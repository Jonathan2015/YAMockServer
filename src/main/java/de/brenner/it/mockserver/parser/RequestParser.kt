package de.brenner.it.mockserver.parser

import com.sun.net.httpserver.HttpExchange
import java.io.IOException
import java.util.logging.Logger

class RequestParser {

    companion object {
        private val LOGGER = Logger.getLogger(RequestParser::class.java.name)
    }

    fun parseRequestBodyFromHttpExchange(exchange: HttpExchange): String {
        val requestBody = exchange.requestBody
        return if (requestBody != null) {
            try {
                var requestBodyContentParsed = ""
                val requestBodyContentByteArray = requestBody.readAllBytes()
                for (signPos in requestBodyContentByteArray.indices) {
                    requestBodyContentParsed += Char(requestBodyContentByteArray[signPos].toUShort())
                }
                requestBodyContentParsed
            } catch (e: IOException) {
                LOGGER.warning("Reading the request body failed, return empty body" + e.message)
                ""
            }
        } else {
            ""
        }
    }
}
