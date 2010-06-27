/*
 * BackupBits
 * @author Chuck Slate
 * @author Michael Milbrath
 */
package net.backupbits.common;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

/**
 * The Interface Digest.
 */
public interface Digest {

	/**
	 * Digest.
	 * 
	 * @param data
	 *            the data
	 * @return the byte[]
	 * @throws NoSuchAlgorithmException
	 *             the no such algorithm exception
	 */
	public byte[] digest(byte[] data) throws NoSuchAlgorithmException;

	/**
	 * Digest.
	 * 
	 * @param file
	 *            the file
	 * @return the byte[]
	 * @throws FileNotFoundException
	 *             the file not found exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws NoSuchAlgorithmException
	 *             the no such algorithm exception
	 */
	public byte[] digest(File file) throws FileNotFoundException, IOException,
			NoSuchAlgorithmException;
}
