package net.backupbits.domain;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class BackupItemTest {

	private EntityManager em = null;

	@Before
	public void setUp() throws Exception {
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("default");
		em = factory.createEntityManager();
	}

	@After
	public void tearDown() throws Exception {
		em.close();
	}

	@Test
	public void testSave() {
		for (int i = 0; i < 50; i++) {
			BackupItem backupItem = new BackupItem();
			backupItem.setItem("backup_item " + i);
			em.getTransaction().begin();
			em.persist(backupItem);
			em.getTransaction().commit();
			em.refresh(backupItem);
		}
	}

}
