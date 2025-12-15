# CI/CD Workflows Documentation

This document describes all GitHub Actions workflows used in the SimpleHIIT project for continuous integration and deployment automation.

## Workflow Interaction Overview

```
┌─────────────────────────────────────────────────────────────────────┐
│                      Developer Creates Pull Request                 │
│                 (with updated dependency graph if needed)           │
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
               │                  │  - Graph Validation    │
               │                  │  - Unit Tests          │
               │                  └────────────┬───────────┘
               └──────────────┬────────────────┘
                              ▼
                 ┌────────────────────────┐
                 │   All Checks Pass      │
                 └────────────┬───────────┘
                ┌─────────────┴──────────────────────────┐
                ▼                                        ▼
 ┌──────────────────────────────┐        ┌──────────────────────────────┐
 │  Automated Path              │        │  Manual Path                 │
 │  Add "HEX merge and delete"  │        │  Developer manually merges   │
 │  label                       │        │  PR from GitHub UI           │
 └──────────────┬───────────────┘        └──────────────┬───────────────┘
                ▼                                       │
   ┌────────────────────────────┐                       │
   │   Auto Merge and Delete    │                       │
   │   - Merges PR              │                       │
   │   - Deletes branch         │                       │
   │   (Uses AUTO_MERGE_PAT)    │                       │
   └────────────┬───────────────┘                       │
                └──────────────────┬────────────────────┘
                                   ▼
                      ┌────────────────────────────┐
                      │   Merged to Master         │
                      │   (Graph already current)  │
                      └────────────────────────────┘
```

## Table of Contents
- [PR Verification Gate](#pr-verification-gate)
- [Android Verification](#android-verification)
- [Auto Merge and Delete](#auto-merge-and-delete)
- [Monthly Master Sanity Check](#monthly-master-sanity-check)

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

**2. Verify Module Dependencies** - Validates module architecture and dependency graph
- Runs `assertModuleGraph` for both mobile and TV apps
- **Generates and validates the dependency graph**
- Fails if the committed graph doesn't match the current module structure
- Provides clear instructions for updating the graph locally

**3. Unit Tests** - Runs all unit tests and generates reports

### Dependency Graph Validation

**Important:** When making changes that affect module dependencies, you must update the dependency graph:

1. Make your dependency changes
2. Run `./scripts/update-dependency-graph.sh` (recommended) or manually run the Gradle task
3. Review changes: `git diff docs/dependency-graph.gv` (text diff shows exact module changes)
4. Commit both graph files: `git add docs/dependency-graph.gv docs/project_dependencies_graph.png`

**What's validated:**
- CI compares `docs/dependency-graph.gv` (DOT file - text format, deterministic)
- The PNG file (`docs/project_dependencies_graph.png`) is for documentation only

**If you forget:** The verification workflow will fail with a clear message explaining how to update the graph.

**Why DOT file instead of PNG?** PNG files can have slight rendering differences even with identical dependencies (platform-specific rendering, font differences). The DOT file is text-based and reliably shows actual dependency changes.

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

## Monthly Master Sanity Check

**Workflow File:** `.github/workflows/monthly-master-sanity-check.yml`

**Trigger:** Monthly on the 1st at 9:00 AM UTC, or manual dispatch

**Purpose:** Comprehensive health check of the master branch to catch configuration drift and violations

### What It Checks

This workflow runs three independent verification jobs:

**1. Module Dependencies Verification**
- Runs `assertModuleGraph` for both mobile and TV apps
- Ensures module dependency rules are not violated
- Creates GitHub issue if violations found

**2. Dependency Graph Verification**
- Generates the dependency graph from current code
- Compares with committed `docs/dependency-graph.gv` (DOT file)
- Creates GitHub issue if graph is out of sync

**3. Deprecation Check**
- Runs full build with deprecation warnings enabled
- Scans for deprecated APIs, libraries, or Gradle features
- Uploads warning details as artifact
- Creates GitHub issue if deprecations found

### GitHub Issues

When problems are detected:
- **One issue per check type** for clear separation
- Issues are **automatically assigned** to @shining-cat
- Previous issues are **automatically closed** when new ones are created
- Issues include:
  - Check date and workflow run link
  - Clear description of the problem
  - Action steps to resolve
  - Links to relevant documentation

**Issue Labels:**
- `sanity-check` - All sanity check issues
- `module-dependencies` - Module architecture violations
- `dependency-graph` - Graph out of sync
- `deprecations` - Deprecation warnings
- Plus: `bug`, `documentation`, or `maintenance` as appropriate

### When To Run Manually

Use `workflow_dispatch` to run this check manually when:
- After major refactoring
- Before release
- After dependency updates
- To verify master branch health

**No issues created** = Master branch is healthy ✅
