---
name: ship
description: Run tests and lint checks, then commit staged changes. Use when the user asks to "ship" the current changes.
---

# Ship

Runs this project's verification steps and, if they pass, commits the currently staged changes.

## Steps

1. Run the test suite: `mvn test`
   - If any test fails, stop immediately. Report the failing test(s) to the user and do not proceed to the remaining steps.

2. Run the project linter.
   - This project currently has **no linter configured** (no checkstyle/spotless/PMD plugin in `pom.xml`, no linter config files in the repo). Note this to the user and skip to step 3. Do not add a linter plugin or dependency to satisfy this step — that requires explicit user approval first (see CLAUDE.md "Off limits").
   - If a linter is ever configured in `pom.xml`, run it here (e.g. `mvn checkstyle:check`, `mvn spotless:check`, or `mvn pmd:check` as applicable) and stop immediately if it reports errors, reporting them to the user without proceeding to step 3.

3. Commit all staged changes.
   - Run `git status` and `git diff --staged` to see what is staged.
   - If nothing is staged, report that and stop — do not stage files yourself unless the user asks.
   - Write a clear, concise commit message describing the *why* of the change, following this repo's existing commit message style (see `git log`).
   - Create the commit (do not amend, do not use `--no-verify`).