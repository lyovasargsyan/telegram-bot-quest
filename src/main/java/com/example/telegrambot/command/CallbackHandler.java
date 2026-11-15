package com.example.telegrambot.command;

import com.example.telegrambot.quiz.QuizService;
import com.example.telegrambot.telegram.TelegramApiClient;
import com.example.telegrambot.telegram.TelegramCallbackQuery;
import org.springframework.stereotype.Component;

@Component
public class CallbackHandler {
    private final TelegramApiClient telegram;
    private final QuizService quiz;

    public CallbackHandler(TelegramApiClient telegram, QuizService quiz) {
        this.telegram = telegram;
        this.quiz = quiz;
    }

    public void handle(TelegramCallbackQuery callback) {
        // TODO(student): Parse callback data, validate this user's current question,
        // reject duplicates/stale events, update the session, answer the callback,
        // and send the next question or final result.
    }
}
