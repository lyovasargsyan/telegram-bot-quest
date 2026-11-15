package com.example.telegrambot.command;

import com.example.telegrambot.telegram.TelegramCallbackQuery;
import com.example.telegrambot.telegram.TelegramMessage;
import com.example.telegrambot.telegram.TelegramUpdate;
import org.springframework.stereotype.Component;

@Component
public class UpdateRouter {
    private final CommandHandler commands;
    private final CallbackHandler callbacks;

    public UpdateRouter(CommandHandler commands, CallbackHandler callbacks) {
        this.commands = commands;
        this.callbacks = callbacks;
    }

    public void route(TelegramUpdate update) {
        TelegramCallbackQuery callback = update.callbackQuery();
        if (callback != null) {
            callbacks.handle(callback);
            return;
        }
        TelegramMessage message = update.message();
        if (message == null || message.text() == null) {
            return;
        }
        String firstWord = message.text().trim().split("\\s+", 2)[0];
        String command = firstWord.split("@", 2)[0];
        switch (command) {
            case "/start" -> commands.start(message);
            case "/help" -> commands.help(message);
            case "/quiz" -> commands.quiz(message);
            case "/score" -> commands.score(message);
            case "/reset" -> commands.reset(message);
            default -> { /* Other messages are outside the required homework. */ }
        }
    }
}
