# 07 — Docker

## Learning objective

Build and run the Spring Boot JAR in a reproducible Java 21 container.

## Concept

The supplied multi-stage Dockerfile builds with Maven, then copies only the runnable JAR into a runtime image. Variables are injected when the container runs. Read [Docker guide](../docs/06-docker.md).

## Practical task

- [ ] Build the image from the repository root.
- [ ] Run with environment variables and a matching port mapping; request `/health`.
- [ ] Inspect logs, stop the container, and explain why `.env` is excluded from the image.

## Acceptance criteria

The image builds, runs as a non-root user, and responds at `/health`. No credentials are stored in the image or Git.

## Optional hints

Use `docker ps`, `docker logs`, and `docker stop` to diagnose the run.

## Expected result

A locally verified Docker image ready for Render deployment.
