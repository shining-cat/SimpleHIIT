#!/bin/bash
#
# Setup git hooks for this repository
# Run this once after cloning: ./githooks/setup.sh
#

set -e

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
REPO_ROOT="$(cd "$SCRIPT_DIR/.." && pwd)"

echo "Setting up git hooks for SimpleHIIT..."

# Option 1: Configure git to use .githooks directory (recommended)
echo "Configuring git to use .githooks directory..."
cd "$REPO_ROOT"
git config core.hooksPath .githooks

# Make hooks executable
chmod +x "$SCRIPT_DIR/pre-push"
chmod +x "$SCRIPT_DIR/pre-commit"

echo "✓ Git hooks configured successfully!"
echo ""
echo "Active hooks:"
echo "  pre-commit: Runs ktlint checks before commits"
echo "  pre-push:   Auto-merges remote changes before pushing"
echo ""
echo "The hooks will now automatically:"
echo "  - Enforce code style with ktlint (pre-commit)"
echo "  - Handle remote CI commits like dependency graph updates (pre-push)"
echo ""
echo "To disable hooks temporarily:"
echo "  git commit --no-verify"
echo "  git push --no-verify"
