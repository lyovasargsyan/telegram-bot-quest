package com.example.telegrambot.quiz;

import com.example.telegrambot.quiz.AnswerOption;
import com.example.telegrambot.quiz.QuestionPrompt;
import com.example.telegrambot.telegram.InlineButton;
import com.example.telegrambot.telegram.TelegramApiClient;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PromptSender {
    private final TelegramApiClient telegram;

    public PromptSender(TelegramApiClient telegram) {
        this.telegram = telegram;
    }

    public void send(long chatId, QuestionPrompt prompt, int number, int total) {
        List<List<InlineButton>> rows = new ArrayList<>();
        for (AnswerOption option : prompt.options()) {
            String data = "answer:" + prompt.id() + ":" + option.id();
            rows.add(List.of(new InlineButton(option.text(), data)));
        }
        telegram.sendMessageWithKeyboard(chatId,
                "Question " + number + " of " + total + ":\n\n" + prompt.text(), rows);
    }
}