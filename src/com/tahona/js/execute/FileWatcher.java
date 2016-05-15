package com.tahona.js.execute;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class FileWatcher {

	private final Map<String, Long> lastModified = new HashMap<String, Long>();

	public void addToWatched(final File folder) {
		lastModified.put(folder.getAbsolutePath(), folder.lastModified());
	}

	public boolean isChanged(final File folder) {
		final String absolutePath = folder.getAbsolutePath();
		return false == lastModified.containsKey(absolutePath) || isFileModified(folder, absolutePath); 
	}

	private boolean isFileModified(final File folder, final String absolutePath) {
		return false == lastModified.get(absolutePath).equals(folder.lastModified());
	}

	public void clear() {
		lastModified.clear();
	}

}
