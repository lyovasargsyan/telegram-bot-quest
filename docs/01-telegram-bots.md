# How Telegram bots work

A Telegram bot is an account controlled by software. Users talk to it in Telegram chats; Telegram sends updates to your backend and your backend calls the Bot API to reply. Telegram already supplies the mobile and desktop chat UI, so this project needs no frontend. A bot username is public; its token is a secret that authorizes API calls.

An update can contain a `message` (for example `/start`) or a `callback_query` (a button click). The update ID identifies the delivery event. A message has a chat ID (where to send a reply) and a user ID (who acted). In a group, those IDs differ. Read [Telegram’s bot introduction](https://core.telegram.org/bots) and the [Bot API reference](https://core.telegram.org/bots/api), then start [task 01](../tasks/01-create-telegram-bot.md).
