package com.example.telegrambot;

import com.example.telegrambot.config.BotProperties;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BotPropertiesTest {
    @Test
    void requiresSafeSecretAndRedactsValues() {
        assertThrows(IllegalArgumentException.class, () -> new BotProperties("token", "has spaces"));
        assertThrows(IllegalArgumentException.class, () -> new BotProperties("", "safe-secret"));
        String description = new BotProperties("private-token", "private-secret").toString();
        assertFalse(description.contains("private-token"));
        assertFalse(description.contains("private-secret"));
    }
}
