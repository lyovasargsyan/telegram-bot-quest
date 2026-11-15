package com.example.telegrambot.quiz;

import java.util.List;

public record QuestionPrompt(String id, String category, String text, List<AnswerOption> options) { }
