#!/bin/bash
# SPDX-FileCopyrightText: 2024-2026 shining-cat
# SPDX-License-Identifier: GPL-3.0-or-later

# Generate F-Droid changelog from git commits
# Usage: ./scripts/generate-fdroid-changelog.sh <version> <versionCode>
# Example: ./scripts/generate-fdroid-changelog.sh 1.23 23100123

set -e

if [ "$#" -ne 2 ]; then
    echo "Usage: $0 <version> <versionCode>"
    echo "Example: $0 1.23 23100123"
    exit 1
fi

VERSION=$1
VERSION_CODE=$2

# Get the git root directory
GIT_ROOT=$(git rev-parse --show-toplevel)
CHANGELOG_FILE="${GIT_ROOT}/fastlane/metadata/android/en-US/changelogs/${VERSION_CODE}.txt"

# Get the previous tag
PREVIOUS_TAG=$(git tag --sort=-version:refname | head -2 | tail -1)

echo "Generating changelog from ${PREVIOUS_TAG} to current HEAD..."
echo "Output: ${CHANGELOG_FILE}"

# Create the changelog directory if it doesn't exist
mkdir -p "$(dirname "$CHANGELOG_FILE")"

# Generate changelog header
cat > "$CHANGELOG_FILE" << EOF
Version ${VERSION} - [EDIT THIS SUMMARY]

EOF

# Extract commit messages and format them
git log ${PREVIOUS_TAG}..HEAD --oneline --no-merges | while read -r line; do
    # Remove commit hash, keep message
    message=$(echo "$line" | sed 's/^[a-f0-9]\{7,\} //')
    # Skip certain patterns (customize as needed)
    if [[ ! "$message" =~ ^(Merge|bump|lint|format|typo) ]]; then
        echo "- $message" >> "$CHANGELOG_FILE"
    fi
done

echo ""
echo "✓ Draft changelog created at: ${CHANGELOG_FILE}"
echo ""
echo "NEXT STEPS:"
echo "1. Edit ${CHANGELOG_FILE} to:"
echo "   - Replace [EDIT THIS SUMMARY] with a brief version summary"
echo "   - Remove technical/noise commits"
echo "   - Reword commits for user-friendly language"
echo "   - Group related changes"
echo "2. Review and refine the changelog"
echo "3. Optionally create French/Swedish versions if desired"
echo ""
