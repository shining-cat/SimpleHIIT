#!/bin/bash
#
# Git Hooks Setup Script
# 
# Installation:
#   Run once from repository root after cloning:
#     ./.githooks/setup.sh
#
# What this script does:
#   1. Configures git to use .githooks directory (sets core.hooksPath)
#   2. Makes all hook scripts executable
#   3. Displays summary of installed hooks
#
# This is the recommended installation method as it automatically
# applies all hooks in the .githooks directory to your local repository.
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
echo "  pre-commit: Runs ktlint + Spotless checks before commits"
echo "  pre-push:   Auto-merges remote changes before pushing"
echo ""
echo "The hooks will now automatically:"
echo "  - Enforce code style with ktlint (pre-commit)"
echo "  - Enforce license headers with Spotless (pre-commit)"
echo "  - Handle remote CI commits like dependency graph updates (pre-push)"
echo ""
echo "To disable hooks temporarily:"
echo "  git commit --no-verify"
echo "  git push --no-verify"
