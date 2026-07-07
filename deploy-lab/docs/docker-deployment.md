# Docker 部署

## 构建产物

后端镜像使用本地已构建的 JAR：

```bash
cd deploy-lab/backend
mvn clean package
```

前端单实例模式由 Dockerfile 构建静态资源。负载均衡模式使用本地 `frontend/dist` 挂载，先执行：

```bash
cd deploy-lab/frontend
npm install
npm run build
```

## 单后端实例

```bash
cd deploy-lab/deploy/docker
docker compose -f compose.yml up -d --build
docker compose -f compose.yml logs -f
```

访问 `http://localhost`。

## 双后端负载均衡

```bash
cd deploy-lab/deploy/docker
docker compose -f compose-load-balance.yml up -d --build
```

连续刷新 Dashboard，观察 `backend-01` 和 `backend-02` 是否切换。

## 故障实验

```bash
docker compose -f compose-load-balance.yml stop backend-01
curl http://localhost/api/system/info
docker compose -f compose-load-balance.yml start backend-01
```

## 清理

```bash
docker compose -f compose.yml down
docker compose -f compose-load-balance.yml down
```
