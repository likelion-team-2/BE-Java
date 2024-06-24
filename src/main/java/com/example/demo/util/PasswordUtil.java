package com.example.demo.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

@Component
public class PasswordUtil {

    private static String hashAlgorithm; // Static field to hold the hashing algorithm

    @Value("${instance.hash.password}") // Injecting the value from application.properties
    public void setHashAlgorithm(String hashAlgorithm) {
        PasswordUtil.hashAlgorithm = hashAlgorithm;
    }

    public static byte[] generateSalt() throws NoSuchAlgorithmException {
        SecureRandom random = SecureRandom.getInstanceStrong();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return salt;
    }

    public static String hashPassword(String password, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
        PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
        SecretKeyFactory factory = SecretKeyFactory.getInstance(hashAlgorithm);
        byte[] hash = factory.generateSecret(spec).getEncoded();
        return Base64.getEncoder().encodeToString(hash);
    }

    public static HashedPassword hashAndSaltPassword(String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] salt = generateSalt();
        String hashedPassword = hashPassword(password, salt);
        return new HashedPassword(hashedPassword, Base64.getEncoder().encodeToString(salt));
    }

    public static boolean verifyPassword(String originalPassword, String storedHash, String storedSalt) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] salt = Base64.getDecoder().decode(storedSalt);
        String newHash = hashPassword(originalPassword, salt);
        return newHash.equals(storedHash);
    }

//    public static void main(String[] args) {
//        try {
//            String password = "mySecurePassword";
//            HashedPassword hashedPassword = hashAndSaltPassword(password);
//
//            System.out.println("Salt: " + hashedPassword.getSalt());
//            System.out.println("Hashed Password: " + hashedPassword.getHashedPassword());
//
//            // Verify password
//            boolean isPasswordCorrect = verifyPassword("mySecurePassword", hashedPassword.getHashedPassword(), hashedPassword.getSalt());
//            System.out.println("Password verification: " + isPasswordCorrect);
//        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
//            e.printStackTrace();
//        }
//    }
}
