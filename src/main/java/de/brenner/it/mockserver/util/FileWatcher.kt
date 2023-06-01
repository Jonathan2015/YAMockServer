package de.brenner.it.mockserver.util

import de.brenner.it.mockserver.configuration.IConfigurationSource
import java.io.IOException
import java.nio.file.*
import java.nio.file.StandardWatchEventKinds.*
import java.nio.file.attribute.BasicFileAttributes


class FileWatcher(private val configuration: IConfigurationSource) : Thread() {

    companion object {
        private val LOGGER = java.util.logging.Logger.getLogger(FileWatcher::class.java.name)
    }

    private val watcher: WatchService
    private val keys: MutableMap<WatchKey, Path>

    init {
        val configurationPath = Path.of(configuration.getConfigurationRootPath())

        this.watcher = FileSystems.getDefault().newWatchService()
        this.keys = HashMap<WatchKey, Path>()

        LOGGER.info("Scanning $configurationPath ...")
        registerAll(configurationPath)
        LOGGER.info("Done. Start processing events")
        this.start()
    }

    override fun run() {
        LOGGER.info("Start processing events...")
        processEvents()
    }

    /**
     * Register the given directory, and all its sub-directories, with the
     * WatchService.
     */
    @Throws(IOException::class)
    private fun registerAll(start: Path) {
        // register directory and sub-directories
        Files.walkFileTree(start, object : SimpleFileVisitor<Path>() {
            @Throws(IOException::class)
            override fun preVisitDirectory(dir: Path, attrs: BasicFileAttributes): FileVisitResult {
                register(dir)
                return FileVisitResult.CONTINUE
            }
        })
    }

    /**
     * Register the given directory with the WatchService
     */
    @Throws(IOException::class)
    private fun register(dir: Path) {
        val key = dir.register(watcher, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY)
        val prev = keys[key]
        if (prev == null) {
            LOGGER.info("register: $dir")
        } else {
            if (dir != prev) {
                System.out.format("update: $prev -> $dir")
            }
        }
        keys[key] = dir
    }

    fun processEvents() {
        while (true) {


            // wait for key to be signalled
            var key: WatchKey
            key = try {
                watcher.take()
            } catch (x: InterruptedException) {
                return
            }
            val dir = keys[key]
            if (dir == null) {
                LOGGER.warning("WatchKey not recognized!!")
                continue
            }
            for (event in key.pollEvents()) {
                val kind = event.kind()

                // TBD - provide example of how OVERFLOW event is handled
                if (kind === OVERFLOW) {
                    continue
                }

                // Context for directory entry event is the file name of entry
                val ev: WatchEvent<Path> = cast(event)
                val name = ev.context()
                val child = dir.resolve(name)

                // print out event
                LOGGER.info("${event.kind().name()}: $child")

                // if directory is created, and watching recursively, then
                // register it and its sub-directories
                if (kind === ENTRY_CREATE) {
                    try {
                        if (Files.isDirectory(child, LinkOption.NOFOLLOW_LINKS)) {
                            registerAll(child)
                        }
                    } catch (x: IOException) {
                        // ignore to keep sample readbale
                    }
                }
            }

            // reset key and remove from set if directory no longer accessible
            val valid = key.reset()
            if (!valid) {
                keys.remove(key)

                // all directories are inaccessible
                if (keys.isEmpty()) {
                    break
                }
            }
        }
    }

    fun <T> cast(event: WatchEvent<*>): WatchEvent<T> {
        return event as WatchEvent<T>
    }


}