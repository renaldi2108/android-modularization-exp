#!/bin/sh
APP_HOME="$(cd "$(dirname "$0")" && pwd -P)"
exec "$APP_HOME/gradle/wrapper/gradle-wrapper.jar" "$@" 2>/dev/null || \
exec java -jar "$APP_HOME/gradle/wrapper/gradle-wrapper.jar" "$@"
