USE sa_token;

-- 先执行 sa-token.sql 初始化表结构。本脚本只执行一次。
-- 执行前确认用户 ID 10002、10003 和用户名 user1、user2 未被占用。
-- 两个练习账号的登录密码均为 123456，使用现有 BCrypt 密文。
-- 不分配角色，因此两个账号均无角色权限。
INSERT INTO sys_user
(id, username, password, nickname, status, created_by, created_at, updated_by, updated_at, is_deleted)
VALUES (10002, 'user1', '$2a$10$2unzADv2gelCwKWCiF5b3evYsQTMObWpGIfQTNjPVsZr1cdeAB9ou', '普通用户1', 1,
        10001, CURRENT_TIMESTAMP, 10001, CURRENT_TIMESTAMP, 0),
       (10003, 'user2', '$2a$10$2unzADv2gelCwKWCiF5b3evYsQTMObWpGIfQTNjPVsZr1cdeAB9ou', '普通用户2', 1,
        10001, CURRENT_TIMESTAMP, 10001, CURRENT_TIMESTAMP, 0);

-- 核对新增账号。
SELECT id, username, nickname, status, is_deleted
FROM sys_user
WHERE id IN (10002, 10003)
ORDER BY id;
