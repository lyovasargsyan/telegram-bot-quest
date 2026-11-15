# 08 — Render webhook deployment

## Learning objective

Deploy the finished bot behind HTTPS, register a webhook, and diagnose delivery problems.

## Concept

Telegram requires a public HTTPS webhook URL. Render provides it and injects `PORT`. Render Free can sleep; in-memory sessions vanish on restart. Follow the [deployment guide](../docs/07-render-deployment.md).

## Practical task

- [ ] Push code to GitHub without secrets, create a Render Docker Web Service, and set token and webhook secret as environment variables.
- [ ] Set `/health` as the health-check path; verify it from a browser.
- [ ] Register the full `/telegram/webhook` URL with `setWebhook` and inspect `getWebhookInfo`.
- [ ] Test commands and a complete quiz, then test again while your own computer is off; inspect Render logs if delivery fails.

## Acceptance criteria

The public health endpoint works, webhook info shows the correct URL, the bot completes a quiz from Telegram, and it responds without your computer running. The report notes possible cold starts and lost sessions.

## Optional hints

A 401 suggests mismatched secret; 404 suggests a wrong path; no response may indicate a cold start or failed deploy. Never print secrets in logs.

## Expected result

A shareable educational bot, Render URL, GitHub repo, and completed [report](../submission/REPORT_TEMPLATE.md).
