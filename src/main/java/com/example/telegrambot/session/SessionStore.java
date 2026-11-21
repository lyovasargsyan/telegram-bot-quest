package com.example.telegrambot.session;

import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Component
public class SessionStore {
    private final ConcurrentMap<Long, QuizSession> sessions =
            new ConcurrentHashMap<>();
    public Optional<QuizSession> find(long userId) {
        // TODO(student): Use a thread-safe in-memory collection keyed by Telegram user ID.
        return Optional.ofNullable(sessions.get(userId));
    }

    public void save(QuizSession session) {
        // TODO(student): Ensure updates for one user cannot lose progress or count twice.
        sessions.put(session.userId(), session);
    }

    public void remove(long userId) {
        // TODO(student): Remove only this user's session.
        sessions.remove(userId);
    }
}
