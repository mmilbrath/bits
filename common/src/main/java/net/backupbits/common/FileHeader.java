/*
 * BackupBits
 * @author Chuck Slate
 * @author Michael Milbrath
 */
package net.backupbits.common;

import java.io.Serializable;
import java.util.Date;

/**
 * The Class FileHeader.
 */
public class FileHeader implements Serializable {

	/** The Constant serialVersionUID. */
	public static final long serialVersionUID = 20100103;
	
	/** The _file id. */
	int _fileID = 0;
	
	/** The _file size. */
	int _fileSize = 0;
	
	/** The _upload time. */
	Date _uploadTime;
	
	/** The _file name. */
	String _fileName = "";

	/**
	 * Instantiates a new file header.
	 */
	public FileHeader() {
	}

	/**
	 * Instantiates a new file header.
	 * 
	 * @param fileID
	 *            the file id
	 * @param fileName
	 *            the file name
	 * @param fileSize
	 *            the file size
	 * @param uploadTime
	 *            the upload time
	 */
	FileHeader(int fileID, String fileName, int fileSize, Date uploadTime) {
		_fileID = fileID;
		_fileSize = fileSize;
		_uploadTime = uploadTime;
		_fileName = fileName;
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
	public int getFileSize() {
		return _fileSize;
	}

	/**
	 * Gets the file upload time.
	 * 
	 * @return the file upload time
	 */
	public Date getFileUploadTime() {
		return _uploadTime;
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
	public void setFileSize(int fileSize) {
		_fileSize = fileSize;
	}

	/**
	 * Sets the file time stamp.
	 * 
	 * @param uploadTime
	 *            the new file time stamp
	 */
	public void setFileTimeStamp(Date uploadTime) {
		_uploadTime = uploadTime;
	}

}
