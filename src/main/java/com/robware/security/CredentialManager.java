package com.robware.security;

import java.util.prefs.Preferences;

public class CredentialManager {

    private static final String EMAIL_KEY = "email";
    private static final String PASSWORD_KEY = "password";
    private static final String TOKEN_KEY = "token";

    private final Preferences preferences;

    public static void main(String[] args) {
        CredentialManager manager = new CredentialManager();

//        manager.saveCredentials("someEmail", "somePass", "someToken");
        System.out.println("Password: " + manager.getPassword());
    }

    public CredentialManager() {
        preferences = Preferences.userNodeForPackage(getClass());
    }

    public void saveCredentials(String email, String password, String token) {
        preferences.put(EMAIL_KEY, email);
        // Store the password securely using a proper encryption or hashing mechanism
        preferences.put(PASSWORD_KEY, encryptPassword(password));
        preferences.put(TOKEN_KEY, token);
    }

    public String getEmail() {
        return preferences.get(EMAIL_KEY, null);
    }

    public String getPassword() {
        // Decrypt or verify the stored password
        return decryptPassword(preferences.get(PASSWORD_KEY, null));
    }

    public String getToken() {
        return preferences.get(TOKEN_KEY, null);
    }

    private String encryptPassword(String password) {
        // Implement encryption logic
        return EncryptionUtils.encrypt(password);
    }

    private String decryptPassword(String encryptedPassword) {
        // Implement decryption logic
        return EncryptionUtils.decrypt(encryptedPassword);
    }
}
