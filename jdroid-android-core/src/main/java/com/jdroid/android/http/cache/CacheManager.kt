package com.jdroid.android.http.cache

import android.content.Context
import androidx.annotation.WorkerThread
import com.jdroid.android.application.AbstractApplication
import com.jdroid.android.application.AppLaunchStatus
import com.jdroid.java.http.cache.Cache
import com.jdroid.java.http.cache.CachedHttpService
import com.jdroid.java.utils.FileUtils
import com.jdroid.java.utils.LoggerUtils
import com.jdroid.java.utils.StreamUtils
import java.io.File

class CacheManager {

    @WorkerThread
    fun initFileSystemCache() {
        try {

            // Sort the caches by priority
            var caches = getFileSystemCaches()
            caches = caches.sortedByDescending { it.priority }

            for (cache in caches) {
                populateFileSystemCache(cache)
                reduceFileSystemCache(cache)
            }
        } catch (e: Exception) {
            AbstractApplication.get().exceptionHandler.logHandledException(e)
        }
    }

    fun populateFileSystemCache(cache: Cache) {

        if (AbstractApplication.get().appLaunchStatus != AppLaunchStatus.NORMAL) {

            val defaultContent = cache.defaultContent

            if (defaultContent != null && !defaultContent.isEmpty()) {
                for (entry in defaultContent.entries) {
                    val source = AbstractApplication::class.java.classLoader!!.getResourceAsStream(
                        "cache/" + entry.key
                    )
                    if (source != null) {
                        val cacheFile = File(
                            getFileSystemCacheDirectory(cache),
                            CachedHttpService.generateCacheFileName(entry.value)
                        )
                        FileUtils.copyStream(source, cacheFile)
                        LOGGER.debug("Populated " + entry.toString() + " to " + cacheFile.absolutePath)
                        StreamUtils.safeClose(source)
                    }
                }
                LOGGER.debug(cache.name + " cache populated")
            }
        }
    }

    fun reduceFileSystemCache(cache: Cache) {
        if (cache.maximumSize != null) {
            val dir = getFileSystemCacheDirectory(cache)

            // Verify if the cache should be clean
            if (dir != null && dir.exists()) {
                var dirSize = FileUtils.getDirectorySizeInMB(dir)
                LOGGER.info("Cache " + cache.name + " size: " + dirSize + " MB")
                if (dirSize > cache.maximumSize) {
                    // Sort the files by modification date, so we remove the not used files first
                    val files = mutableListOf(*dir.listFiles())
                    files.sortWith(Comparator { file1, file2 -> java.lang.Long.valueOf(file1.lastModified()).compareTo(file2.lastModified()) })

                    // Remove the file until the minimum size is achieved
                    for (file in files) {
                        if (dirSize > cache.minimumSize) {
                            dirSize -= FileUtils.getFileSizeInMB(file)
                            FileUtils.forceDelete(file)
                        } else {
                            break
                        }
                    }
                }
            }
        }
    }

    protected fun getFileSystemCaches(): List<Cache> {
        return listOf()
    }

    fun cleanFileSystemCache() {
        for (each in getFileSystemCaches()) {
            cleanFileSystemCache(each)
        }
    }

    fun cleanFileSystemCache(cache: Cache) {
        FileUtils.forceDelete(getFileSystemCacheDirectory(cache))
    }

    fun getFileSystemCacheDirectory(cache: Cache): File {
        return AbstractApplication.get().applicationContext.getDir(CACHE_DIRECTORY_PREFIX + cache.name, Context.MODE_PRIVATE)
    }

    companion object {

        private val LOGGER = LoggerUtils.getLogger(CacheManager::class.java)
        private const val CACHE_DIRECTORY_PREFIX = "cache_"
    }
}
