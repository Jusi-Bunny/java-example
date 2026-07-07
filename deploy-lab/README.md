# Deploy Lab

Deploy Lab 是一个前后端分离的部署实验项目，用于练习 Spring Boot、Vue、Nginx、Linux 原生部署、systemd、Docker Compose、多实例负载均衡、更新和回滚。

## 模块结构

```text
deploy-lab/
├── backend/   Spring Boot 3.5 后端
├── frontend/  Vue 3 + TypeScript + Vite 前端
├── deploy/    Nginx、systemd、Docker、脚本
└── docs/      本地开发、接口和部署说明
```

## 本地启动

后端：

```bash
cd backend
mvn spring-boot:run
```

前端：

```bash
cd frontend
npm install
npm run dev
```

浏览器访问 `http://localhost:5173`。Vite 会把 `/api` 和 `/actuator` 代理到 `http://localhost:8080`。

## 构建

```bash
cd deploy-lab
deploy/native/scripts/build.sh
```

构建产物：

- 后端：`backend/target/deploy-lab.jar`
- 前端：`frontend/dist`

## Docker

先构建后端 JAR 和前端静态资源，再启动：

```bash
cd deploy/docker
docker compose -f compose.yml up -d --build
```

双后端负载均衡：

```bash
cd deploy/docker
docker compose -f compose-load-balance.yml up -d --build
```

## 关键接口

- `GET /api/system/info`
- `GET|POST|PUT|DELETE /api/http/inspect`
- `GET|POST|DELETE /api/messages`
- `GET|DELETE /api/request-records`
- `GET /api/diagnostics/delay?milliseconds=1000`
- `GET /api/diagnostics/status?code=503`
- `GET /actuator/health`

更多说明见 `docs/`。
