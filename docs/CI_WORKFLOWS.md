# CI/CD Workflows Documentation

This document describes all GitHub Actions workflows used in the SimpleHIIT project for continuous integration and deployment automation.

## Table of Contents
- [Android Verification](#android-verification)
- [Auto Merge and Delete](#auto-merge-and-delete)
- [Update Module Dependency Graph](#update-module-dependency-graph)

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

**Purpose:** Generates and commits updated module dependency graph

### What It Does

Runs `./gradlew generateUnifiedDependencyGraph` to create `docs/project_dependencies_graph.png` and commits it back to the repository if changed.

The generated graph is also uploaded as an artifact with 90-day retention.

---

*Last Updated: 2025-11-20*
