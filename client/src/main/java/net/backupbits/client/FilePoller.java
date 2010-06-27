/*
 * BackupBits
 * @author Chuck Slate
 * @author Michael Milbrath
 */
package net.backupbits.client;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Iterator;

import net.backupbits.common.ChannelConnectException;
import net.backupbits.common.ChannelReadException;
import net.backupbits.common.ChannelWriteException;

/**
 * The Class FilePoller.
 */
public class FilePoller implements Poller {

	/** The monitored files service. */
	private MonitoredFilesService monitoredFilesService;

	/** The monitor. */
	private Monitor monitor;

	/** The poll frequency. */
	private int pollFrequency = 10000;

	/** The stop monitoring. */
	private boolean stopMonitoring = true;

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Thread#run()
	 */
	public void run() {

		stopMonitoring = false;

		while (!stopMonitoring) {

			if ((monitor != null) && (monitoredFilesService != null)) {

				final Iterator<String> fileIterator = monitoredFilesService
						.getActiveFileList().iterator();

				while (fileIterator.hasNext() && !stopMonitoring) {
					try {
						monitor.checkFile(fileIterator.next());
					} catch (SQLException e) {
						e.printStackTrace();
					} catch (final FileNotFoundException e) {
						e.printStackTrace();
					} catch (final ChannelConnectException e) {
						e.printStackTrace();
					} catch (final ChannelReadException e) {
						e.printStackTrace();
					} catch (final ChannelWriteException e) {
						e.printStackTrace();
					} catch (final IOException e) {
						e.printStackTrace();
					} catch (final NoSuchAlgorithmException e) {
						e.printStackTrace();
					}
				}
			}

			try {
				Thread.sleep(pollFrequency);
			} catch (final InterruptedException e) {
			}
		}
	}

	/**
	 * Sets the monitor.
	 *
	 * @param monitor
	 *            the new monitor
	 */
	public void setMonitor(Monitor monitor) {
		this.monitor = monitor;
	}

	/**
	 * Sets the monitored files service.
	 *
	 * @param monitoredFilesService
	 *            the new monitored files service
	 */
	public void setMonitoredFilesService(
			MonitoredFilesService monitoredFilesService) {
		this.monitoredFilesService = monitoredFilesService;
	}

	/**
	 * Sets the poll frequency.
	 *
	 * @param pollFrequency
	 *            the new poll frequency
	 */
	public void setPollFrequency(int pollFrequency) {
		this.pollFrequency = pollFrequency;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.backupbits.client.ResourceMonitor#stopMonitoring()
	 */
	public void stopMonitoring() {
		stopMonitoring = true;
	}

}
