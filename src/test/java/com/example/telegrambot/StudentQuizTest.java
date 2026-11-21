
package com.example.telegrambot;

import com.example.telegrambot.quiz.AnswerOption;
import com.example.telegrambot.quiz.Question;
import com.example.telegrambot.quiz.QuizResult;
import com.example.telegrambot.quiz.QuizService;
import com.example.telegrambot.repository.QuestionRepository;
import com.example.telegrambot.session.QuizSession;
import com.example.telegrambot.session.SessionStore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StudentQuizTest {

    private SessionStore sessions;
    private QuizService quizService;

    @BeforeEach
    void setUp() {
        QuestionRepository questions = mock(QuestionRepository.class);
        sessions = mock(SessionStore.class);

        List<AnswerOption> options = List.of(
                new AnswerOption("0", "Option 1"),
                new AnswerOption("1", "Option 2"),
                new AnswerOption("2", "Option 3"),
                new AnswerOption("3", "Option 4")
        );

        List<Question> questionBank = List.of(
                new Question("0", "Java", "Question 1", options, "0"),
                new Question("1", "Java", "Question 2", options, "1"),
                new Question("2", "Java", "Question 3", options, "0"),
                new Question("3", "Java", "Question 4", options, "1"),
                new Question("4", "Java", "Question 5", options, "0")
        );

        when(questions.findAll()).thenReturn(questionBank);

        quizService = new QuizService(questions, sessions);
    }

    @Test
    void selectsFiveDistinctQuestions() {
        List<Question> selectedQuestions = quizService.selectQuestions(5);

        assertEquals(5, selectedQuestions.size());

        long distinctCount = selectedQuestions.stream()
                .map(Question::id)
                .distinct()
                .count();

        assertEquals(5, distinctCount);
    }

    @Test
    void correctAndIncorrectAnswersProduceExpectedScore() throws Exception {
        QuizSession firstSession = new QuizSession(
                123L, 456L, List.of("0", "1"), 0, 0, false
        );

        QuizSession secondSession = new QuizSession(
                123L, 456L, List.of("0", "1"), 1, 1, false
        );

        when(sessions.find(123L))
                .thenReturn(Optional.of(firstSession))
                .thenReturn(Optional.of(secondSession));

        boolean firstAnswer = quizService.submitAnswer(123L, "0", "0");

        assertTrue(firstAnswer);

        boolean secondAnswer = quizService.submitAnswer(123L, "1", "0");

        assertFalse(secondAnswer);

        ArgumentCaptor<QuizSession> captor =
                ArgumentCaptor.forClass(QuizSession.class);

        verify(sessions, times(2)).save(captor.capture());

        List<QuizSession> savedSessions = captor.getAllValues();

        assertEquals(1, savedSessions.get(0).score());
        assertTrue(savedSessions.get(0).currentAnswered());

        assertEquals(1, savedSessions.get(1).score());
        assertTrue(savedSessions.get(1).currentAnswered());
    }

    @Test
    void twoUsersAndDuplicateCallbacksAreSafe() throws Exception {
        QuizSession user1 = new QuizSession(
                100L, 1000L, List.of("0"), 0, 0, false
        );

        QuizSession user2 = new QuizSession(
                200L, 2000L, List.of("0"), 0, 0, false
        );

        when(sessions.find(100L)).thenReturn(Optional.of(user1));
        when(sessions.find(200L)).thenReturn(Optional.of(user2));

        boolean user1Result = quizService.submitAnswer(100L, "0", "0");

        assertTrue(user1Result);

        boolean user2Result = quizService.submitAnswer(200L, "0", "1");

        assertFalse(user2Result);

        ArgumentCaptor<QuizSession> captor =
                ArgumentCaptor.forClass(QuizSession.class);

        verify(sessions, times(2)).save(captor.capture());

        List<QuizSession> savedSessions = captor.getAllValues();

        assertEquals(100L, savedSessions.get(0).userId());
        assertEquals(1, savedSessions.get(0).score());

        assertEquals(200L, savedSessions.get(1).userId());
        assertEquals(0, savedSessions.get(1).score());

        QuizSession answeredSession = new QuizSession(
                100L, 1000L, List.of("0"), 0, 1, true
        );

        when(sessions.find(100L))
                .thenReturn(Optional.of(answeredSession));

        assertThrows(
                IllegalStateException.class,
                () -> quizService.submitAnswer(100L, "0", "0")
        );

        verify(sessions, times(2)).save(any(QuizSession.class));
    }

    @Test
    void fifthAnswerCompletesQuiz() throws Exception {
        QuizSession session = new QuizSession(
                123L,
                456L,
                List.of("0", "1", "2", "3", "4"),
                4,
                3,
                false
        );

        when(sessions.find(123L)).thenReturn(Optional.of(session));

        boolean correct = quizService.submitAnswer(123L, "4", "0");

        assertTrue(correct);

        ArgumentCaptor<QuizSession> captor =
                ArgumentCaptor.forClass(QuizSession.class);

        verify(sessions).save(captor.capture());

        QuizSession completedSession = captor.getValue();

        assertEquals(4, completedSession.currentIndex());
        assertEquals(4, completedSession.score());
        assertTrue(completedSession.currentAnswered());

        when(sessions.find(123L))
                .thenReturn(Optional.of(completedSession));

        QuizResult result = quizService.finishQuiz(123L);

        assertEquals(123L, result.userId());
        assertEquals(4, result.correctAnswers());
        assertEquals(5, result.totalQuestions());
    }
}
