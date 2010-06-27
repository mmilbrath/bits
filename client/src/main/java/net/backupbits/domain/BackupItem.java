/*
 * BackupBits
 * @author Chuck Slate
 * @author Michael Milbrath
 */
package net.backupbits.domain;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * The Class BackupItem.
 */
@Entity
@Table(name = "BACKUPITEM")
public class BackupItem implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The id. */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID")
	private int id;

	/** The updated on. */
	@Column(name = "UPDATED_ON", nullable = false)
	private Timestamp updatedOn = new Timestamp(new Date().getTime());

	/** The item. */
	@Column(name = "ITEM")
	private String item;

	/** The backup item logs. */
	@OneToMany(fetch = FetchType.EAGER, cascade = { CascadeType.ALL }, mappedBy = "backupItem")
	private List<BackupItemLog> backupItemLogs = new ArrayList<BackupItemLog>();

	/**
	 * Gets the backup item logs.
	 * 
	 * @return the backup item logs
	 */
	public List<BackupItemLog> getBackupItemLogs() {
		return backupItemLogs;
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
	 * Gets the item.
	 * 
	 * @return the item
	 */
	public String getItem() {
		return item;
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
	 * Sets the backup item logs.
	 * 
	 * @param backupItemLogs
	 *            the new backup item logs
	 */
	public void setBackupItemLogs(List<BackupItemLog> backupItemLogs) {
		this.backupItemLogs = backupItemLogs;
	}

	/**
	 * Sets the item.
	 * 
	 * @param item
	 *            the new item
	 */
	public void setItem(String item) {
		this.item = item;
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
