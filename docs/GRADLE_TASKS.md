<!--
  ~ SPDX-FileCopyrightText: 2024-2026 shining-cat
  ~ SPDX-License-Identifier: GPL-3.0-or-later
  -->
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

**Recommended: Use the helper script** (automatically handles both files):

```bash
./scripts/update-dependency-graph.sh
```

This script:
- Generates the dependency graph (both `.gv` and `.png` files)
- Copies the `.gv` file to `docs/` for CI validation
- Provides clear next steps

**Manual approach** (if needed):

```bash
./gradlew generateUnifiedDependencyGraph --no-configure-on-demand
cp build/reports/dependency-graph.gv docs/dependency-graph.gv
```

**IDE location**: `SimpleHIIT > Tasks > documentation > generateUnifiedDependencyGraph`

**Output files:**
- `build/reports/dependency-graph.gv` - Generated DOT file (intermediate)
- `docs/dependency-graph.gv` - Committed DOT file (validated by CI)
- `docs/project_dependencies_graph.png` - Visual documentation

The generated graph shows both mobile and TV app modules (`:android:mobile:*` and `:android:tv:*`) along with shared modules (`:domain:*`, `:data`, `:commonUtils`, etc.) to visualize the complete project architecture.

**Important: When to Run This Task**

🔴 **MUST RUN** when making dependency changes:
- Adding/removing modules
- Modifying `dependencies {}` blocks in build.gradle.kts
- Changing module structure

**Workflow:**
1. Make your dependency changes
2. Run `./scripts/update-dependency-graph.sh`
3. Review changes: `git diff docs/dependency-graph.gv`
4. Commit code changes AND both graph files together
5. Create PR with all changes

**Why?**
- Graph changes visible in PR for review
- CI validates the DOT file matches the code (reliable, deterministic)
- Everything stays synchronized
- Prevents merge failures

**Note**: Graphviz must be installed to generate the PNG. Install with:
- macOS: `brew install graphviz`
- Ubuntu: `sudo apt-get install graphviz`
- Windows: `choco install graphviz` or download from https://graphviz.org/download/

See [MODULE_DEPENDENCIES.md](MODULE_DEPENDENCIES.md) for architecture details and [CI_WORKFLOWS.md](CI_WORKFLOWS.md) for validation details.

---

## Build Performance Analysis

### Gradle Build Scans

Gradle Build Scans provide detailed performance insights and build analysis. The project is configured to automatically publish scans on build failures.

**Local Development (On-Demand):**
```bash
# Generate a build scan for any build
./gradlew assembleDebug --scan
./gradlew clean build --scan

# The scan URL will be displayed in the output
# Example: https://scans.gradle.com/s/abc123xyz
```

**CI/CD (Automatic on Failures):**
- Build scans are automatically published when builds fail on CI
- Helps debug issues without local reproduction
- No manual intervention needed

**What Build Scans Show:**
- Task execution timeline and duration
- Configuration phase performance
- Dependency resolution details
- Cache hit/miss ratios
- Environment configuration (JVM, Gradle version)
- Console output and test results
- Performance recommendations

**Notes:**
- Scans are published to `scans.gradle.com` (free public service)
- Links are publicly accessible and expire after 90 days
- Minimal performance overhead (5-15 seconds for publishing)
- Use the `--scan` flag only when investigating performance issues
- For regular builds, omit `--scan` for optimal performance

**Alternative**: Use Android Studio's built-in Build Analyzer for quick, local build insights without publishing to external services.

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

Scans all project dependencies and reports available updates. Configured to show stable versions only (RELEASE, FINAL, GA, or numeric versions). Filters out alpha, beta, RC, and snapshot versions.

**IDE location**: `SimpleHIIT > Tasks > help > dependencyUpdates`

**Output**: Console report showing:
- Dependencies with available updates
- Current version vs latest available version
- Categorized by type (Gradle, plugins, dependencies)

The task uses the Ben Manes Gradle Versions Plugin with custom configuration to reject unstable versions.

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
gradle tasks are also leveraged in the repository itself.

See [CI_WORKFLOWS.md](CI_WORKFLOWS.md) for a complete description.
