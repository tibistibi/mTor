package nl.bhit.mtor.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

public final class CryptographicUtils {
	
	private static final Logger LOG = Logger.getLogger(CryptographicUtils.class);
	
	public static final long QR_LOGIN_TIME_WINDOW_FRAME_MILIS = 5 /*MINUTES*/ * 60 /*SECONDS X MINUTE*/ * 1000 /*MILLIS X SECOND*/; 
	
	/**
	 * Enumeration of all possible digest algorithms.
	 */
	public static enum DIGEST_ALGORITHM {
		SHA256("SHA-256");
		
		private String algorithm;
		
		private DIGEST_ALGORITHM(String algorithm) {
			this.algorithm = algorithm;
		}

		public String getAlgorithm() {
			return algorithm;
		}
		
	}
	
	private CryptographicUtils(){
		//It's a utils class, no instantiation.
	}
	
	/**
	 * Returns a hashed string digested by specified algorithm.
	 * 
	 * @param digestAlgorithm
	 * 					Digest algorithm that we want to use.
	 * @param strToDigest
	 * 					String that we want to digest.
	 * @return
	 * 			Digested string.
	 */
	public static String digestString(DIGEST_ALGORITHM digestAlgorithm, String strToDigest) {
		if (StringUtils.isBlank(strToDigest)) {
			return null;
		}
		
		String digestedString = null;
		try {
			MessageDigest sha = MessageDigest.getInstance(digestAlgorithm.getAlgorithm());
			sha.update(strToDigest.getBytes());
			byte[] hash = sha.digest();
			BigInteger bigInt = new BigInteger(1, hash);
			digestedString = bigInt.toString(16);
		} catch (NoSuchAlgorithmException nsae) {
			LOG.error(digestAlgorithm + " was not found", nsae);
		}
		
		return digestedString;
	}

}
