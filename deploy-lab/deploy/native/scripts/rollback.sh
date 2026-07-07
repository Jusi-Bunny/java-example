#!/usr/bin/env bash
set -euo pipefail

VERSION="${1:?Usage: rollback.sh <version>}"
APP_HOME="/opt/deploy-lab"
TARGET="$APP_HOME/backend/releases/deploy-lab-$VERSION.jar"

if [[ ! -f "$TARGET" ]]; then
  echo "Release not found: $TARGET" >&2
  exit 1
fi

sudo ln -sfn "$TARGET" "$APP_HOME/backend/deploy-lab.jar"
sudo systemctl restart deploy-lab
curl -fsS "http://127.0.0.1:8080/actuator/health"
