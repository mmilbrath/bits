/*
 * BackupBits
 * @author Chuck Slate
 * @author Michael Milbrath
 */
package net.backupbits.client;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.persistence.EntityManager;

import net.backupbits.common.ChannelConnectException;
import net.backupbits.common.ChannelReadException;
import net.backupbits.common.ChannelWriteException;
import net.backupbits.common.Digest;
import net.backupbits.common.FileUtils;
import net.backupbits.common.Verb;
import net.backupbits.domain.BackupItem;
import net.backupbits.domain.BackupItemLog;

/**
 * The Class MonitorImpl.
 */
public class MonitorImpl implements Monitor {

	/** The backup service. */
	private BackupService backupService;

	/** The entity manager. */
	private EntityManager entityManager;

	/** The digest. */
	private Digest digest;

	/** The Constant UPDATEDON_ORDER. */
	static final Comparator<BackupItemLog> UPDATEDON_ORDER = new Comparator<BackupItemLog>() {

		public int compare(BackupItemLog l1, BackupItemLog l2) {
			return l2.getUpdatedOn().compareTo(l1.getUpdatedOn());
		}
	};

	/* (non-Javadoc)
	 * @see net.backupbits.client.Monitor#checkFile(java.lang.String)
	 */
	public void checkFile(String filePath) throws FileNotFoundException,
			IOException, NoSuchAlgorithmException, ChannelConnectException,
			ChannelReadException, ChannelWriteException, SQLException {

		final File uncompressedFile = new File(filePath);
		if (!uncompressedFile.exists()) {
			throw new FileNotFoundException(uncompressedFile.getCanonicalPath());
		}

		if (uncompressedFile.isDirectory()) {
			for (String file : uncompressedFile.list()) {
				checkFile(uncompressedFile + File.separator + file);
			}
			return;
		}

		// query the database to see if the item has been backed up previously
		final List<BackupItem> backupItems = findWithName(uncompressedFile
				.getCanonicalPath());

		BackupItem backupItem;
		if (!backupItems.isEmpty()) {
			backupItem = backupItems.get(0);
		} else {
			backupItem = new BackupItem();
			backupItem.setItem(uncompressedFile.getCanonicalPath());
		}

		// get the uncompressed digest
		final byte[] uncompressedDigest = digest.digest(uncompressedFile);

		// get the latest log entry for the item
		final List<BackupItemLog> backupItemLogs = backupItem
				.getBackupItemLogs();
		Collections.sort(backupItemLogs, UPDATEDON_ORDER);

		// if the item has not been backed up or if the uncompressed digest
		// does not match the digest from the last time the item was backed up
		if (backupItemLogs.isEmpty()
				|| !MessageDigest.isEqual(uncompressedDigest, backupItemLogs
						.get(0).getUncompressedDigest())) {

			// compress the item
			final String compressedFileName = FileUtils
					.getBaseFileName(uncompressedFile)
					+ ".zip";
			final File compressedFile = File.createTempFile(compressedFileName,
					null);
			FileZipper.zip(uncompressedFile.getCanonicalPath(), compressedFile
					.getCanonicalPath());

			// backup the item to server
			final String status = backupService.backup(uncompressedFile
					.getCanonicalPath()
					+ ".zip", compressedFile.length(), new FileInputStream(
					compressedFile));

			// if backup was successful then record entry in the logs
			if (status.compareTo(Verb.SERVER_RESPONSE_BACKUP_SUCCESS) == 0) {

				final BackupItemLog backupItemLog = new BackupItemLog();

				backupItemLog.setStatus(status);
				backupItemLog.setCompressedItemName(uncompressedFile
						.getCanonicalPath()
						+ ".zip");
				backupItemLog.setServer(backupService.toString());
				backupItemLog.setUncompressedDigest(uncompressedDigest);
				backupItemLog
						.setCompressedDigest(digest.digest(compressedFile));

				backupItem.getBackupItemLogs().add(backupItemLog);

				entityManager.getTransaction().begin();
				entityManager.persist(backupItem);
				entityManager.getTransaction().commit();
			}

			// if the compressed file exists, delete it
			if (compressedFile.exists()) {
				compressedFile.delete();
			}

		}

	}

	/**
	 * Find with name.
	 *
	 * @param name
	 *            the name
	 * @return the list
	 */
	@SuppressWarnings("unchecked")
	public List<BackupItem> findWithName(String name) {
		return entityManager.createQuery(
				"SELECT b from BackupItem b WHERE b.item = :name")
				.setParameter("name", name).getResultList();
	}

	/* (non-Javadoc)
	 * @see net.backupbits.client.Monitor#setBackupService(net.backupbits.client.BackupService)
	 */
	public void setBackupService(BackupService backupService) {
		this.backupService = backupService;
	}

	/**
	 * Sets the dAO factory.
	 *
	 * @param daoFactory
	 *            the new dAO factory
	 */
	public void setDAOFactory(EntityManager daoFactory) {
		entityManager = daoFactory;
	}

	/* (non-Javadoc)
	 * @see net.backupbits.client.Monitor#setDigest(net.backupbits.common.Digest)
	 */
	public void setDigest(Digest digest) {
		this.digest = digest;
	}
}
