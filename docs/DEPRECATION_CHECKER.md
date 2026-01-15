<!--
  ~ SPDX-FileCopyrightText: 2024-2026 shining-cat
  ~ SPDX-License-Identifier: GPL-3.0-or-later
  -->
# Deprecation Checker Workflow

## Overview

The Deprecation Checker is a GitHub Actions workflow that automatically scans the codebase monthly to detect deprecation warnings and outdated dependencies. It helps maintain code quality by identifying deprecated APIs and library usage before they become breaking changes.

A status badge in the README shows the current state:
- **🟢 Green badge**: No deprecation warnings detected - codebase is clean
- **🔴 Red badge**: Deprecation warnings found - clicking the badge takes you to the open issue with details

## Features

- **Automated Monthly Checks**: Runs on the 1st of each month at 9:00 AM UTC
- **Manual Trigger**: Can be triggered manually via GitHub Actions UI
- **Multiple Detection Methods**:
  - Build-time deprecation warnings from Gradle
  - Android Lint deprecation checks
  - Outdated dependency detection
- **Automated Issue Management**: Creates or updates GitHub issues automatically
- **Detailed Reports**: Generates comprehensive reports saved as artifacts
- **Smart Issue Handling**: Updates existing issues instead of creating duplicates

## How It Works

### 1. Detection Process

The workflow performs build and deprecation checks with proper error handling:

#### Build and Deprecation Check
```bash
./gradlew clean assembleDebug --warning-mode all
```
- Runs a full project build with all warnings enabled
- **Properly captures and reports build failures** (critical fix as of Dec 2025)
- Uses `${PIPESTATUS[0]}` to detect Gradle exit code even when piping output
- Reports two distinct failure scenarios:
  - **Build failure**: Critical issue - the build failed before completion
  - **Deprecation warnings**: Maintenance issue - build succeeded but found deprecations
- Captures deprecation warnings from Kotlin and Java compilation
- Filters and extracts deprecation-related messages

#### Lint Deprecation Check
```bash
./gradlew lintDebug
```
- Runs Android Lint to detect deprecated API usage
- Scans for Android framework deprecations
- Checks for deprecated library methods

#### Dependency Updates Check
```bash
./gradlew dependencyUpdates
```
- Uses the dependency update plugin
- Identifies outdated dependencies
- Helps prevent using deprecated library versions

### 2. Report Generation

The workflow generates a streamlined markdown report including:
- Summary of total deprecations found
- **Direct download links to detailed artifacts** for easy access
- List of available report files in the artifact
- Build deprecation warnings grouped by file
- Lint deprecation warnings
- Outdated dependencies list

### 3. GitHub Issue Creation

When deprecations are found:
- **First run**: Creates a new issue with labels `deprecation-warning` and `technical-debt`
- **Subsequent runs**: Updates the existing open issue with a new comment
- **No deprecations**: No issue is created/updated

### 4. Artifacts

All reports are uploaded as GitHub Actions artifacts with **direct download links included in the GitHub issue**.

The artifact package includes:
- `build-output.log`: Complete build output
- `deprecations.txt`: Extracted build deprecations
- `lint-deprecations.txt`: Lint deprecation findings
- `outdated-deps.txt`: Outdated dependencies
- `deprecation-report.md`: Final formatted report

**Accessing Artifacts:**
- Click the download link at the top of the GitHub issue
- Or visit the workflow run page and download from the Artifacts section
- Artifacts are retained for 90 days

### Schedule

The workflow runs on a cron schedule defined in the workflow file:

```yaml
schedule:
  - cron: '0 9 1 * *'  # 1st day of each month at 9:00 AM UTC
```

## Related Documentation

- [CI Workflows](CI_WORKFLOWS.md)
- [Gradle Tasks](GRADLE_TASKS.md)
- [GitHub Actions Documentation](https://docs.github.com/en/actions)
