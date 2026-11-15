# 03 — Quiz rules

## Learning objective

Select five distinct questions and implement a clear, testable quiz progression and score.

## Concept

`QuestionRepository` supplies 30 checked questions. `Question` includes the answer key; `QuestionPrompt` omits it. `QuizService` is student-owned. Read [architecture](../docs/05-quiz-architecture.md).

## Practical task

- [ ] Implement question selection, starting a quiz, answer validation, moving to the next question, and finishing a quiz in `QuizService`.
- [ ] Implement `/quiz` in `CommandHandler` so one question is delivered at a time.
- [ ] Decide how to handle `/quiz` when an unfinished quiz exists, and document the choice.

## Acceptance criteria

Exactly five distinct questions are selected; only the current question can be answered; a correct answer increments score once; final result is from 0 to 5; the correct option ID is never sent to users before they answer.

## Optional hints

Write tests before wiring Telegram replies. Keep score changes in one place.

## Expected result

A five-question flow that finishes with a score, without exposing the answer key.
