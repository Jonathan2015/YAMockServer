package de.brenner.it.mockserver.util

import java.nio.file.FileSystems
import java.nio.file.Path
import java.nio.file.StandardWatchEventKinds.*

class FileWatcher(val configurationPath: Path) {

    init{
        val watchService = FileSystems.newFileSystem(configurationPath).newWatchService()
        configurationPath.register(watchService,  ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY)
    }
}