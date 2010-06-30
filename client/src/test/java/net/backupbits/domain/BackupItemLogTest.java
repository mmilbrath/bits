package net.backupbits.domain;

import java.util.Random;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class BackupItemLogTest {

	private EntityManager em = null;
	private Random rand = null;

	@Before
	public void setUp() throws Exception {
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("default");
		em = factory.createEntityManager();
		rand = new Random();
	}

	@After
	public void tearDown() throws Exception {
		em.close();
	}

	@Test
	public void testSave() {
		byte[] digest = new byte[4096];
		for (int i = 0; i < 50; i++) {
			rand.nextBytes(digest);
			BackupItemLog backupItemLog = new BackupItemLog();
			backupItemLog.setCompressedDigest(digest);
			backupItemLog.setUncompressedDigest(digest);
			backupItemLog.setCompressedItemName("compressedItemName_" + i);
			backupItemLog.setServer("server_" + i);

			em.getTransaction().begin();
			em.persist(backupItemLog);
			em.getTransaction().commit();
			em.refresh(backupItemLog);
		}
	}

}
