#!/bin/bash
# Update dependency graph - generates both .gv and .png files
# Run this after making changes to module dependencies

set -e

# Change to project root directory (parent of scripts/)
cd "$(dirname "$0")/.."

echo "🔄 Generating dependency graph..."
./gradlew generateUnifiedDependencyGraph --no-configure-on-demand

echo "📋 Copying .gv file to docs..."
cp build/reports/dependency-graph.gv docs/dependency-graph.gv

echo ""
echo "✅ Done! Files updated:"
echo "  - docs/dependency-graph.gv (for CI validation)"
echo "  - docs/project_dependencies_graph.png (for documentation)"
echo ""
echo "📝 Next steps:"
echo "  1. Review changes: git diff docs/dependency-graph.gv"
echo "  2. Commit both files: git add docs/dependency-graph.gv docs/project_dependencies_graph.png"
echo "  3. Include in your PR with the dependency changes"
echo ""
