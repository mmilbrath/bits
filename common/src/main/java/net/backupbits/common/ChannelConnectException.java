/*
 * BackupBits
 * @author Chuck Slate
 * @author Michael Milbrath
 */
package net.backupbits.common;

/**
 * The Class ChannelConnectException.
 */
public class ChannelConnectException extends Exception {

	/** The Constant DEFAULT_MESSAGE. */
	private static final String DEFAULT_MESSAGE = "Error connecting to channel";

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 8675309;

	/**
	 * Instantiates a new channel connect exception.
	 */
	public ChannelConnectException() {
		super(DEFAULT_MESSAGE);
	}

	/**
	 * Instantiates a new channel connect exception.
	 * 
	 * @param message
	 *            the message
	 */
	public ChannelConnectException(String message) {
		super(message);
	}
}
