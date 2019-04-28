create database dmc_user default character set utf8mb4 collate utf8mb4_unicode_ci;
USE dmc_user;

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

ALTER TABLE `sec_user`
  ADD COLUMN `phone_number` VARCHAR(32) COMMENT '企业用户手机号码',
  ADD COLUMN `business_license` VARCHAR(255) COMMENT '企业营业执照资料地址',
  MODIFY COLUMN `status` VARCHAR(16) COMMENT '当前用户状态';

INSERT INTO `sec_role`(`id`, `role`) VALUES
        ('6EE0F98E-2F06-4974-B176-C72C9D925029', 'ADMIN'),
        ('f7fc4beb-6fa3-47d0-8303-06c82b56a929', 'ACCOUNT'),
        ('A56AD320-EF8F-46FC-873A-61765D15C8DF', 'CLIENT'),
        ('FE4C6055-6C53-4151-B100-25262F849E9F', 'MANAGER');

INSERT INTO `sec_permission` (`id`, `permission`) VALUES
        ('840F53DD-DAD9-4EE8-B909-8C39785F7D85', 'user:create'),
        ('4A466354-82BA-4EA7-9CB2-C777D1BF4A6F', 'user:view'),
        ('57BC950F-9FA6-4CFD-BA1D-1C559A364907', 'api:create'),
        ('D6F7BF0B-02A4-4778-93E0-6FCA448444DF', 'api:view');

INSERT INTO `sec_rolepermission` (`id`, `role_id`, `permission_id`)
VALUES
  -- ADMIN: all permissions
  ('BBD2CCE4-14BF-4206-A549-00A8EF634DD7', '6EE0F98E-2F06-4974-B176-C72C9D925029', '4A466354-82BA-4EA7-9CB2-C777D1BF4A6F'),
  ('D4A2197F-9EAC-4992-8178-BF65B66E9034', '6EE0F98E-2F06-4974-B176-C72C9D925029', '4A466354-82BA-4EA7-9CB2-C777D1BF4A6F'),
  ('35C7632D-9595-47DE-92D0-672C4527AD28', '6EE0F98E-2F06-4974-B176-C72C9D925029', '57BC950F-9FA6-4CFD-BA1D-1C559A364907'),
  ('7CFFB990-B55E-4B3F-B9CD-B259EE48E9F0', '6EE0F98E-2F06-4974-B176-C72C9D925029', 'D6F7BF0B-02A4-4778-93E0-6FCA448444DF'),
  -- MANAGER: user:view, api:create/view
  ('804582E7-E4C0-43D1-A489-9D4BCA541D8A', 'FE4C6055-6C53-4151-B100-25262F849E9F', '4A466354-82BA-4EA7-9CB2-C777D1BF4A6F'),
  ('1374C798-19B7-4798-BF93-C81C4F5A1434', 'FE4C6055-6C53-4151-B100-25262F849E9F', '57BC950F-9FA6-4CFD-BA1D-1C559A364907'),
  ('0E4009D3-BE21-4723-9688-F3B138915D00', 'FE4C6055-6C53-4151-B100-25262F849E9F', 'D6F7BF0B-02A4-4778-93E0-6FCA448444DF'),
  -- CLIENT: api:view
  ('67E6D72F-9036-46D4-91CB-F022F59CE5BA', 'A56AD320-EF8F-46FC-873A-61765D15C8DF', 'D6F7BF0B-02A4-4778-93E0-6FCA448444DF');

INSERT INTO dmc_user.sec_user  VALUES ('69a9edb6-76f7-486f-8d4f-ce12de973363', '2018-07-16 09:20:47', '2018-07-16 09:24:41', 'admin', '123', '123', 'ALLOWABLE', '15556353237', 'http://imglf1.ph.126.net/AfkWJLHu3jfgTTxmfWfTYQ==/6630861057513359518.gif');
INSERT INTO dmc_user.sec_user  VALUES ('6e4e5a09-3a36-441d-8a04-b810ee17b3f9', '2018-07-16 09:20:47', '2018-07-16 09:24:41', 'account', '123', '123', 'ALLOWABLE', '15556353237', 'http://imglf1.ph.126.net/AfkWJLHu3jfgTTxmfWfTYQ==/6630861057513359518.gif');
INSERT INTO dmc_user.sec_user  VALUES ('6b81710b-5677-4743-b141-c6510c35593e', '2018-07-16 09:20:47', '2018-07-16 09:24:41', 'manager', '123', '123', 'ALLOWABLE', '15556353237', 'http://imglf1.ph.126.net/AfkWJLHu3jfgTTxmfWfTYQ==/6630861057513359518.gif');
INSERT INTO dmc_user.sec_user  VALUES ('6defc1a2-3724-4494-84d2-2ff347a212fd', '2018-07-16 09:20:47', '2018-07-16 09:24:41', 'qy1', '123', '123', 'ALLOWABLE', '15556353237', 'http://imglf1.ph.126.net/AfkWJLHu3jfgTTxmfWfTYQ==/6630861057513359518.gif');
INSERT INTO dmc_user.sec_user  VALUES ('98545636-db12-4f47-8463-75c847999dea', '2018-07-16 09:20:47', '2018-07-16 09:24:41', 'qy2', '123', '123', 'ALLOWABLE', '15556353237', 'http://imglf1.ph.126.net/AfkWJLHu3jfgTTxmfWfTYQ==/6630861057513359518.gif');
INSERT INTO dmc_user.sec_user  VALUES ('b8bf3580-a173-43dc-b163-f86829327ae1', '2018-07-16 09:20:47', '2018-07-16 09:24:41', 'qy3', '123', '123', 'ALLOWABLE', '15556353237', 'http://imglf1.ph.126.net/AfkWJLHu3jfgTTxmfWfTYQ==/6630861057513359518.gif');
INSERT INTO dmc_user.sec_user  VALUES ('02a4e4f2-17fa-4ddb-82f2-8102a4eda2dd', '2018-07-16 09:20:47', '2018-07-16 09:24:41', 'test1', '123', '123', 'CHECKED', '15556353237', 'http://imglf1.ph.126.net/AfkWJLHu3jfgTTxmfWfTYQ==/6630861057513359518.gif');
INSERT INTO dmc_user.sec_user  VALUES ('89ef6d9f-8124-4f40-897d-d13ac16c1cc5', '2018-07-16 09:20:47', '2018-07-16 09:24:41', 'test2', '123', '123', 'ABANDONED', '15556353237', 'http://imglf1.ph.126.net/AfkWJLHu3jfgTTxmfWfTYQ==/6630861057513359518.gif');

-- 更新密码
-- 密码为123
UPDATE `sec_user` SET  `password` = '$2a$10$NQuf8uM3FIyyMAaqEiijU.2xG6Gxwl0ullQ0t8dpvGRyvvW/I5KzC';

INSERT INTO `sec_userrole` (`id`, `user_id`, `role_id`) VALUES
        ('ED44A950-2D8C-403E-88AA-E02858D26FAB', '69a9edb6-76f7-486f-8d4f-ce12de973363', '6EE0F98E-2F06-4974-B176-C72C9D925029'),
        ('080E9737-BD09-4906-A3BF-0CD50D0F5AE2', '6b81710b-5677-4743-b141-c6510c35593e', 'FE4C6055-6C53-4151-B100-25262F849E9F'),
        ('4694FB8D-936A-42DD-BADB-D720CFE16C68', '6defc1a2-3724-4494-84d2-2ff347a212fd', 'A56AD320-EF8F-46FC-873A-61765D15C8DF'),
        ('B4D1B0E5-D389-40AF-B59B-832D7C9F85AF', '98545636-db12-4f47-8463-75c847999dea', 'A56AD320-EF8F-46FC-873A-61765D15C8DF'),
        ('1234B0E5-D389-40AF-B59B-832D7C9F85AF', '6e4e5a09-3a36-441d-8a04-b810ee17b3f9', 'f7fc4beb-6fa3-47d0-8303-06c82b56a929'),
        ('53429E1D-89C6-413C-ADAE-A3208B68100D', 'b8bf3580-a173-43dc-b163-f86829327ae1', 'A56AD320-EF8F-46FC-873A-61765D15C8DF');
