# 04 — Inline buttons and callbacks

## Learning objective

Send answers as inline buttons, interpret a click safely, and acknowledge the callback.

## Concept

Each button sends `callback_data` back in a `callback_query`. The provided `TelegramApiClient` can send keyboards and call `answerCallbackQuery`. Callback data should identify the question and option, but should not reveal the correct answer. See [Bot API guide](../docs/03-telegram-bot-api.md).

## Practical task

- [ ] Build four buttons for each prompt with concise callback data.
- [ ] Implement `CallbackHandler.handle` to parse and validate data and call the quiz service.
- [ ] Acknowledge both valid and stale callbacks; send feedback and the next prompt or result.

## Acceptance criteria

A user can complete a quiz by tapping buttons. Malformed, stale, or repeated callback data does not change score or advance twice; the callback is acknowledged so the Telegram spinner ends.

## Optional hints

Telegram limits callback-data size. Keep it short and treat it as untrusted input. See [callback query reference](https://core.telegram.org/bots/api#callbackquery).

## Expected result

Interactive, sequential quiz questions with safe feedback.
