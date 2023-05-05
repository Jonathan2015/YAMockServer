package de.brenner.it.server.config

import de.brenner.it.server.handler.HttpHandlerEntries
import de.brenner.it.server.jsonutil.JsonUtil
import java.io.File
import java.util.logging.Logger

class ConfigParser(private val configuration: File?) {


    private var jsonUtil: JsonUtil

    companion object {
        private val LOGGER = Logger.getLogger(ConfigParser::class.java.name)
    }

    init {
        this.jsonUtil = JsonUtil
    }

    fun parseConfiguration(): HttpHandlerEntries {
        LOGGER.info("Start parsing configuration for file with path=" + (configuration?.name ?: "no config provided"))
        val httpHandlerEntries = configuration?.let { this.jsonUtil.readFileContent(it) }
        return if (httpHandlerEntries == null || httpHandlerEntries.paths == null) {
            LOGGER.info("Parsing created 0 HttpHandlerEntries.")
            HttpHandlerEntries(listOf())
        } else {
            LOGGER.info("Parsing created " + httpHandlerEntries.paths.size + " HttpHandlerEntries.")
            httpHandlerEntries
        }
    }

}
