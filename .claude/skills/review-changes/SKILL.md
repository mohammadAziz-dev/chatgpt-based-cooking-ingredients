---
name: review-changes
description: Review the current uncommitted changes without modifying anything. Use when the user asks to "review" the current changes/diff.
---

# Review Changes

Reviews the working tree's uncommitted changes for correctness and quality issues. Read-only: never modifies files, stages files, commits, pushes, or installs dependencies.

## Steps

1. Run `git status` to see which files are modified/added/deleted/untracked.
2. Run `git diff` (and `git diff --staged` if anything is staged) to see the actual changes. For new untracked files, read their contents directly.
3. Review the changes for:
   - Bugs or incorrect behavior
   - Unnecessary complexity
   - Security problems (e.g. hardcoded secrets, injection risks, plaintext password storage)
   - Violations of this repo's `CLAUDE.md` conventions (Controller → Service structure, constructor injection, no unrelated endpoint changes, no plaintext passwords, no dependency changes without asking, no hardcoded secrets)
   - Missing or weak tests for the changed behavior
4. Report findings clearly, grouped by severity, citing the relevant file (and line, if useful) for each.
5. If no important problems are found, say so clearly instead of inventing minor nitpicks.

Do not edit, stage, commit, push, or install anything as part of this review.