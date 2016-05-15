package com.tahona.js.execute;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import com.tahona.js.tool.Logger;
import com.yahoo.platform.yui.compressor.JavaScriptCompressor;

public class JsOneBuilder {

	private static final String NEW_LINE = "\r\n";
	private static final String INTEND = "                 ";
	// key = source folders
	// value = result file
	private final TreeMap<String, String> catalogToFileMap;
	private final Map<String, String> processedFiles = new LinkedHashMap<String, String>();

	private final FileWatcher fileWatcher = new FileWatcher();

	public JsOneBuilder(final TreeMap<String, String> map) {
		this.catalogToFileMap = map;
	}

	public void build() throws IOException {
		// cannot be cached because of delete case
		fileWatcher.clear();
		processedFiles.clear();

		for (final String keys : catalogToFileMap.navigableKeySet()) {
			processedFiles.clear();
			final File folder = new File(keys);
			processFilesInFolder(folder);
			saveToFile(catalogToFileMap.get(keys));
		}
	}

	private void saveToFile(final String saveFilePath) throws IOException {
		final File file = getOrCreateFile(saveFilePath);
		FileUtils.writeStringToFile(file, compileAll(processedFiles));

		//Generate development file
		final File specialDev = getOrCreateFile(createDevName(saveFilePath));
		FileUtils.writeStringToFile(specialDev, generateDevelopmentString(processedFiles));
	}

	private String generateDevelopmentString(final Map<String, String> processedFiles) {
		final StringBuilder b = new StringBuilder();
		for (final String filePath : processedFiles.keySet()) {
			b.append(generateComment(filePath));
			b.append(NEW_LINE);
			b.append(processedFiles.get(filePath));
		}
		return b.toString();
	}

	private Object generateComment(final String filePath) {
		final StringBuilder b = new StringBuilder();
		b.append(NEW_LINE);
		b.append(NEW_LINE);
		b.append("/**");
		b.append(NEW_LINE);
		b.append("* file: ");
		b.append(filePath);
		b.append(NEW_LINE);
		b.append("*/");

		return b.toString();
	}

	private String createDevName(final String saveFilePath) {
		final String name = saveFilePath.replace(".js", "");
		return name + "-dev.js";
	}

	private File getOrCreateFile(final String saveFilePath) {
		final File file = new File(saveFilePath);
		if (false == file.exists()) {
			try {
				file.createNewFile();
			} catch (final IOException e) {
				e.printStackTrace();
			}
		}
		return file;
	}

	private final StringErrorReporter errorReporter = new StringErrorReporter();

	private String compileAll(final Map<String, String> processedFiles) {
		final List<String> list = new ArrayList<String>();

		for (final String keys : processedFiles.keySet()) {
			final String fileAsString = processedFiles.get(keys);

			final StringReader r = new StringReader(fileAsString);
			final StringWriter w = new StringWriter();

			try {
				errorReporter.setFile(keys);
				final JavaScriptCompressor c = new JavaScriptCompressor(r, errorReporter);
				c.compress(w, 10000, true, false, false, false);
				list.add(w.toString());
			} catch (final Exception e) {
				final List<String> errorsMessage = errorReporter.getMessages();
				errorsMessage.add(0, "ERROR: " + keys + "");
				Logger.d(StringUtils.join(errorsMessage, " \n " + INTEND));
			}
		}

		return StringUtils.join(list, " \n");
	}

	private void processFilesInFolder(final File folder) {
		if (false == folder.exists()) {
			throw new IllegalArgumentException(folder.getAbsolutePath() + " does not exist!");
		}
		if (false == folder.isDirectory()) {
			throw new IllegalArgumentException(folder.getAbsolutePath() + " is not a folder!");
		}

		final String[] filesOrFolders = folder.list();

		final List<File> directories = new ArrayList<File>();

		for (final String fileOrFolder : filesOrFolders) {
			final File file = new File(buildFileFullPath(folder, fileOrFolder));
			if (file.exists() && file.isFile() && fileOrFolder.contains(".js")) {
				processFile(file);
			} else if (file.exists() && file.isDirectory()) {
				directories.add(file);
			}
		}

		for (final File directory : directories) {
			processFilesInFolder(directory);
		}
	}

	private String buildFileFullPath(final File folder, final String fileOrFolder) {
		return folder.getAbsoluteFile() + "/" + fileOrFolder;
	}

	private void processFile(final File file) {
		fileWatcher.addToWatched(file);
		try {
			processedFiles.put(file.getAbsolutePath(), FileUtils.readFileToString(file));
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

	public boolean isResourceChanged() {
		final String[] array = (String[]) catalogToFileMap.navigableKeySet().toArray(new String[] {});
		for (final String string : array) {
			if (hasFileChanged(string)) {
				return true;
			}
		}
		return false;
	}

	private boolean hasAnyFileInFolderChanged(final File folder, final String[] array) {
		if (array != null) {
			for (final String fileOrFolder : array) {
				if (hasFileChanged(buildFileFullPath(folder, fileOrFolder))) {
					return true;
				}
			}
		}
		return false;
	}

	private boolean hasFileChanged(final String fullFilepaths) {
		final File folder = new File(fullFilepaths);
		boolean isFileChanged = folder.isFile() && fileWatcher.isChanged(folder);
		
		return isFileChanged || hasAnyFileInFolderChanged(folder, folder.list());
	}
}
