package de.brenner.it

import de.brenner.it.mockserver.MockServer

object Main {
    @JvmStatic
    fun main(args: Array<String>) {
        MockServer().start()
    }
}