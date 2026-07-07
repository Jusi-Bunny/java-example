# Deploy Lab Backend

Spring Boot 后端，数据全部保存在 JVM 内存中，重启后清空。

## 运行

```bash
mvn spring-boot:run
```

可用环境变量：

```bash
SERVER_PORT=8080
APP_INSTANCE_ID=backend-local-01
APP_VERSION=1.0.0
DIAGNOSTICS_ENABLED=true
MAX_MESSAGES=200
MAX_REQUEST_RECORDS=100
```

## 打包

```bash
mvn clean package
java -jar target/deploy-lab.jar
```
