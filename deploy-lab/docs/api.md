# API 说明

统一响应：

```json
{
  "code": 0,
  "message": "success",
  "data": {},
  "traceId": "request-id",
  "timestamp": "2026-07-07T16:00:00Z"
}
```

响应头：

- `X-Request-Id`
- `X-Instance-Id`
- `X-App-Version`

## 系统信息

`GET /api/system/info`

返回应用版本、实例编号、环境、启动时间、运行时长、主机、端口、JVM 内存和诊断开关。

## 请求检查

`GET|POST|PUT|DELETE /api/http/inspect`

返回后端实际收到的 method、URI、query、请求头、请求体、客户端 IP、转发头、实例编号和接收时间。

敏感请求头会脱敏：

- `Authorization`
- `Cookie`
- `Set-Cookie`
- `Proxy-Authorization`

## 临时消息

- `GET /api/messages?limit=50`
- `POST /api/messages`
- `GET /api/messages/{id}`
- `DELETE /api/messages/{id}`
- `DELETE /api/messages`

消息只存内存，支持过期时间和定时清理。

## 请求记录

- `GET /api/request-records?limit=100`
- `DELETE /api/request-records`

只记录请求元数据。

## 诊断

- `GET /api/diagnostics/delay?milliseconds=1000`
- `GET /api/diagnostics/status?code=503`
- `GET /api/diagnostics/error`

延迟最大 5000ms，状态码只允许 `200、400、401、403、404、429、500、502、503、504`。
