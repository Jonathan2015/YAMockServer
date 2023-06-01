package de.brenner.it.mockserver.configuration

import de.brenner.it.mockserver.handler.HttpPath
import java.nio.file.Path

interface IConfigurationSource {
    /**
     * Provides a Mapping of URI paths as a s String to a {@link HttpPath}
     */
    fun getRegisteredPaths(): Map<String, HttpPath>

    /**
     * Tell the configuration to reload or update the paths
     */
    fun updatePaths()

    /**
     * Get the root path of the configuration
     */
    fun getConfigurationRootPath(): String
}