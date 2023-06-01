package de.brenner.it.mockserver.configuration

import de.brenner.it.mockserver.handler.HttpPath
import de.brenner.it.mockserver.util.JsonUtil
import java.io.File
import java.nio.file.Paths
import java.util.logging.Logger

class DefaultConfigurationSource : IConfigurationSource {

    private var configFilePath: String = "src/main/resources/config.json"

    private var configuration: File?

    private lateinit var registeredPaths: Map<String, HttpPath>

    companion object {
        private val LOGGER = Logger.getLogger(DefaultConfigurationSource::class.java.name)
    }

    init {
        try {
            configuration = Paths.get(configFilePath).toFile()
            this.registeredPaths = readRegisteredPaths()
        } catch (e: Exception) {
            LOGGER.warning("Reading the config failed error=${e.message}")
            configuration = null
        }
    }

    private fun readRegisteredPaths(): Map<String, HttpPath> {
        LOGGER.info("Start parsing configuration for file with path=${configuration?.name ?: "no config provided"}")
        val fileEntries = configuration?.let { JsonUtil.readFileContent(it) }
        return if (fileEntries?.paths == null) {
            LOGGER.info("Parsing created 0 Paths.")
            mapOf()
        } else {
            LOGGER.info("Parsing created ${fileEntries.paths.size} Paths.")
            val map = mutableMapOf<String, HttpPath>()
            fileEntries.paths.forEach {
                map[it.path!!] = it
            }
            map
        }
    }

    override fun getRegisteredPaths(): Map<String, HttpPath> {
        return this.registeredPaths
    }

    override fun updatePaths() {
        this.registeredPaths = readRegisteredPaths()
    }

    override fun getConfigurationRootPath(): String {
        return if (configuration == null) {
            ""
        } else {
            this.configuration!!.parent!!
        }
    }

}
