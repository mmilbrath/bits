/*
 * BackupBits
 * @author Chuck Slate
 * @author Michael Milbrath
 */
package net.backupbits.common;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * The Class Instruction.
 */
public class Instruction implements Serializable {

	/**
	 * The Enum Category.
	 */
	public static enum Category {

		/** The BACKUP. */
		BACKUP,

		/** The RESTORE. */
		RESTORE,

		/** The UNKNOWN. */
		UNKNOWN
	}

	/** The Constant serialVersionUID. */
	public static final long serialVersionUID = 20060911;

	/** The _user name. */
	private String _userName = null;

	/** The _file name. */
	private String _fileName = null;

	/** The _file id. */
	private int _fileID = 0;

	// The objects are used to pass data. The actual data and the way to
	// interpret
	// it will depend directly on the verb
	/** The _file size. */
	private long _fileSize = 0;

	/** The _notes. */
	private String _notes = null;

	/** The _file list. */
	private ArrayList<FileHeader> _fileList = new ArrayList<FileHeader>();

	// The "verb" is the action to take
	/** The _verb. */
	private String _verb = null;

	/** The client version. */
	private long clientVersion = 2008010201;

	// Sets the server and client versions
	// Format = YYYYMMDD##
	// E.g., the fourth release of the day on May 2, 2003 would be
	// 2003050204
	/** The server version. */
	private final long serverVersion = 2009050201;

	;

	/**
	 * Instantiates a new instruction.
	 */
	public Instruction() {
		this(null, null, 0);
	}

	/**
	 * Instantiates a new instruction.
	 * 
	 * @param verb
	 *            the verb
	 * @param userName
	 *            the user name
	 */
	public Instruction(String verb, String userName) {
		this(userName, verb, 0);
	}

	/**
	 * Instantiates a new instruction.
	 * 
	 * @param userName
	 *            the user name
	 * @param verb
	 *            the verb
	 * @param fileSize
	 *            the file size
	 */
	public Instruction(String userName, String verb, long fileSize) {
		_verb = verb;
		_fileSize = fileSize;
		_userName = userName;
	}

	/**
	 * Instantiates a new instruction.
	 * 
	 * @param userName
	 *            the user name
	 * @param verb
	 *            the verb
	 * @param fileSize
	 *            the file size
	 * @param fileName
	 *            the file name
	 */
	public Instruction(String userName, String verb, long fileSize,
			String fileName) {
		_verb = verb;
		_fileSize = fileSize;
		_fileName = fileName;
		_userName = userName;
	}

	/**
	 * Gets the client version.
	 * 
	 * @return the client version
	 */
	public long getClientVersion() {
		return clientVersion;
	}

	/**
	 * Gets the file id.
	 * 
	 * @return the file id
	 */
	public int getFileID() {
		return _fileID;
	}

	/**
	 * Gets the file list.
	 * 
	 * @return the file list
	 */
	public ArrayList<FileHeader> getFileList() {
		return _fileList;
	}

	/**
	 * Gets the file name.
	 * 
	 * @return the file name
	 */
	public String getFileName() {
		return _fileName;
	}

	/**
	 * Gets the file size.
	 * 
	 * @return the file size
	 */
	public long getFileSize() {
		return _fileSize;
	}

	/**
	 * Gets the notes.
	 * 
	 * @return the notes
	 */
	public String getNotes() {
		return _notes;
	}

	/**
	 * Gets the server version.
	 * 
	 * @return the server version
	 */
	public long getServerVersion() {
		return serverVersion;
	}

	/**
	 * Gets the user name.
	 * 
	 * @return the user name
	 */
	public String getUserName() {
		return _userName;
	}

	/**
	 * Gets the verb.
	 * 
	 * @return the verb
	 */
	public String getVerb() {
		return _verb;
	}

	/**
	 * Reset.
	 */
	public void reset() {
		_verb = null;
		_fileSize = 0;
		_notes = null;
		_fileName = null;
		_userName = null;
	}

	/**
	 * Sets the client version.
	 * 
	 * @param newClientVersion
	 *            the new client version
	 */
	public void setClientVersion(long newClientVersion) {
		clientVersion = newClientVersion;
	}

	/**
	 * Sets the file id.
	 * 
	 * @param fileID
	 *            the new file id
	 */
	public void setFileID(int fileID) {
		_fileID = fileID;
	}

	/**
	 * Sets the file list.
	 * 
	 * @param fileList
	 *            the new file list
	 */
	public void setFileList(ArrayList<FileHeader> fileList) {
		_fileList = fileList;
	}

	/**
	 * Sets the file name.
	 * 
	 * @param fileName
	 *            the new file name
	 */
	public void setFileName(String fileName) {
		_fileName = fileName;
	}

	/**
	 * Sets the file size.
	 * 
	 * @param fileSize
	 *            the new file size
	 */
	public void setFileSize(long fileSize) {
		_fileSize = fileSize;
	}

	/**
	 * Sets the notes.
	 * 
	 * @param notes
	 *            the new notes
	 */
	public void setNotes(String notes) {
		_notes = notes;
	}

	/**
	 * Sets the user name.
	 * 
	 * @param userName
	 *            the new user name
	 */
	public void setUserName(String userName) {
		_userName = userName;
	}

	/**
	 * Sets the verb.
	 * 
	 * @param verb
	 *            the new verb
	 */
	public void setVerb(String verb) {
		_verb = verb;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuffer b = new StringBuffer();
		b.append("verb : " + _verb + ",");
		if (_fileName != null) {
			b.append("fileName : " + _fileName + ",");
			b.append("fileSize : " + _fileSize);
		}
		if (_userName != null) {
			b.append("Username : " + _userName);
		}
		return b.toString();
	}
}
