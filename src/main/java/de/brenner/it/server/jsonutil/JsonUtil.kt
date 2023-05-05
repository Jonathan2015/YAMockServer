package de.brenner.it.server.jsonutil

import com.fasterxml.jackson.databind.ObjectMapper
import de.brenner.it.server.handler.HttpHandlerEntries
import java.io.File
import java.io.IOException
import java.util.logging.Logger

object JsonUtil {

    private val LOGGER = Logger.getLogger(JsonUtil::class.java.name)

    fun readFileContent(configuration: File): HttpHandlerEntries? {
        return try {
            ObjectMapper().readValue(configuration, HttpHandlerEntries::class.java)
        } catch (e: IOException) {
            LOGGER.warning("Reading the configuraton failed, " + e.message)
            null
        }
    }
}