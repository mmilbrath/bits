/*
 * BackupBits
 * @author Chuck Slate
 * @author Michael Milbrath
 */
package net.backupbits.common;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * The Class MD5Digest.
 */
public class MD5Digest implements Digest {

	/** The Constant log. */
	private static final Log log = LogFactory.getLog(MD5Digest.class);

	/** The buffer size. */
	private int bufferSize = 1024;

	/** The byte_array. */
	private final byte[] byte_array = new byte[bufferSize];

	/*
	 * (non-Javadoc)
	 *
	 * @see com.backupbits.common.Digest#digest(byte[])
	 */
	public byte[] digest(byte[] data) throws NoSuchAlgorithmException {
		final MessageDigest digest = MessageDigest.getInstance("MD5");

		digest.reset();
		digest.update(data);

		return digest.digest();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.backupbits.common.Digest#digest(java.io.File)
	 */
	public byte[] digest(File file) throws FileNotFoundException, IOException,
			NoSuchAlgorithmException {
		final MessageDigest digest = MessageDigest.getInstance("MD5");
		digest.reset();
		digestFile(digest, file);
		return digest.digest();
	}

	/**
	 * Digest file.
	 *
	 * @param digest
	 *            the digest
	 * @param file
	 *            the file
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws NoSuchAlgorithmException
	 *             the no such algorithm exception
	 */
	private void digestFile(MessageDigest digest, File file)
			throws IOException, NoSuchAlgorithmException {

		log.debug(file.getCanonicalFile());

		if (!file.isDirectory()) {
			final FileInputStream fis = new FileInputStream(file);
			final BufferedInputStream bis = new BufferedInputStream(fis);

			int bytes_read = 0;
			while (bis.available() > 0) {
				bytes_read = bis.read(byte_array, 0, bufferSize);
				digest.update(byte_array, 0, bytes_read);
			}
		} else {
			final File[] list = file.listFiles();
			for (final File element : list) {
				digestFile(digest, element);
			}
		}
	}

	/**
	 * Sets the buffer size.
	 *
	 * @param bufferSize
	 *            the new buffer size
	 */
	public void setBufferSize(int bufferSize) {
		this.bufferSize = bufferSize;
	}
}
