#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/../../.." && pwd)"
VERSION="${1:-1.0.0}"
APP_HOME="/opt/deploy-lab"

sudo install -d -o deploylab -g deploylab "$APP_HOME/backend/releases" "$APP_HOME/backend/config" "$APP_HOME/frontend" /var/log/deploy-lab
sudo install -m 0644 "$ROOT_DIR/backend/target/deploy-lab.jar" "$APP_HOME/backend/releases/deploy-lab-$VERSION.jar"
sudo ln -sfn "$APP_HOME/backend/releases/deploy-lab-$VERSION.jar" "$APP_HOME/backend/deploy-lab.jar"
sudo rsync -a --delete "$ROOT_DIR/frontend/dist/" "$APP_HOME/frontend/"
sudo chown -R deploylab:deploylab "$APP_HOME" /var/log/deploy-lab

sudo systemctl daemon-reload
sudo systemctl restart deploy-lab
sudo systemctl reload nginx

"$ROOT_DIR/deploy/smoke-test.sh" "http://127.0.0.1"
