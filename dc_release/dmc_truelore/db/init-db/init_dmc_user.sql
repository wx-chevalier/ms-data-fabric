create database dmc_user default character set utf8mb4 collate utf8mb4_unicode_ci;

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

USE dmc_user;


/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;

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
  -- ACCOUNT: user:view/create
  ('BBD2CCE4-14BF-4206-A549-00A8EF634DD7', 'f7fc4beb-6fa3-47d0-8303-06c82b56a929', '4A466354-82BA-4EA7-9CB2-C777D1BF4A6F'),
  ('D4A2197F-9EAC-4992-8178-BF65B66E9034', 'f7fc4beb-6fa3-47d0-8303-06c82b56a929', '840F53DD-DAD9-4EE8-B909-8C39785F7D85'),
  -- MANAGER: user:view, api:create/view
  ('804582E7-E4C0-43D1-A489-9D4BCA541D8A', 'FE4C6055-6C53-4151-B100-25262F849E9F', '4A466354-82BA-4EA7-9CB2-C777D1BF4A6F'),
  ('1374C798-19B7-4798-BF93-C81C4F5A1434', 'FE4C6055-6C53-4151-B100-25262F849E9F', '57BC950F-9FA6-4CFD-BA1D-1C559A364907'),
  ('0E4009D3-BE21-4723-9688-F3B138915D00', 'FE4C6055-6C53-4151-B100-25262F849E9F', 'D6F7BF0B-02A4-4778-93E0-6FCA448444DF'),
  -- CLIENT: api:view
  ('67E6D72F-9036-46D4-91CB-F022F59CE5BA', 'A56AD320-EF8F-46FC-873A-61765D15C8DF', 'D6F7BF0B-02A4-4778-93E0-6FCA448444DF');


-- 密码：Account@truelore2018
INSERT INTO dmc_user.sec_user  VALUES ('6e4e5a09-3a36-441d-8a04-b810ee17b3f9', '2018-07-16 09:20:47', '2018-07-16 09:24:41', 'account', '$2a$10$XC5adiRtONTzOPf8RrWjwOVbbUsh4ui6H4WyMjR5aV/aa7Q64OSyW', '123', 'ALLOWABLE', '+86 13600000000', 'http://imglf1.ph.126.net/AfkWJLHu3jfgTTxmfWfTYQ==/6630861057513359518.gif');
-- 密码：Manager@truelore2018
INSERT INTO dmc_user.sec_user  VALUES ('6b81710b-5677-4743-b141-c6510c35593e', '2018-07-16 09:20:47', '2018-07-16 09:24:41', 'manager', '$2a$10$xLsLz2k7Lln4RJQq9zfxa.lBIpyyws9cKl1LrlyO.KtZ4lbyHm5CK', '123', 'ALLOWABLE', '+86 13600000000', 'http://imglf1.ph.126.net/AfkWJLHu3jfgTTxmfWfTYQ==/6630861057513359518.gif');


INSERT INTO `sec_userrole` (`id`, `user_id`, `role_id`) VALUES
        ('ED44A950-2D8C-403E-88AA-E02858D26FAB', '69a9edb6-76f7-486f-8d4f-ce12de973363', '6EE0F98E-2F06-4974-B176-C72C9D925029'),
        ('080E9737-BD09-4906-A3BF-0CD50D0F5AE2', '6b81710b-5677-4743-b141-c6510c35593e', 'FE4C6055-6C53-4151-B100-25262F849E9F'),
        ('1234B0E5-D389-40AF-B59B-832D7C9F85AF', '6e4e5a09-3a36-441d-8a04-b810ee17b3f9', 'f7fc4beb-6fa3-47d0-8303-06c82b56a929');


/*!40101 SET character_set_client = @saved_cs_client */;


/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
