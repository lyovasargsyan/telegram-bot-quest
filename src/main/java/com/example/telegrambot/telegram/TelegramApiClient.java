package com.example.telegrambot.telegram;

import com.example.telegrambot.config.BotProperties;
import com.example.telegrambot.exception.TelegramApiException;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.List;
import java.util.Map;

@Component
public class TelegramApiClient {
    private final RestClient client;

    public TelegramApiClient(RestClient.Builder builder, BotProperties properties) {
        this.client = builder.baseUrl("https://api.telegram.org/bot" + properties.botToken()).build();
    }

    public void sendMessage(long chatId, String text) {
        call("sendMessage", Map.of("chat_id", chatId, "text", text));
    }

    public void sendMessageWithKeyboard(long chatId, String text, List<List<InlineButton>> rows) {
        call("sendMessage", Map.of(
                "chat_id", chatId,
                "text", text,
                "reply_markup", Map.of("inline_keyboard", rows)));
    }

    public void answerCallbackQuery(String callbackQueryId, String text) {
        call("answerCallbackQuery", Map.of("callback_query_id", callbackQueryId, "text", text));
    }

    private void call(String method, Object request) {
        try {
            ApiResponse response = client.post()
                    .uri("/" + method)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(request)
                    .retrieve()
                    .body(ApiResponse.class);
            if (response == null || !response.ok()) {
                throw new TelegramApiException("Telegram API returned an unsuccessful response for " + method);
            }
        } catch (RestClientException exception) {
            // HTTP client exceptions can contain the request URL, which includes the bot token.
            throw new TelegramApiException("Telegram API request failed for " + method);
        }
    }

    private record ApiResponse(boolean ok) { }
}
