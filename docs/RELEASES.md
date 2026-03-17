<!--
  ~ SPDX-FileCopyrightText: 2024-2026 shining-cat
  ~ SPDX-License-Identifier: GPL-3.0-or-later
  -->
# GitHub Releases

This document explains the automated release process for SimpleHIIT.

---

## Overview

SimpleHIIT uses GitHub Actions to automatically build, sign, and publish release APKs when version tags are pushed to the repository.

**What happens automatically:**
- Signed APK builds for both mobile and TV variants
- Changelog generation from git commit history
- GitHub Release creation with downloadable APKs
- Properly named artifacts: `SimpleHIIT-mobile-vX.XX.apk` and `SimpleHIIT-tv-vX.XX.apk`

---

## Release Infrastructure

### GitHub Secrets

The following secrets are configured in the repository for APK signing:
- `SIGNING_KEYSTORE_BASE64` - Base64-encoded release keystore
- `SIGNING_KEYSTORE_PASSWORD` - Keystore password
- `SIGNING_KEY_ALIAS` - Key alias (`simplehiit`)
- `SIGNING_KEY_PASSWORD` - Key password

### Signing Configuration

Release signing is configured in:
- `build-logic/convention/src/main/kotlin/fr/shiningcat/simplehiit/plugins/AndroidAppHandheldConventionPlugin.kt`
- `build-logic/convention/src/main/kotlin/fr/shiningcat/simplehiit/plugins/AndroidAppTvConventionPlugin.kt`

Signing is only applied when environment variables are present (CI/CD), ensuring local debug builds remain unsigned.

### Release Workflow

The automated release workflow is defined in:
- `.github/workflows/release.yml`

**Trigger:** Push of version tags matching pattern `v*` (e.g., `v0.05`, `v1.23`)

**Steps:**
1. Checkout code
2. Set up JDK 21
3. Decode release keystore from secrets
4. Build signed mobile APK
5. Build signed TV APK
6. Generate changelog from commits
7. Create GitHub release with APKs attached

---

## Creating a Release

### Step 1: Create Release Branch

```bash
# Ensure you're on master with latest changes
git checkout master
git pull origin master

# Create release branch (e.g., for version 1.23)
git checkout -b release/v1.23
```

### Step 2: Update Version Numbers and F-Droid Metadata (in the fastlane folder)

**2a. Update version in Config.kt:**

Edit `build-logic/convention/src/main/kotlin/fr/shiningcat/simplehiit/config/Config.kt`:

```kotlin
object ConfigHandheld {
    val config =
        AndroidConfig(
            // ...
            versionCode = 23100123,  // Format: XXYYZZZZ (23=minSDK, 10=handheld, 0123=version)
            versionName = "1.23",
            // ...
        )
}

object ConfigTv {
    val config =
        AndroidConfig(
            // ...
            versionCode = 23010123,  // Format: XXYYZZZZ (23=minSDK, 01=TV, 0123=version)
            versionName = "1.23",
            // ...
        )
}
```

**Version Code Format:** `XXYYZZZZ`
- `XX` = Min SDK version (23)
- `YY` = Device family (10=handheld, 01=TV)
- `ZZZZ` = Version number padded to 4 digits (e.g., 1.23 → 0123)

**2b. Create F-Droid changelog (if app is on F-Droid):**

Generate a draft changelog from git commits:
```bash
./scripts/generate-fdroid-changelog.sh 1.23 23100123
```

This creates `fastlane/metadata/android/en-US/changelogs/23100123.txt` with commits since the last tag.

**Then edit the generated file to:**
- Replace `[EDIT THIS SUMMARY]` with a brief version summary
- Remove technical/internal commits not relevant for users
- Reword commits in user-friendly language
- Group related changes

**Note:** English-only is acceptable. F-Droid and GitHub changelogs don't need to be identical.

### Step 3: Commit and Push

```bash
# Stage all changes (version + F-Droid metadata)
git add build-logic/convention/src/main/kotlin/fr/shiningcat/simplehiit/config/Config.kt
git add fastlane/metadata/  # if F-Droid changelogs were created

# Commit with clear message
git commit -m "Bump version to 1.23"

# Push the release branch
git push origin release/v1.23
```

### Step 4: Create and Merge Pull Request

1. Create PR from `release/v1.23` to `master`
2. Wait for CI checks to pass
3. Merge the PR

### Step 5: Tag the Release

After the PR is merged to master:

```bash
# Update local master
git checkout master
git pull origin master

# Create version tag
git tag v1.23

# Explicitly push a tag (unambiguous)
git push origin refs/tags/v1.23

# Or use --tags to push all tags
git push --tags
```

---

## F-Droid Auto-Update

After a version tag is pushed, F-Droid automatically detects and builds the new version:
- Detection occurs within 24-48 hours of tag push
- F-Droid fetches fastlane metadata (including changelogs) from the repository
- Build and publication takes an additional 1-3 days
- Monitor build status at: https://monitor.f-droid.org/

**Note:** No manual submission is needed for updates after initial F-Droid setup. See `_WIP-plans/FDROID_PROCESS.md` for F-Droid submission and maintenance documentation.

---

## Post-Tag: Monitoring & Verification

### Step 6: Monitor GitHub Actions Release Build

1. Go to: https://github.com/shining-cat/SimpleHIIT/actions
2. Watch the "Release Build" workflow complete (typically 5-10 minutes)
3. Check for any errors in the workflow logs

**If build fails with Gradle version inconsistencies:**
1. Go to: https://github.com/shining-cat/SimpleHIIT/actions/caches
2. Delete all caches (or at least Gradle-related caches)
3. Re-run the failed workflow or re-push the tag

### Step 7: Verify GitHub Release

1. Go to: https://github.com/shining-cat/SimpleHIIT/releases
2. Verify the new release appears with:
   - Correct version tag
   - Both mobile and TV APKs
   - Auto-generated changelog from commits
3. Download and test APKs on actual devices

---

## GitHub Actions Cache Management

### Understanding the Cache

The `gradle/actions/setup-gradle@v4` action automatically caches Gradle artifacts to speed up builds:
- Gradle wrapper files
- Dependencies
- Build cache
- Configuration cache

**This improves build times BUT can cause issues when Gradle/AGP versions change.**

### When to Clear the Cache

Clear GitHub Actions cache if you experience:
- ❌ Build failures after Gradle or AGP version changes
- ❌ Inconsistent metadata in APKs (e.g., wrong Gradle version in kotlin-tooling-metadata.json)
- ❌ Unexplained build errors that don't occur locally

### How to Clear the Cache

**Option 1: Manual Cache Deletion (Recommended)**
1. Go to: https://github.com/shining-cat/SimpleHIIT/actions/caches
2. Click "Delete all caches" or select specific Gradle caches to delete
3. Trigger a new build (push tag or re-run workflow)

**Option 2: Disable Caching Temporarily**

Modify `.github/workflows/release.yml`:
```yaml
- name: Setup Gradle
  uses: gradle/actions/setup-gradle@v4
  with:
    cache-disabled: true  # Force clean build, no caching
```

After one successful build, remove this flag to re-enable caching for future builds.

**Option 3: Read-Only Cache Mode**

Force cache refresh:
```yaml
- name: Setup Gradle
  uses: gradle/actions/setup-gradle@v4
  with:
    cache-read-only: true  # Ignore old cache, create fresh cache
```

### Best Practice

After upgrading Gradle, AGP, or Kotlin versions, manually clear GitHub Actions caches before creating a release tag. This prevents stale cached artifacts from being included in release APKs.
