package com.example.song4u.Util;

import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.util.Base64;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Enumeration;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AES256Cipher {
	
	public static byte[] ivBytes = { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 };

	// 키스토어 키 존재 여부 확인
	public static boolean isExistKey(String alias) throws Exception {
		KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");
		keyStore.load(null);
		Enumeration<String> aliases = keyStore.aliases();
		while (aliases.hasMoreElements()) {
			String nextAlias = aliases.nextElement();
			if (nextAlias.equals(alias)) {
				return true;
			}
		}
		return false;
	}

	// 키스토어 키 생성
	public static void generateKey(String alias) throws Exception {
		final KeyGenerator keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
		final KeyGenParameterSpec keyGenParameterSpec = new KeyGenParameterSpec.Builder(alias,
				KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
				.setBlockModes(KeyProperties.BLOCK_MODE_GCM)
				.setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
				.build();
		keyGenerator.init(keyGenParameterSpec);
		keyGenerator.generateKey();
	}

	// 키스토어 키 조회
	public static SecretKey getKeyStoreKey(String alias) throws Exception {
		KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");
		keyStore.load(null);
		final KeyStore.SecretKeyEntry secretKeyEntry = (KeyStore.SecretKeyEntry) keyStore.getEntry(alias, null);
		return secretKeyEntry.getSecretKey();
	}

	// 키스토어 키로 AES256 암호화
	public static String[] encByKeyStoreKey(SecretKey secretKey, String plainText) throws Exception {
		Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
		cipher.init(Cipher.ENCRYPT_MODE, secretKey);
		byte[] enc = cipher.doFinal(plainText.getBytes());
		byte[] iv = cipher.getIV();
		String encText = Base64.encodeToString(enc, 0);
		String ivText = Base64.encodeToString(iv, 0);

		String[] result = new String[2];
		result[0] = encText;
		result[1] = ivText;
		return result;
	}

	// 키스토어 키로 AES256 복호화
	public static String decByKeyStoreKey(SecretKey secretKey, String encText, String iv) throws Exception {
		Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
		GCMParameterSpec spec = new GCMParameterSpec(128, Base64.decode(iv, 0));
		cipher.init(Cipher.DECRYPT_MODE, secretKey, spec);
		byte[] dec = cipher.doFinal(Base64.decode(encText, 0));  // 확인 필요
		return new String(dec);
	}

	public static String AES_Encode(String str, String key)	throws java.io.UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException,	IllegalBlockSizeException, BadPaddingException {
		
		byte[] textBytes = str.getBytes("UTF-8");
		AlgorithmParameterSpec ivSpec = new IvParameterSpec(ivBytes);
		     SecretKeySpec newKey = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
		     Cipher cipher = null;
		cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, newKey, ivSpec);
		
		return Base64.encodeToString(cipher.doFinal(textBytes), 0);
	}

	public static String AES_Decode(String str, String key)	throws java.io.UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		
		byte[] textBytes =Base64.decode(str,0);
		//byte[] textBytes = str.getBytes("UTF-8");
		AlgorithmParameterSpec ivSpec = new IvParameterSpec(ivBytes);
		SecretKeySpec newKey = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		cipher.init(Cipher.DECRYPT_MODE, newKey, ivSpec);
		return new String(cipher.doFinal(textBytes), "UTF-8");
	}
}