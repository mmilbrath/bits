/*
 * BackupBits
 * @author Chuck Slate
 * @author Michael Milbrath
 */
package net.backupbits.client;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import net.backupbits.common.ChannelConnectException;
import net.backupbits.common.ChannelReadException;
import net.backupbits.common.ChannelWriteException;
import net.backupbits.common.FileHeader;

/**
 * The Interface BackupService.
 */
public interface BackupService {

	/**
	 * Backup.
	 *
	 * @param fileName
	 *            the file name
	 * @param fileSize
	 *            the file size
	 * @param inputStream
	 *            the input stream
	 * @return the string
	 * @throws ChannelConnectException
	 *             the channel connect exception
	 * @throws ChannelReadException
	 *             the channel read exception
	 * @throws ChannelWriteException
	 *             the channel write exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws FileNotFoundException
	 *             the file not found exception
	 * @throws NoSuchAlgorithmException
	 *             the no such algorithm exception
	 */
	public abstract String backup(String fileName, long fileSize,
			InputStream inputStream) throws ChannelConnectException,
			ChannelReadException, ChannelWriteException, IOException,
			FileNotFoundException, NoSuchAlgorithmException;

	/**
	 * Query.
	 *
	 * @return the array list
	 * @throws ChannelConnectException
	 *             the channel connect exception
	 * @throws ChannelWriteException
	 *             the channel write exception
	 * @throws ChannelReadException
	 *             the channel read exception
	 */
	public abstract ArrayList<FileHeader> query()
			throws ChannelConnectException, ChannelWriteException,
			ChannelReadException;

	/**
	 * Restore.
	 *
	 * @param fileID
	 *            the file id
	 * @return the input stream
	 * @throws ChannelConnectException
	 *             the channel connect exception
	 * @throws ChannelWriteException
	 *             the channel write exception
	 * @throws ChannelReadException
	 *             the channel read exception
	 */
	public abstract InputStream restore(int fileID)
			throws ChannelConnectException, ChannelWriteException,
			ChannelReadException;

}
