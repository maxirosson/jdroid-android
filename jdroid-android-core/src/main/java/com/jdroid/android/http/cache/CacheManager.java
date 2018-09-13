package com.jdroid.android.http.cache;

import android.content.Context;
import android.support.annotation.WorkerThread;

import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.application.AppLaunchStatus;
import com.jdroid.java.collections.Lists;
import com.jdroid.java.http.cache.Cache;
import com.jdroid.java.http.cache.CachedHttpService;
import com.jdroid.java.utils.FileUtils;
import com.jdroid.java.utils.LoggerUtils;
import com.jdroid.java.utils.StreamUtils;

import org.slf4j.Logger;

import java.io.File;
import java.io.InputStream;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class CacheManager {

	private static final Logger LOGGER = LoggerUtils.getLogger(CacheManager.class);

	private static final String CACHE_DIRECTORY_PREFIX = "cache_";

	@WorkerThread
	public void initFileSystemCache() {
		try {

			// Sort the caches by priority
			List<Cache> caches = getFileSystemCaches();
			Collections.sort(caches, new Comparator<Cache>() {

				@Override
				public int compare(Cache cache1, Cache cache2) {
					return cache2.getPriority().compareTo(cache1.getPriority());
				}
			});

			for (Cache cache : caches) {
				populateFileSystemCache(cache);
				reduceFileSystemCache(cache);
			}
		} catch (Exception e) {
			AbstractApplication.get().getExceptionHandler().logHandledException(e);
		}
	}

	@SuppressWarnings("resource")
	public void populateFileSystemCache(Cache cache) {

		if (!AbstractApplication.get().getAppLaunchStatus().equals(AppLaunchStatus.NORMAL)) {

			Map<String, String> defaultContent = cache.getDefaultContent();

			if ((defaultContent != null) && !defaultContent.isEmpty()) {
				for (Map.Entry<String, String> entry : defaultContent.entrySet()) {
					InputStream source = AbstractApplication.class.getClassLoader().getResourceAsStream(
						"cache/" + entry.getKey());
					if (source != null) {
						File cacheFile = new File(getFileSystemCacheDirectory(cache),
							CachedHttpService.generateCacheFileName(entry.getValue()));
						FileUtils.copyStream(source, cacheFile);
						LOGGER.debug("Populated " + entry.toString() + " to " + cacheFile.getAbsolutePath());
						StreamUtils.safeClose(source);
					}
				}
				LOGGER.debug(cache.getName() + " cache populated");
			}
		}
	}

	public void reduceFileSystemCache(Cache cache) {
		if (cache.getMaximumSize() != null) {
			File dir = getFileSystemCacheDirectory(cache);

			// Verify if the cache should be clean
			if ((dir != null) && dir.exists()) {
				float dirSize = FileUtils.getDirectorySizeInMB(dir);
				LOGGER.info("Cache " + cache.getName() + " size: " + dirSize + " MB");
				if (dirSize > cache.getMaximumSize()) {
					// Sort the files by modification date, so we remove the not used files first
					List<File> files = Lists.newArrayList(dir.listFiles());
					Collections.sort(files, new Comparator<File>() {

						@Override
						public int compare(File file1, File file2) {
							return Long.valueOf(file1.lastModified()).compareTo(file2.lastModified());
						}
					});

					// Remove the file until the minimum size is achieved
					for (File file : files) {
						if (dirSize > cache.getMinimumSize()) {
							dirSize -= FileUtils.getFileSizeInMB(file);
							FileUtils.forceDelete(file);
						} else {
							break;
						}
					}
				}
			}
		}
	}

	protected List<Cache> getFileSystemCaches() {
		return Lists.newArrayList();
	}

	public void cleanFileSystemCache() {
		for (Cache each : getFileSystemCaches()) {
			cleanFileSystemCache(each);
		}
	}

	public void cleanFileSystemCache(Cache cache) {
		FileUtils.forceDelete(getFileSystemCacheDirectory(cache));
	}

	public File getFileSystemCacheDirectory(Cache cache) {
		return AbstractApplication.get().getApplicationContext().getDir(CACHE_DIRECTORY_PREFIX + cache.getName(), Context.MODE_PRIVATE);
	}
}
