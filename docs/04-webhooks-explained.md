# Webhooks and local testing

With a webhook, Telegram makes an HTTPS POST to your public URL when an update arrives. The deployed URL must include `/telegram/webhook`, for example `https://your-service.onrender.com/telegram/webhook`. The `setWebhook` `secret_token` causes Telegram to send `X-Telegram-Bot-Api-Secret-Token`; the controller checks it before routing. `/health` is public. A bad or missing secret returns 401; a valid JSON update with an ID gets 200. Do not log the token or secret.

Locally, test the boundary without publishing your computer or calling Telegram. With dummy environment values and the app running, a sample request is:

```bash
curl -i -X POST http://localhost:8080/telegram/webhook \
  -H 'Content-Type: application/json' \
  -H "X-Telegram-Bot-Api-Secret-Token: $TELEGRAM_WEBHOOK_SECRET" \
  -d '{"update_id":1,"message":{"message_id":2,"chat":{"id":7},"from":{"id":8},"text":"/start"}}'
```

On Windows PowerShell, use `Invoke-RestMethod` or `curl.exe` and PowerShell’s variable syntax. The starter returns 200 but does not send a welcome message until students implement it. [WebhookControllerTest](../src/test/java/com/example/telegrambot/controller/WebhookControllerTest.java) uses a mocked router, so no network or real token is needed.

Telegram’s `getUpdates` long polling and an outgoing webhook cannot operate at the same time for one bot. This project uses webhooks for deployment and does not start a polling loop. See [official webhook reference](https://core.telegram.org/bots/api#setwebhook) and [deployment guide](07-render-deployment.md).
