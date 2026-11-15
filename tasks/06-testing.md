# 06 — Unit testing

## Learning objective

Prove quiz behavior without calling the real Telegram API or needing a live bot.

## Concept

JUnit 5 tests behavior; Mockito can replace `TelegramApiClient` and other collaborators. Infrastructure tests already cover question loading and webhook secret validation. The disabled student test outline is a place to start.

## Practical task

- [ ] Write tests for five distinct questions, correct and incorrect answers, score calculation, completion, two-user isolation, duplicate answers, and stale callbacks.
- [ ] Mock Telegram API calls while testing command and callback handlers.
- [ ] Enable and replace the student test outline; run `./mvnw clean verify`.

## Acceptance criteria

All tests pass offline using dummy credentials. No test sends a real Telegram message. At least one test would fail if duplicate callbacks increased score twice.

## Optional hints

Use deterministic question selection in tests. See [Spring Boot testing reference](https://docs.spring.io/spring-boot/reference/testing/index.html).

## Expected result

A meaningful green test suite for the finished quiz behavior.
