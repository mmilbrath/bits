/*
 * BackupBits
 * @author Chuck Slate
 * @author Michael Milbrath
 */
package net.backupbits.common;

/**
 * The Interface Verb.
 */
public interface Verb {

	// Client request for backup
	/** The Constant CLIENT_REQUEST_BACKUP_FILE. */
	public static final String CLIENT_REQUEST_BACKUP_FILE = "CLIENT_REQUEST_BACKUP_FILE";

	// Client request for restore
	/** The Constant CLIENT_REQUEST_RESTORE_FILE. */
	public static final String CLIENT_REQUEST_RESTORE_FILE = "CLIENT_REQUEST_RESTORE_FILE";

	// Client request for list of backed up files
	/** The Constant CLIENT_REQUEST_GET_FILELIST. */
	public static final String CLIENT_REQUEST_GET_FILELIST = "CLIENT_REQUEST_GET_FILELIST";

	// Close the connection
	/** The Constant CLIENT_REQUEST_CLOSE_CONNECTION. */
	public static final String CLIENT_REQUEST_CLOSE_CONNECTION = "CLIENT_REQUEST_CLOSE_CONNECTION";

	// Server response to failed back up
	/** The Constant SERVER_RESPONSE_BACKUP_FAILURE. */
	public static final String SERVER_RESPONSE_BACKUP_FAILURE = "SERVER_RESPONSE_BACKUP_FAILURE";

	// Server response that it cannot even try backup (not enough allocated
	// space?)
	/** The Constant SERVER_RESPONSE_BACKUP_REJECTED. */
	public static final String SERVER_RESPONSE_BACKUP_REJECTED = "SERVER_RESPONSE_BACKUP_REJECTED";

	// Server response to good back up
	/** The Constant SERVER_RESPONSE_BACKUP_SUCCESS. */
	public static final String SERVER_RESPONSE_BACKUP_SUCCESS = "SERVER_RESPONSE_BACKUP_SUCCESS";

	// Server response to failed restore
	/** The Constant SERVER_RESPONSE_RESTORE_FAILURE. */
	public static final String SERVER_RESPONSE_RESTORE_FAILURE = "SERVER_RESPONSE_RESTORE_FAILURE";

	// Server response that it cannot even try restore (bad file ID? passed in)
	/** The Constant SERVER_RESPONSE_RESTORE_REJECTED. */
	public static final String SERVER_RESPONSE_RESTORE_REJECTED = "SERVER_RESPONSE_RESTORE_REJECTED";

	// Server response that it can proceed with restore (file ID good)
	/** The Constant SERVER_RESPONSE_RESTORE_ACCEPTED. */
	public static final String SERVER_RESPONSE_RESTORE_ACCEPTED = "SERVER_RESPONSE_RESTORE_ACCEPTED";

	// Server response to good restore
	/** The Constant SERVER_RESPONSE_RESTORE_SUCCESS. */
	public static final String SERVER_RESPONSE_RESTORE_SUCCESS = "SERVER_RESPONSE_RESTORE_SUCCESS";

	// Server response containing list of files
	/** The Constant SERVER_RESPONSE_RETURN_FILELIST. */
	public static final String SERVER_RESPONSE_RETURN_FILELIST = "SERVER_RESPONSE_RETURN_FILELIST";

	// public static final String SEND_FILE = "SEND_FILE";
	// The client has sent a request the server doesn't understand
	/** The Constant SERVER_RESPONSE_INVALID_VERB. */
	public static final String SERVER_RESPONSE_INVALID_VERB = "SERVER_RESPONSE_INVALID_VERB";
}
