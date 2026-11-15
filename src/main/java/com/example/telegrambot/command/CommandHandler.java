package com.example.telegrambot.command;

import com.example.telegrambot.quiz.QuizService;
import com.example.telegrambot.session.SessionStore;
import com.example.telegrambot.telegram.TelegramApiClient;
import com.example.telegrambot.telegram.TelegramMessage;
import org.springframework.stereotype.Component;

@Component
public class CommandHandler {
    private final TelegramApiClient telegram;
    private final QuizService quiz;
    private final SessionStore sessions;

    public CommandHandler(TelegramApiClient telegram, QuizService quiz, SessionStore sessions) {
        this.telegram = telegram;
        this.quiz = quiz;
        this.sessions = sessions;
    }

    public void start(TelegramMessage message) {
        // TODO(student): Welcome this user and explain how to begin.
    }

    public void help(TelegramMessage message) {
        // TODO(student): Describe /quiz, /score, and /reset.
    }

    public void quiz(TelegramMessage message) {
        // TODO(student): Start a five-question quiz and send question one.
    }

    public void score(TelegramMessage message) {
        // TODO(student): Report this user's score or absence of a session.
    }

    public void reset(TelegramMessage message) {
        // TODO(student): Clear only this user's session.
    }
}
