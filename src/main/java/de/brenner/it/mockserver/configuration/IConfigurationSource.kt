package de.brenner.it.mockserver.configuration

import de.brenner.it.mockserver.handler.HttpPath

interface IConfigurationSource {
    fun getConfiguration(): MutableMap<String, HttpPath>
}