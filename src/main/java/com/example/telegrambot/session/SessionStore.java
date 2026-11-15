package com.example.telegrambot.session;

import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class SessionStore {
    public Optional<QuizSession> find(long userId) {
        // TODO(student): Use a thread-safe in-memory collection keyed by Telegram user ID.
        throw new UnsupportedOperationException("TODO(student): find session");
    }

    public void save(QuizSession session) {
        // TODO(student): Ensure updates for one user cannot lose progress or count twice.
        throw new UnsupportedOperationException("TODO(student): save session");
    }

    public void remove(long userId) {
        // TODO(student): Remove only this user's session.
        throw new UnsupportedOperationException("TODO(student): remove session");
    }
}
