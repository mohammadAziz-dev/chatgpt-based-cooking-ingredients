# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Commands

No Maven wrapper is present, so use a locally installed `mvn`.

- Build: `mvn compile`
- Run all tests: `mvn test`
- Run a single test: `mvn test -Dtest=ChatgptBasedCookingIngredientsApplicationTests`
- Package: `mvn package`
- Run the app: `mvn spring-boot:run` (requires `OPENAI_API_KEY` env var set, see below)

## Architecture

Spring Boot 4 / Java 25 REST service that classifies cooking ingredients (vegan / vegetarian / regular) by calling the OpenAI Chat Completions API.

Request flow: `IngredientController` (`POST /ingredients`, raw string body) → `IngredientService.categorizeIngredient` → OpenAI `/chat/completions` via a `RestClient`.

- `IngredientService` builds the `RestClient` itself (base URL `https://api.openai.com/v1`), reading the API key from the `OPENAI_API_KEY` environment variable at construction time (`System.getenv`, not a Spring property) and sending it as the `Authorization: Bearer` header. The service is currently hardcoded to model `gpt-5-mini` and a fixed classification prompt.
- `com.example.chatgptbasedcookingingredients.openai` contains minimal records mirroring the parts of the OpenAI chat completions request/response schema actually used: `OpenAiRequest` (model, messages), `OpenAiMessage` (role, content), `OpenAiResponse` (choices), `OpenAiChoice` (message). Extend these records if more of the OpenAI response needs to be consumed.
- `spring-boot-starter-restclient-test` is a test dependency (per the Spring Boot 4 migration in the git history) for exercising `RestClient` calls against a mock server rather than mocking with WebFlux/WebClient test utilities.
- `src/main/resources/application.properties` is currently empty — no Spring-managed configuration exists yet (e.g. no `spring.application.name`, no server port override).
- Lombok is used (`@RequiredArgsConstructor` on `IngredientController`) and is excluded from the final Spring Boot fat jar via the `spring-boot-maven-plugin` exclude configuration in `pom.xml`.

## Conventions

- Follow the existing Controller → Service structure.
  - This applies to new registration logic as well.
- Use constructor injection.
- Do not change unrelated existing endpoints.
- Passwords must never be stored in plain text.

## Off limits

- Do not add or change dependencies without asking first.
- Never hardcode API keys or other secrets in source code.
