# Git Hooks

Custom git hooks for the SimpleHIIT project.

## Setup

Run once after cloning the repository:

```bash
./.githooks/setup.sh
```

Or manually:

```bash
git config core.hooksPath .githooks
chmod +x .githooks/pre-push
chmod +x .githooks/pre-commit
```

## Available Hooks

### pre-commit

Runs ktlint code style checks before allowing commits.

**What it does:**
1. Runs `./gradlew ktlintCheck --daemon` before each commit
2. Blocks the commit if ktlint finds formatting violations
3. Provides instructions to fix issues

**Why it helps:**
- Prevents accidentally committing code that violates ktlint rules
- Ensures consistent code style across the project
- Catches formatting issues early, before CI/PR review

**When it runs:**
Every time you execute `git commit`

**If violations are found:**
```bash
❌ Ktlint check failed! Commit blocked.

To fix the issues, run:
  ./gradlew ktlintFormat

Then stage your changes and commit again.
```

**Bypass if needed:**
```bash
git commit --no-verify
```

### pre-push

Automatically handles remote changes before pushing, specifically for CI-generated commits (like dependency graph updates).

**What it does:**
1. Fetches the remote branch
2. Checks if remote has new commits
3. If yes, automatically merges the remote changes
4. Proceeds with push if successful

**Why merge instead of rebase?**
- Safer: No history rewriting
- Preserves commit hashes (important if you pushed commits before CI ran)
- CI changes (dependency graph) are typically trivial
- PRs usually squash-merge anyway, so the merge commit disappears

**When it helps:**
- You create a PR
- CI runs and commits the dependency graph
- You make more local changes
- You try to push → hook automatically rebases and pushes

**Bypass if needed:**
```bash
git push --no-verify
```

## How It Works

[//]: # (```)
Your workflow                        Without hook              With hook
─────────────────────────────────────────────────────────────────────────
1. git push                    →     Creates PR              Creates PR
2. CI runs, commits graph      →     (remote updated)        (remote updated)
3. Make local changes          →     Ready to push           Ready to push
4. git push                    →     ❌ Error: fetch first   ✅ Auto-merge + push
```

## Troubleshooting

**Hook doesn't run:**
- Check: `git config core.hooksPath` should return `.githooks`
- Check: `.githooks/pre-push` is executable (`chmod +x`)

**Merge conflicts:**
The hook will stop and show instructions. Usually:
```bash
# Resolve conflicts in your editor
git add <resolved-files>
git merge --continue
# Or abort with: git merge --abort
```

**Disable hooks temporarily:**
```bash
git push --no-verify
```

**Remove hooks:**
```bash
git config --unset core.hooksPath
