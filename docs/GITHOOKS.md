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
- **Smart checking:** Runs ktlintCheck first to detect issues early
- **Fast-fails:** Immediately blocks commits on non-auto-correctable issues
- Only formats when all issues are auto-correctable
- Temporarily stashes unstaged changes to avoid modifying them
- Automatically re-stages formatted files
- Provides clear, color-coded console feedback with helpful guidance

**Workflow:**

1. **Identify Staged Files:**
   - Detects which Kotlin files are staged for commit
   - If no Kotlin files are staged → commit proceeds immediately

2. **Isolate Staged Changes:**
   - Temporarily stashes any unstaged changes (if present)
   - This ensures only staged files are checked/formatted

3. **Check Code Style (Step 1):**
   - Runs `./gradlew ktlintCheck --daemon`
   - If all checks pass → commit proceeds immediately (fast path!)
   - If issues found → proceeds to analysis

4. **Analyze Issues:**
   - Detects if issues can be auto-corrected
   - Searches for "(cannot be auto-corrected)" in ktlint output
   - **If non-auto-correctable issues found → fail immediately with guidance**
   - If all issues are auto-correctable → proceeds to formatting

5. **Auto-Format (if applicable):**
   - Runs `./gradlew ktlintFormat --daemon`
   - Only runs if all issues were determined to be auto-correctable

6. **Verify (Step 2):**
   - Runs `./gradlew ktlintCheck --daemon` again
   - Ensures formatting actually fixed all issues

7. **Restore and Update:**
   - Restores any stashed unstaged changes
   - Re-stages formatted files automatically
   - Commit proceeds if all checks pass

**Example Outputs:**

**Scenario 1: No Kotlin files staged**
```
════════════════════════════════════════════════════════════
Running ktlint checks on staged files...
════════════════════════════════════════════════════════════

✅ No Kotlin files staged for commit
✅ Commit proceeding...
```

**Scenario 2: Code already formatted correctly**
```
════════════════════════════════════════════════════════════
Running ktlint checks on staged files...
════════════════════════════════════════════════════════════

Staged Kotlin files:
  • android/mobile/app/src/main/java/Example.kt

Step 1: Checking code style...

✅ Code style check passed
✅ Commit proceeding...
```

**Scenario 3: Issues auto-corrected successfully**
```
════════════════════════════════════════════════════════════
Running ktlint checks on staged files...
════════════════════════════════════════════════════════════

Staged Kotlin files:
  • android/mobile/app/src/main/java/Example.kt

Temporarily stashing unstaged changes...
Step 1: Checking code style...

Code style issues found. Analyzing...

All issues are auto-correctable. Running formatter...

Step 2: Verifying formatting fixed all issues...

Auto-formatted files:
  • android/mobile/app/src/main/java/Example.kt

Re-staging formatted files...
✅ Formatted files have been re-staged
✅ All code style checks passed
✅ Commit proceeding...
Restoring unstaged changes...
```

**Scenario 4: Non-auto-correctable issues (commit blocked)**
```
════════════════════════════════════════════════════════════
Running ktlint checks on staged files...
════════════════════════════════════════════════════════════

Staged Kotlin files:
  • shared-ui/home/di/HomeModuleHilt.kt

Step 1: Checking code style...

Code style issues found. Analyzing...

❌ Found 1 issue(s) that cannot be auto-corrected!

Non-auto-correctable issues:
  HomeModuleHilt.kt:1:1 File 'HomeModuleHilt.kt' contains a single class and should be named 'HomeModule.kt' (cannot be auto-corrected)

These issues must be fixed manually before committing.

Common fixes:
  • File naming: Add @file:Suppress("MatchingDeclarationName") if temporary
  • Or rename the file to match the class name

After fixing issues:
  1. Stage your changes: git add <files>
  2. Try committing again

To bypass this check (not recommended):
  git commit --no-verify
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
