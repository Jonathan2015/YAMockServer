package de.brenner.it

import de.brenner.it.server.MockServer

object Main {
    @JvmStatic
    fun main(args: Array<String>) {
        MockServer().start()
    }
}