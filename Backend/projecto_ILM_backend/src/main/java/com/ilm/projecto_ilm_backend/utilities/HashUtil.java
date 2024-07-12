package com.ilm.projecto_ilm_backend.utilities;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
/**
 * HashUtil is a utility class that provides a method to convert a string to its SHA-256 hash.
 */
public class HashUtil {
    /**
     * This method converts the provided string to its SHA-256 hash.
     * It first gets an instance of the MessageDigest for the SHA-256 algorithm.
     * Then, it converts the string to bytes and computes the hash.
     * Finally, it converts the hash bytes to a hexadecimal string and returns it.
     *
     * @param originalString the string to be converted to its SHA-256 hash
     * @return the SHA-256 hash of the provided string
     * @throws RuntimeException if the SHA-256 algorithm is not found
     */
    public static String toSHA256(String originalString) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedhash = digest.digest(originalString.getBytes(java.nio.charset.StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder(2 * encodedhash.length);
            for (int i = 0; i < encodedhash.length; i++) {
                String hex = Integer.toHexString(0xff & encodedhash[i]);
                if(hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}

