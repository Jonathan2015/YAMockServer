package de.brenner.it.server

import com.sun.net.httpserver.HttpServer
import de.brenner.it.server.handler.HttpHandlerBuilder
import java.io.File
import java.io.IOException
import java.net.InetSocketAddress
import java.nio.file.Paths
import java.util.logging.Logger

class MockServer {

    companion object {
        private val LOGGER = Logger.getLogger(MockServer::class.java.name)
    }

    private val port = 9090
    private val config: File?
    private val httpHandlerBuilder: HttpHandlerBuilder
    private val configFilePath = "src/main/resources/config.json"

    init {
        config = configFile
        httpHandlerBuilder = HttpHandlerBuilder(config, )
    }

    fun start() {
        val inetSocketAddress = InetSocketAddress(port)
        try {
            val server = HttpServer.create(
                    inetSocketAddress,
                    1,
                    "/",
                    httpHandlerBuilder.buildHttpHandler()
            )
            server.start()
            LOGGER.info("Started server on port" + server.address.port)
        } catch (e: IOException) {
            LOGGER.warning("Starting the server failed" + e.message)
        }
    }

    private val configFile: File?
        private get() = try {
            Paths.get(configFilePath).toFile()
        } catch (e: Exception) {
            LOGGER.warning("Reading the config failed")
            null
        }

}
