package de.brenner.it.mockserver.logger

import com.sun.net.httpserver.HttpExchange
import de.brenner.it.mockserver.parser.RequestParser
import java.util.logging.Logger

object ExchangeLogger {

    private val LOGGER = Logger.getLogger(ExchangeLogger::class.java.name)
    private val requestParser: RequestParser = RequestParser()

    fun logHttpExchange(exchange: HttpExchange, message: String) {
        LOGGER.info(">>>>>$message")
        LOGGER.info(">>>>>>>>>>Method=" + exchange.requestMethod)
        LOGGER.info(">>>>>>>>>>Path=" + exchange.requestURI)
        LOGGER.info(">>>>>>>>>>Body=" + requestParser.parseRequestBodyFromHttpExchange(exchange))
    }
}