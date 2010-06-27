/*
 * BackupBits
 * @author Chuck Slate
 * @author Michael Milbrath
 */
package net.backupbits.client;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * The Class MonitoredFilesServiceImpl.
 */
public class MonitoredFilesServiceImpl implements MonitoredFilesService {

	/** The location. */
	private final String location;

	/**
	 * Instantiates a new monitored files service impl.
	 *
	 * @param location
	 *            the location
	 */
	public MonitoredFilesServiceImpl(String location) {
		this.location = location;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.backupbits.client.MonitoredFiles#getActiveFileList()
	 */
	public List<String> getActiveFileList() {
		final ArrayList<String> list = new ArrayList<String>();

		try {
			final File monitored_files = new File(location);

			// use buffering, reading one line at a time
			// FileReader always assumes default encoding is OK!
			final BufferedReader input = new BufferedReader(new FileReader(
					monitored_files));

			try {
				String line = null; // not declared within while loop

				/*
				 * readLine is a bit quirky : it returns the content of a line
				 * MINUS the newline. it returns null only for the END of the
				 * stream. it returns an empty String if two newlines appear in
				 * a row.
				 */
				while ((line = input.readLine()) != null) {
					list.add(line);
				}
			} finally {
				input.close();
			}
		} catch (final IOException ex) {
			ex.printStackTrace();
		}

		return list;
	}
}
