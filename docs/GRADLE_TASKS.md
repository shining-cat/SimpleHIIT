# Gradle Tasks Reference

## Module Dependency Enforcement

### Validate Dependencies

```bash
# Validate both mobile and TV app dependencies
./gradlew :android:mobile:app:assertModuleGraph --no-configure-on-demand
./gradlew :android:tv:app:assertModuleGraph --no-configure-on-demand
```

Validates all module dependencies against configured rules for both mobile and TV apps. Runs automatically on PRs via CI.

IDE locations:
- Mobile: `SimpleHIIT > android > mobile > app > Tasks > verification > assertModuleGraph`
- TV: `SimpleHIIT > android > tv > app > Tasks > verification > assertModuleGraph`

### Generate Complete Unified Graph (TV + Mobile) Locally

```bash
./gradlew generateUnifiedDependencyGraph --no-configure-on-demand
```

This Gradle task generates both graphs, combines them, and converts to PNG. It's cross-platform and works on macOS, Linux, and Windows.
It's also the same task that will be run on all merge on master on the CI.

**IDE location**: `SimpleHIIT > Tasks > documentation > generateUnifiedDependencyGraph`

**Output files:**
- Graphviz file: `build/reports/dependency-graph.gv`
- PNG image: `project_dependencies_graph.png` (in project root)

The generated graph shows both mobile and TV app modules (`:android:mobile:*` and `:android:tv:*`) along with shared modules (`:domain:*`, `:data`, `:commonUtils`, etc.) to visualize the complete project architecture.

**Note**: Graphviz must be installed to generate the PNG. Install with:
- macOS: `brew install graphviz`
- Ubuntu: `sudo apt-get install graphviz`
- Windows: `choco install graphviz` or download from https://graphviz.org/download/

See [MODULE_DEPENDENCIES.md](MODULE_DEPENDENCIES.md) for architecture details.

---

## Code Quality

### KtLint

```bash
./gradlew ktlintCheck    # Check code style, Runs automatically on PR on the CI
./gradlew ktlintFormat   # Auto-format code
```
IDE location: `SimpleHIIT > Tasks > formatting`

### Dependency Updates

```bash
./gradlew dependencyUpdates
```

Check for available updates (stable versions only). IDE location: `SimpleHIIT > Tasks > help`

---

## Test Coverage

### Kover

```bash
./gradlew testDebugUnitTest koverHtmlReport
open build/reports/kover/html/index.html
```

Aggregates coverage from all modules. Excludes generated code (Hilt, Room, etc.).

See [KOVER_CODE_COVERAGE.md](KOVER_CODE_COVERAGE.md) for configuration details.

---

## CI/CD

The project uses two separate GitHub Actions workflows:

**Verification Workflow** (`.github/workflows/android-verifications.yml`)
- Runs on: Pull requests to `master` or `develop`
- Tasks:
  1. `ktlintCheck` - Code style validation
  2. `assertModuleGraph` - Dependency rules enforcement (for both mobile AND TV apps)
  3. `testDebugUnitTest` - Unit tests
- Purpose: Validate changes before merging

**Update Dependency Graph Workflow** (`.github/workflows/update-module-dependency-graph.yml`)
- Runs on: Merges to `master` (automatic) or manual trigger
- Tasks:
  1. Generate unified dependency graph visualization (combining mobile AND TV modules)
  2. Commit updated graph to repository
  3. Upload graph as artifact (90-day retention)
- Purpose: Keep dependency graph up-to-date with validated changes
