---
name: prepare-pr
description: Prepare a PR title and description from the current branch compared with main. Use when the user asks to "prepare a PR", "draft a PR description", or similar.
---

# Prepare PR

Drafts a concise, professional GitHub Pull Request title and description by comparing the current branch with `main`. Read-only: never modifies files, stages files, commits, pushes, merges, or opens the PR.

## Steps

1. Run `git branch --show-current` to get the current branch name.
2. Run `git log main..HEAD --oneline` to see the commits unique to this branch.
3. Run `git diff main...HEAD` (three-dot diff, i.e. against the merge base) to see the actual changes.
4. If there are no commits and no diff between the branch and `main`, stop and report that there are no meaningful changes to prepare a PR for. Do not invent a title or description.
5. Otherwise, generate:
   - **One concise PR title** summarizing the change (imperative mood, no trailing period).
   - **A PR description** with exactly these sections:

     ```markdown
     ## Summary
     Short explanation of the purpose.

     ## Changes
     - Clear bullet points for the important changes.

     ## Testing
     - Explain how the changes were verified.
     ```

     - Base the Summary and Changes sections on the commit log and diff.
     - Base the Testing section on evidence in the diff/commits (e.g. added or updated tests, a `mvn test` run mentioned in commit messages). If there's no evidence of testing, say so plainly (e.g. "Not verified — no test changes or run recorded") rather than fabricating a testing story.

6. Present the title and description to the user as plain text/markdown output. Do not create, stage, commit, push, merge, or open a pull request as part of this skill.