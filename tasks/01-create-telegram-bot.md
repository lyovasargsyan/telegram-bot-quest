# 01 — Create a Telegram bot

## Learning objective

Explain the difference between a public bot username and a secret API token; configure a local Java app without committing credentials.

## Concept

BotFather creates bot accounts and issues tokens. A token authorizes API calls. The webhook secret is a separate value used to authenticate incoming webhook requests. Read [BotFather setup](../docs/02-botfather-setup.md).

## Practical task

- [ ] Create a bot with BotFather and verify its public username opens in Telegram.
- [ ] Set `TELEGRAM_BOT_TOKEN` and a separate random `TELEGRAM_WEBHOOK_SECRET` in your shell or IDE run configuration.
- [ ] Start the app and request `/health`; test the missing-secret startup failure without revealing values.

## Acceptance criteria

The app starts with environment variables, `/health` responds, and no credential appears in Git or your report. You can explain username versus token.

## Optional hints

Use [.env.example](../.env.example) as a list of names; Spring Boot does not load it automatically. Explore [BotFather’s official guide](https://core.telegram.org/bots/tutorial#obtain-your-bot-token).

## Expected result

A private, configured starter app and a verified bot account. The bot will not answer yet.
