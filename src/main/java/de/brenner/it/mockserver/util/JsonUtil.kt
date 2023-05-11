package de.brenner.it.mockserver.util

import com.fasterxml.jackson.databind.ObjectMapper
import de.brenner.it.mockserver.handler.HttpPathEntries
import java.io.File
import java.io.IOException
import java.util.logging.Logger

object JsonUtil {

    private val LOGGER = Logger.getLogger(JsonUtil::class.java.name)

    fun readFileContent(configuration: File): HttpPathEntries? {
        return try {
            ObjectMapper().readValue(configuration, HttpPathEntries::class.java)
        } catch (e: IOException) {
            LOGGER.warning("Reading the configuraton failed, " + e.message)
            null
        }
    }
}