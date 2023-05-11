package de.brenner.it.mockserver.handler

import com.sun.net.httpserver.HttpExchange
import com.sun.net.httpserver.HttpHandler
import de.brenner.it.mockserver.configuration.DefaultConfigurationSource
import de.brenner.it.mockserver.configuration.IConfigurationSource
import de.brenner.it.mockserver.logger.ExchangeLogger
import de.brenner.it.mockserver.parser.RequestParser
import de.brenner.it.mockserver.parser.ResponseParser
import java.io.IOException
import java.util.*
import java.util.logging.Logger

class HttpPathBuilder(configurationSource: IConfigurationSource) {

    companion object {
        private val LOGGER = Logger.getLogger(HttpPathBuilder::class.java.name)
    }

    private val requestParser: RequestParser
    private val responseParser: ResponseParser
    private val httpHandlerEntries: MutableMap<String, HttpPath>
    private val configParser: DefaultConfigurationSource

    init {
        configParser = DefaultConfigurationSource()
        requestParser = RequestParser()
        responseParser = ResponseParser()
        httpHandlerEntries = configurationSource.getConfiguration()
    }

    fun buildHttpHandler(): HttpHandler {
        return if (httpHandlerEntries.isEmpty()) {
            LOGGER.info("No HttpHandlerEntries were defined, return default HttpHandler for \"/\"")
            enableDefaultHttpHandling()
        } else {
            LOGGER.info("HttpHandlerEntries=$httpHandlerEntries")
            enableHttpHandlingBasedOnConfig()
        }
    }

    private fun enableHttpHandlingBasedOnConfig(): HttpHandler {
        return HttpHandler { exchange: HttpExchange ->
            exchange.use { exchange ->
                val httpHandlerEntryOptional = getHttpHandlerEntry(exchange)
                val message: String
                if (httpHandlerEntryOptional.isPresent) {
                    message = "Valid request"
                    generateResponse(exchange, httpHandlerEntryOptional.get())
                } else {
                    message = "Invalid request"
                    exchange.sendResponseHeaders(400, 0)
                }
                ExchangeLogger.logHttpExchange(exchange, message)
            }
        }
    }

    private fun enableDefaultHttpHandling(): HttpHandler {
        return HttpHandler { exchange: HttpExchange ->
            exchange.use { exchange ->
                ExchangeLogger.logHttpExchange(exchange, "no Configuration was loaded - Print request data only:")
                exchange.sendResponseHeaders(200, 0)
            }
        }
    }

    private fun generateResponse(exchange: HttpExchange, httpHandlerEntry: HttpPath) {
        try {
            val parentPath: String = configParser.getConfigurationRootPath()
            val responseBody = responseParser.getResponseBody(parentPath, httpHandlerEntry)
            val responseLength = responseBody.size
            exchange.sendResponseHeaders(httpHandlerEntry.responseStatus!!, responseLength.toLong())
            if (responseLength > 0) {
                exchange.responseBody.write(responseBody)
            }
        } catch (e: IOException) {
            LOGGER.warning("Sending response body failed, ${e.message}")
            val responseErrorMessage = "Internal Server Error"
            exchange.sendResponseHeaders(500, responseErrorMessage.length.toLong())
            exchange.responseBody.write(responseErrorMessage.toByteArray())
        }
    }

    private fun getHttpHandlerEntry(exchange: HttpExchange): Optional<HttpPath> {
        val httpPathEntry = httpHandlerEntries.get(exchange.requestURI.toASCIIString())
        return if (httpPathEntry != null && httpPathEntry.requestMethod!!.lowercase(Locale.getDefault()) == exchange.requestMethod.lowercase(Locale.getDefault())) {
            Optional.of(httpPathEntry)
        } else {
            Optional.empty()
        }
    }

}
