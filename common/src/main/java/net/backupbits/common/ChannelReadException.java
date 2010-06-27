/*
 * BackupBits
 * @author Chuck Slate
 * @author Michael Milbrath
 */
package net.backupbits.common;

/**
 * The Class ChannelReadException.
 */
public class ChannelReadException extends Exception {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1696230504914837882L;

	/** The Constant DEFAULT_MESSAGE. */
	private static final String DEFAULT_MESSAGE = "Error reading from the channel";

	/**
	 * Instantiates a new channel read exception.
	 */
	public ChannelReadException() {
		super(DEFAULT_MESSAGE);
	}

	/**
	 * Instantiates a new channel read exception.
	 * 
	 * @param message
	 *            the message
	 */
	public ChannelReadException(String message) {
		super(message);
	}
}