#!/bin/sh
# Remove build output and local IDE/Xcode state. Not needed for normal builds.
#
# Usage: ./cleanup.sh [--ide] [--deep]
#   (no flag)  build directories, Kotlin state, Xcode user data, .DS_Store files and Kotlin/Native temp leftovers
#   --ide      also remove .idea (IntelliJ/Android Studio re-imports the project on next open)
#   --deep     also stop the Gradle and Kotlin daemons and remove the project's .gradle (configuration cache);
#              the shared ~/.gradle and ~/.konan caches are left alone
set -eu

cd "$(dirname "$0")"

ide=false
deep=false
for arg in "$@"; do
  case "$arg" in
    --ide) ide=true ;;
    --deep) deep=true ;;
    -h | --help) sed -n '2,9p' "$0"; exit 0 ;;
    *) echo "Unknown option: $arg" >&2; exit 2 ;;
  esac
done

free_space() { df -h . | awk 'NR == 2 { print $4 }'; }
before=$(free_space)

if [ "$deep" = true ]; then
  # Stop daemons first so they don't hold files open or recreate them
  ./gradlew --stop >/dev/null 2>&1 || true
  pkill -f KotlinCompileDaemon 2>/dev/null || true
fi

# Build output: the root, build-logic and every module (./gradlew clean does the same, but needs a daemon)
rm -rf build build-logic/build build-logic/.kotlin
find app core feature -maxdepth 2 -type d -name build -prune -exec rm -rf {} +
rm -rf .kotlin

# Xcode per-user state for the iOS app
rm -rf app/ios/Walenje.xcodeproj/project.xcworkspace/xcuserdata app/ios/Walenje.xcodeproj/xcuserdata

# Finder metadata
find . -name .DS_Store -not -path './.git/*' -delete

# Kotlin/Native leaves "included*" directories in the temp folder when a link fails or is interrupted
tmp="${TMPDIR:-/tmp}"
find "$tmp" -maxdepth 1 -type d -name 'included*' -user "$(id -un)" -exec rm -rf {} + 2>/dev/null || true

if [ "$ide" = true ]; then
  rm -rf .idea
fi

if [ "$deep" = true ]; then
  rm -rf .gradle build-logic/.gradle
fi

echo "Free space: $before -> $(free_space)"
