package com.example.telegrambot.command;

import com.example.telegrambot.quiz.*;
import com.example.telegrambot.session.QuizSession;
import com.example.telegrambot.session.SessionStore;
import com.example.telegrambot.telegram.InlineButton;
import com.example.telegrambot.telegram.TelegramApiClient;
import com.example.telegrambot.telegram.TelegramCallbackQuery;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class CallbackHandler {
    private final TelegramApiClient telegram;
    private final QuizService quiz;
    private final SessionStore sessions;
    private final PromptSender prompts;

    public CallbackHandler(TelegramApiClient telegram, QuizService quiz, SessionStore sessions, PromptSender prompts) {
        this.telegram = telegram;
        this.quiz = quiz;
        this.sessions = sessions;
        this.prompts = prompts;
    }

    public void handle(TelegramCallbackQuery callback) throws Exception {
        // TODO(student): Parse callback data, validate this user's current question,
        // reject duplicates/stale events, update the session, answer the callback,
        // and send the next question or final result.
        long userId = callback.from().id();

        String[] parts = callback.data() == null ? new String[0] : callback.data().split(":");
        if (parts.length != 3 || !parts[0].equals("answer") || parts[1].isBlank() || parts[2].isBlank()) {
            telegram.answerCallbackQuery(callback.id(), "Unknown action.");
            return;
        }
        String questionId = parts[1];
        String optionId = parts[2];


        Optional<QuizSession> found = sessions.find(userId);
        if (found.isEmpty()) {
            telegram.answerCallbackQuery(callback.id(), "No quiz in progress. Send /quiz.");
            return;
        }
        QuizSession session = found.get();

        int index = session.currentIndex();
        if (index < 0 || index >= session.questionIds().size()
                || !session.questionIds().get(index).equals(questionId)) {
            telegram.answerCallbackQuery(callback.id(), "That question is no longer active.");
            return;
        }
        if (session.currentAnswered()) {
            telegram.answerCallbackQuery(callback.id(), "You already answered this one.");
            return;
        }

        boolean correct;
        try {
            correct = quiz.submitAnswer(userId, questionId, optionId);
        } catch (RuntimeException e) {
            telegram.answerCallbackQuery(callback.id(), "That answer can't be accepted.");
            return;
        }
        telegram.answerCallbackQuery(callback.id(), correct ? "Correct!" : "Wrong.");

        if (index + 1 < session.questionIds().size()) {
            QuestionPrompt prompt = quiz.nextQuestion(userId);
            prompts.send(session.chatId(), prompt, index + 2, session.questionIds().size());
        } else {
            QuizResult result = quiz.finishQuiz(userId);
            telegram.sendMessage(session.chatId(),
                    "Quiz finished! Final score: " + result.correctAnswers()
                            + " out of " + result.totalQuestions());
        }
    }
}
