# Git Hooks Documentation

## Overview

This project uses git hooks to automate code quality checks and streamline the development workflow. Git hooks are scripts that run automatically at specific points in the git workflow.

## Installation

### Quick Setup

Run the setup script once after cloning the repository:

```bash
./.githooks/setup.sh
```

This script will:
1. Configure git to use the `.githooks` directory (sets `core.hooksPath`)
2. Make all hook scripts executable
3. Display a summary of installed hooks

### Manual Installation (Alternative)

If you prefer manual setup, you can configure git hooks with:

```bash
git config core.hooksPath .githooks
chmod +x .githooks/pre-commit
chmod +x .githooks/pre-push
```

## Active Hooks

### pre-commit Hook

**Purpose:** Enforces code style consistency using ktlint before allowing commits.

**Features:**
- Only formats files that are staged for commit (supports partial commits)
- Temporarily stashes unstaged changes to avoid formatting them
- Automatically re-stages formatted files
- Provides clear, color-coded console feedback
- Blocks commits if formatting issues cannot be auto-fixed

**Workflow:**

1. **Identify Staged Files:**
   - Detects which Kotlin files are staged for commit
   - If no Kotlin files are staged → commit proceeds immediately

2. **Isolate Staged Changes:**
   - Temporarily stashes any unstaged changes (if present)
   - This ensures only staged files are formatted

3. **Format:**
   - Runs `./gradlew ktlintFormat --daemon`
   - Formats all files in the project (but only staged files are present)

4. **Restore and Update:**
   - Restores any stashed unstaged changes
   - Re-stages formatted files automatically
   - If formatting succeeded → commit proceeds
   - If formatting failed → commit is blocked

**When Commit is Blocked:**

If ktlint encounters issues that cannot be auto-fixed, you'll see:

```
❌ Ktlint formatting failed! Commit blocked.

The formatter encountered issues that cannot be auto-fixed.
Review the errors above and fix them manually.

After fixing issues:
  1. Stage your changes: git add <files>
  2. Try committing again

To bypass this check (not recommended):
  git commit --no-verify
```

**Example Output:**

No Kotlin files staged:
```
════════════════════════════════════════════════════════════
Running ktlint format on staged files...
════════════════════════════════════════════════════════════

✅ No Kotlin files staged for commit
✅ Commit proceeding...
```

Files auto-formatted:
```
════════════════════════════════════════════════════════════
Running ktlint format on staged files...
════════════════════════════════════════════════════════════

Staged Kotlin files:
  • android/mobile/app/src/main/java/Example.kt

Temporarily stashing unstaged changes...
Restoring unstaged changes...

⚠️  Staged files were auto-formatted by ktlint!

Auto-formatted files:
  • android/mobile/app/src/main/java/Example.kt

Re-staging formatted files...
✅ Formatted files have been re-staged
✅ Commit proceeding...
```

No formatting needed:
```
════════════════════════════════════════════════════════════
Running ktlint format on staged files...
════════════════════════════════════════════════════════════

Staged Kotlin files:
  • android/mobile/app/src/main/java/Example.kt

✅ No formatting changes needed
✅ Commit proceeding...
```

### pre-push Hook

**Purpose:** Automatically handles remote changes before pushing, particularly useful for CI commits like dependency graph updates.

**Features:**
- Fetches and merges remote changes automatically
- Handles CI commits (like dependency graph updates) seamlessly
- Provides color-coded feedback about the merge process
- Skips on protected branches (master/develop)

**Workflow:**

1. **Check Current Branch:**
   - Skips if on `master` or `develop` (protected branches)

2. **Fetch Remote:**
   - Fetches the latest changes from the remote branch

3. **Compare States:**
   - **Up to date:** Push proceeds immediately
   - **First push:** No remote branch exists yet, proceeds
   - **Remote ahead:** Automatically merges remote changes
   - **Local ahead:** Push proceeds
   - **Diverged:** Attempts to merge remote changes

4. **Auto-merge:**
   - If merge succeeds → push proceeds
   - If merge conflicts → push is blocked, manual resolution required

**Example Output:**

Branch up to date:
```
Pre-push: Checking for remote changes...
Pre-push: Branch is up to date
```

Auto-merging remote changes:
```
Pre-push: Checking for remote changes...
Pre-push: Remote has new commits (likely CI updates)
Pre-push: Automatically merging...
Pre-push: Successfully merged, proceeding with push
```

Merge conflicts:
```
Pre-push: Checking for remote changes...
Pre-push: Branches have diverged
Pre-push: Attempting to merge...
Pre-push: Merge failed - conflicts need manual resolution
Resolve conflicts, then: git merge --continue
Or abort with: git merge --abort
```

## Bypassing Hooks

While not recommended, you can bypass hooks when necessary:

### Bypass pre-commit:
```bash
git commit --no-verify
```

### Bypass pre-push:
```bash
git push --no-verify
```

**Note:** Bypassing hooks should only be done in exceptional circumstances, as they exist to maintain code quality and prevent issues.

## Troubleshooting

### Hooks Not Running

If hooks aren't executing:

1. Verify hooks are configured:
   ```bash
   git config core.hooksPath
   ```
   Should output: `.githooks`

2. Re-run the setup script:
   ```bash
   ./.githooks/setup.sh
   ```

3. Check hook files are executable:
   ```bash
   ls -la .githooks/
   ```
   Hook files should have execute permissions (`-rwxr-xr-x`)

## Related Documentation

- [Ktlint Formatting Guide](KTLINT_FORMATTING_GUIDE.md) - Detailed information about code formatting
- [Gradle Tasks](GRADLE_TASKS.md) - Available Gradle commands including ktlintFormat
