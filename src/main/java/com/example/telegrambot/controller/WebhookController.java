package com.example.telegrambot.controller;

import com.example.telegrambot.command.UpdateRouter;
import com.example.telegrambot.config.BotProperties;
import com.example.telegrambot.telegram.TelegramUpdate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

@RestController
@RequestMapping("/telegram")
public class WebhookController {
    private static final Logger log = LoggerFactory.getLogger(WebhookController.class);
    private final BotProperties properties;
    private final UpdateRouter router;

    public WebhookController(BotProperties properties, UpdateRouter router) {
        this.properties = properties;
        this.router = router;
    }

    @PostMapping("/webhook")
    public ResponseEntity<Void> receive(
            @RequestHeader(value = "X-Telegram-Bot-Api-Secret-Token", required = false) String suppliedSecret,
            @RequestBody TelegramUpdate update) throws Exception {
        if (suppliedSecret == null || !MessageDigest.isEqual(
                properties.webhookSecret().getBytes(StandardCharsets.UTF_8),
                suppliedSecret.getBytes(StandardCharsets.UTF_8))) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        if (update == null || update.updateId() == null) {
            return ResponseEntity.badRequest().build();
        }
        try {
            router.route(update);
        } catch (Exception e) {
            log.error("Failed to handle update", e);
        }
        return ResponseEntity.ok().build();
    }
}
