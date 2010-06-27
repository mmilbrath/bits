/*
 * BackupBits
 * @author Chuck Slate
 * @author Michael Milbrath
 */
package net.backupbits.client;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import net.backupbits.common.Channel;
import net.backupbits.common.ChannelConnectException;
import net.backupbits.common.ChannelReadException;
import net.backupbits.common.ChannelWriteException;
import net.backupbits.common.FileHeader;
import net.backupbits.common.Instruction;
import net.backupbits.common.Verb;

/**
 * The Class BackupServiceImpl.
 */
public class BackupServiceImpl implements BackupService {

	/** The buffer size. */
	private int bufferSize = 1024;

	/** The channel. */
	private Channel channel;

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.backupbits.client.BackupService#backup(com.backupbits.domain.BackupItem
	 * )
	 */
	public String backup(String fileName, long fileSize, InputStream inputStream)
			throws ChannelConnectException, ChannelWriteException,
			ChannelReadException, IOException, FileNotFoundException,
			NoSuchAlgorithmException {

		String status = Verb.SERVER_RESPONSE_BACKUP_FAILURE;

		Instruction action = new Instruction();

		action.setVerb(Verb.CLIENT_REQUEST_BACKUP_FILE);
		action.setFileName(fileName);
		action.setFileSize(fileSize);
		channel.write(action);

		final BufferedInputStream bis = new BufferedInputStream(inputStream);
		final byte[] byte_array = new byte[bufferSize];
		while (bis.available() > 0) {
			channel.write(byte_array, bis.read(byte_array, 0, bufferSize));
		}

		// read the status
		action = channel.read();
		status = action.getVerb();

		// send quit request
		action.reset();
		action.setVerb(Verb.CLIENT_REQUEST_CLOSE_CONNECTION);
		channel.write(action);

		channel.close();

		return status;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.backupbits.client.BackupService#query()
	 */
	public ArrayList<FileHeader> query() throws ChannelConnectException,
			ChannelWriteException, ChannelReadException {
		Instruction action = new Instruction();

		action.setVerb(Verb.CLIENT_REQUEST_GET_FILELIST);
		channel.write(action);

		action = channel.read();

		final ArrayList<FileHeader> list = action.getFileList();

		// send quit request
		action.reset();
		action.setVerb(Verb.CLIENT_REQUEST_CLOSE_CONNECTION);
		channel.write(action);

		channel.close();

		return list;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see net.backupbits.client.BackupService#restore(int)
	 */
	public InputStream restore(int fileID) throws ChannelConnectException,
			ChannelWriteException, ChannelReadException {
		final Instruction action = new Instruction();

		action.setVerb(Verb.CLIENT_REQUEST_RESTORE_FILE);
		action.setFileID(fileID);
		channel.write(action);

		return channel.getInputStream();
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

	/**
	 * Sets the channel.
	 *
	 * @param channel
	 *            the new channel
	 */
	public void setChannel(Channel channel) {
		this.channel = channel;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		final StringBuffer b = new StringBuffer();
		b.append(channel);
		return b.toString();
	}

}
