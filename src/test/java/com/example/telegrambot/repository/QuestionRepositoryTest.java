package com.example.telegrambot.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class QuestionRepositoryTest {
    @Test
    void loadsThirtyValidQuestions() throws Exception {
        var questions = new QuestionRepository(new ObjectMapper()).findAll();
        assertEquals(30, questions.size());
        assertEquals(30, questions.stream().map(question -> question.id()).distinct().count());
        assertTrue(questions.stream().allMatch(question -> question.options().size() == 4));
    }

    @Test
    void publicPromptOmitsAnswerKey() throws Exception {
        var question = new QuestionRepository(new ObjectMapper()).findAll().getFirst();
        String json = new ObjectMapper().writeValueAsString(question.prompt());
        assertFalse(json.contains("correctOptionId"));
    }
}
