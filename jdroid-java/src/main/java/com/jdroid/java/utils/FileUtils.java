package com.jdroid.java.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.slf4j.Logger;
import com.jdroid.java.exception.UnexpectedException;

/**
 * This class contains functions for working with files within the application.
 * 
 * @author Maxi Rosson
 */
public abstract class FileUtils {
	
	private static final Logger LOGGER = LoggerUtils.getLogger(FileUtils.class);
	
	// Amount of bytes on a megabyte
	public static final int BYTES_TO_MB = 1048576;
	private static final int BUFFER_SIZE = 16384;
	
	/**
	 * Reads the inputStream and returns a byte array with all the information
	 * 
	 * @param inputStream The inputStream to be read
	 * @return A byte array with all the inputStream's information
	 * @throws IOException The exception thrown when an error reading the inputStream happens
	 */
	public static byte[] readAsBytes(InputStream inputStream) throws IOException {
		int cnt = 0;
		byte[] buffer = new byte[BUFFER_SIZE];
		InputStream is = new BufferedInputStream(inputStream);
		try {
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			cnt = is.read(buffer);
			while (cnt != -1) {
				outputStream.write(buffer, 0, cnt);
				cnt = is.read(buffer);
			}
			return outputStream.toByteArray();
		} finally {
			safeClose(is);
		}
	}
	
	/**
	 * Reads the file and returns a byte array with all the information
	 * 
	 * @param file The file to be read
	 * @return A byte array with all the file's information
	 * @throws IOException The exception thrown when an error reading the file happens
	 */
	@SuppressWarnings("resource")
	public static byte[] readAsBytes(File file) throws IOException {
		return readAsBytes(new FileInputStream(file));
	}
	
	/**
	 * @param filePath The path to the file
	 * @return a file
	 */
	public static File checkFile(String filePath) {
		File file = new File(filePath);
		if (!FileUtils.exist(filePath)) {
			file.mkdirs();
		}
		return file;
	}
	
	/**
	 * @param filePath The file path to the file for verify the existence
	 * @return True if exist a file with in the file path
	 */
	public static boolean exist(String filePath) {
		return new File(filePath).exists();
	}
	
	/**
	 * Deletes an instance of {@link File} even if it is a directory containing files.<br>
	 * If the file is a directory and has contents, then executes itself on every content.
	 * 
	 * @see File#delete()
	 * @param file The {@link File} to be deleted.
	 */
	public static void forceDelete(File file) {
		if (file.exists()) {
			
			// If the File instance to delete is a directory, delete all it's
			// contents.
			if (file.isDirectory()) {
				for (File contentFile : file.listFiles()) {
					FileUtils.forceDelete(contentFile);
				}
			}
			
			if (file.delete()) {
				LOGGER.debug("File " + file.getPath() + " was succesfully deleted.");
			} else {
				LOGGER.warn("File " + file.getPath() + " couldn't be deleted.");
			}
		}
	}
	
	/**
	 * Renames or moves a determined {@link File} instance to a destination defined by another {@link File} instance.<br>
	 * Differs from the {@link File#renameTo(File)} method in the fact that this method logs if the operation was
	 * successful.<br>
	 * 
	 * @see File#renameTo(File)
	 * @param fileToBeMoved The file to be renamed or moved.
	 * @param destination The {@link File} instance that denotes the new location
	 * @return <b>boolean</b> true if the file has been successfully renamed or moved.
	 */
	public static boolean renameOrMove(File fileToBeMoved, File destination) {
		boolean result = fileToBeMoved.renameTo(destination);
		if (result) {
			LOGGER.debug("File " + fileToBeMoved.getPath() + " was succesfully renamed or moved.");
		} else {
			LOGGER.error("File " + fileToBeMoved.getPath() + " couldn't be renamed or moved.");
		}
		return result;
	}
	
	public static File createTempFile() {
		File file = null;
		try {
			file = File.createTempFile("tempFile", ".tmp");
		} catch (IOException e) {
			throw new UnexpectedException(e);
		}
		return file;
	}
	
	public static File createTempDir() {
		File file = FileUtils.createTempFile();
		File dir = new File(file.getAbsolutePath() + "dir");
		dir.mkdir();
		return dir;
	}
	
	public static File toTempFile(String content) {
		
		File file = null;
		try {
			file = File.createTempFile("tempFile", ".tmp");
		} catch (IOException e) {
			throw new UnexpectedException(e);
		}
		try {
			PrintWriter printWriter = new PrintWriter(file);
			printWriter.append(content);
			printWriter.close();
			return file;
		} catch (FileNotFoundException e) {
			throw new UnexpectedException(e);
		}
	}
	
	public static void createFile(String content, String parentPath, String fileName) {
		try {
			new File(parentPath).mkdirs();
			PrintWriter printWriter = new PrintWriter(parentPath + File.separatorChar + fileName);
			printWriter.append(StringUtils.defaultString(content));
			printWriter.close();
		} catch (FileNotFoundException e) {
			throw new UnexpectedException(e);
		}
	}
	
	/**
	 * Receives a File and iterates over all its lines and returns a String.
	 * 
	 * @param file The file
	 * @return The content of the file as String
	 */
	public static String toString(File file) {
		try {
			return FileUtils.toString(new FileInputStream(file));
		} catch (FileNotFoundException e) {
			throw new UnexpectedException(
					new StringBuilder("The file doesn't exist [").append(file).append("]").toString(), e);
		}
	}
	
	public static String toString(InputStream in, Boolean closeStream) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		StringBuilder contentBuilder = new StringBuilder();
		String text = null;
		
		// repeat until all lines are read
		try {
			Boolean firstLine = true;
			while ((text = reader.readLine()) != null) {
				if (!firstLine) {
					contentBuilder.append(System.getProperty("line.separator"));
				}
				firstLine = false;
				contentBuilder.append(text);
			}
		} catch (IOException e) {
			throw new UnexpectedException("Error reading the stream", e);
		} finally {
			if (closeStream) {
				safeClose(in);
			}
		}
		return contentBuilder.toString();
	}
	
	/**
	 * Receives an InputStream and iterates over all its lines and returns a String.
	 * 
	 * @param in the InputStream to be converted
	 * @return The content of the file as String
	 */
	public static String toString(InputStream in) {
		return toString(in, true);
	}
	
	/**
	 * @param source the source {@link InputStream}
	 * @param destin the destin {@link File}
	 */
	@SuppressWarnings("resource")
	public static void copyStream(InputStream source, File destin) {
		FileOutputStream out = null;
		try {
			File dir = destin.getParentFile();
			if (dir != null) {
				dir.mkdirs();
			}
			out = new FileOutputStream(destin);
			FileUtils.copyStream(source, out);
		} catch (IOException e) {
			throw new UnexpectedException(
					new StringBuilder("Error copying the file to [").append(destin).append("]").toString(), e);
		} finally {
			safeClose(out);
		}
	}
	
	/**
	 * @param source the source {@link InputStream}
	 * @param destin the destin {@link OutputStream}
	 * @param closeOutputStream
	 */
	public static void copyStream(InputStream source, OutputStream destin, Boolean closeOutputStream) {
		try {
			int count = 0;
			byte[] buffer = new byte[FileUtils.BUFFER_SIZE];
			while ((count = source.read(buffer, 0, FileUtils.BUFFER_SIZE)) != -1) {
				destin.write(buffer, 0, count);
			}
		} catch (IOException e) {
			throw new UnexpectedException("Error copying file", e);
		} finally {
			if (closeOutputStream) {
				safeClose(destin);
			}
		}
	}
	
	/**
	 * @param source the source {@link InputStream}
	 * @param destin the destin {@link OutputStream}
	 */
	public static void copyStream(InputStream source, OutputStream destin) {
		copyStream(source, destin, true);
	}
	
	/**
	 * @param source the source {@link InputStream}
	 * @return the input stream that can be reset {@link ByteArrayInputStream}
	 */
	public static ByteArrayInputStream copy(InputStream source) {
		ByteArrayOutputStream tmp = new ByteArrayOutputStream();
		copyStream(source, tmp, true);
		return new ByteArrayInputStream(tmp.toByteArray());
	}
	
	public static File zipFile(String directoryToZipPath) {
		ZipOutputStream zipOutputStream = null;
		try {
			File zipFile = FileUtils.createTempFile();
			zipOutputStream = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(zipFile)));
			
			// Get a list of the files to zip
			File directoryToZip = new File(directoryToZipPath);
			zipFileItem(directoryToZipPath, zipOutputStream, directoryToZip, null);
			return zipFile;
		} catch (FileNotFoundException e) {
			throw new UnexpectedException(e);
		} finally {
			try {
				zipOutputStream.close();
			} catch (IOException e) {
				LOGGER.warn("Exception thrown when the zipOutputStream was being closed", e);
			}
		}
	}
	
	private static void zipFileItem(String directoryToZipPath, ZipOutputStream zipOutputStream, File fileItem,
			String parentItemPath) {
		
		try {
			String files[] = fileItem.list();
			
			for (int i = 0; i < files.length; i++) {
				String itemRelativePath = (parentItemPath != null ? parentItemPath + File.separatorChar : "")
						+ files[i];
				File itemFile = new File(directoryToZipPath + File.separatorChar + itemRelativePath);
				if (itemFile.isDirectory()) {
					FileUtils.zipFileItem(directoryToZipPath, zipOutputStream, itemFile, itemRelativePath);
				} else {
					FileInputStream entryInputStream = new FileInputStream(fileItem.getAbsolutePath()
							+ File.separatorChar + files[i]);
					ZipEntry entry = new ZipEntry(itemRelativePath);
					zipOutputStream.putNextEntry(entry);
					FileUtils.copyStream(entryInputStream, zipOutputStream, false);
					entryInputStream.close();
				}
			}
		} catch (FileNotFoundException e) {
			throw new UnexpectedException(e);
		} catch (IOException e) {
			throw new UnexpectedException(e);
		}
	}
	
	public static void safeClose(Closeable closeable) {
		if (closeable != null) {
			try {
				closeable.close();
			} catch (IOException e) {
				LOGGER.warn("Exception thrown when trying to close the closeable", e);
			}
		}
	}
	
	/**
	 * Counts the size of a directory recursively (sum of the length of all files).
	 * 
	 * @param directory directory to inspect, must not be null
	 * @return size of directory in bytes, 0 if directory is security restricted
	 */
	public static long getDirectorySize(File directory) {
		if (!directory.exists()) {
			throw new IllegalArgumentException(directory + " does not exist");
		}
		
		if (!directory.isDirectory()) {
			throw new IllegalArgumentException(directory + " is not a directory");
		}
		long size = 0;
		File[] files = directory.listFiles();
		if (files == null) {
			// null if security restricted
			return 0L;
		}
		for (int i = 0; i < files.length; i++) {
			File file = files[i];
			
			if (file.isDirectory()) {
				size += getDirectorySize(file);
			} else {
				size += file.length();
			}
		}
		return size;
	}
}
