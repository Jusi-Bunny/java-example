# Deploy Lab 开发计划书

## 1. 项目基本信息

| 项目 | 内容 |
|---|---|
| 项目名称 | Deploy Lab |
| 中文名称 | 部署实验室 |
| 项目类型 | 前后端分离 Web 项目 |
| 核心用途 | 学习 Spring Boot、Nginx、Linux 原生部署和 Docker 部署 |
| 前端 | Vue 3 + TypeScript + Vite |
| 后端 | Java 17 + Spring Boot 3.5.x |
| 数据存储 | JVM 内存 |
| 外部服务 | 无 |
| 目标环境 | Linux 服务器 |
| 第一版版本号 | `1.0.0` |

---

## 2. 项目背景

Deploy Lab 是一个面向开发和部署学习的小型工具，用于展示当前后端实例的运行状态、检查 HTTP 请求信息、测试接口延迟与异常，以及维护少量临时消息。

项目不引入 MySQL、Redis、消息队列、对象存储等外部服务。所有临时数据均保存在 JVM 内存中，应用重启后自动清空。

项目重点不在业务复杂度，而在完整实践以下流程：

1. 前后端本地开发；
2. 前端静态资源构建；
3. Spring Boot JAR 打包；
4. Linux 原生部署；
5. systemd 服务管理；
6. Nginx 静态资源托管；
7. Nginx 反向代理；
8. HTTPS 配置；
9. Docker 镜像构建；
10. Docker Compose 部署；
11. 多实例负载均衡；
12. 服务更新、回滚与迁移。

---

## 3. 项目目标

### 3.1 功能目标

第一版应提供以下功能：

- 查看后端运行状态；
- 查看当前后端实例信息；
- 查看前端构建信息；
- 检查后端实际收到的 HTTP 请求；
- 测试不同请求方法和请求体；
- 模拟接口延迟；
- 模拟常见 HTTP 错误；
- 创建和删除临时消息；
- 查看最近的请求记录；
- 检查 Spring Boot 健康状态。

### 3.2 学习目标

完成项目后，应能够独立完成：

- Vue 项目的构建和部署；
- Spring Boot 项目的打包和启动；
- Spring Boot 外置配置；
- Spring Boot 日志外置；
- systemd 服务编写和管理；
- Nginx `server` 和 `location` 配置；
- Nginx 静态资源托管；
- SPA 路由回退；
- Nginx 反向代理；
- 客户端真实 IP 转发；
- HTTPS 和 HTTP 跳转；
- Dockerfile 编写；
- Docker Compose 服务编排；
- Docker 容器网络通信；
- Spring Boot 多实例部署；
- Nginx upstream 负载均衡；
- 服务更新和回滚；
- 从一台服务器迁移到另一台服务器。

---

## 4. 非目标范围

第一版明确不实现以下功能：

- 用户注册和登录；
- RBAC 权限管理；
- MySQL 数据持久化；
- Redis 缓存；
- 文件上传与存储；
- 消息队列；
- 分布式锁；
- 微服务拆分；
- Kubernetes；
- 复杂后台管理系统；
- 第三方 OAuth；
- 邮件和短信通知；
- 复杂监控平台；
- 高并发性能优化。

这些内容与当前部署学习目标关系不大，过早加入会增加项目复杂度。

---

## 5. 总体架构

### 5.1 生产环境请求链路

```text
浏览器
  │
  │ HTTP / HTTPS
  ▼
Nginx
  ├── /                  → Vue 静态资源
  ├── /assets/           → 前端 JS、CSS、图片
  ├── /api/              → Spring Boot
  └── /actuator/health   → Spring Boot 健康检查
```

### 5.2 多实例部署结构

```text
浏览器
  │
  ▼
Nginx
  │
  ├── backend-01:8080
  └── backend-02:8080
```

每个后端实例返回自己的实例编号：

```json
{
  "instanceId": "backend-01",
  "appVersion": "1.0.0"
}
```

连续刷新页面时，可以观察请求被分配到不同实例。

### 5.3 Docker Compose 结构

```text
deploy-lab-network
  ├── deploy-lab-nginx
  ├── deploy-lab-backend-01
  └── deploy-lab-backend-02
```

Nginx 通过服务名称访问后端：

```nginx
upstream deploy_lab_backend {
    server backend-01:8080;
    server backend-02:8080;
}
```

---

## 6. 技术选型

### 6.1 后端技术

| 技术 | 选择 |
|---|---|
| Java | Java 17 |
| Web 框架 | Spring Boot 3.5.x |
| Web 容器 | Spring Boot 默认嵌入式 Tomcat |
| 参数校验 | Jakarta Validation |
| 健康检查 | Spring Boot Actuator |
| 构建工具 | Maven |
| JSON | Jackson |
| 日志 | Logback |
| 测试 | JUnit 5、MockMvc |
| 数据存储 | ConcurrentHashMap |
| 定时清理 | Spring `@Scheduled` |

### 6.2 前端技术

| 技术 | 选择 |
|---|---|
| 框架 | Vue 3 |
| 开发语言 | TypeScript |
| 构建工具 | Vite |
| 路由 | Vue Router |
| HTTP 客户端 | Axios |
| 状态管理 | 第一版不引入 Pinia |
| UI 组件库 | 第一版不引入 |
| 样式 | 原生 CSS 或 SCSS |
| 包管理器 | npm 或 pnpm |

### 6.3 部署技术

| 技术 | 用途 |
|---|---|
| Linux | 生产运行环境 |
| Nginx | 静态资源、反向代理、HTTPS、负载均衡 |
| systemd | 管理原生部署的 Spring Boot 服务 |
| Docker | 后端和前端容器化 |
| Docker Compose | 多容器编排 |
| Shell | 构建和部署脚本 |
| curl | 健康检查与部署验证 |

---

## 7. 项目目录规划

```text
deploy-lab/
├── backend/
│   ├── pom.xml
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   └── resources/
│   │   └── test/
│   └── README.md
│
├── frontend/
│   ├── package.json
│   ├── vite.config.ts
│   ├── src/
│   ├── public/
│   └── README.md
│
├── deploy/
│   ├── native/
│   │   ├── nginx/
│   │   │   └── deploy-lab.conf
│   │   ├── systemd/
│   │   │   └── deploy-lab.service
│   │   └── scripts/
│   │       ├── build.sh
│   │       ├── deploy.sh
│   │       └── rollback.sh
│   │
│   └── docker/
│       ├── compose.yml
│       ├── compose-load-balance.yml
│       ├── backend.Dockerfile
│       ├── frontend.Dockerfile
│       └── nginx/
│           ├── nginx.conf
│           └── conf.d/
│               └── default.conf
│
├── docs/
│   ├── development.md
│   ├── api.md
│   ├── native-deployment.md
│   ├── docker-deployment.md
│   ├── nginx-guide.md
│   └── requests.http
│
├── .gitignore
└── README.md
```

---

## 8. 后端模块设计

后端保持传统分层，不使用复杂的领域驱动设计。

```text
com.example.deploylab
├── common
│   ├── ApiResponse
│   ├── ErrorCode
│   ├── GlobalExceptionHandler
│   └── TraceIdFilter
├── config
├── controller
├── dto
│   ├── request
│   └── response
├── model
├── repository
├── service
├── interceptor
└── DeployLabApplication
```

### 8.1 系统信息模块

用于展示当前实例和运行环境。

#### 展示字段

- 应用名称；
- 应用版本；
- 实例编号；
- 当前环境；
- 启动时间；
- 运行时长；
- Java 版本；
- 操作系统；
- 主机名；
- 容器名称；
- 当前服务端口；
- 当前时间；
- JVM 可用处理器数量；
- JVM 已使用内存；
- JVM 最大内存；
- 是否启用诊断接口。

#### 接口

```http
GET /api/system/info
```

响应示例：

```json
{
  "code": 0,
  "message": "success",
  "data": {
    "applicationName": "deploy-lab",
    "appVersion": "1.0.0",
    "instanceId": "backend-01",
    "activeProfile": "prod",
    "hostname": "deploy-lab-server",
    "javaVersion": "17.0.11",
    "osName": "Linux",
    "serverPort": 8080,
    "startedAt": "2026-07-07T15:00:00+09:00",
    "uptimeSeconds": 3600,
    "currentTime": "2026-07-07T16:00:00+09:00"
  },
  "traceId": "019abc123def",
  "timestamp": "2026-07-07T16:00:00+09:00"
}
```

### 8.2 HTTP 请求检查模块

前端发送请求，后端原样展示实际接收到的信息。

#### 返回内容

- HTTP Method；
- URI；
- Query 参数；
- 请求头；
- Content-Type；
- 请求体；
- 客户端 IP；
- `X-Real-IP`；
- `X-Forwarded-For`；
- `X-Forwarded-Proto`；
- Host；
- User-Agent；
- 实例编号；
- 接收时间。

#### 接口

```http
GET    /api/http/inspect
POST   /api/http/inspect
PUT    /api/http/inspect
DELETE /api/http/inspect
```

#### 安全处理

以下请求头不得原样返回：

- `Authorization`
- `Cookie`
- `Set-Cookie`
- `Proxy-Authorization`

统一替换为：

```text
******
```

请求体最大展示长度建议为 10 KB，超过部分进行截断。

### 8.3 临时消息模块

用于练习完整的前后端 CRUD。

#### 数据结构

```java
public class Message {

    private Long id;

    private String title;

    private String content;

    private Instant createdAt;

    private Instant expiresAt;
}
```

#### 数据存储

```java
private final ConcurrentHashMap<Long, Message> storage =
        new ConcurrentHashMap<>();

private final AtomicLong sequence = new AtomicLong();
```

#### 业务规则

- 标题不能为空；
- 标题最多 100 个字符；
- 内容最多 2,000 个字符；
- 默认最多保存 200 条；
- 超出数量时删除最早的数据；
- 可以设置过期时间；
- 定时删除过期消息；
- 应用重启后数据丢失。

#### 接口

```http
GET    /api/messages
POST   /api/messages
GET    /api/messages/{id}
DELETE /api/messages/{id}
DELETE /api/messages
```

第一版可以不实现修改接口，避免功能膨胀。

### 8.4 最近请求记录模块

使用拦截器记录最近的接口访问情况。

#### 保存字段

```java
public class RequestRecord {

    private Long id;

    private String traceId;

    private String method;

    private String path;

    private Integer status;

    private Long durationMs;

    private String clientIp;

    private String instanceId;

    private Instant createdAt;
}
```

#### 业务规则

- 最多保存 100 条；
- 只保存请求元数据；
- 不保存请求体；
- 不保存 Cookie 和 Authorization；
- `/actuator/health` 可以不计入记录；
- 应用重启后记录清空。

#### 接口

```http
GET    /api/request-records
DELETE /api/request-records
```

### 8.5 诊断模块

诊断模块专门用于验证 Nginx 超时、状态码和异常处理。

#### 延迟接口

```http
GET /api/diagnostics/delay?milliseconds=1000
```

规则：

- 最小值为 0；
- 最大值为 5,000 毫秒；
- 禁止无限等待；
- 返回实际等待时间和实例编号。

#### 状态码接口

```http
GET /api/diagnostics/status?code=503
```

只允许：

```text
200、400、401、403、404、429、500、502、503、504
```

#### 异常接口

```http
GET /api/diagnostics/error
```

用于验证：

- Spring Boot 全局异常处理；
- Nginx 日志；
- 前端错误展示；
- traceId 定位。

#### 配置开关

```yaml
deploy-lab:
  diagnostics:
    enabled: true
```

生产环境暴露到公网时，可以通过环境变量关闭：

```bash
DEPLOY_LAB_DIAGNOSTICS_ENABLED=false
```

---

## 9. API 通用规范

### 9.1 统一响应结构

```java
public record ApiResponse<T>(
        int code,
        String message,
        T data,
        String traceId,
        Instant timestamp
) {
}
```

成功响应：

```json
{
  "code": 0,
  "message": "success",
  "data": {},
  "traceId": "019abc123def",
  "timestamp": "2026-07-07T16:00:00+09:00"
}
```

错误响应：

```json
{
  "code": 40001,
  "message": "消息标题不能为空",
  "data": null,
  "traceId": "019abc123def",
  "timestamp": "2026-07-07T16:00:00+09:00"
}
```

### 9.2 HTTP 状态码

不要所有请求都返回 HTTP 200。

| 场景 | HTTP 状态 |
|---|---:|
| 查询成功 | 200 |
| 创建成功 | 201 |
| 参数错误 | 400 |
| 数据不存在 | 404 |
| 请求过多 | 429 |
| 服务器异常 | 500 |
| 服务不可用 | 503 |

### 9.3 Trace ID

每个请求生成一个 traceId。

处理规则：

1. 请求包含 `X-Request-Id` 时优先使用；
2. 不存在时由后端生成；
3. 写入 MDC；
4. 写入响应头；
5. 写入响应体；
6. 写入日志；
7. 写入最近请求记录。

响应头：

```http
X-Request-Id: 019abc123def
X-Instance-Id: backend-01
X-App-Version: 1.0.0
```

### 9.4 分页

由于内存数据量很小，第一版不实现复杂分页。

列表接口可以使用：

```http
GET /api/messages?limit=50
```

`limit` 最大值为 200。

---

## 10. Spring Boot 配置规划

### 10.1 配置文件

```text
application.yml
application-dev.yml
application-prod.yml
```

### 10.2 基础配置

```yaml
spring:
  application:
    name: deploy-lab

server:
  port: ${SERVER_PORT:8080}
  shutdown: graceful
  forward-headers-strategy: framework

management:
  endpoints:
    web:
      exposure:
        include: health,info
  endpoint:
    health:
      show-details: never

deploy-lab:
  instance-id: ${APP_INSTANCE_ID:${HOSTNAME:local}}
  diagnostics:
    enabled: ${DIAGNOSTICS_ENABLED:true}
  storage:
    max-messages: ${MAX_MESSAGES:200}
    max-request-records: ${MAX_REQUEST_RECORDS:100}
```

### 10.3 Actuator 暴露范围

只暴露：

```text
/actuator/health
/actuator/info
```

不对公网暴露：

- `/actuator/env`
- `/actuator/beans`
- `/actuator/configprops`
- `/actuator/heapdump`
- `/actuator/loggers`

### 10.4 日志规划

开发环境：

```text
控制台输出
```

生产环境：

```text
/var/log/deploy-lab/application.log
/var/log/deploy-lab/error.log
```

日志格式至少包含：

```text
时间
日志级别
线程
traceId
instanceId
类名
消息
```

示例：

```text
2026-07-07 16:00:00 INFO [http-nio-8080-exec-1]
[traceId=019abc123def] [instanceId=backend-01]
Request completed method=GET path=/api/system/info status=200 duration=12ms
```

---

## 11. 前端页面规划

### 11.1 页面路由

```text
/                     首页
/dashboard            运行状态
/request-inspector    请求检查
/messages             临时消息
/diagnostics          诊断工具
/request-records      请求记录
```

`/` 自动跳转至 `/dashboard`。

### 11.2 整体布局

```text
┌─────────────────────────────────────────────┐
│ Deploy Lab     后端：在线   backend-01      │
├───────────────┬─────────────────────────────┤
│ 运行状态      │                             │
│ 请求检查      │          页面内容           │
│ 临时消息      │                             │
│ 请求记录      │                             │
│ 诊断工具      │                             │
└───────────────┴─────────────────────────────┘
```

### 11.3 运行状态页面

使用卡片展示：

- 后端在线状态；
- 应用版本；
- 实例编号；
- 当前环境；
- 启动时间；
- 运行时长；
- Java 版本；
- 操作系统；
- 主机名；
- JVM 内存；
- 前端版本；
- 前端构建时间。

页面每 10 秒自动刷新一次状态。

### 11.4 请求检查页面

页面表单包含：

- Method 选择；
- Query 参数；
- Content-Type；
- 请求体；
- 自定义请求头；
- 发送按钮。

支持 Method：

```text
GET
POST
PUT
DELETE
```

请求发送后展示：

```text
响应状态
请求耗时
响应头
服务端接收的客户端 IP
X-Forwarded-For
请求体
当前后端实例
traceId
```

### 11.5 临时消息页面

包含：

- 消息标题输入框；
- 消息内容输入框；
- 过期时间；
- 创建按钮；
- 消息列表；
- 删除按钮；
- 清空按钮。

页面显著提示：

> 当前数据仅保存在后端内存中，应用重启后将全部清空。

### 11.6 请求记录页面

列表字段：

| 字段 | 说明 |
|---|---|
| 时间 | 请求时间 |
| Method | HTTP 方法 |
| Path | 请求路径 |
| Status | HTTP 状态 |
| Duration | 请求耗时 |
| Client IP | 客户端 IP |
| Instance | 后端实例 |
| Trace ID | 请求追踪编号 |

支持：

- 手动刷新；
- 自动刷新开关；
- 清空记录；
- 按实例筛选；
- 按 Method 筛选；
- 按 HTTP 状态筛选。

### 11.7 诊断页面

提供以下按钮：

- 延迟 500ms；
- 延迟 1 秒；
- 延迟 3 秒；
- 返回 400；
- 返回 404；
- 返回 500；
- 返回 503；
- 抛出服务器异常。

每次测试展示：

- HTTP 状态；
- 总耗时；
- Nginx 是否返回错误；
- 后端实例；
- traceId。

---

## 12. 前端请求配置

### 12.1 开发环境

浏览器访问：

```text
http://localhost:5173
```

Vite 将 `/api` 转发至：

```text
http://localhost:8080
```

示例：

```ts
export default defineConfig({
  server: {
    proxy: {
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true
      },
      '/actuator': {
        target: 'http://localhost:8080',
        changeOrigin: true
      }
    }
  }
})
```

### 12.2 生产环境

前端始终使用相对地址：

```ts
const apiBaseUrl = '/api'
```

生产环境由 Nginx 转发，因此不需要在浏览器中跨域访问后端。

这种方式可以同时实现：

- 前后端源码分离；
- 前后端独立构建；
- 生产环境统一域名；
- 避免不必要的 CORS 配置；
- 方便后端地址迁移。

---

## 13. Nginx 学习计划

### 13.1 第一阶段：静态资源托管

目标：

- 使用 Nginx 托管 Vue `dist`；
- 配置首页；
- 访问静态资源；
- 理解 `root` 和 `index`。

基本结构：

```nginx
location / {
    root /opt/deploy-lab/frontend;
    index index.html;
}
```

### 13.2 第二阶段：SPA 路由回退

直接刷新：

```text
/dashboard
/messages
/diagnostics
```

不应返回 Nginx 404。

配置：

```nginx
location / {
    root /opt/deploy-lab/frontend;
    try_files $uri $uri/ /index.html;
}
```

### 13.3 第三阶段：后端反向代理

```nginx
location /api/ {
    proxy_pass http://127.0.0.1:8080;

    proxy_set_header Host $host;
    proxy_set_header X-Real-IP $remote_addr;
    proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
    proxy_set_header X-Forwarded-Proto $scheme;
    proxy_set_header X-Request-Id $request_id;
}
```

### 13.4 第四阶段：静态资源缓存

带哈希的文件：

```nginx
location /assets/ {
    expires 30d;
    add_header Cache-Control "public, immutable";
}
```

首页禁止长期缓存：

```nginx
location = /index.html {
    add_header Cache-Control "no-cache";
}
```

### 13.5 第五阶段：压缩

开启：

```nginx
gzip on;
gzip_types
    text/plain
    text/css
    application/json
    application/javascript;
```

验证：

```bash
curl -I -H "Accept-Encoding: gzip" https://example.com/assets/index.js
```

### 13.6 第六阶段：超时实验

先调用：

```text
/api/diagnostics/delay?milliseconds=5000
```

再调整：

```nginx
proxy_connect_timeout 5s;
proxy_send_timeout 5s;
proxy_read_timeout 5s;
```

观察：

- 正常返回；
- Nginx 504；
- 后端仍在执行；
- access log 和 error log 的差异。

### 13.7 第七阶段：HTTPS

实现：

```text
http://example.com
        ↓ 301
https://example.com
```

验证：

- HTTP 强制跳转 HTTPS；
- 证书是否正确；
- `X-Forwarded-Proto` 是否为 `https`；
- 后端是否正确识别原始协议。

### 13.8 第八阶段：负载均衡

```nginx
upstream deploy_lab_backend {
    server 127.0.0.1:8081;
    server 127.0.0.1:8082;
}

location /api/ {
    proxy_pass http://deploy_lab_backend;
}
```

验证：

- 刷新页面时实例编号发生变化；
- 停止一个实例后服务仍然可用；
- 恢复实例后重新参与请求；
- 不同实例的内存消息互不共享。

最后一点非常重要：由于数据保存在实例内存中，负载均衡后，某个实例创建的消息不会自动出现在另一个实例中。这可以帮助理解为什么真实分布式系统需要共享数据库或缓存。

---

## 14. Linux 原生部署计划

### 14.1 服务器目录

```text
/opt/deploy-lab/
├── backend/
│   ├── deploy-lab.jar
│   ├── config/
│   │   └── application-prod.yml
│   └── releases/
│       ├── deploy-lab-1.0.0.jar
│       └── deploy-lab-1.0.1.jar
│
└── frontend/
    ├── index.html
    └── assets/

/var/log/deploy-lab/
├── application.log
└── error.log
```

### 14.2 创建运行用户

创建独立的系统用户：

```text
deploylab
```

应用不使用 root 身份运行。

### 14.3 systemd 服务

服务文件：

```text
/etc/systemd/system/deploy-lab.service
```

核心配置应包含：

```ini
[Unit]
Description=Deploy Lab Backend
After=network.target

[Service]
User=deploylab
Group=deploylab
WorkingDirectory=/opt/deploy-lab/backend
ExecStart=/usr/bin/java -jar /opt/deploy-lab/backend/deploy-lab.jar
Restart=on-failure
RestartSec=5
SuccessExitStatus=143

Environment=SPRING_PROFILES_ACTIVE=prod
Environment=APP_INSTANCE_ID=backend-native-01
Environment=SERVER_PORT=8080

[Install]
WantedBy=multi-user.target
```

### 14.4 原生部署流程

```text
1. 本地构建前端
2. 本地打包后端
3. 上传 dist
4. 上传 JAR
5. 替换软链接或当前 JAR
6. 重启 systemd 服务
7. 检查 Actuator
8. 检查 Nginx
9. 执行接口冒烟测试
```

### 14.5 原生部署验收命令

```bash
systemctl status deploy-lab
journalctl -u deploy-lab -f
curl http://127.0.0.1:8080/actuator/health
curl http://127.0.0.1/api/system/info
nginx -t
systemctl reload nginx
```

---

## 15. Docker 部署计划

### 15.1 后端 Dockerfile

第一版采用“本地构建 JAR，镜像只负责运行”的方式，便于理解镜像内容。

```dockerfile
FROM eclipse-temurin:17-jre

WORKDIR /app

COPY target/deploy-lab.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
```

后续再增加 Maven 多阶段构建版本。

### 15.2 前端 Dockerfile

采用多阶段构建：

```text
Node 镜像构建 Vue
       ↓
Nginx 镜像运行静态资源
```

结构示例：

```dockerfile
FROM node:lts-alpine AS build

WORKDIR /app
COPY package*.json ./
RUN npm ci
COPY ../.. .
RUN npm run build

FROM nginx:stable-alpine
COPY --from=build /app/dist /usr/share/nginx/html
COPY nginx/default.conf /etc/nginx/conf.d/default.conf
```

### 15.3 Docker Compose 服务

```yaml
services:
  backend:
    build:
      context: ../../backend
      dockerfile: ../deploy/docker/backend.Dockerfile
    environment:
      SPRING_PROFILES_ACTIVE: prod
      APP_INSTANCE_ID: backend-docker-01
      SERVER_PORT: 8080
    expose:
      - "8080"

  nginx:
    build:
      context: ../../frontend
      dockerfile: ../deploy/docker/frontend.Dockerfile
    ports:
      - "80:80"
    depends_on:
      backend:
        condition: service_healthy
```

### 15.4 健康检查

后端容器：

```yaml
healthcheck:
  test:
    [
      "CMD",
      "wget",
      "-q",
      "--spider",
      "http://localhost:8080/actuator/health"
    ]
  interval: 10s
  timeout: 3s
  retries: 5
  start_period: 20s
```

### 15.5 Docker 部署阶段

按照以下顺序练习：

#### 模式一：只有后端使用 Docker

```text
宿主机 Nginx
      ↓
Docker Spring Boot
```

学习：

- 端口映射；
- 容器日志；
- 宿主机访问容器；
- 环境变量；
- 容器重启。

#### 模式二：前后端全部 Docker 化

```text
Nginx 容器
      ↓
Spring Boot 容器
```

学习：

- Compose；
- 服务名访问；
- 容器网络；
- 构建上下文；
- 容器健康检查。

#### 模式三：两个后端实例

```text
Nginx 容器
  ├── backend-01
  └── backend-02
```

学习：

- upstream；
- 多实例；
- 负载均衡；
- 单实例故障；
- 内存数据不共享。

---

## 16. 部署实验清单

项目完成后，需要实际执行以下实验。

### 16.1 基础实验

- 前端本地开发服务器访问后端；
- Vite 开发代理正常；
- Spring Boot JAR 独立运行；
- 前端构建产物由 Nginx 提供；
- `/api` 请求由 Nginx 转发；
- Vue 路由刷新不出现 404。

### 16.2 请求头实验

检查：

```text
Host
X-Real-IP
X-Forwarded-For
X-Forwarded-Proto
X-Request-Id
```

对比以下三种访问方式：

```text
直接访问 Spring Boot
通过宿主机 Nginx 访问
通过 Docker Nginx 访问
```

### 16.3 故障实验

- 停止后端，观察 Nginx 502；
- 调低代理超时，观察 504；
- 后端返回 500；
- 后端启动失败；
- Nginx 配置语法错误；
- Docker 健康检查失败；
- 删除容器后重新创建；
- 重启服务器后服务自动启动。

### 16.4 数据实验

- 创建临时消息；
- 重启后端；
- 验证消息消失；
- 启动两个后端；
- 在实例 1 创建消息；
- 请求被转发到实例 2；
- 观察数据不一致。

### 16.5 更新与回滚实验

- 部署 `1.0.0`；
- 更新到 `1.0.1`；
- 页面显示新版本；
- 模拟新版启动失败；
- 回滚到 `1.0.0`；
- 验证服务恢复。

---

## 17. 测试计划

### 17.1 后端单元测试

需要覆盖：

- 消息创建；
- 消息删除；
- 消息数量限制；
- 过期消息清理；
- 请求记录数量限制；
- 系统信息获取；
- 延迟参数上限；
- 非法状态码；
- 敏感请求头脱敏。

### 17.2 后端接口测试

使用 MockMvc 测试：

```text
GET /api/system/info
POST /api/http/inspect
POST /api/messages
GET /api/messages
DELETE /api/messages/{id}
GET /api/diagnostics/delay
GET /api/diagnostics/status
GET /actuator/health
```

### 17.3 前端检查

第一版至少执行：

```bash
npm run type-check
npm run build
```

检查：

- TypeScript 无错误；
- 前端可以成功构建；
- 页面刷新正常；
- API 错误可以展示；
- 后端离线有明确提示；
- 移动端宽度下页面不严重错位。

### 17.4 部署冒烟测试

编写脚本：

```text
deploy/smoke-test.sh
```

测试内容：

```bash
curl -f /actuator/health
curl -f /api/system/info
curl -f /api/messages
curl -f /
```

脚本任何一步失败，应返回非零退出码。

---

## 18. 安全限制

虽然项目主要用于学习，但部署到公网时仍要进行基本保护。

### 18.1 不展示敏感系统信息

不返回：

- 服务器完整环境变量；
- 用户目录；
- Java 启动参数；
- 文件系统路径；
- 云服务凭证；
- Authorization；
- Cookie；
- 内网服务列表。

### 18.2 诊断接口限制

- 最大延迟 5 秒；
- 不允许用户指定任意长时间；
- 不允许执行系统命令；
- 不允许读取服务器文件；
- 不允许发起任意 URL 请求；
- 可通过环境变量完全关闭；
- 可使用 Nginx 对诊断路径进行 IP 限制。

### 18.3 Actuator 限制

公网仅允许访问：

```text
health
info
```

其他 Actuator 端点全部关闭。

### 18.4 Nginx 限制

建议配置：

```nginx
client_max_body_size 1m;
limit_req;
server_tokens off;
```

消息和请求检查功能不需要大请求体。

---

## 19. 开发阶段与里程碑

### 阶段一：项目初始化

任务：

- 创建 Git 仓库；
- 创建前端工程；
- 创建后端工程；
- 统一目录；
- 配置 `.gitignore`；
- 编写基础 README；
- 确认前后端可以启动。

交付物：

```text
前端欢迎页面
后端 /api/system/info
基础项目结构
```

验收标准：

- 前端启动成功；
- 后端启动成功；
- 浏览器能请求后端；
- Maven 和前端构建均成功。

### 阶段二：后端核心功能

任务：

- 统一响应结构；
- 全局异常处理；
- Trace ID；
- 系统信息；
- 请求检查；
- 临时消息；
- 请求记录；
- 诊断接口；
- Actuator。

验收标准：

- 所有接口可通过 `requests.http` 调用；
- 参数错误返回正确状态码；
- 请求日志包含 traceId；
- 健康检查可用。

### 阶段三：前端功能

任务：

- 整体布局；
- Dashboard；
- 请求检查；
- 临时消息；
- 请求记录；
- 诊断页面；
- API 错误处理；
- 后端在线状态。

验收标准：

- 所有功能可通过页面操作；
- 页面不依赖数据库；
- 后端重启后页面能够恢复连接；
- 后端离线时有明确提示。

### 阶段四：Nginx 本地实验

任务：

- 构建前端；
- Nginx 托管静态资源；
- 配置 SPA 路由；
- 代理 `/api`；
- 转发请求头；
- 配置日志；
- 配置缓存；
- 配置 gzip。

验收标准：

- 统一通过 Nginx 访问；
- 不再直接暴露前端开发端口；
- `/dashboard` 刷新正常；
- 后端可以获取真实转发头。

### 阶段五：Linux 原生部署

任务：

- 规划服务器目录；
- 创建运行用户；
- 上传 JAR；
- 上传前端；
- 编写 systemd；
- 配置外置参数；
- 配置日志；
- 配置开机自启。

验收标准：

- 服务器重启后服务自动恢复；
- systemd 可以启动、停止和重启应用；
- 日志路径正确；
- 健康检查正常。

### 阶段六：Docker 部署

任务：

- 后端 Dockerfile；
- 前端 Dockerfile；
- Compose；
- Docker 网络；
- 环境变量；
- 健康检查；
- 日志查看；
- 容器重启策略。

验收标准：

```bash
docker compose up -d
```

执行后可以直接访问完整系统。

### 阶段七：多实例和负载均衡

任务：

- 启动两个后端；
- 设置不同 instanceId；
- 配置 Nginx upstream；
- 测试轮询；
- 停止一个实例；
- 观察故障切换；
- 验证内存数据不共享。

验收标准：

- 页面能显示当前响应实例；
- 单个后端停止后系统仍可用；
- 请求可以在两个实例间切换。

### 阶段八：更新、回滚和迁移

任务：

- 编写发布脚本；
- 保存历史版本；
- 设计软链接切换；
- 编写回滚脚本；
- 导出 Docker 镜像或重新构建；
- 将系统迁移到另一台服务器。

验收标准：

- 可以从 `1.0.1` 回滚至 `1.0.0`；
- 新服务器只需安装必要运行环境即可部署；
- 项目不依赖旧服务器中的数据库和 Redis。

---

## 20. Git 版本规划

建议使用以下标签：

| 标签 | 内容 |
|---|---|
| `v0.1.0` | 项目初始化 |
| `v0.2.0` | 后端接口完成 |
| `v0.3.0` | 前端页面完成 |
| `v0.4.0` | Nginx 部署完成 |
| `v0.5.0` | 原生部署完成 |
| `v0.6.0` | Docker 部署完成 |
| `v0.7.0` | 负载均衡完成 |
| `v1.0.0` | 文档与实验全部完成 |

每个部署阶段都打一个 Git 标签，方便对比和回滚。

---

## 21. 最终交付物

项目完成时应包含：

### 源代码

- Vue 前端源码；
- Spring Boot 后端源码；
- 单元测试；
- 接口测试。

### 部署文件

- Nginx 配置；
- systemd 服务文件；
- 后端 Dockerfile；
- 前端 Dockerfile；
- Docker Compose；
- 负载均衡配置；
- 构建脚本；
- 部署脚本；
- 回滚脚本；
- 冒烟测试脚本。

### 项目文档

- 本地开发说明；
- 接口说明；
- Nginx 配置说明；
- 原生部署说明；
- Docker 部署说明；
- 多实例部署说明；
- 更新和回滚说明；
- 服务器迁移说明；
- 常见故障排查说明。

---

## 22. 第一版完成标准

Deploy Lab `1.0.0` 需要同时满足以下条件：

- 前后端源码相互独立；
- 前端能够构建为静态资源；
- 后端能够构建为可执行 JAR；
- 不依赖 MySQL、Redis 等外部服务；
- 临时数据保存在内存；
- Nginx 能够提供前端页面；
- Nginx 能够代理后端接口；
- Vue 路由刷新不会出现 404；
- 后端能够识别代理请求头；
- systemd 可以管理后端；
- Docker Compose 可以启动完整项目；
- Actuator 健康检查可用；
- 可以启动两个后端实例；
- Nginx 可以进行负载均衡；
- 停止一个实例后服务仍能访问；
- 可以完成一次版本更新；
- 可以完成一次版本回滚；
- README 中的步骤可以从零复现部署过程。

---

## 23. 后续扩展方向

完成第一版后，可以按照学习价值逐步增加以下功能。

### 优先级一：SSE 实时请求日志

后端收到请求后，通过 SSE 实时推送到前端。

可以学习：

- 长连接；
- Nginx 代理缓冲；
- 连接超时；
- 容器重启后的连接恢复。

### 优先级二：文件上传检测

只读取文件信息，不持久化。

可以学习：

- Multipart；
- Nginx 上传大小限制；
- 上传超时；
- 临时目录；
- Docker 文件系统。

### 优先级三：蓝绿部署实验

同时运行：

```text
backend-blue
backend-green
```

通过切换 Nginx upstream 完成版本切换。

可以学习：

- 无停机更新；
- 配置检查；
- Nginx reload；
- 快速回滚。

### 优先级四：简单访问保护

增加一个固定管理令牌，不实现完整用户系统。

可以学习：

- 请求头认证；
- Nginx 路径保护；
- 环境变量注入；
- Secret 管理。

### 优先级五：自动化构建

使用 GitHub Actions：

```text
提交代码
   ↓
运行测试
   ↓
构建前端
   ↓
构建 JAR
   ↓
构建 Docker 镜像
```

在掌握手工部署之前，不建议直接开始 CI/CD。先手工完成一次完整部署，才能理解自动化流程实际替代了哪些步骤。

---

## 24. 推荐实施顺序

整个项目应严格按照以下顺序推进：

```text
后端系统信息接口
        ↓
后端请求检查接口
        ↓
内存消息 CRUD
        ↓
前端页面
        ↓
本地联调
        ↓
Nginx 静态资源
        ↓
Nginx 反向代理
        ↓
Linux 原生部署
        ↓
systemd
        ↓
HTTPS
        ↓
后端 Docker 化
        ↓
前端 Docker 化
        ↓
Docker Compose
        ↓
双后端实例
        ↓
Nginx 负载均衡
        ↓
版本更新与回滚
        ↓
服务器迁移
```

不要一开始就同时编写 Docker、Nginx、systemd 和业务代码。每完成一个阶段，都应保证项目处于可运行、可验证、可回滚的状态。
