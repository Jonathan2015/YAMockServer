package de.brenner.it.mockserver.configuration

import de.brenner.it.mockserver.handler.HttpPath
import java.nio.file.Path

interface IConfigurationSource {
    /**
     * Provides a Mapping of URI paths as a s String to a {@link HttpPath}
     */
    fun getRegisteredPaths(): Map<String, HttpPath>

    /**
     * This method can be used to update the registered paths.
     */
    fun updatePaths(updatedRegisteredPaths: Map<String, HttpPath>)

    /**
     * Get the root path of the configuration
     */
    fun getConfigurationRootPath(): String
}