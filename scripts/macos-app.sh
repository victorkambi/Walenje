#!/bin/sh
# Build the native macOS app and wrap it in build/macos/Walenje.app.
#
# Kotlin Toolchain links app/macos into a bare executable and does not package native apps or their Compose
# resources. Compose for macOS reads resources from <bundle>/Contents/Resources/compose-resources/, so this
# script assembles a minimal .app bundle with the executable, an Info.plist and every module's composeResources.
#
# Usage: scripts/macos-app.sh [--release] [--open]
set -eu

cd "$(dirname "$0")/.."

variant=debug
open_app=false
for arg in "$@"; do
  case "$arg" in
    --release) variant=release ;;
    --open) open_app=true ;;
    *) echo "Unknown option: $arg" >&2; exit 2 ;;
  esac
done

KOTLIN_CLI_NO_WELCOME_BANNER=1 ./kotlin build -m macos -p macosArm64 -v "$variant"

variant_dir=$(echo "$variant" | awk '{ print toupper(substr($0, 1, 1)) substr($0, 2) }')
exe="build/tasks/_macos_linkMacosArm64${variant_dir}/macos.kexe"
[ -f "$exe" ] || { echo "Executable not found: $exe" >&2; exit 1; }

app=build/macos/Walenje.app
rm -rf "$app"
mkdir -p "$app/Contents/MacOS" "$app/Contents/Resources/compose-resources/composeResources"
cp "$exe" "$app/Contents/MacOS/Walenje"

# Each module's resources live under the package Compose generates for it: settings.compose.resources.packageName
# when set, otherwise <module directory name>.generated.resources.
for res in */*/composeResources; do
  module_dir=$(dirname "$res")
  package=$(awk '/packageName:/ { print $2 }' "$module_dir/module.yaml")
  [ -n "$package" ] || package="$(basename "$module_dir" | tr 'A-Z-' 'a-z_').generated.resources"
  mkdir -p "$app/Contents/Resources/compose-resources/composeResources/$package"
  cp -R "$res/." "$app/Contents/Resources/compose-resources/composeResources/$package/"
done

cat > "$app/Contents/Info.plist" <<PLIST
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE plist PUBLIC "-//Apple//DTD PLIST 1.0//EN" "http://www.apple.com/DTDs/PropertyList-1.0.dtd">
<plist version="1.0">
<dict>
  <key>CFBundleExecutable</key><string>Walenje</string>
  <key>CFBundleIdentifier</key><string>kambi.victor.Walenje</string>
  <key>CFBundleName</key><string>Walenje</string>
  <key>CFBundleDisplayName</key><string>Walenje</string>
  <key>CFBundlePackageType</key><string>APPL</string>
  <key>CFBundleShortVersionString</key><string>1.0</string>
  <key>CFBundleVersion</key><string>1</string>
  <key>LSMinimumSystemVersion</key><string>14.0</string>
  <key>NSHighResolutionCapable</key><true/>
  <key>NSPrincipalClass</key><string>NSApplication</string>
</dict>
</plist>
PLIST

# Ad-hoc signature so Gatekeeper and LocalAuthentication accept the bundle on this Mac
codesign --force --deep --sign - "$app" >/dev/null 2>&1 || echo "warning: ad-hoc codesign failed" >&2

echo "Built $app"
[ "$open_app" = false ] || open "$app"
