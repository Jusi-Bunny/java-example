CREATE DATABASE IF NOT EXISTS sa_token DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;

USE sa_token;

-- ============================================
-- 1. 核心表结构
-- ============================================

-- 用户表
CREATE TABLE IF NOT EXISTS sys_user
(
    id         BIGINT       NOT NULL COMMENT '用户 ID',
    username   VARCHAR(64)  NOT NULL COMMENT '用户名',
    password   VARCHAR(255) NOT NULL COMMENT '密码',
    nickname   VARCHAR(64)           DEFAULT NULL COMMENT '昵称',
    status     TINYINT      NOT NULL DEFAULT 1 COMMENT '状态：1启用，0禁用',
    created_by BIGINT                DEFAULT NULL COMMENT '创建人 ID',
    created_at DATETIME              DEFAULT NULL COMMENT '创建时间',
    updated_by BIGINT                DEFAULT NULL COMMENT '修改人 ID',
    updated_at DATETIME              DEFAULT NULL COMMENT '修改时间',
    is_deleted TINYINT      NOT NULL DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除',
    PRIMARY KEY (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT '用户表';

-- 角色表
CREATE TABLE IF NOT EXISTS sys_role
(
    id          BIGINT       NOT NULL COMMENT '角色ID',
    role_code   VARCHAR(50)  NOT NULL UNIQUE COMMENT '角色编码',
    role_name   VARCHAR(100) NOT NULL COMMENT '角色名称',
    description VARCHAR(500) COMMENT '角色描述',
    status      TINYINT               DEFAULT 1 COMMENT '状态: 0-禁用, 1-启用',
    created_by  BIGINT                DEFAULT NULL COMMENT '创建人 ID',
    created_at  DATETIME              DEFAULT NULL COMMENT '创建时间',
    updated_by  BIGINT                DEFAULT NULL COMMENT '修改人 ID',
    updated_at  DATETIME              DEFAULT NULL COMMENT '修改时间',
    is_deleted  TINYINT      NOT NULL DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除',
    PRIMARY KEY (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT '角色表';

-- 权限表
CREATE TABLE IF NOT EXISTS sys_permission
(
    id              BIGINT       NOT NULL COMMENT '权限 ID',
    permission_code VARCHAR(100) NOT NULL UNIQUE COMMENT '权限编码',
    permission_name VARCHAR(100) NOT NULL COMMENT '权限名称',
    permission_type TINYINT      NOT NULL COMMENT '权限类型: 1-菜单, 2-按钮, 3-接口, 4-数据',
    parent_id       BIGINT                DEFAULT 0 COMMENT '父权限 ID',
    path            VARCHAR(200) COMMENT '前端路由路径/接口路径',
    component       VARCHAR(200) COMMENT '前端组件路径',
    icon            VARCHAR(50) COMMENT '图标',
    sort            INT                   DEFAULT 0 COMMENT '排序',
    status          TINYINT               DEFAULT 1 COMMENT '状态: 0-禁用, 1-启用',
    created_by      BIGINT                DEFAULT NULL COMMENT '创建人 ID',
    created_at      DATETIME              DEFAULT NULL COMMENT '创建时间',
    updated_by      BIGINT                DEFAULT NULL COMMENT '修改人 ID',
    updated_at      DATETIME              DEFAULT NULL COMMENT '修改时间',
    is_deleted      TINYINT      NOT NULL DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除',
    PRIMARY KEY (id),
    KEY idx_permission_parent_id (parent_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT '权限表';

-- ============================================
-- 2. 关系映射表
-- ============================================

-- 用户角色关联表
CREATE TABLE IF NOT EXISTS sys_user_role
(
    id         BIGINT  NOT NULL COMMENT '主键 ID',
    user_id    BIGINT  NOT NULL COMMENT '用户 ID',
    role_id    BIGINT  NOT NULL COMMENT '角色 ID',
    created_by BIGINT           DEFAULT NULL COMMENT '创建人 ID',
    created_at DATETIME         DEFAULT NULL COMMENT '创建时间',
    updated_by BIGINT           DEFAULT NULL COMMENT '修改人 ID',
    updated_at DATETIME         DEFAULT NULL COMMENT '修改时间',
    is_deleted TINYINT NOT NULL DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除',
    PRIMARY KEY (id),
    KEY idx_user_role_user_id (user_id),
    KEY idx_user_role_role_id (role_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT '用户角色关联表';

-- 角色权限关联表
CREATE TABLE IF NOT EXISTS sys_role_permission
(
    id            BIGINT  NOT NULL COMMENT '主键 ID',
    role_id       BIGINT  NOT NULL COMMENT '角色 ID',
    permission_id BIGINT  NOT NULL COMMENT '权限 ID',
    created_by    BIGINT           DEFAULT NULL COMMENT '创建人 ID',
    created_at    DATETIME         DEFAULT NULL COMMENT '创建时间',
    updated_by    BIGINT           DEFAULT NULL COMMENT '修改人 ID',
    updated_at    DATETIME         DEFAULT NULL COMMENT '修改时间',
    is_deleted    TINYINT NOT NULL DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除',
    PRIMARY KEY (id),
    KEY idx_role_permission_role_id (role_id),
    KEY idx_role_permission_permission_id (permission_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT '角色权限关联表';

-- ============================================
-- 3. 初始化数据
-- ============================================

-- 初始化管理员用户（登录密码：123456）
INSERT IGNORE INTO sys_user
(id, username, password, nickname, status, created_by, created_at, updated_by, updated_at, is_deleted)
VALUES (10001, 'admin', '$2a$10$2unzADv2gelCwKWCiF5b3evYsQTMObWpGIfQTNjPVsZr1cdeAB9ou', '管理员', 1,
        10001, CURRENT_TIMESTAMP, 10001, CURRENT_TIMESTAMP, 0);

-- 初始化角色
INSERT IGNORE INTO sys_role
(id, role_code, role_name, description, status, created_by, created_at, updated_by, updated_at, is_deleted)
VALUES (1001, 'admin', '管理员', '系统管理员角色', 1, 10001, CURRENT_TIMESTAMP, 10001, CURRENT_TIMESTAMP, 0),
       (1002, 'super-admin', '超级管理员', '系统超级管理员角色', 1, 10001, CURRENT_TIMESTAMP, 10001, CURRENT_TIMESTAMP,
        0);

-- 初始化权限，不初始化 user.delete，用于演示无权限场景
INSERT IGNORE INTO sys_permission
(id, permission_code, permission_name, permission_type, parent_id, path, component, icon, sort, status,
 created_by, created_at, updated_by, updated_at, is_deleted)
VALUES (101, '101', '示例权限', 3, 0, NULL, NULL, NULL, 1, 1,
        10001, CURRENT_TIMESTAMP, 10001, CURRENT_TIMESTAMP, 0),
       (102, 'user.add', '新增用户', 3, 0, NULL, NULL, NULL, 2, 1,
        10001, CURRENT_TIMESTAMP, 10001, CURRENT_TIMESTAMP, 0),
       (103, 'user.update', '修改用户', 3, 0, NULL, NULL, NULL, 3, 1,
        10001, CURRENT_TIMESTAMP, 10001, CURRENT_TIMESTAMP, 0),
       (104, 'user.get', '查看用户', 3, 0, NULL, NULL, NULL, 4, 1,
        10001, CURRENT_TIMESTAMP, 10001, CURRENT_TIMESTAMP, 0),
       (105, 'art.*', '文章全部权限', 3, 0, NULL, NULL, NULL, 5, 1,
        10001, CURRENT_TIMESTAMP, 10001, CURRENT_TIMESTAMP, 0);

-- 为管理员用户分配角色
INSERT IGNORE INTO sys_user_role
(id, user_id, role_id, created_by, created_at, updated_by, updated_at, is_deleted)
VALUES (10001, 10001, 1001, 10001, CURRENT_TIMESTAMP, 10001, CURRENT_TIMESTAMP, 0),
       (10002, 10001, 1002, 10001, CURRENT_TIMESTAMP, 10001, CURRENT_TIMESTAMP, 0);

-- 为管理员角色分配权限
INSERT IGNORE INTO sys_role_permission
(id, role_id, permission_id, created_by, created_at, updated_by, updated_at, is_deleted)
VALUES (10001, 1001, 101, 10001, CURRENT_TIMESTAMP, 10001, CURRENT_TIMESTAMP, 0),
       (10002, 1001, 102, 10001, CURRENT_TIMESTAMP, 10001, CURRENT_TIMESTAMP, 0),
       (10003, 1001, 103, 10001, CURRENT_TIMESTAMP, 10001, CURRENT_TIMESTAMP, 0),
       (10004, 1001, 104, 10001, CURRENT_TIMESTAMP, 10001, CURRENT_TIMESTAMP, 0),
       (10005, 1001, 105, 10001, CURRENT_TIMESTAMP, 10001, CURRENT_TIMESTAMP, 0);
