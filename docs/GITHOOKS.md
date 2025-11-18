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
- Runs `ktlintCheck` twice if the first attempt fails
- Automatically handles minor auto-fixable formatting issues
- Provides clear, color-coded console feedback
- Only blocks commits if issues remain after both attempts

**Workflow:**

1. **First Check:**
   - Runs `./gradlew ktlintCheck --daemon`
   - If successful → commit proceeds
   - If failed → proceeds to second check

2. **Second Check (if first failed):**
   - Runs ktlintCheck again
   - If successful → commit proceeds (ktlint auto-fixed minor issues)
   - If failed → commit is blocked

**When Commit is Blocked:**

If ktlint finds issues that require manual intervention, you'll see:

```
❌ Ktlint check failed after 2 attempts! Commit blocked.

Action required:
  1. Run: ./gradlew ktlintFormat
  2. Review and stage the changes
  3. Commit again

To bypass this check (not recommended):
  git commit --no-verify
```

**Example Output:**

Success on first attempt:
```
════════════════════════════════════════════════════════════
Running ktlint check (attempt 1/2)...
════════════════════════════════════════════════════════════

✅ Ktlint check passed on first attempt!
✅ Commit proceeding...
```

Success on second attempt (auto-fixed):
```
════════════════════════════════════════════════════════════
Running ktlint check (attempt 1/2)...
════════════════════════════════════════════════════════════

⚠️  First ktlint check detected issues
⚠️  Ktlint may have auto-fixed some minor formatting issues

════════════════════════════════════════════════════════════
Running ktlint check (attempt 2/2)...
════════════════════════════════════════════════════════════

✅ Ktlint check passed on second attempt!
✅ Minor issues were auto-fixed by ktlint
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

### Ktlint Check Failing

If the pre-commit hook keeps failing:

1. Run ktlint format manually:
   ```bash
   ./gradlew ktlintFormat
   ```

2. Review the changes:
   ```bash
   git diff
   ```

3. Stage the changes:
   ```bash
   git add .
   ```

4. Commit again:
   ```bash
   git commit -m "Your message"
   ```

### Pre-push Merge Conflicts

If the pre-push hook encounters merge conflicts:

1. Resolve the conflicts in your IDE or text editor

2. Continue the merge:
   ```bash
   git merge --continue
   ```

3. Push again:
   ```bash
   git push
   ```

Or abort and handle manually:
```bash
git merge --abort
```

## Related Documentation

- [Ktlint Formatting Guide](./KTLINT_FORMATTING_GUIDE.md) - Detailed information about code formatting
- [Gradle Tasks](./GRADLE_TASKS.md) - Available Gradle commands including ktlintFormat

## Best Practices

1. **Run ktlintFormat before committing** to catch issues early
2. **Keep your branch up to date** to minimize merge conflicts
3. **Review auto-formatted changes** before committing
4. **Only bypass hooks** when absolutely necessary and understand the implications
5. **Update hooks** after pulling changes to ensure you have the latest versions
