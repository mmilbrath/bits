/*
 * BackupBits
 * @author Chuck Slate
 * @author Michael Milbrath
 */
package net.backupbits.common;

/**
 * The Class ChannelWriteException.
 */
public class ChannelWriteException extends Exception {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 686306107615316994L;

	/** The Constant DEFAULT_MESSAGE. */
	private static final String DEFAULT_MESSAGE = "Error writing to the channel";

	/**
	 * Instantiates a new channel write exception.
	 */
	public ChannelWriteException() {
		super(DEFAULT_MESSAGE);
	}

	/**
	 * Instantiates a new channel write exception.
	 * 
	 * @param message
	 *            the message
	 */
	public ChannelWriteException(String message) {
		super(message);
	}
}
