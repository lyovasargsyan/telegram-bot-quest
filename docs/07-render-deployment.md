# Deploy the completed bot to Render

This is for the **completed student bot**, after local tests and Docker pass. Render Free Web Services can sleep after idle time and take time to wake. Their local filesystem is ephemeral, and this project’s in-memory sessions disappear after a restart. This is suitable for learning, not a 24/7 production guarantee. Review [Render Free limits](https://render.com/docs/free) before deploying.

1. Push your repository to GitHub without `.env`, tokens, webhook secrets, or reports containing them.
2. In Render, create a **Web Service**, connect the repository, choose the **Docker** runtime and the **Free** instance type if available for your account, and deploy. Render builds the supplied Dockerfile. See [Render web service documentation](https://render.com/docs/web-services).
3. In the service environment settings, add `TELEGRAM_BOT_TOKEN` and a separate `TELEGRAM_WEBHOOK_SECRET`. Do not paste values into Git. The app binds `0.0.0.0` and reads Render’s `PORT`; locally it defaults to 8080.
4. Set the health-check path to `/health`. Wait for a healthy deploy and open `https://YOUR-SERVICE.onrender.com/health`.
5. Register the full webhook path with Telegram. In a local Bash session with your secrets already set, use variables to avoid typing token values into the command itself:

   ```bash
   export WEBHOOK_URL='https://YOUR-SERVICE.onrender.com/telegram/webhook'
   curl --fail-with-body --silent --show-error \
     --request POST "https://api.telegram.org/bot${TELEGRAM_BOT_TOKEN}/setWebhook" \
     --data-urlencode "url=${WEBHOOK_URL}" \
     --data-urlencode "secret_token=${TELEGRAM_WEBHOOK_SECRET}" \
     --data-urlencode 'allowed_updates=["message","callback_query"]'
   ```

   The token is still part of the request URL and may appear in shell history only if you type a literal token. Do not enable verbose HTTP tracing or share the command output with secrets. Use PowerShell’s `$env:` syntax instead of Bash `export` on Windows.
6. Query `https://api.telegram.org/bot${TELEGRAM_BOT_TOKEN}/getWebhookInfo` from a private local shell. Confirm the URL ends in `/telegram/webhook`, and inspect `last_error_message` if Telegram reports delivery problems. [Official webhook methods](https://core.telegram.org/bots/api#setwebhook).
7. Send `/start` and `/quiz` to your bot. Inspect Render logs for errors without logging secrets. Then turn off your computer and try again from Telegram. A cold start may delay the reply.

Troubleshooting: A 401 means the configured webhook secret and Render secret differ; 404 often means the URL omitted `/telegram/webhook`; a health failure may mean the app failed to bind `PORT` or startup configuration was missing; a webhook error may mean HTTPS, service state, or Telegram API configuration is wrong. `getUpdates` cannot be used while a webhook is active. Do not add keep-alive traffic to evade free-tier sleep behavior. Never disable TLS verification. See [Render health checks](https://render.com/docs/health-checks), [deployment troubleshooting](https://render.com/docs/troubleshooting-deploys), and [task 08](../tasks/08-deployment.md).
