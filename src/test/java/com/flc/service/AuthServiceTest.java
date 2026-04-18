package com.flc.service;

import com.flc.model.UserAccount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AuthServiceTest {

    private AuthService auth;

    @BeforeEach
    void setUp() {
        auth = new AuthService();
    }

    @Test
    void registerThenLoginSucceeds() {
        assertTrue(auth.register("alice", "Alice Smith", "secret1", "secret1").isEmpty());
        assertTrue(auth.login("Alice", "secret1").isEmpty());
        UserAccount u = auth.getCurrentUser();
        assertNotNull(u);
        assertEquals("alice", u.getUsername());
        assertEquals("Alice Smith", u.getDisplayName());
    }

    @Test
    void loginFailsForUnknownUser() {
        Optional<String> err = auth.login("nobody", "x");
        assertTrue(err.isPresent());
        assertNull(auth.getCurrentUser());
    }

    @Test
    void registerRejectsDuplicateUsername() {
        assertTrue(auth.register("bob", "Bob", "password", "password").isEmpty());
        Optional<String> err = auth.register("BOB", "Other", "password", "password");
        assertTrue(err.isPresent());
    }

    @Test
    void logoutClearsSession() {
        assertTrue(auth.register("cara", "Cara", "abcdef", "abcdef").isEmpty());
        assertTrue(auth.login("cara", "abcdef").isEmpty());
        auth.logout();
        assertNull(auth.getCurrentUser());
    }
}
