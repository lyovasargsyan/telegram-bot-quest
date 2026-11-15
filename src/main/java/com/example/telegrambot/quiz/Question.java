package com.example.telegrambot.quiz;

import java.util.List;

public record Question(String id, String category, String text,
                       List<AnswerOption> options, String correctOptionId) {
    // Only send QuestionPrompt to Telegram; this record contains the answer key.
    public QuestionPrompt prompt() {
        return new QuestionPrompt(id, category, text, List.copyOf(options));
    }
}
