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
2. Set up JDK 17
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

### Step 2: Update Version Numbers

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

### Step 3: Commit and Push

```bash
# Stage the version change
git add build-logic/convention/src/main/kotlin/fr/shiningcat/simplehiit/config/Config.kt

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

# Push the tag (this triggers the release workflow)
git push origin v1.23
```

### Step 6: Monitor Release Build

1. Go to: https://github.com/shining-cat/SimpleHIIT/actions
2. Watch the "Release Build" workflow complete (typically 5-10 minutes)
3. Check for any errors in the workflow logs

### Step 7: Verify Release

1. Go to: https://github.com/shining-cat/SimpleHIIT/releases
2. Verify the new release appears with:
   - Correct version tag
   - Both mobile and TV APKs
   - Auto-generated changelog
3. Download and test APKs on actual devices

---

## Troubleshooting

### Build Fails with Signing Error

- Verify GitHub secrets are correctly configured
- Check that keystore hasn't expired
- Verify passwords match the keystore

### Workflow Doesn't Trigger

- Ensure tag follows `v*` pattern (e.g., `v1.23`, not `1.23`)
- Verify tag was pushed: `git push origin v1.23`
- Check if tag exists: `git tag -l`

### Release Created but Missing APKs

- Check workflow logs for build failures
- Verify APK paths in workflow match actual output locations
- Ensure both mobile and TV builds completed successfully

### Need to Delete a Tag

```bash
# Delete local tag
git tag -d v1.23

# Delete remote tag
git push origin :refs/tags/v1.23
```

---

## Version History

Releases are available at: https://github.com/shining-cat/SimpleHIIT/releases

Each release includes:
- Mobile APK for handheld devices
- TV APK for Android TV devices
- Changelog with commits since previous release
- Release notes (if manually added)

---

## Future Enhancements

Potential improvements to the release process:

- **F-Droid Integration** - Automatic publishing to F-Droid store
- **Release Notes Automation** - Parse conventional commits for structured changelogs
- **Multi-platform Builds** - Desktop (JVM/native) and iOS variants when KMP is implemented
- **Beta Releases** - Support for pre-release tags (e.g., `v1.23-beta1`)
