#!/bin/sh
# Remove build output and local IDE/Xcode state. Not needed for normal builds.
#
# Usage: ./cleanup.sh [--ide] [--deep]
#   (no flag)  project build output, Xcode user data, .DS_Store files and Kotlin/Native temp leftovers
#   --ide      also remove .idea (IntelliJ/Android Studio re-imports the project on next open)
#   --deep     also remove Kotlin Toolchain's download cache (archives already extracted elsewhere;
#              re-downloaded only if needed)
set -eu

cd "$(dirname "$0")"

ide=false
deep=false
for arg in "$@"; do
  case "$arg" in
    --ide) ide=true ;;
    --deep) deep=true ;;
    -h | --help) sed -n '2,8p' "$0"; exit 0 ;;
    *) echo "Unknown option: $arg" >&2; exit 2 ;;
  esac
done

free_space() { df -h . | awk 'NR == 2 { print $4 }'; }
before=$(free_space)

# Project build output (./kotlin clean does the same, but needs the toolchain to start)
rm -rf build

# Xcode per-user state for the iOS app
rm -rf app/ios/module.xcodeproj/project.xcworkspace/xcuserdata app/ios/module.xcodeproj/xcuserdata

# Finder metadata
find . -name .DS_Store -not -path './.git/*' -delete

# Kotlin/Native leaves "included*" directories in the temp folder when a link fails or is interrupted
tmp="${TMPDIR:-/tmp}"
find "$tmp" -maxdepth 1 -type d -name 'included*' -user "$(id -un)" -exec rm -rf {} + 2>/dev/null || true

if [ "$ide" = true ]; then
  rm -rf .idea
fi

if [ "$deep" = true ]; then
  case "$(uname -s)" in
    Darwin) cache="$HOME/Library/Caches/JetBrains/Kotlin" ;;
    *) cache="${XDG_CACHE_HOME:-$HOME/.cache}/JetBrains/Kotlin" ;;
  esac
  rm -rf "$cache/download.cache"
fi

echo "Free space: $before -> $(free_space)"
