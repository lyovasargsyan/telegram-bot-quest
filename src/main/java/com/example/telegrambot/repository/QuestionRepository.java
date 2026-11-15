package com.example.telegrambot.repository;

import com.example.telegrambot.quiz.AnswerOption;
import com.example.telegrambot.quiz.Question;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository
public class QuestionRepository {
    private final List<Question> questions;

    public QuestionRepository(ObjectMapper mapper) throws IOException {
        try (var stream = new ClassPathResource("questions.json").getInputStream()) {
            this.questions = List.copyOf(mapper.readValue(stream, new TypeReference<List<Question>>() { }));
        }
        validate(questions);
    }

    public List<Question> findAll() {
        return questions;
    }

    private static void validate(List<Question> questions) {
        if (questions.isEmpty()) {
            throw new IllegalArgumentException("Question bank is empty");
        }
        Set<String> questionIds = new HashSet<>();
        for (Question question : questions) {
            if (question == null || blank(question.id()) || !questionIds.add(question.id())
                    || blank(question.category()) || blank(question.text()) || blank(question.correctOptionId())
                    || question.options() == null || question.options().size() != 4) {
                throw new IllegalArgumentException("Invalid or duplicate question in question bank");
            }
            Set<String> optionIds = new HashSet<>();
            for (AnswerOption option : question.options()) {
                if (option == null || blank(option.id()) || blank(option.text()) || !optionIds.add(option.id())) {
                    throw new IllegalArgumentException("Invalid answer option for question " + question.id());
                }
            }
            if (!optionIds.contains(question.correctOptionId())) {
                throw new IllegalArgumentException("Missing correct option for question " + question.id());
            }
        }
    }

    private static boolean blank(String value) {
        return value == null || value.isBlank();
    }
}
