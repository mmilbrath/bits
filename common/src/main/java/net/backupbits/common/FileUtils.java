/*
 * BackupBits
 * @author Chuck Slate
 * @author Michael Milbrath
 */
package net.backupbits.common;

import java.io.File;

/**
 * The Class FileUtils.
 */
public class FileUtils {

	/**
	 * Gets the base extension.
	 * 
	 * @param file
	 *            the file
	 * @return the base extension
	 */
	public static String getBaseExtension(File file) {
		final int index = file.getName().lastIndexOf(".");
		if ((index > 0) && (index <= file.getName().length() - 2)) {
			return file.getName().substring(index + 1, file.getName().length());
		}
		return null;
	}

	/**
	 * Gets the base file name.
	 * 
	 * @param file
	 *            the file
	 * @return the base file name
	 */
	public static String getBaseFileName(File file) {
		final int index = file.getName().lastIndexOf(".");
		if ((index > 0) && (index <= file.getName().length() - 2)) {
			return file.getName().substring(0, index);
		}
		return file.getName();
	}

}
