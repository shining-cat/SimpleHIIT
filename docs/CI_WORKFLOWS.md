# CI/CD Workflows Documentation

This document describes all GitHub Actions workflows used in the SimpleHIIT project for continuous integration and deployment automation.

## Table of Contents
- [Android Verification](#android-verification)
- [Auto Merge and Delete](#auto-merge-and-delete)
- [Update Module Dependency Graph](#update-module-dependency-graph)
- [Monthly Deprecation Check](#monthly-deprecation-check)

---

## Android Verification

**Workflow File:** `.github/workflows/android-verifications.yml`

**Trigger:** Pull requests to `master` or `develop` branches (ignores `.md` and `.png` files)

**Purpose:** Validates code quality, module structure, and functionality

### Jobs

**1. Ktlint Check** - Ensures Kotlin coding standards
**2. Verify Module Dependencies** - Validates module architecture
**3. Unit Tests** - Runs all unit tests and generates reports

---

## Auto Merge and Delete

**Workflow File:** `.github/workflows/auto-merge.yml`

**Trigger:** Label addition or Android Verification completion

**Purpose:** Automatically merges PRs and deletes branches when labeled and all checks pass

### Usage

Add this label to any PR:
```
HEX merge and delete
```

When all Android Verification checks pass, the PR will be automatically merged and the branch deleted.

**Works in any order:**
- Label added → Checks pass → Auto-merge
- Checks pass → Label added → Auto-merge

---

## Update Module Dependency Graph

**Workflow File:** `.github/workflows/update-module-dependency-graph.yml`

**Trigger:** Pushes to `master` branch or manual dispatch

**Purpose:** Generates and creates a PR with updated module dependency graph

### What It Does

1. Runs `./gradlew generateUnifiedDependencyGraph` to create `docs/project_dependencies_graph.png`
2. Checks if the graph has changed
3. If changes detected:
   - Creates/updates the `auto-update-dependency-graph` branch
   - Commits the updated graph
   - Creates a pull request (if one doesn't already exist)
   - Adds the `HEX merge and delete` label

The PR is automatically labeled with `HEX merge and delete`, which triggers the auto-merge workflow to merge it once all required status checks pass. The temporary branch is automatically deleted after merging.

The generated graph is also uploaded as an artifact with 90-day retention.

### Repository Settings Requirements

For this workflow to function properly, ensure these settings are configured:

**GitHub Repository Settings → Actions → General → Workflow permissions:**
- Select "Read and write permissions"
- Check "Allow GitHub Actions to create and approve pull requests"

Without these settings enabled, the workflow will fail with: `"GitHub Actions is not permitted to create or approve pull requests"`

### Implementation Details

This workflow uses native GitHub Actions capabilities:
- `git` commands for branch management and commits
- `actions/github-script@v7` for PR creation via GitHub REST API
- No third-party actions required for PR creation

---

## Monthly Deprecation Check

**Workflow File:** `.github/workflows/deprecation-check.yml`

**Trigger:** Monthly on the 1st at 9:00 AM UTC, or manual dispatch

**Purpose:** Scans codebase for deprecation warnings and outdated dependencies

### What It Does

Performs build deprecation checks, Android Lint checks, and dependency updates analysis. Automatically creates or updates a GitHub issue when deprecations are found, with detailed reports uploaded as artifacts (90-day retention).

See [DEPRECATION_CHECKER.md](DEPRECATION_CHECKER.md) for full documentation.

---

*Last Updated: 2025-11-20*
