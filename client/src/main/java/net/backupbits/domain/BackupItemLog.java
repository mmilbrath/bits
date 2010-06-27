/*
 * BackupBits
 * @author Chuck Slate
 * @author Michael Milbrath
 */
package net.backupbits.domain;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * The Class BackupItemLog.
 */
@Entity
@Table(name = "BACKUPITEM_LOG")
public class BackupItemLog implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The id. */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID")
	private int id;

	/** The backup item. */
	@ManyToOne
	private BackupItem backupItem;

	/** The updated on. */
	@Column(name = "UPDATED_ON", nullable = false)
	private Timestamp updatedOn = new Timestamp(new Date().getTime());

	/** The status. */
	@Column(name = "STATUS")
	private String status;

	/** The server. */
	@Column(name = "SERVER")
	private String server;

	/** The compressed item name. */
	@Column(name = "COMPRESSED_ITEM")
	private String compressedItemName;

	/** The compressed digest. */
	@Column(name = "COMPRESSED_DIGEST")
	private byte[] compressedDigest;

	/** The uncompressed digest. */
	@Column(name = "UNCOMPRESSED_DIGEST")
	private byte[] uncompressedDigest;

	/**
	 * Gets the backup item.
	 * 
	 * @return the backup item
	 */
	public BackupItem getBackupItem() {
		return backupItem;
	}

	/**
	 * Gets the compressed digest.
	 * 
	 * @return the compressed digest
	 */
	public byte[] getCompressedDigest() {
		return compressedDigest;
	}

	/**
	 * Gets the compressed item name.
	 * 
	 * @return the compressed item name
	 */
	public String getCompressedItemName() {
		return compressedItemName;
	}

	/**
	 * Gets the id.
	 * 
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * Gets the server.
	 * 
	 * @return the server
	 */
	public String getServer() {
		return server;
	}

	/**
	 * Gets the status.
	 * 
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * Gets the uncompressed digest.
	 * 
	 * @return the uncompressed digest
	 */
	public byte[] getUncompressedDigest() {
		return uncompressedDigest;
	}

	/**
	 * Gets the updated on.
	 * 
	 * @return the updated on
	 */
	public Timestamp getUpdatedOn() {
		return updatedOn;
	}

	/**
	 * Sets the compressed digest.
	 * 
	 * @param compressedDigest
	 *            the new compressed digest
	 */
	public void setCompressedDigest(byte[] compressedDigest) {
		this.compressedDigest = compressedDigest;
	}

	/**
	 * Sets the compressed item name.
	 * 
	 * @param compressedItemName
	 *            the new compressed item name
	 */
	public void setCompressedItemName(String compressedItemName) {
		this.compressedItemName = compressedItemName;
	}

	/**
	 * Sets the server.
	 * 
	 * @param server
	 *            the new server
	 */
	public void setServer(String server) {
		this.server = server;
	}

	/**
	 * Sets the status.
	 * 
	 * @param status
	 *            the new status
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * Sets the uncompressed digest.
	 * 
	 * @param uncompressedDigest
	 *            the new uncompressed digest
	 */
	public void setUncompressedDigest(byte[] uncompressedDigest) {
		this.uncompressedDigest = uncompressedDigest;
	}

	/**
	 * Sets the updated on.
	 * 
	 * @param updatedOn
	 *            the new updated on
	 */
	public void setUpdatedOn(Timestamp updatedOn) {
		this.updatedOn = updatedOn;
	}

}
