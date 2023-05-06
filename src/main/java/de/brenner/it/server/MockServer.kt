package de.brenner.it.server

import com.sun.net.httpserver.HttpServer
import de.brenner.it.server.handler.HttpHandlerBuilder
import java.io.IOException
import java.net.InetSocketAddress
import java.util.logging.Logger

/**
 * TODO
 * - Make config path based on a canonical path
 * - configuration validation
 * - add different request,response types xml ,json, text/html
 */
class MockServer(private val port: Int = 9090, private val configFilePath: String = "src/main/resources/config.json") {

    companion object {
        private val LOGGER = Logger.getLogger(MockServer::class.java.name)
    }

    fun start() {
        val inetSocketAddress = InetSocketAddress(port)
        try {
            val server = HttpServer.create(
                    inetSocketAddress,
                    1,
                    "/",
                    HttpHandlerBuilder(configFilePath).buildHttpHandler()
            )
            server.start()
            LOGGER.info("Started server on port=${server.address.port}")
        } catch (e: IOException) {
            LOGGER.warning("Starting the server failed" + e.message)
        }
    }

}
