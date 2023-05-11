package de.brenner.it.mockserver

import com.sun.net.httpserver.HttpServer
import de.brenner.it.mockserver.configuration.DefaultConfigurationSource
import de.brenner.it.mockserver.configuration.IConfigurationSource
import de.brenner.it.mockserver.handler.HttpPathBuilder
import java.io.IOException
import java.net.InetSocketAddress
import java.util.logging.Logger

class MockServer(private var port: Int = 9090, private var configurationSource: IConfigurationSource = DefaultConfigurationSource()) {

    companion object {
        private val LOGGER = Logger.getLogger(MockServer::class.java.name)
    }

    fun port(port: Int): MockServer {
        this.port = port
        return this
    }

    fun configurationSource(configurationSource: IConfigurationSource): MockServer {
        this.configurationSource = configurationSource
        return this
    }

    fun start() {
        val inetSocketAddress = InetSocketAddress(port)
        try {
            val server = HttpServer.create(
                    inetSocketAddress,
                    1,
                    "/",
                    HttpPathBuilder(configurationSource).buildHttpHandler()
            )
            server.start()
            LOGGER.info("Started server on port=${server.address.port}")
        } catch (e: IOException) {
            LOGGER.warning("Starting the server failed" + e.message)
        }
    }

}
