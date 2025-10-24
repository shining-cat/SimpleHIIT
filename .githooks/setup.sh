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

echo "✓ Git hooks configured successfully!"
echo ""
echo "The pre-push hook will now automatically:"
echo "  - Check for remote changes before pushing"
echo "  - Automatically rebase CI commits (like dependency graph updates)"
echo "  - Prevent push conflicts"
echo ""
echo "To disable hooks temporarily: git push --no-verify"
