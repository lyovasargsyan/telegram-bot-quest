# 02 — Basic commands

## Learning objective

Handle `/start` and `/help` messages and send useful replies through the provided API client.

## Concept

Telegram sends a message update containing user ID, chat ID, and text. `UpdateRouter` finds commands. A reply goes to the chat ID; user ID identifies the person. See [bot API guide](../docs/03-telegram-bot-api.md).

## Practical task

- [ ] Implement `CommandHandler.start` with a welcome message and how to begin a quiz.
- [ ] Implement `CommandHandler.help` describing `/quiz`, `/score`, and `/reset`.
- [ ] Test command routing with mocked `TelegramApiClient`, including a command addressed to your bot such as `/start@YourBot`.

## Acceptance criteria

Each command sends one appropriate reply to the correct chat; unsupported text does not trigger a misleading reply. No token or secret appears in logs.

## Optional hints

Use `TelegramMessage.chat().id()` for the reply. The router already separates a command from a callback.

## Expected result

Users receive a welcome and a help message in Telegram.
