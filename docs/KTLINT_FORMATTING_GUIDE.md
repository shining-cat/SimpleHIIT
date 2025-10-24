# Ktlint Formatting Guide

## The Problem

Android Studio's built-in "Reformat Code" formatter **does not fully respect ktlint rules**, even with .editorconfig settings. This causes issues like:
- Method chain dots moving to the beginning of lines instead of the end
- Incorrect blank line insertion (e.g., in `when` blocks)
- Other subtle formatting differences that cause ktlint checks to fail

## The Root Cause

Android Studio uses IntelliJ's Kotlin formatter, which has its own opinions about code style. While .editorconfig can influence some settings, IntelliJ's formatter has limitations and cannot be fully aligned with ktlint, particularly for:
- Method call chain wrapping
- Blank line rules
- Some indentation edge cases

## The Solution: Proper Formatting Workflow

### ✅ DO: Use These Methods for Formatting

#### 1. **In-File Formatting (Recommended for Individual Files)**
When working on a single file:
- Open the file in the editor
- Press `Cmd+Alt+L` (Mac) or `Ctrl+Alt+L` (Windows/Linux)
- The ktlint plugin will assist with formatting
- This works better than bulk operations

#### 2. **Ktlint Gradle Task (Recommended for Bulk Operations)**
For formatting multiple files or the entire project:
```bash
# Format all Kotlin files according to ktlint rules
./gradlew ktlintFormat

# Check formatting without making changes
./gradlew ktlintCheck
```

#### 3. **Ktlint Plugin Manual Format**
If you have the ktlint plugin enabled:
- Right-click on a file/folder in the Project view
- Select "Apply ktlint Format"
- Note: The plugin mode is set to MANUAL in `.idea/ktlint-plugin.xml`

### ❌ DON'T: Avoid These Methods

#### 1. **Folder-Level "Reformat Code"**
**DO NOT** select a folder and use "Code → Reformat Code"
- This uses IntelliJ's formatter, which conflicts with ktlint
- Will cause ktlint checks to fail
- Creates unnecessary changes that need to be reverted

#### 2. **Commit Window "Reformat Code" Checkbox**
In the commit dialog, **DO NOT** check "Reformat code"
- Go to **Settings → Version Control → Commit**
- Ensure "Reformat code" is unchecked
- Or if checked, understand it will use IntelliJ's formatter, not ktlint

## Recommended Workflow

### Daily Development
1. Work on your code normally
2. Use `Cmd+Alt+L` to format individual files as you work
3. Before committing, run `./gradlew ktlintCheck` to verify
4. If ktlint reports issues, run `./gradlew ktlintFormat` to fix them

### Before Committing
```bash
# Check if your code meets ktlint standards
./gradlew ktlintCheck

# If there are issues, auto-fix them
./gradlew ktlintFormat

# Then stage and commit your changes
git add .
git commit -m "Your message"
```

### Pre-Commit Hook (Enabled)
A git pre-commit hook has been set up in `.githooks/pre-commit` that automatically runs ktlint before each commit:
- Runs `./gradlew ktlintCheck --daemon` before allowing commits
- If ktlint finds issues, the commit is blocked
- Provides instructions to fix issues with `./gradlew ktlintFormat`
- Can be bypassed (not recommended) with `git commit --no-verify`

This ensures you never accidentally commit code that violates ktlint rules.

**Setup:**
The hook is configured via `.githooks/setup.sh`. If you cloned the repo and haven't set up hooks yet, run:
```bash
./.githooks/setup.sh
```

See `.githooks/GITHOOKS.md` for more details about all available git hooks.

## Understanding the .editorconfig

The `.editorconfig` file has been updated with settings that attempt to align Android Studio's formatter with ktlint. However, these are **best-effort only** and cannot guarantee full compatibility.

The most important settings:
```ini
# Method call chain wrapping - keep dot at the end of the line
ij_kotlin_method_call_chain_wrap = off

# Blank lines in when blocks
ij_kotlin_blank_lines_around_block_when_branches = 0
```

## Summary

| Action | Tool to Use | ✅/❌ |
|--------|-------------|-------|
| Format single file | `Cmd+Alt+L` in editor | ✅ OK |
| Format multiple files | `./gradlew ktlintFormat` | ✅ Recommended |
| Format entire project | `./gradlew ktlintFormat` | ✅ Recommended |
| Reformat folder in IDE | Folder → Reformat Code | ❌ Avoid |
| Commit with "Reformat code" | Commit dialog checkbox | ❌ Avoid |
| Check formatting | `./gradlew ktlintCheck` | ✅ Recommended |

## Quick Reference

```bash
# Check formatting (doesn't modify files)
./gradlew ktlintCheck

# Auto-fix formatting issues
./gradlew ktlintFormat

# Format only changed files (faster)
./gradlew ktlintFormat -PinternalKtlintGitFilter=true
```

## Troubleshooting

If you accidentally formatted with Android Studio's formatter:
1. Revert the changes: `git checkout -- .`
2. Run ktlint format: `./gradlew ktlintFormat`
3. Review the proper changes: `git diff`
4. Stage only the desired changes

## Further Reading

- [Ktlint documentation](https://pinterest.github.io/ktlint/)
- [EditorConfig properties](https://editorconfig.org/)
- [Ktlint Gradle plugin](https://github.com/JLLeitschuh/ktlint-gradle)
