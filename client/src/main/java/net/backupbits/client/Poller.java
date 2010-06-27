/*
 * BackupBits
 * @author Chuck Slate
 * @author Michael Milbrath
 */
package net.backupbits.client;

/**
 * The Interface Poller.
 */
public interface Poller extends Runnable {

	/**
	 * Stop monitoring.
	 */
	public abstract void stopMonitoring();

}
