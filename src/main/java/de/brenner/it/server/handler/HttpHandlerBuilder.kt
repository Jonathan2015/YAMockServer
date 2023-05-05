package de.brenner.it.server.handler

import com.sun.net.httpserver.HttpExchange
import com.sun.net.httpserver.HttpHandler
import de.brenner.it.server.config.ConfigParser
import de.brenner.it.server.logger.ExchangeLogger
import de.brenner.it.server.request.RequestParser
import de.brenner.it.server.response.ResponseParser
import java.io.File
import java.io.IOException
import java.util.*
import java.util.logging.Logger

class HttpHandlerBuilder(private val configuration: File?) {

    companion object {
        private val LOGGER = Logger.getLogger(HttpHandlerBuilder::class.java.name)
    }

    private val requestParser: RequestParser
    private val responseParser: ResponseParser
    private val httpHandlerEntries: HttpHandlerEntries

    init {
        val configParser = ConfigParser(configuration)
        requestParser = RequestParser()
        responseParser = ResponseParser()
        httpHandlerEntries = configParser.parseConfiguration()
    }

    fun buildHttpHandler(): HttpHandler {
        return if (httpHandlerEntries.paths.isEmpty()) {
            LOGGER.info("No HttpHandlerEntries were defined, return default HttpHandler for \"/\"")
            defaultHttpHandler
        } else {
            LOGGER.info("HttpHandlerEntries=" + httpHandlerEntries.toString())
            httpHandlingByConfig
        }
    }

    private val httpHandlingByConfig: HttpHandler
        private get() = HttpHandler { exchange: HttpExchange ->
            exchange.use { exchange ->
                val httpHandlerEntryOptional = getHttpHandlerEntry(exchange)
                val message: String
                if (httpHandlerEntryOptional.isPresent) {
                    message = "Valid request"
                    generateResponse(exchange, httpHandlerEntryOptional.get())
                } else {
                    message = "Invalid request"
                    sendResponseBadRequest(exchange)
                }
                ExchangeLogger.logHttpExchange(exchange, message)
            }
        }

    private val defaultHttpHandler: HttpHandler
        private get() = HttpHandler { exchange: HttpExchange ->
            exchange.use { exchange ->
                ExchangeLogger.logHttpExchange(exchange, "Print request data only:")
                sendResponseHttpOk(exchange)
            }
        }

    private fun generateResponse(exchange: HttpExchange, httpHandlerEntry: HttpHandlerEntry) {
        try {
            val parentPath: String = if (configuration == null) {
                ""
            } else {
                configuration!!.parent
            }
            val responseBody = responseParser.getResponseBody(parentPath, httpHandlerEntry)
            val responseLength = responseBody.size
            exchange.sendResponseHeaders(httpHandlerEntry.responseStatus!!, responseLength.toLong())
            if (responseLength > 0) {
                exchange.responseBody.write(responseBody)
            }
        } catch (e: IOException) {
            LOGGER.warning("Sending response body failed, " + e.message)
        }
    }

    private fun getHttpHandlerEntry(exchange: HttpExchange): Optional<HttpHandlerEntry> {
        return httpHandlerEntries.paths.stream().filter { httpHandlerEntry: HttpHandlerEntry ->
            httpHandlerEntry.path!!.contains(exchange.requestURI.toASCIIString()) &&
                    httpHandlerEntry.requestMethod!!.lowercase(Locale.getDefault()) == exchange.requestMethod.lowercase(Locale.getDefault())
        }.findFirst()
    }

    @Throws(IOException::class)
    private fun sendResponseHttpOk(exchange: HttpExchange) {
        exchange.sendResponseHeaders(200, 0)
    }

    @Throws(IOException::class)
    private fun sendResponseBadRequest(exchange: HttpExchange) {
        exchange.sendResponseHeaders(400, 0)
    }

}
