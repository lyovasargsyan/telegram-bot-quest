package com.example.telegrambot.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "telegram")
public record BotProperties(String botToken, String webhookSecret) {
    public BotProperties {
        if (botToken == null || botToken.isBlank()) {
            throw new IllegalArgumentException("TELEGRAM_BOT_TOKEN must be set");
        }
        if (webhookSecret == null || !webhookSecret.matches("[A-Za-z0-9_-]{1,256}")) {
            throw new IllegalArgumentException("TELEGRAM_WEBHOOK_SECRET must contain 1-256 letters, digits, _ or -");
        }
    }

    @Override
    public String toString() {
        return "BotProperties[botToken=REDACTED, webhookSecret=REDACTED]";
    }
}
