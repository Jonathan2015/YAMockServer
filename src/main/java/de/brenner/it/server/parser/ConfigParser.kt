package de.brenner.it.server.parser

import de.brenner.it.server.handler.HttpHandlerEntries
import de.brenner.it.server.util.JsonUtil
import java.io.File
import java.nio.file.Paths
import java.util.logging.Logger

class ConfigParser(configFilePath: String) {

    private var configuration: File?

    companion object {
        private val LOGGER = Logger.getLogger(ConfigParser::class.java.name)
    }

    init {
        try {
            configuration = Paths.get(configFilePath).toFile()
        } catch (e: Exception) {
            LOGGER.warning("Reading the config failed")
            configuration = null
        }
    }

    fun getConfigurationRootPath(): String {
        return if (configuration == null) {
            ""
        } else {
            return configuration!!.parent
        }
    }

    fun parseConfiguration(): HttpHandlerEntries {
        LOGGER.info("Start parsing configuration for file with path=" + (configuration?.name ?: "no config provided"))
        val httpHandlerEntries = configuration?.let { JsonUtil.readFileContent(it) }
        return if (httpHandlerEntries == null || httpHandlerEntries.paths == null) {
            LOGGER.info("Parsing created 0 HttpHandlerEntries.")
            HttpHandlerEntries(listOf())
        } else {
            LOGGER.info("Parsing created " + httpHandlerEntries.paths.size + " HttpHandlerEntries.")
            httpHandlerEntries
        }
    }

}
