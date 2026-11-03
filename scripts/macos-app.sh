#!/bin/sh
# Build the native macOS app as app/macos/build/macos/Walenje.app, sign it ad hoc and optionally open it.
# The bundle itself comes from the :app:macos:assembleMacosApp{Debug,Release} Gradle tasks.
#
# Usage: scripts/macos-app.sh [--release] [--open]
set -eu

cd "$(dirname "$0")/.."

variant=Debug
open_app=false
for arg in "$@"; do
  case "$arg" in
    --release) variant=Release ;;
    --open) open_app=true ;;
    *) echo "Unknown option: $arg" >&2; exit 2 ;;
  esac
done

./gradlew -Pwalenje.apple=true ":app:macos:assembleMacosApp$variant"

app=app/macos/build/macos/Walenje.app

# Ad-hoc signature so Gatekeeper and LocalAuthentication accept the bundle on this Mac
codesign --force --deep --sign - "$app" >/dev/null 2>&1 || echo "warning: ad-hoc codesign failed" >&2

echo "Built $app"
[ "$open_app" = false ] || open "$app"
