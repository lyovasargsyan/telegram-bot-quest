# Telegram Bot Quest report — your name

Copy this file to `submission/REPORT.md` and complete it. `REPORT.md` is ignored until you choose to submit it; use `git add -f submission/REPORT.md` when ready. Do not include tokens or secrets.

## Links

- GitHub repository:
- Render service URL:
- Telegram bot username/link:

## Completed tasks

Summarize tasks 01–08 and show screenshots or command output for build, tests, Docker, `/health`, webhook status, and a real bot conversation.

## Telegram integration

How does a Telegram update reach your Java application? How do replies return? How did you confirm the webhook URL and secret without showing the secret?

## Backend architecture

Explain the controller, router, command handlers, quiz service, question repository, and Telegram API client. Which code did you implement?

## User sessions

Explain your key (`userId`), chat ID use, concurrency choice, duplicate/stale callback checks, and what happens after restart.

## Testing

List tests for selection, correct/incorrect answers, scoring, separate users, duplicate callbacks, and completion. Include `./mvnw clean verify` result.

## Deployment

Explain Docker build, Render configuration, `PORT`, `/health`, `setWebhook`, `getWebhookInfo`, and a test while your computer was off. Note any cold start observed.

## Problems and solutions

For each notable problem, record the symptom, investigation, fix, and what you learned.
