package com.flc.model;

import java.util.Objects;

/**
 * Registered user for login (separate from {@link Member} used for bookings).
 */
public final class UserAccount {
    private final String username;
    private final String displayName;
    private final String passwordHash;

    public UserAccount(String username, String displayName, String passwordHash) {
        this.username = Objects.requireNonNull(username);
        this.displayName = Objects.requireNonNull(displayName);
        this.passwordHash = Objects.requireNonNull(passwordHash);
    }

    public String getUsername() {
        return username;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getPasswordHash() {
        return passwordHash;
    }
}
