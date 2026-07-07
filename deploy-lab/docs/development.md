# 本地开发

## 依赖

- Java 17+
- Maven 3.9+
- Node.js 20+
- npm

## 后端

```bash
cd deploy-lab/backend
mvn spring-boot:run
```

默认地址：`http://localhost:8080`。

## 前端

```bash
cd deploy-lab/frontend
npm install
npm run dev
```

默认地址：`http://localhost:5173`。

Vite 已配置代理：

- `/api` -> `http://localhost:8080`
- `/actuator` -> `http://localhost:8080`

## 联调检查

```bash
curl http://localhost:8080/actuator/health
curl http://localhost:8080/api/system/info
curl http://localhost:5173
```
