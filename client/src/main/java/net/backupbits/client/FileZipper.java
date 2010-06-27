/*
 * BackupBits
 * @author Chuck Slate
 * @author Michael Milbrath
 */
package net.backupbits.client;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * The Class FileZipper.
 */
public class FileZipper {

	/** The Constant bufferSize. */
	private static final int bufferSize = 2156;

	/**
	 * Make zip file.
	 * 
	 * @param sourceFileName
	 *            the source file name
	 * @param targetZipFileName
	 *            the target zip file name
	 * @return true, if successful
	 */
	private static boolean makeZipFile(String sourceFileName,
			String targetZipFileName) {
		try {

			// Get a File object from the file name
			final File sourceFile = new File(sourceFileName);

			// Create a ZipOutputStream.
			// This is ultimately our target Zip file.
			final ZipOutputStream targetZOS = new ZipOutputStream(
					new FileOutputStream(targetZipFileName));

			// If the file to be zipped is a dir...
			if (sourceFile.isDirectory()) {
				zipDir(sourceFile, targetZOS);
			}

			// If the file to be zipped is a single file...
			else {
				zipFile(sourceFile, targetZOS);
			}

			// close the stream
			targetZOS.close();

			return true;
		} catch (final Exception e) {
			e.printStackTrace();

			return false;
		}
	}

	/**
	 * Zip.
	 * 
	 * @param sourceFileName
	 *            the source file name
	 * @param zipFileName
	 *            the zip file name
	 * @return true, if successful
	 */
	public static boolean zip(String sourceFileName, String zipFileName) {
		return makeZipFile(sourceFileName, zipFileName);
	}

	/**
	 * Zip dir.
	 * 
	 * @param sourceFile
	 *            the source file
	 * @param targetZOS
	 *            the target zos
	 */
	private static void zipDir(File sourceFile, ZipOutputStream targetZOS) {
		try {

			// Get a listing of the directory content
			final String[] dirList = sourceFile.list();
			final byte[] readBuffer = new byte[bufferSize];
			int bytesIn = 0;

			// Loop through dirList, and zip the files
			for (final String element : dirList) {
				final File f = new File(sourceFile.getAbsoluteFile(), element);

				// Looking for subdirectories...
				if (f.isDirectory()) {

					// if the File object is a directory, call this
					// function again to add its content recursively
					zipDir(f, targetZOS);

					// loop again
					continue;
				}

				// if we reached here, the File object f was not a directory
				// create a FileInputStream on top of f
				final FileInputStream fis = new FileInputStream(f);

				// create a new zip entry
				final ZipEntry anEntry = new ZipEntry(f.getPath());

				// place the zip entry in the ZipOutputStream object
				targetZOS.putNextEntry(anEntry);

				// now write the content of the file to the ZipOutputStream
				while ((bytesIn = fis.read(readBuffer)) != -1) {
					targetZOS.write(readBuffer, 0, bytesIn);
				}

				// close the Stream
				fis.close();
			}
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Zip file.
	 * 
	 * @param souceFile
	 *            the souce file
	 * @param targetZOS
	 *            the target zos
	 */
	private static void zipFile(File souceFile, ZipOutputStream targetZOS) {
		try {
			final byte[] readBuffer = new byte[bufferSize];
			int bytesIn = 0;

			// if we reached here, the File object f was not a directory
			// create a FileInputStream on top of f
			final FileInputStream fis = new FileInputStream(souceFile
					.getAbsolutePath());

			// create a new zip entry
			final ZipEntry anEntry = new ZipEntry(souceFile.getAbsolutePath());

			// place the zip entry in the ZipOutputStream object
			targetZOS.putNextEntry(anEntry);

			// now write the content of the file to the ZipOutputStream
			while ((bytesIn = fis.read(readBuffer)) != -1) {
				targetZOS.write(readBuffer, 0, bytesIn);
			}

			// close the FileInputStream
			fis.close();
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}
}
