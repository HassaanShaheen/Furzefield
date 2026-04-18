package com.flc.service;

import com.flc.model.UserAccount;
import com.flc.util.PasswordHasher;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class AuthService {

    private final Map<String, UserAccount> usersByUsername = new ConcurrentHashMap<>();
    private UserAccount currentUser;

    public Optional<String> register(String username, String displayName, String password, String confirmPassword) {
        String u = normalizeUsername(username);
        if (u.isEmpty()) {
            return Optional.of("Please enter a username.");
        }
        if (u.length() < 3) {
            return Optional.of("Username must be at least 3 characters.");
        }
        if (!u.matches("[a-zA-Z0-9_]+")) {
            return Optional.of("Username may only contain letters, numbers, and underscores.");
        }
        if (displayName == null || displayName.trim().isEmpty()) {
            return Optional.of("Please enter your name.");
        }
        if (password == null || password.length() < 6) {
            return Optional.of("Password must be at least 6 characters.");
        }
        if (!password.equals(confirmPassword)) {
            return Optional.of("Passwords do not match.");
        }
        if (usersByUsername.containsKey(u)) {
            return Optional.of("That username is already taken.");
        }
        String hash = PasswordHasher.hash(password);
        usersByUsername.put(u, new UserAccount(u, displayName.trim(), hash));
        return Optional.empty();
    }

    public Optional<String> login(String username, String password) {
        String u = normalizeUsername(username);
        if (u.isEmpty() || password == null || password.isEmpty()) {
            return Optional.of("Enter username and password.");
        }
        UserAccount account = usersByUsername.get(u);
        if (account == null) {
            return Optional.of("Unknown username or wrong password.");
        }
        if (!PasswordHasher.hash(password).equals(account.getPasswordHash())) {
            return Optional.of("Unknown username or wrong password.");
        }
        currentUser = account;
        return Optional.empty();
    }

    public void logout() {
        currentUser = null;
    }

    public UserAccount getCurrentUser() {
        return currentUser;
    }

    private static String normalizeUsername(String username) {
        return username == null ? "" : username.trim().toLowerCase();
    }
}
