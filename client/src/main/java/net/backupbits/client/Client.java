/*
 * BackupBits
 * @author Chuck Slate
 * @author Michael Milbrath
 */
package net.backupbits.client;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import net.backupbits.common.MD5Digest;

import org.apache.log4j.PropertyConfigurator;

/**
 * The Class Client.
 */
public class Client {

	/*
	 * http://java.sun.com/developer/technicalArticles/J2SE/Desktop/persistenceapi
	 * /
	 */

	/**
	 * The main method.
	 *
	 * @param args
	 *            the arguments
	 */
	public static void main(String args[]) {
		new Client().start();
	}

	/** The poller. */
	FilePoller poller = null;

	/** The backup service. */
	BackupServiceImpl backupService = null;

	/** The prop. */
	Properties prop = null;

	/**
	 * Instantiates a new client.
	 */
	public Client() {
		EntityManagerFactory factory = Persistence
				.createEntityManagerFactory("default");
		javax.persistence.EntityManager em = factory.createEntityManager();

		Properties prop = new Properties();
		FileInputStream inputFileStream = null;
		try {
			inputFileStream = new FileInputStream("client.conf");
			prop.load(inputFileStream);
		} catch (Exception e) {
		} finally {
			try {
				inputFileStream.close();
			} catch (IOException e) {
			}
		}
		PropertyConfigurator.configureAndWatch(prop.getProperty(
				"log4j.configuration", "client.conf"));

		SocketChannel channel = new SocketChannel();
		channel.setHost(prop.getProperty("server.ip", "127.0.0.1"));
		channel.setPort(Integer.parseInt(prop
				.getProperty("server.port", "4430")));

		backupService = new BackupServiceImpl();
		backupService.setChannel(channel);

		MonitorImpl monitor = new MonitorImpl();
		monitor.setDAOFactory(em);
		monitor.setBackupService(backupService);
		monitor.setDigest(new MD5Digest());

		MonitoredFilesServiceImpl monitoredFilesService = new MonitoredFilesServiceImpl(
				prop.getProperty("client.monitor", "client_files.txt"));

		poller = new FilePoller();
		poller.setMonitor(monitor);
		poller.setMonitoredFilesService(monitoredFilesService);
		poller.setPollFrequency(Integer.parseInt(prop.getProperty(
				"monitor.interval", "60000")));
	}

	/**
	 * Gets the backup service.
	 *
	 * @return the backup service
	 */
	public BackupService getBackupService() {
		return backupService;
	}

	/**
	 * Start.
	 */
	public void start() {

		try {

			final Thread pollerThread = new Thread(poller);
			pollerThread.start();
			pollerThread.join();
			System.exit(0);
		} catch (final Exception err) {
			System.err.println(err.getMessage());
			System.exit(1);
		}
	}
}
