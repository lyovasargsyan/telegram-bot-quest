# 05 — User sessions

## Learning objective

Keep independent quiz progress for multiple users and handle concurrent or duplicate updates.

## Concept

The user ID identifies a participant; chat ID identifies where to reply. `QuizSession` models progress. `SessionStore` is intentionally incomplete. A thread-safe map such as `ConcurrentHashMap` is suitable for this local homework, but a map alone does not make multi-step score updates atomic. Read [architecture](../docs/05-quiz-architecture.md).

## Practical task

- [ ] Implement `SessionStore` and integrate it with `QuizService`.
- [ ] Implement `/score` and `/reset` for only the requesting user.
- [ ] Handle duplicate, delayed, and out-of-order callbacks, including two clicks close together.
- [ ] Document what happens to sessions after a restart or Render spin-down.

## Acceptance criteria

Two users can quiz independently, one user cannot change another's score, duplicate answers do not count twice, and reset removes only one user's progress.

## Optional hints

Consider `ConcurrentHashMap.compute` for an atomic per-user transition. Use the callback user ID, not chat ID, to find a session.

## Expected result

Reliable in-memory progress for each user while the application process remains alive.
