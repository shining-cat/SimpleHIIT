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

**Handling Re-runs:**
- The workflow correctly handles check re-runs by tracking the most recent check run for each job
- When a check is re-run, multiple check runs with the same name exist - the gate only considers the latest one
- This prevents false timeouts when developers retry failed checks

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

### Architecture

The workflow uses a **modular, reusable design** with 4 independent checker workflows and 1 orchestrator:

**Reusable Checkers:**
- `reusable-module-dependencies-checker.yml`
- `reusable-dependency-graph-checker.yml`
- `reusable-deprecation-checker.yml`
- `reusable-dependency-updates-checker.yml`

**Orchestrator:**
- `monthly-master-sanity-check.yml` - Calls all checkers and reports combined results

### What It Checks

1. **Module Dependencies** - Validates architecture rules (no UI-to-UI dependencies, no domain→data, etc.)
2. **Dependency Graph** - Verifies committed graph matches current module structure
3. **Deprecations** - Scans build output for deprecation warnings
4. **Dependency Updates** - Checks for available library updates

### Three-State Model

Each checker can return one of three states:

| State | Meaning | Issue Created | Workflow Status |
|-------|---------|---------------|-----------------|
| `success` | Check completed, no issues found | ❌ No (closes existing issues) | ✅ SUCCESS |
| `success-issues` | Check completed, found violations/updates | ✅ **Yes** (creates actionable issue) | ✅ SUCCESS |
| `failure` | Check couldn't run (build error, config issue) | ✅ **Yes** (reports failure) | ❌ FAILURE |

**Key Insight:** Finding violations is **not a failure** - it's expected behavior. The workflow succeeds and creates issues for follow-up.

### GitHub Issues - Automatic Management

**When checks complete successfully but find issues (`success-issues`):**
- Creates issue with title: `[Check Name] [N] issue(s)` or `[Check Name] Available (N updates)`
- Contains actionable checklist of violations/updates found
- Provides download link to full report artifact
- Includes local reproduction commands

**When checks fail to run (`failure`):**
- Creates issue with title: `[Check Name] Failed to Complete`
- Extracts "What went wrong" from Gradle error output
- Provides build/configuration error details
- Includes debugging instructions

**Issue Lifecycle:**
- Each check type has a unique label (e.g., `sanity-check-module-deps`)
- When workflow runs:
  - ✅ **success**: Closes any existing issues with that label
  - ⚠️ **success-issues**: Closes old issue, creates new one with current findings
  - ❌ **failure**: Closes old issue, creates new one with error details
- Issues are **NOT assigned** (avoids notification noise)
- Issues are **auto-closed** when resolved on next run

**Issue Labels:**
- `modules-dependencies-violation` - Module architecture violations or build failures
- `modules-dependencies-graph` - Dependency graph out of sync or generation failures
- `deprecations` and `build-failure` - Deprecation warnings found or build failures
- `outdated-external-dependencies` - Available dependency updates or check failures

### Example Issue Titles

**When violations found:**
- "Module Dependencies Violation (3 issues)"
- "Dependency Graph Out of Sync (1 issue)"
- "Dependency Updates Available (5 updates)"

**When checks fail:**
- "Module Dependencies Check Failed to Complete"
- "Dependency Graph Check Failed to Complete"
- "Deprecation Check Failed to Complete"

### Workflow Success/Failure

**The workflow is considered successful if:**
- All 4 checks complete (even if they find violations)
- Issues are created for any findings
- Result summary shows green checkmarks or warnings

**The workflow fails only if:**
- One or more checks cannot run due to build/configuration errors
- Issues are created explaining what prevented the check from running
