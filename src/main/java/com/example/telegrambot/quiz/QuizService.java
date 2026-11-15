package com.example.telegrambot.quiz;

import com.example.telegrambot.repository.QuestionRepository;
import com.example.telegrambot.session.QuizSession;
import com.example.telegrambot.session.SessionStore;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuizService {
    private final QuestionRepository questions;
    private final SessionStore sessions;

    public QuizService(QuestionRepository questions, SessionStore sessions) {
        this.questions = questions;
        this.sessions = sessions;
    }

    public List<Question> selectQuestions(int count) {
        // TODO(student): Select distinct questions from the repository.
        throw new UnsupportedOperationException("TODO(student): selectQuestions");
    }

    public QuizSession startQuiz(long userId, long chatId) {
        // TODO(student): Select five questions and create this user's session.
        throw new UnsupportedOperationException("TODO(student): startQuiz");
    }

    public boolean submitAnswer(long userId, String questionId, String optionId) {
        // TODO(student): Check ownership/current question and prevent duplicate scoring.
        throw new UnsupportedOperationException("TODO(student): submitAnswer");
    }

    public QuestionPrompt nextQuestion(long userId) {
        // TODO(student): Advance safely and expose a prompt without the answer key.
        throw new UnsupportedOperationException("TODO(student): nextQuestion");
    }

    public QuizResult finishQuiz(long userId) {
        // TODO(student): Calculate the result when all five answers are complete.
        throw new UnsupportedOperationException("TODO(student): finishQuiz");
    }
}
