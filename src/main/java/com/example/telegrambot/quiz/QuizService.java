package com.example.telegrambot.quiz;

import com.example.telegrambot.repository.QuestionRepository;
import com.example.telegrambot.session.QuizSession;
import com.example.telegrambot.session.SessionStore;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

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
        List<Question> questionsList = new ArrayList<>(questions.findAll());
        Collections.shuffle(questionsList);

        return questionsList.stream()
                .limit(count)
                .toList();

    }

    public QuizSession startQuiz(long userId, long chatId) {
        // TODO(student): Select five questions and create this user's session.
        List<Question> questions = selectQuestions(5);
        List<String> questionIds = questions.stream()
                .map(Question::id)
                .toList();

        return new QuizSession(
                userId,
                chatId,
                questionIds,
                0,
                0,
                false
        );
    }

    public boolean submitAnswer(long userId, String questionId, String optionId) throws Exception {
        // TODO(student): Check ownership/current question and prevent duplicate scoring.
        Optional<QuizSession> quizSession = sessions.find(userId);
        if (quizSession.isEmpty()) {
            throw new IllegalStateException("Quiz session not found");
        }
        QuizSession session = quizSession.get();
        if (session.currentAnswered()) {
            throw new IllegalStateException("Question already answered");
        }

        if (session.currentIndex() >= session.questionIds().size()) {
            throw new IllegalStateException("Quiz already finished");
        }

        String currentQuestionId =
                session.questionIds().get(session.currentIndex());

        if (!currentQuestionId.equals(questionId)) {
            throw new IllegalArgumentException("Incorrect question");
        }

        Question question = questions.findAll().get(Integer.parseInt(questionId));

        boolean correct = question.correctOptionId().equals(optionId);

        QuizSession updatedSession = new QuizSession(
                session.userId(),
                session.chatId(),
                session.questionIds(),
                session.currentIndex(),
                session.score() + (correct ? 1 : 0),
                true
        );

        sessions.save(updatedSession);

        return correct;
    }

    public QuestionPrompt nextQuestion(long userId) {
        // TODO(student): Advance safely and expose a prompt without the answer key.
        Optional<QuizSession> quizSession = sessions.find(userId);

        if (quizSession.isEmpty()) {
            throw new IllegalStateException("Quiz session not found");
        }

        QuizSession session = quizSession.get();

        if (!session.currentAnswered()) {
            throw new IllegalStateException("Previous question not answered");
        }

        int nextIndex = session.currentIndex() + 1;

        if (nextIndex >= session.questionIds().size()) {
            throw new IllegalStateException("All questions answered");
        }

        String questionId = session.questionIds().get(nextIndex);

        Question question = questions.findAll().get(Integer.parseInt(questionId));


        QuizSession updatedSession = new QuizSession(
                session.userId(),
                session.chatId(),
                session.questionIds(),
                nextIndex,
                session.score(),
                false
        );

        sessions.save(updatedSession);

        return new QuestionPrompt(
                question.id(),
                question.category(),
                question.text(),
                question.options().stream()
                        .map(option -> new AnswerOption(
                                option.id(),
                                option.text()
                        ))
                        .toList()
        );
    }

    public QuizResult finishQuiz(long userId) {
        // TODO(student): Calculate the result when all five answers are complete.
        Optional<QuizSession> quizSession = sessions.find(userId);

        if (quizSession.isEmpty()) {
            throw new IllegalStateException("Quiz session not found");
        }

        QuizSession session = quizSession.get();

        if (!session.currentAnswered()
                || session.currentIndex() != session.questionIds().size() - 1) {
            throw new IllegalStateException("Quiz is not completed");
        }

        return new QuizResult(
                userId,
                session.score(),
                session.questionIds().size()
        );
    }
}
