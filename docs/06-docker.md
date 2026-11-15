# Docker locally

The [Dockerfile](../Dockerfile) uses a Maven/Java 21 build stage and a smaller Java 21 runtime stage. It runs the JAR as a non-root user. `.dockerignore` keeps local build output, Git data, and secrets out of the build context. The image contains code and the question bank, never credentials.

Build with `docker build -t telegram-bot-quest .`. With real environment variables already set in your shell, run `docker run --rm -p 8080:8080 -e TELEGRAM_BOT_TOKEN -e TELEGRAM_WEBHOOK_SECRET telegram-bot-quest`. Then request `http://localhost:8080/health`. On Windows PowerShell, verify how Docker passes variables from your shell and use `docker logs` or the attached terminal to inspect startup. `PORT` can be set to another container port, but the `-p` mapping must match it. Never bake secrets into a Dockerfile or image. See [task 07](../tasks/07-docker.md) and [Docker multi-stage builds](https://docs.docker.com/build/building/multi-stage/).
