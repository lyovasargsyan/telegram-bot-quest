package com.example.telegrambot.telegram;

import com.fasterxml.jackson.annotation.JsonProperty;

public record InlineButton(String text, @JsonProperty("callback_data") String callbackData) {
}
