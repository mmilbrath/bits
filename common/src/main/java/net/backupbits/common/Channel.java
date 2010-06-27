/*
 * BackupBits
 * @author Chuck Slate
 * @author Michael Milbrath
 */
package net.backupbits.common;

import java.io.InputStream;

/**
 * The Interface Channel.
 */
public interface Channel {

	/**
	 * Close.
	 * 
	 * @throws ChannelConnectException
	 *             the channel connect exception
	 */
	public abstract void close() throws ChannelConnectException;

	/**
	 * Gets the input stream.
	 * 
	 * @return the input stream
	 * @throws ChannelConnectException
	 *             the channel connect exception
	 * @throws ChannelReadException
	 *             the channel read exception
	 */
	InputStream getInputStream() throws ChannelConnectException,
			ChannelReadException;

	/**
	 * Read.
	 * 
	 * @return the instruction
	 * @throws ChannelConnectException
	 *             the channel connect exception
	 * @throws ChannelReadException
	 *             the channel read exception
	 */
	public abstract Instruction read() throws ChannelConnectException,
			ChannelReadException;

	/**
	 * Write.
	 * 
	 * @param data
	 *            the data
	 * @param count
	 *            the count
	 * @throws ChannelConnectException
	 *             the channel connect exception
	 * @throws ChannelWriteException
	 *             the channel write exception
	 */
	public abstract void write(byte[] data, int count)
			throws ChannelConnectException, ChannelWriteException;

	/**
	 * Write.
	 * 
	 * @param i
	 *            the i
	 * @throws ChannelConnectException
	 *             the channel connect exception
	 * @throws ChannelWriteException
	 *             the channel write exception
	 */
	public abstract void write(Instruction i) throws ChannelConnectException,
			ChannelWriteException;
}
