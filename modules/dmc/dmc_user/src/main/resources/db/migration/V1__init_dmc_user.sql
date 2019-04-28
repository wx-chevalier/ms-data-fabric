CREATE TABLE `sec_permission` (
  `id`              VARCHAR(36)  NOT NULL,
  `create_datetime` DATETIME     NOT NULL         DEFAULT NOW(),
  `modify_datetime` DATETIME     NOT NULL         DEFAULT NOW() ON UPDATE NOW(),
  `permission`      VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `sec_permission__indexes` (`permission`) USING BTREE
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COMMENT ='权限表';

CREATE TABLE `sec_role` (
  `id`              VARCHAR(36) NOT NULL,
  `create_datetime` DATETIME    NOT NULL         DEFAULT NOW(),
  `modify_datetime` DATETIME    NOT NULL         DEFAULT NOW() ON UPDATE NOW(),
  `role`            VARCHAR(36) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `sec_role__indexes` (`role`) USING BTREE
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COMMENT ='角色表';

CREATE TABLE `sec_rolepermission` (
  `id`              VARCHAR(36) NOT NULL,
  `create_datetime` DATETIME    NOT NULL         DEFAULT NOW(),
  `modify_datetime` DATETIME    NOT NULL         DEFAULT NOW() ON UPDATE NOW(),
  `role_id`         VARCHAR(36) NOT NULL,
  `permission_id`   VARCHAR(36) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `sec_rolepermission__indexes` (`role_id`, `permission_id`) USING BTREE
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COMMENT ='角色权限表';

CREATE TABLE `sec_user` (
  `id`              VARCHAR(36)  NOT NULL,
  `create_datetime` DATETIME     NOT NULL  DEFAULT NOW(),
  `modify_datetime` DATETIME     NOT NULL  DEFAULT NOW() ON UPDATE NOW(),
  `username`        VARCHAR(36)  NOT NULL,
  `password`        VARCHAR(254) NOT NULL,
  `passsalt`        VARCHAR(254) NOT NULL,
  `status`          TINYINT(1)             DEFAULT 1,
  PRIMARY KEY (`id`),
  KEY `sec_user__indexes` (`username`, `password`, `passsalt`) USING BTREE
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COMMENT ='用户表';

CREATE TABLE `sec_userpermission` (
  `id`              VARCHAR(36) NOT NULL,
  `create_datetime` DATETIME    NOT NULL DEFAULT NOW(),
  `modify_datetime` DATETIME             DEFAULT NOW() ON UPDATE NOW(),
  `user_id`         VARCHAR(36) NOT NULL,
  `permission_id`   VARCHAR(36) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `sec_userpermission__indexes` (`user_id`, `permission_id`) USING BTREE
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COMMENT ='用户权限表';

CREATE TABLE `sec_userrole` (
  `id`              VARCHAR(36) NOT NULL,
  `create_datetime` DATETIME    NOT NULL DEFAULT NOW(),
  `modify_datetime` DATETIME             DEFAULT NOW() ON UPDATE NOW(),
  `user_id`         VARCHAR(36) NOT NULL,
  `role_id`         VARCHAR(36) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `sec_userrole__indexes` (`user_id`, `role_id`) USING BTREE
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COMMENT ='用户角色表';

