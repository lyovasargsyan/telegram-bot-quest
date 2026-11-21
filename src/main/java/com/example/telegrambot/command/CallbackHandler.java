package com.example.telegrambot.command;

import com.example.telegrambot.quiz.AnswerOption;
import com.example.telegrambot.quiz.QuestionPrompt;
import com.example.telegrambot.quiz.QuizResult;
import com.example.telegrambot.quiz.QuizService;
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

    public CallbackHandler(TelegramApiClient telegram, QuizService quiz, SessionStore sessions) {
        this.telegram = telegram;
        this.quiz = quiz;
        this.sessions = sessions;
    }

    public void handle(TelegramCallbackQuery callback) throws Exception {
        // TODO(student): Parse callback data, validate this user's current question,
        // reject duplicates/stale events, update the session, answer the callback,
        // and send the next question or final result.
        long userId = callback.from().id();
        String[] parts = callback.data() == null ? new String[0] : callback.data().split(":");
        if (parts.length != 3 || !parts[0].equals("answer")) {
            telegram.answerCallbackQuery(callback.id(), "Unknown action.");
            return;
        }
        String questionId = parts[1];
        try {
            Integer.parseInt(parts[2]);
        } catch (NumberFormatException e) {
            telegram.answerCallbackQuery(callback.id(), "Unknown action.");
            return;
        }

        Optional<QuizSession> found = sessions.find(userId);
        if (found.isEmpty()) {
            telegram.answerCallbackQuery(callback.id(), "No quiz in progress. Send /quiz.");
            return;
        }
        QuizSession session = found.get();

        String currentId = session.questionIds().get(session.currentIndex());
        if (!currentId.equals(questionId)) {
            telegram.answerCallbackQuery(callback.id(), "That question is no longer active.");
            return;
        }
        if (session.currentAnswered()) {
            telegram.answerCallbackQuery(callback.id(), "You already answered this one.");
            return;
        }

        boolean correct = quiz.submitAnswer(userId, questionId, parts[2]);
        telegram.answerCallbackQuery(callback.id(), correct ? "Correct!" : "Wrong.");

        if (session.currentIndex() + 1 < session.questionIds().size()) {
            QuestionPrompt prompt = quiz.nextQuestion(userId);

            List<List<InlineButton>> rows = new ArrayList<>();
            for (AnswerOption option : prompt.options()) {
                String callbackData = "answer:" + prompt.id() + ":" + option.id();
                rows.add(List.of(new InlineButton(option.text(), callbackData)));
            }

            telegram.sendMessageWithKeyboard(session.chatId(), prompt.text(), rows);
        } else {
            QuizResult result = quiz.finishQuiz(userId);
            telegram.sendMessage(session.chatId(),
                    "Quiz finished! Final score: " + result.correctAnswers()
                            + " out of " + result.totalQuestions());
        }
    }
}
