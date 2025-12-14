# CI/CD Workflows Documentation

This document describes all GitHub Actions workflows used in the SimpleHIIT project for continuous integration and deployment automation.

## Table of Contents
- [PR Verification Gate](#pr-verification-gate)
- [Android Verification](#android-verification)
- [Auto Merge and Delete](#auto-merge-and-delete)
- [Update Module Dependency Graph](#update-module-dependency-graph)
- [Monthly Deprecation Check](#monthly-deprecation-check)

---

## PR Verification Gate

**Workflow File:** `.github/workflows/pr-verification-gate.yml`

**Trigger:** All pull requests to `master` or `develop` branches

**Purpose:** Single entry point for PR validation that intelligently routes based on changed files

### How It Works

This is the **single required status check** for branch protection. It provides intelligent validation routing:

**For Documentation/Asset-Only PRs:**
- Detects changes to `.md`, `.png`, `.jpg`, `.svg`, `.txt` files
- Immediately passes without running code verification checks
- Provides clear status: "Documentation/assets only - verification checks skipped"

**For Code Changes:**
- Waits for all Android Verification checks to complete
- Reports their combined status
- Fails if any verification check fails

### Benefits

- ✅ Single check to configure in branch protection rules
- ✅ No wasted CI resources on documentation PRs
- ✅ Automatic handling of dependency graph PRs (which only update .png files)
- ✅ Easy to modify exempt file patterns without touching repo settings
- ✅ Clear, descriptive status messages

### Implementation Details

- Uses `actions/github-script@v7` to analyze PR file changes
- Polls for Android Verification check completion (10-second intervals, 60-minute timeout)
- Configurable exempt file patterns in workflow file

---

## Android Verification

**Workflow File:** `.github/workflows/android-verifications.yml`

**Trigger:** Pull requests to `master` or `develop` branches (ignores `.md` and `.png` files)

**Purpose:** Validates code quality, module structure, and functionality

**Note:** These checks are monitored by the PR Verification Gate. They run in parallel but don't need to be individually required in branch protection.

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

### Repository Settings Requirements

This workflow requires the same repository settings as the Update Module Dependency Graph workflow:

**GitHub Repository Settings → Actions → General → Workflow permissions:**
- Select "Read and write permissions"
- Check "Allow GitHub Actions to create and approve pull requests"

### Special Handling

**Auto-generated Dependency Graph PRs:**
- PRs from the `auto-update-dependency-graph` branch bypass verification checks
- Rationale: These PRs only update `.png` files which are already ignored by android-verifications
- The workflow detects the branch name and immediately marks the PR as ready to merge

### Implementation Details

Uses native GitHub Actions capabilities:
- `actions/github-script@v7` for all GitHub API interactions (PR details, status checks, merge, branch deletion)
- Default `GITHUB_TOKEN` (no personal access token required)

---

## Update Module Dependency Graph

**Workflow File:** `.github/workflows/update-module-dependency-graph.yml`

**Trigger:** Pushes to `master` branch or manual dispatch

**Purpose:** Automatically updates the module dependency graph after code changes

### What It Does

1. **Ensures human-made changes only** - Exits immediately if last commit is from `github-actions[bot]` to avoid bots looping on themselves
2. Runs `./gradlew generateUnifiedDependencyGraph` to create `docs/project_dependencies_graph.png`
3. Checks if the graph has changed (secondary protection via diff)
4. If changes detected:
   - Commits the updated graph **directly to master**
   - Uses `[skip ci]` in commit message to prevent triggering other workflows

The generated graph is also uploaded as an artifact with 90-day retention.

### Bot Loop Prevention

This workflow is designed to **only run on human-made changes** to prevent automated bots from looping on themselves:

1. **Author Check (Primary):** Exits immediately if last commit is from `github-actions[bot]` - ensures workflow only processes human changes
2. **Diff Check (Secondary):** Only commits if graph actually changed - prevents unnecessary commits
3. **Skip CI Tag:** Commit message includes `[skip ci]` to prevent triggering other workflows

This triple-layer approach ensures the workflow never triggers itself or creates infinite automation loops.

### Branch Protection Compatibility

**Important:** This workflow commits directly to master, bypassing the "Require pull request" protection rule. This is intentional and safe because:

- ✅ Workflow runs AFTER human PR is merged (protection already passed)
- ✅ Workflow has explicit `contents: write` permission (GitHub Actions feature)
- ✅ Only updates documentation (.png file), not code
- ✅ Bot-check prevents infinite loops
- ✅ Graph is deterministic (generated from verified code)

**For human developers:** Branch protection remains fully active - PRs required, checks must pass.

### Repository Settings Requirements

**GitHub Repository Settings → Actions → General → Workflow permissions:**
- Select "Read and write permissions" ✅ REQUIRED

### Implementation Details

This workflow uses native GitHub Actions capabilities:
- `git` commands for commits and pushes
- GITHUB_TOKEN only (no Personal Access Token required)
- Leverages workflow `contents: write` permission to push to protected branch
- No third-party actions required

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
