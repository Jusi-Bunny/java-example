# Linux 原生部署

## 服务器目录

```text
/opt/deploy-lab/
├── backend/
│   ├── deploy-lab.jar
│   ├── config/application-prod.yml
│   └── releases/
└── frontend/

/var/log/deploy-lab/
```

## 运行用户

```bash
sudo useradd --system --create-home --shell /usr/sbin/nologin deploylab
sudo mkdir -p /opt/deploy-lab/backend/config /opt/deploy-lab/frontend /var/log/deploy-lab
sudo chown -R deploylab:deploylab /opt/deploy-lab /var/log/deploy-lab
```

## systemd

```bash
sudo cp deploy/native/systemd/deploy-lab.service /etc/systemd/system/deploy-lab.service
sudo systemctl daemon-reload
sudo systemctl enable deploy-lab
sudo systemctl start deploy-lab
```

## Nginx

```bash
sudo cp deploy/native/nginx/deploy-lab.conf /etc/nginx/conf.d/deploy-lab.conf
sudo nginx -t
sudo systemctl reload nginx
```

## 发布

```bash
deploy/native/scripts/build.sh
deploy/native/scripts/deploy.sh 1.0.0
```

## 验收

```bash
systemctl status deploy-lab
journalctl -u deploy-lab -f
curl http://127.0.0.1:8080/actuator/health
curl http://127.0.0.1/api/system/info
nginx -t
```
