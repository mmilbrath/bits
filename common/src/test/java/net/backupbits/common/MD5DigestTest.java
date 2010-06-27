package net.backupbits.common;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class MD5DigestTest {

	private MD5Digest digest = null;
	private Random rand = null;

	@Before
	public void setUp() throws Exception {
		digest = new MD5Digest();
		rand = new Random();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void digestBytes() throws NoSuchAlgorithmException {
		byte[] bytes = new byte[4096];
		rand.nextBytes(bytes);

		assert(MessageDigest.isEqual(
				digest.digest(bytes),
				digest.digest(bytes)));
	}

	@Test
	public void digestDifferentBytes() throws NoSuchAlgorithmException {
		byte[] bytes1 = new byte[4096];
		byte[] bytes2 = new byte[4096];
		rand.nextBytes(bytes1);
		rand.nextBytes(bytes2);

		assert(!MessageDigest.isEqual(
				digest.digest(bytes1),
				digest.digest(bytes2)));
	}

}
