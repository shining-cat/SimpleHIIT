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

The workflow performs three types of checks:

#### Build Deprecation Check
```bash
./gradlew build --warning-mode=all
```
- Runs a full project build with all warnings enabled
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

The workflow generates a comprehensive markdown report including:
- Summary of total deprecations found
- Build deprecation warnings
- Lint deprecation warnings
- Outdated dependencies list
- Action items checklist

### 3. GitHub Issue Creation

When deprecations are found:
- **First run**: Creates a new issue with labels `deprecation-warning` and `technical-debt`
- **Subsequent runs**: Updates the existing open issue with a new comment
- **No deprecations**: No issue is created/updated

### 4. Artifacts

All reports are uploaded as GitHub Actions artifacts:
- `build-output.log`: Complete build output
- `deprecations.txt`: Extracted build deprecations
- `lint-deprecations.txt`: Lint deprecation findings
- `outdated-deps.txt`: Outdated dependencies
- `deprecation-report.md`: Final formatted report

Artifacts are retained for 90 days.

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
