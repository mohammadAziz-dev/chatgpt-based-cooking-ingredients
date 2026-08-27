---
name: verify
description: Run tests and report git status without modifying files or committing. Use when the user asks to "verify" the project.
---

# Verify

Checks that the project builds and its tests pass, then reports the working tree state. Read-only: never modifies files, stages files, commits, pushes, or installs dependencies.

## Steps

1. Run `mvn clean test`.
   - If any test fails (or the build fails to compile), stop immediately. Report the failing test(s)/build error to the user and do not proceed to step 2.

2. If tests pass, run `git status`.
   - Report whether the working tree is clean.
   - If not clean, summarize the uncommitted changes (modified/added/deleted/untracked files) without staging, committing, or otherwise altering them.
