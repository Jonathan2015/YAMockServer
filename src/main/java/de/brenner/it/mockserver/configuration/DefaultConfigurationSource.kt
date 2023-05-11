package de.brenner.it.mockserver.configuration

import de.brenner.it.mockserver.handler.HttpPath
import de.brenner.it.mockserver.util.JsonUtil
import java.io.File
import java.nio.file.Paths
import java.util.logging.Logger

class DefaultConfigurationSource : IConfigurationSource {

    private var configFilePath: String = "src/main/resources/config.json"

    private var configuration: File?

    companion object {
        private val LOGGER = Logger.getLogger(DefaultConfigurationSource::class.java.name)
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

    override fun getConfiguration(): MutableMap<String, HttpPath> {
        LOGGER.info("Start parsing configuration for file with path=" + (configuration?.name ?: "no config provided"))
        val httpHandlerEntries = configuration?.let { JsonUtil.readFileContent(it) }
        return if (httpHandlerEntries == null || httpHandlerEntries.paths == null) {
            LOGGER.info("Parsing created 0 Paths.")
            mutableMapOf()
        } else {
            LOGGER.info("Parsing created ${httpHandlerEntries.paths.size} Paths.")
            val map = mutableMapOf<String, HttpPath>()
            httpHandlerEntries.paths.forEach {
                map[it.path!!] = it
            }
            return map
        }
    }

}
