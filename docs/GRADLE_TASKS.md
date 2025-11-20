# Gradle Tasks Reference

Project-specific Gradle tasks and useful commands.

## Module Dependency Enforcement

### Validate Dependencies

```bash
./gradlew assertModuleGraph --no-configure-on-demand
```

Validates all module dependencies against configured rules. Runs automatically on every build and PR.

### Generate Dependency Graph

```bash
./gradlew :android:mobile:app:generateModulesGraphvizText \
  -Pmodules.graph.of.module=:android:mobile:app \
  --no-configure-on-demand
```

Generates Graphviz visualization. Convert to PNG: `dot -Tpng build/reports/dependency-graph.gv -o graph.png`

Online: [Graphviz Online](https://dreampuf.github.io/GraphvizOnline/) | [WebGraphviz](http://www.webgraphviz.com/)

### Module Statistics

```bash
./gradlew generateModulesGraphStatistics --no-configure-on-demand
```

See [MODULE_DEPENDENCIES.md](MODULE_DEPENDENCIES.md) for architecture details.

---

## Code Quality

### KtLint

```bash
./gradlew ktlintCheck    # Check code style
./gradlew ktlintFormat   # Auto-format code
```

Runs automatically on PR. IDE location: `SimpleHIIT > Tasks > formatting`

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

## Useful Combinations

### Full Verification (before PR)

```bash
./gradlew clean \
  ktlintCheck \
  assertModuleGraph --no-configure-on-demand \
  testDebugUnitTest \
  koverHtmlReport
```

### Quick Check (during development)

```bash
./gradlew ktlintCheck testDebugUnitTest
```

### Update and Verify

```bash
./gradlew dependencyUpdates \
  ktlintCheck \
  assertModuleGraph --no-configure-on-demand
```

---

## CI/CD

The project uses two separate GitHub Actions workflows:

**Verification Workflow** (`.github/workflows/android-verifications.yml`)
- Runs on: Pull requests to `master` or `develop`
- Tasks:
  1. `ktlintCheck` - Code style validation
  2. `assertModuleGraph` - Dependency rules enforcement
  3. `testDebugUnitTest` - Unit tests
- Purpose: Validate changes before merging

**Update Dependency Graph Workflow** (`.github/workflows/update-module-dependency-graph.yml`)
- Runs on: Merges to `master` (automatic) or manual trigger
- Tasks:
  1. Generate dependency graph visualization
  2. Commit updated graph to repository
  3. Upload graph as artifact (90-day retention)
- Purpose: Keep dependency graph up-to-date with validated changes

---

## Troubleshooting

### "Configuration on demand" Warning

Add `--no-configure-on-demand` flag:
```bash
./gradlew assertModuleGraph --no-configure-on-demand
```

### Gradle Daemon Issues

```bash
./gradlew --stop
./gradlew clean build
```

### Cache Problems

```bash
rm -rf ~/.gradle/caches/
./gradlew clean build --refresh-dependencies
```

### OutOfMemory

Edit `gradle.properties`:
```properties
org.gradle.jvmargs=-Xmx4g -XX:MaxMetaspaceSize=512m
