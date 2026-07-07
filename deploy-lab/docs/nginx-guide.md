# Nginx 实验说明

## 静态资源

```nginx
location / {
    root /opt/deploy-lab/frontend;
    index index.html;
}
```

## SPA 回退

```nginx
location / {
    try_files $uri $uri/ /index.html;
}
```

## 反向代理

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

## 负载均衡

```nginx
upstream deploy_lab_backend {
    server 127.0.0.1:8081;
    server 127.0.0.1:8082;
}
```

刷新 Dashboard 时观察实例编号是否变化。因为消息存储在内存中，不同实例之间的数据不会共享。
