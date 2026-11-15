package com.example.telegrambot.telegram;

import com.example.telegrambot.config.BotProperties;
import com.example.telegrambot.exception.TelegramApiException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;

class TelegramApiClientTest {
    private final RestClient.Builder builder = RestClient.builder();
    private final MockRestServiceServer server = MockRestServiceServer.bindTo(builder).build();
    private final TelegramApiClient client = new TelegramApiClient(builder, new BotProperties("test-token", "test-secret"));

    @Test
    void sendsMessageAndKeyboardAsJson() {
        server.expect(requestTo("https://api.telegram.org/bottest-token/sendMessage"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.chat_id").value(7))
                .andExpect(jsonPath("$.reply_markup.inline_keyboard[0][0].callback_data").value("q01-a"))
                .andRespond(withSuccess("{\"ok\":true}", MediaType.APPLICATION_JSON));
        client.sendMessageWithKeyboard(7, "Question?", List.of(List.of(new InlineButton("A", "q01-a"))));
        server.verify();
    }

    @Test
    void hidesTokenInErrors() {
        server.expect(requestTo("https://api.telegram.org/bottest-token/answerCallbackQuery"))
                .andRespond(withStatus(HttpStatus.BAD_REQUEST).body("{\"ok\":false}"));
        TelegramApiException error = assertThrows(TelegramApiException.class,
                () -> client.answerCallbackQuery("callback-1", "Too late"));
        assertFalse(error.getMessage().contains("test-token"));
        server.verify();
    }
}
