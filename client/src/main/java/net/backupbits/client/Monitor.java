/*
 * BackupBits
 * @author Chuck Slate
 * @author Michael Milbrath
 */
package net.backupbits.client;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

import net.backupbits.common.ChannelConnectException;
import net.backupbits.common.ChannelReadException;
import net.backupbits.common.ChannelWriteException;
import net.backupbits.common.Digest;

/**
 * The Interface Monitor.
 */
public interface Monitor {

	/**
	 * Check file.
	 * 
	 * @param filePath
	 *            the file path
	 * @throws FileNotFoundException
	 *             the file not found exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws NoSuchAlgorithmException
	 *             the no such algorithm exception
	 * @throws ChannelConnectException
	 *             the channel connect exception
	 * @throws ChannelReadException
	 *             the channel read exception
	 * @throws ChannelWriteException
	 *             the channel write exception
	 * @throws SQLException
	 *             the sQL exception
	 */
	public abstract void checkFile(String filePath)
			throws FileNotFoundException, IOException,
			NoSuchAlgorithmException, ChannelConnectException,
			ChannelReadException, ChannelWriteException, SQLException;

	/**
	 * Sets the backup service.
	 * 
	 * @param backupService
	 *            the new backup service
	 */
	public abstract void setBackupService(BackupService backupService);

	/**
	 * Sets the digest.
	 * 
	 * @param digest
	 *            the new digest
	 */
	public abstract void setDigest(Digest digest);
}
