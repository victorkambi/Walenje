#!/bin/sh
# Remove build output and IDE state. Not needed for normal builds.
rm -rf .idea
rm -rf build
rm -rf app/ios/module.xcodeproj/project.xcworkspace/xcuserdata
rm -rf app/ios/module.xcodeproj/xcuserdata
