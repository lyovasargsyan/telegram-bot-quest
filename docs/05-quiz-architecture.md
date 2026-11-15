# Quiz architecture and student boundary

`WebhookController` validates the secret and passes a typed `TelegramUpdate` to `UpdateRouter`. The router recognizes command messages and callback queries. `CommandHandler` and `CallbackHandler` contain `TODO(student)` methods. Students connect them to `QuizService`, `SessionStore`, and the supplied `TelegramApiClient`.

`QuestionRepository` loads and checks the bundled JSON at startup. Every question has four distinct option IDs and one `correctOptionId` that matches an option. The `Question` model is internal; `QuestionPrompt` omits the correct answer for Telegram. `QuizService` deliberately throws `UnsupportedOperationException` in student-owned methods; no completed quiz behavior is hidden in the starter.

Use Telegram **user ID** to key a session, since two users in one group can have separate quizzes. Keep the **chat ID** for replies. A callback carries a user, a message/chat, and callback data. Validate that it matches the session’s current question before changing score. Consider an atomic update for one user so duplicate or concurrent callbacks cannot score twice. An in-memory map is lost when the process restarts; Render Free can restart or sleep. See [task 03](../tasks/03-quiz-logic.md), [task 05](../tasks/05-user-sessions.md), and [Telegram update model](https://core.telegram.org/bots/api#update).
