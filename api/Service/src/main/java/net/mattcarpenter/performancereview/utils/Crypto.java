package net.mattcarpenter.performancereview.utils;

import net.mattcarpenter.performancereview.model.CredentialModel;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

public class Crypto {

    public static final String PASSWORD_HASH_ALGORITHM = "PBKDF2WithHmacSHA1";
    public static final String PRNG_NAME = "SHA1PRNG";

    public static CredentialModel generatePasswordHashAndSalt(String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
        int iterations = 1000;
        char[] chars = password.toCharArray();
        byte[] salt = getSalt();

        PBEKeySpec spec = new PBEKeySpec(chars, salt, iterations, 64 * 8);
        SecretKeyFactory skf = SecretKeyFactory.getInstance(PASSWORD_HASH_ALGORITHM);
        byte[] hash = skf.generateSecret(spec).getEncoded();

        return new CredentialModel(toHex(hash),toHex(salt), PASSWORD_HASH_ALGORITHM, iterations);
    }

    private static byte[] getSalt() throws NoSuchAlgorithmException {
        SecureRandom sr = SecureRandom.getInstance(PRNG_NAME);
        byte[] salt = new byte[16];
        sr.nextBytes(salt);
        return salt;
    }

    private static String toHex(byte[] array) throws NoSuchAlgorithmException {
        BigInteger bi = new BigInteger(1, array);
        String hex = bi.toString(16);
        int paddingLength = (array.length * 2) - hex.length();
        if(paddingLength > 0)
        {
            return String.format("%0"  +paddingLength + "d", 0) + hex;
        }else{
            return hex;
        }
    }

    public static boolean validatePassword(String password, CredentialModel credential) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] salt = fromHex(credential.getPasswordSalt());
        byte[] hash = fromHex(credential.getPassword());

        PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, credential.getIterations(), hash.length * 8);
        SecretKeyFactory skf = SecretKeyFactory.getInstance(PASSWORD_HASH_ALGORITHM);
        byte[] testHash = skf.generateSecret(spec).getEncoded();

        int diff = hash.length ^ testHash.length;
        for(int i = 0; i < hash.length && i < testHash.length; i++)
        {
            diff |= hash[i] ^ testHash[i];
        }
        return diff == 0;
    }

    private static byte[] fromHex(String hex) throws NoSuchAlgorithmException {
        byte[] bytes = new byte[hex.length() / 2];
        for(int i = 0; i<bytes.length ;i++)
        {
            bytes[i] = (byte)Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
        }
        return bytes;
    }
}
