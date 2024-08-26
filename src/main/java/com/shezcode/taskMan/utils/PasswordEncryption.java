package com.shezcode.taskMan.utils;


import org.mindrot.jbcrypt.BCrypt;

public class PasswordEncryption {

    // Encrypts the password using BCrypt
    public static String encryptPassword(String plainPassword) {
        // Generate a salt and hash the password
        String hashedPassword = BCrypt.hashpw(plainPassword, BCrypt.gensalt());
        return hashedPassword;
    }

    // Verify the password with its hash
    public static boolean checkPassword(String plainPassword, String hashedPassword) {
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }
}
