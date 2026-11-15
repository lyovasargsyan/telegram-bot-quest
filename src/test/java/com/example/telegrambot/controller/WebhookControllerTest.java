package com.example.telegrambot.controller;

import com.example.telegrambot.command.UpdateRouter;
import com.example.telegrambot.config.BotProperties;
import com.example.telegrambot.telegram.TelegramUpdate;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class WebhookControllerTest {
    private final UpdateRouter router = mock(UpdateRouter.class);
    private final MockMvc mvc = MockMvcBuilders.standaloneSetup(
            new WebhookController(new BotProperties("test-token", "test-secret"), router),
            new HealthController()).build();

    @Test
    void healthIsPublic() throws Exception {
        mvc.perform(get("/health")).andExpect(status().isOk()).andExpect(jsonPath("$.status").value("UP"));
    }

    @Test
    void rejectsMissingAndWrongSecret() throws Exception {
        String update = "{\"update_id\":1}";
        mvc.perform(post("/telegram/webhook").contentType(MediaType.APPLICATION_JSON).content(update))
                .andExpect(status().isUnauthorized());
        mvc.perform(post("/telegram/webhook").header("X-Telegram-Bot-Api-Secret-Token", "wrong")
                        .contentType(MediaType.APPLICATION_JSON).content(update))
                .andExpect(status().isUnauthorized());
        verifyNoInteractions(router);
    }

    @Test
    void routesValidUpdate() throws Exception {
        mvc.perform(post("/telegram/webhook").header("X-Telegram-Bot-Api-Secret-Token", "test-secret")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"update_id\":1,\"message\":{\"chat\":{\"id\":7},\"from\":{\"id\":8},\"text\":\"/start\"}}"))
                .andExpect(status().isOk());
        verify(router).route(org.mockito.ArgumentMatchers.argThat(
                update -> update.updateId() == 1 && update.message().chat().id() == 7));
    }

    @Test
    void rejectsMissingUpdateId() throws Exception {
        mvc.perform(post("/telegram/webhook").header("X-Telegram-Bot-Api-Secret-Token", "test-secret")
                        .contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isBadRequest());
        verifyNoInteractions(router);
    }
}
