package com.example.telegrambot.session;

import java.util.List;

public record QuizSession(long userId, long chatId, List<String> questionIds,
                          int currentIndex, int score, boolean currentAnswered) { }
