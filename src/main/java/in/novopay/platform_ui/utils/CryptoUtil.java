package in.novopay.platform_ui.utils;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.ShortBufferException;
import javax.crypto.interfaces.PBEKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;

public class CryptoUtil {
//	private static final String RSA_ECB_PKCS1_PADDING = "RSA/ECB/PKCS1Padding";
    public static final String AES_ECB_PKCS5_PADDING = "AES/ECB/PKCS5Padding";
    public static final String AES_PKCS7_PADDING = "AES/PKCS7Padding";
//	private static final String RANDOM_NUMBER_GENERATION_ALGORITHM = "SHA1PRNG";
//	private static final int SALT_SIZE = 16;
//	private static final int SYMMETRIC_KEY_SIZE = 256;

    public CryptoUtil() {
    }

    public static byte[] encryptUsingPublicKey(PublicKey key, byte[] data) {
        byte[] cipherText = null;

        try {
            Cipher e = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            e.init(1, key);
            cipherText = e.doFinal(data);
        } catch (Exception var4) {
        }

        return Base64.encodeBase64(cipherText);
    }

    public static byte[] encryptRSA(Key key, byte[] data) {
        byte[] cipherText = null;

        try {
            Cipher e = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            e.init(1, key);
            cipherText = e.doFinal(data);
        } catch (Exception var4) {
        }

        return Base64.encodeBase64(cipherText);
    }

    public static byte[] decryptRSA(PublicKey key, byte[] data) {
        byte[] decodedData = Base64.decodeBase64(data);
        byte[] decryptedText = null;

        try {
            Cipher e = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            e.init(2, key);
            decryptedText = e.doFinal(decodedData);
        } catch (Exception var5) {
        }

        return decryptedText;
    }

    public static byte[] encryptUsingPublicKey(byte[] key, byte[] data) {
        PublicKey publicKey = null;

        try {
            publicKey = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(key));
        } catch (Exception var4) {
        }

        return encryptUsingPublicKey(publicKey, data);
    }

    public static byte[] decryptUsingPrivateKey(Key key, byte[] data) {
        byte[] decodedData = Base64.decodeBase64(data);
        byte[] decryptedText = null;

        try {
            Cipher e = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            e.init(2, key);
            decryptedText = e.doFinal(decodedData);
        } catch (Exception var5) {
        }

        return decryptedText;
    }

    public static byte[] decryptUsingPrivateKey(byte[] key, byte[] data) {
        PrivateKey privateKey = null;

        try {
            privateKey = KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(key));
        } catch (Exception var4) {
        }

        return decryptUsingPrivateKey((Key) privateKey, data);
    }

    public static SecretKey generateAesKey() {
        SecretKey secretKey = null;

        try {
            KeyGenerator e = KeyGenerator.getInstance("AES");
            e.init(256);
            secretKey = e.generateKey();
        } catch (Exception var2) {
        }

        return secretKey;
    }

    public static byte[] encryptUsingAesKey(SecretKey key, byte[] data, String transformation) {
        byte[] encryptedText = null;

        try {
            Cipher e = Cipher.getInstance(transformation);
            e.init(1, key);
            encryptedText = e.doFinal(data);
        } catch (Exception var5) {
        }

        return encryptedText;
    }

    public static byte[] decryptUsingAesKey(SecretKey key, byte[] data, String transformation) throws Exception {
        byte[] decryptedText = null;
        Cipher e = Cipher.getInstance(transformation);
        e.init(2, key);
        decryptedText = e.doFinal(data);
        return decryptedText;
    }

//    public static String generateSha256Hash(String data) {
//        try {
//            MessageDigest e = MessageDigest.getInstance("SHA-256");
//            e.update(data.getBytes());
//            byte[] b = e.digest();
//            return Hex.encodeHexString(b);
//        } catch (Exception var3) {
//            Log.e("Error while generating SHA-256 Hash of data" + data, var3);
//            return "";
//        }
//    }

    public static byte[] generateSha256Hash(byte[] data) {
        try {
            MessageDigest e = MessageDigest.getInstance("SHA-256");
            e.update(data);
            byte[] b = e.digest();
            return b;
        } catch (Exception var3) {
            return new byte[0];
        }
    }

    public static byte[] encryptUsingPassword(String password, byte[] data) throws CryptoException {
        try {
            byte[] e = createSalt();
            SecretKey encKey = generateKey(password, e);
            byte[] encryptedData = encrypt(encKey, data);
            return combine(e, encryptedData);
        } catch (Exception var5) {
            throw new CryptoException("Error while encrpting using password" , var5);
        }
    }

    public static byte[] decryptUsingPassword(String password, byte[] data) throws CryptoException {
        try {
            byte[] e = extractSalt(data);
            byte[] encryptedData = extractKey(data);
            SecretKey encKey = generateKey(password, e);
            return decrypt(encKey, encryptedData);
        } catch (Exception var5) {
            throw new CryptoException("Error while decrypting using password", var5);
        }
    }

    private static SecretKey generateKey(String password, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
        PBEKeySpec passwordKeySpec = new PBEKeySpec(password.toCharArray(), salt, 1000, 128);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        PBEKey key = (PBEKey) factory.generateSecret(passwordKeySpec);
        SecretKeySpec encKey = new SecretKeySpec(key.getEncoded(), "AES");
        return encKey;
    }

    private static byte[] createSalt() throws NoSuchAlgorithmException {
        SecureRandom rand = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[16];
        rand.nextBytes(salt);
        return salt;
    }

    private static byte[] combine(byte[] salt, byte[] encryptedData) {
        byte[] encryptedDataWithSalt = new byte[salt.length + encryptedData.length];
        System.arraycopy(salt, 0, encryptedDataWithSalt, 0, salt.length);
        System.arraycopy(encryptedData, 0, encryptedDataWithSalt, salt.length, encryptedData.length);
        return encryptedDataWithSalt;
    }

    private static byte[] extractKey(byte[] data) {
        byte[] encryptedData = new byte[data.length - 16];
        System.arraycopy(data, 16, encryptedData, 0, encryptedData.length);
        return encryptedData;
    }

    private static byte[] extractSalt(byte[] data) {
        byte[] salt = new byte[16];
        System.arraycopy(data, 0, salt, 0, salt.length);
        return salt;
    }

    public static byte[] encrypt(SecretKey key, byte[] data) throws ShortBufferException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException {
        Cipher cipher = createCipherForEncryption(key, 1);
        return doCrypto(cipher, data);
    }

    public static byte[] decrypt(SecretKey key, byte[] data) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, ShortBufferException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = createCipherForEncryption(key, 2);
        return doCrypto(cipher, data);
    }

    private static Cipher createCipherForEncryption(SecretKey key, int cipherMode) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(cipherMode, key, new IvParameterSpec(new byte[16]));
        return cipher;
    }

    private static byte[] doCrypto(Cipher cipher, byte[] data) throws ShortBufferException, IllegalBlockSizeException, BadPaddingException {
        return cipher.doFinal(data);
    }
   
    public String getEncrypterPassword(String key,String password) throws Exception {
    	final byte[] publicKeyAsBytes = Base64.decodeBase64(key);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        PublicKey publicKey = kf.generatePublic(new X509EncodedKeySpec(publicKeyAsBytes));
        Cipher decrypt = Cipher.getInstance("RSA");
        decrypt.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] encryptedPassword = decrypt.doFinal(password.toString().getBytes());
        return Base64.encodeBase64String(encryptedPassword);
    }

    public static void main(String[] args) throws Exception {
        /*System.out.println("Test");
        SecureRandom rnd = new SecureRandom();
        String text = "Hello, my dear! ... " + System.getProperty("user.home");
        byte[] textData = text.getBytes();
        IvParameterSpec iv = new IvParameterSpec(rnd.generateSeed(16));
        KeyGenerator generator = KeyGenerator.getInstance("AES");
        generator.init(128);
        SecretKey k = generator.generateKey();
        Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
        c.init(1, k, iv);
        c.update(textData);
        byte[] data = c.doFinal();
        System.out.println("E: " + data.length);
        c = Cipher.getInstance("AES/CBC/PKCS5Padding");
        c.init(2, k, iv);
        c.update(data);
        System.out.println("R: " + c.doFinal().length);*/
    	
    }
}
