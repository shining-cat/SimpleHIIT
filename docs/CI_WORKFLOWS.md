# CI/CD Workflows Documentation

This document describes all GitHub Actions workflows used in the SimpleHIIT project for continuous integration and deployment automation.

## Workflow Interaction Overview

```
┌─────────────────────────────────────────────────────────────────────┐
│                      Developer Creates Pull Request                 │
└────────────────────────────────┬────────────────────────────────────┘
                                 ▼
                    ┌────────────────────────────┐
                    │   PR Verification Gate     │
                    │   (Required Status Check)  │
                    └────────────┬───────────────┘
                ┌────────────────┴────────────────┐
                ▼                                 ▼
    ┌──────────────────────┐        ┌──────────────────────┐
    │   Doc/Asset Only?    │        │   Code Changes?      │
    │   Pass Immediately   │        │   Wait for checks    │
    └──────────┬───────────┘        └──────────┬───────────┘
               │                               ▼
               │                  ┌────────────────────────┐
               │                  │  Android Verification  │
               │                  │  - Ktlint Check        │
               │                  │  - Module Dependencies │
               │                  │  - Unit Tests          │
               │                  └────────────┬───────────┘
               └──────────────┬────────────────┘
                              ▼
                 ┌────────────────────────┐
                 │   All Checks Pass      │
                 └────────────┬───────────┘
                 ┌────────────┴─────────────────────────────┐
                 ▼                                          ▼
 ┌──────────────────────────────┐            ┌──────────────────────────────┐
 │  Automated Path              │            │  Manual Path                 │
 │  Add "HEX merge and delete"  │            │  Developer manually merges   │
 │  label                       │            │  PR from GitHub UI           │
 └──────────────┬───────────────┘            └──────────────┬───────────────┘
                ▼                                           │
   ┌────────────────────────────┐                           │
   │   Auto Merge and Delete    │                           │
   │   - Merges PR              │                           │
   │   - Deletes branch         │                           │
   │   (Uses AUTO_MERGE_PAT)    │                           │
   └────────────┬───────────────┘                           │
                └──────────────────┬────────────────────────┘
                                   ▼
                      ┌────────────────────────────┐
                      │   Merged to Master         │
                      └────────────┬───────────────┘
                                   ▼
                  ┌─────────────────────────────────────┐
                  │  Update Module Dependency Graph     │
                  │  1. Check author (bot? → exit)      │
                  │  2. Generate graph                  │
                  │  3. If changed → push to master     │
                  │  (Uses AUTO_MERGE_PAT)              │
                  └─────────────────┬───────────────────┘
                                    ▼
                       ┌────────────────────────┐
                       │  Graph Updated on      │
                       │  Master (if needed)    │
                       │  Bot-check prevents ∞  │
                       └────────────────────────┘
```
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

### Special Handling

**Auto-generated Dependency Graph PRs:**
- PRs from the `auto-update-dependency-graph` branch bypass verification checks
- Rationale: These PRs only update `.png` files which are already ignored by android-verifications
- The workflow detects the branch name and immediately marks the PR as ready to merge

## Update Module Dependency Graph

**Workflow File:** `.github/workflows/update-module-dependency-graph.yml`

**Trigger:** Pushes to `master` branch (automatic after PR merge)

**Purpose:** Safety net to ensure module dependency graph stays synchronized with code

### Developer Workflow (When Changing Dependencies)

**Important:** When you modify module dependencies, you must update the graph locally:

1. Make your dependency changes (add modules, modify build.gradle.kts, etc.)
2. Run locally: `./gradlew generateUnifiedDependencyGraph`
3. Commit both code changes AND updated graph together
4. Create PR with all changes
5. Merge after checks pass

**Why update locally?**
- Graph changes visible in PR for review
- Everything stays in sync
- PR Verification Gate passes immediately
- No surprise commits after merge

### What the CI Workflow Does

This workflow acts as a **safety net** after PR merges:

1. **Ensures human-made changes only** - Exits immediately if last commit is from `github-actions[bot]` to avoid bots looping on themselves
2. Runs `./gradlew generateUnifiedDependencyGraph` to regenerate the graph
3. Checks if the graph differs from committed version
4. If out of sync (developer forgot to update):
   - Commits the updated graph **directly to master**
   - Uses `[skip ci]` in commit message to prevent triggering other workflows
   - Uploads graph as artifact for review

**In normal workflow:** Graph is already up to date, CI finds no changes ✅

**If graph forgotten:** CI catches it and updates automatically ⚠️


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

**GitHub Repository Settings → Branches → master → Branch protection rules:**
- "Do not allow bypassing the above settings" must be **UNCHECKED** ✅ REQUIRED
  - This allows the workflow (using AUTO_MERGE_PAT) to push directly to master
  - All other protection rules remain active (PRs required for humans)

## Monthly Deprecation Check

**Workflow File:** `.github/workflows/deprecation-check.yml`

**Trigger:** Monthly on the 1st at 9:00 AM UTC, or manual dispatch

**Purpose:** Scans codebase for deprecation warnings and outdated dependencies

### What It Does

Performs build deprecation checks, Android Lint checks, and dependency updates analysis. Automatically creates or updates a GitHub issue when deprecations are found, with detailed reports uploaded as artifacts (90-day retention).

See [DEPRECATION_CHECKER.md](DEPRECATION_CHECKER.md) for full documentation.
