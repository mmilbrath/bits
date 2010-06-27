/*
 * BackupBits
 * @author Chuck Slate
 * @author Michael Milbrath
 */
package net.backupbits.client;

import java.util.List;

/**
 * The Interface MonitoredFilesService.
 */
public interface MonitoredFilesService {

	/**
	 * Gets the active file list.
	 * 
	 * @return the active file list
	 */
	public List<String> getActiveFileList();
}
