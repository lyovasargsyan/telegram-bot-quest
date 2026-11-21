package com.example.telegrambot.command;

import com.example.telegrambot.quiz.QuizService;
import com.example.telegrambot.session.QuizSession;
import com.example.telegrambot.session.SessionStore;
import com.example.telegrambot.telegram.TelegramApiClient;
import com.example.telegrambot.telegram.TelegramMessage;
import jakarta.websocket.Session;
import org.springframework.stereotype.Component;

import java.util.Optional;

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
        telegram.sendMessage(message.chat().id(), "Hi, you are using my firstbot");
    }

    public void help(TelegramMessage message) {
        // TODO(student): Describe /quiz, /score, and /reset.
        telegram.sendMessage(message.chat().id(),
                "Available commands:\n\n"
                        + "/quiz - start a five-question quiz\n"
                        + "/score - show your current score\n"
                        + "/reset - clear your session and start over\n"
                        + "/help - show this message");

    }

    public void quiz(TelegramMessage message) {
        // TODO(student): Start a five-question quiz and send question one.
        long chatId = message.chat().id();
        long userId = message.from().id();

        QuizSession session = quiz.startQuiz(userId, chatId);
        sessions.save(session);
        telegram.sendMessage(chatId,
                "Quiz started! Question 1 of 5:\n\n" + quiz.nextQuestion(userId));
    }

    public void score(TelegramMessage message) {
        // TODO(student): Report this user's score or absence of a session.
        long chatId = message.chat().id();
        long userId = message.from().id();

        Optional<QuizSession> found = sessions.find(userId);
        if (found.isEmpty()) {
            telegram.sendMessage(chatId, "You don't have a quiz in progress. Send /quiz to start one.");
            return;
        }
        QuizSession session = found.get();
        int answered = session.currentIndex() + (session.currentAnswered() ? 1 : 0);
        telegram.sendMessage(chatId,
                "Your score: " + session.score());

    }

    public void reset(TelegramMessage message) {
        // TODO(student): Clear only this user's session.
        long chatId = message.chat().id();
        long userId = message.from().id();

        sessions.remove(userId);
        telegram.sendMessage(chatId, "Your session was cleared. Send /quiz to start again.");

    }
}
