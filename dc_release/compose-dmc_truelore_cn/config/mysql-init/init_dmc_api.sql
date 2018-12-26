create database dmc_api default character set utf8mb4 collate utf8mb4_unicode_ci;

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

USE dmc_api;


/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `dc_active_record` (
  `id`              VARCHAR(36) NOT NULL
  COMMENT '记录ID',
  `path`            VARCHAR(255) DEFAULT NULL
  COMMENT '请求路径',
  `operation`       VARCHAR(8)   DEFAULT NULL
  COMMENT '请求方式',
  `info`            TEXT        NOT NULL
  COMMENT '操作日志信息',
  `access_datetime` DATETIME     DEFAULT CURRENT_TIMESTAMP
  COMMENT '记录日期',
  `access_userid`   VARCHAR(36)  DEFAULT NULL
  COMMENT '访问用户ID',
  `remark`          TEXT COMMENT '备注信息',
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COMMENT ='用户API权限变动记录表';

CREATE TABLE `dc_api` (
  `id`              VARCHAR(36)  NOT NULL
  COMMENT '接口资源ID',
  `api_name`        VARCHAR(255) NOT NULL
  COMMENT '接口名称',
  `api_operation`   VARCHAR(16)  NOT NULL
  COMMENT '接口请求方式',
  `api_type`        VARCHAR(36)  NOT NULL
  COMMENT '接口类型',
  `api_protocol`    VARCHAR(8)   NOT NULL DEFAULT 'HTTP'
  COMMENT '接口请求协议',
  `api_path`        VARCHAR(512)          DEFAULT NULL
  COMMENT '外部接口地址,内部请求时候为NULL',
  `api_summary`     VARCHAR(512)          DEFAULT NULL
  COMMENT '接口综述',
  `project_id`      VARCHAR(36)  NOT NULL
  COMMENT '所属项目ID',
  `folder_id`       VARCHAR(36)           DEFAULT NULL
  COMMENT '所属组别ID',
  `create_datetime` DATETIME              DEFAULT CURRENT_TIMESTAMP
  COMMENT '创建时间',
  `modify_datetime` DATETIME              DEFAULT CURRENT_TIMESTAMP
  COMMENT '修改时间',
  `create_userid`   VARCHAR(36)  NOT NULL
  COMMENT '创建人id',
  `modify_userid`   VARCHAR(36)           DEFAULT NULL
  COMMENT '修改人id',
  `status`          TINYINT(4)            DEFAULT '1'
  COMMENT '是否有效 默认有效',
  PRIMARY KEY (`id`),
  KEY `api_pro_index` (`project_id`),
  KEY `api_folder_index` (`folder_id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COMMENT ='用户的API接口表';

CREATE TABLE `dc_api_doc` (
  `id`              VARCHAR(36)                   NOT NULL,
  `title`           VARCHAR(255) DEFAULT 'noName' NULL,
  `content`         TEXT,
  `content_type`    VARCHAR(32) DEFAULT NULL,
  `create_userid`   VARCHAR(36) DEFAULT NULL,
  `modify_userid`   VARCHAR(36) DEFAULT NULL,
  `create_datetime` DATETIME    DEFAULT NULL,
  `modify_datetime` DATETIME    DEFAULT NULL,
  `status`          TINYINT(4)  DEFAULT NULL,
  `api_id`          VARCHAR(36)                   NOT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE `dc_api_impl` (
  `id`              VARCHAR(36) NOT NULL
  COMMENT 'API实现ID',
  `api_id`          VARCHAR(36) NOT NULL
  COMMENT 'api接口ID',
  `api_impl`        TEXT        NOT NULL
  COMMENT 'API实现',
  `create_datetime` DATETIME    DEFAULT CURRENT_TIMESTAMP
  COMMENT '创建时间',
  `modify_datetime` DATETIME    DEFAULT NULL
  COMMENT '修改时间',
  `create_userid`   VARCHAR(36) DEFAULT NULL
  COMMENT '创建人id',
  `modify_userid`   VARCHAR(36) DEFAULT NULL
  COMMENT '修改人id',
  `status`          TINYINT(4)  DEFAULT '1'
  COMMENT '当前状态，默认有效',
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COMMENT ='接口SQL脚本表';

CREATE TABLE `dc_api_model_definition` (
  `id`               VARCHAR(36)  NOT NULL
  COMMENT '模型定义ID',
  `project_id`       VARCHAR(36)  NOT NULL
  COMMENT 'model所属项目id',
  `model_name`       VARCHAR(255) NOT NULL
  COMMENT '模型名称',
  `model_definition` TEXT         NOT NULL
  COMMENT '模型的实现',
  `model_describe`   TEXT COMMENT '模型描述',
  `create_datetime`  DATETIME    DEFAULT CURRENT_TIMESTAMP
  COMMENT '创建时间',
  `modify_datetime`  DATETIME    DEFAULT NULL
  COMMENT '修改时间',
  `create_userid`    VARCHAR(36) DEFAULT NULL
  COMMENT '创建人id',
  `modify_userid`    VARCHAR(36) DEFAULT NULL
  COMMENT '修改人id',
  PRIMARY KEY (`id`),
  KEY `project_id_index` (`project_id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COMMENT ='模型定义表';

CREATE TABLE `dc_api_param` (
  `id`              VARCHAR(36) NOT NULL
  COMMENT '参数ID',
  `api_id`          VARCHAR(36) NOT NULL
  COMMENT '所属接口ID',
  `param_type`      ENUM ('REQUEST_BODY', 'PARAMETER') DEFAULT NULL,
  `param_model`     TEXT        NOT NULL
  COMMENT '参数模型 https://github.com/OAI/OpenAPI-Specification/blob/master/versions/3.0.1.md#schemaObject',
  `create_datetime` DATETIME                          DEFAULT CURRENT_TIMESTAMP
  COMMENT '创建时间',
  `modify_datetime` DATETIME                          DEFAULT CURRENT_TIMESTAMP
  COMMENT '修改时间',
  `create_userid`   VARCHAR(36) NOT NULL
  COMMENT '创建人id',
  `modify_userid`   VARCHAR(36)                       DEFAULT NULL
  COMMENT '修改人id',
  PRIMARY KEY (`id`),
  KEY `api_id_index` (`api_id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COMMENT ='API参数表';

CREATE TABLE `dc_api_response` (
  `id`              VARCHAR(36) NOT NULL
  COMMENT '返回值ID',
  `api_id`          VARCHAR(36) NOT NULL
  COMMENT '所属接口ID',
  `response_model`  TEXT        NOT NULL
  COMMENT '返回值模型',
  `create_datetime` DATETIME    DEFAULT CURRENT_TIMESTAMP
  COMMENT '创建时间',
  `modify_datetime` DATETIME    DEFAULT CURRENT_TIMESTAMP
  COMMENT '修改时间',
  `create_userid`   VARCHAR(36) NOT NULL
  COMMENT '创建人id',
  `modify_userid`   VARCHAR(36) DEFAULT NULL
  COMMENT '修改人id',
  PRIMARY KEY (`id`),
  KEY `api_id_index` (`api_id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COMMENT ='API返回值表';

CREATE TABLE `dc_data_column` (
  `data_source_id`  VARCHAR(36) DEFAULT NULL
  COMMENT '数据源ID',
  `data_base_name`  VARCHAR(64) DEFAULT NULL
  COMMENT '数据库名称',
  `table_name`      VARCHAR(64) DEFAULT NULL
  COMMENT '表名称',
  `column_name`     VARCHAR(64) DEFAULT NULL
  COMMENT '字段名称',
  `column_type`     VARCHAR(64) DEFAULT 'VARCHAR'
  COMMENT '数据类型',
  `column_comment`  VARCHAR(64) DEFAULT 'VARCHAR'
  COMMENT '数据描述',
  `create_datetime` DATETIME    DEFAULT CURRENT_TIMESTAMP
  COMMENT '创建时间',
  `modify_datetime` DATETIME    DEFAULT NULL
  COMMENT '修改时间',
  `create_userid`   VARCHAR(36) DEFAULT NULL
  COMMENT '创建人id',
  `modify_userid`   VARCHAR(36) DEFAULT NULL
  COMMENT '修改人id',
  KEY `index1` (`data_source_id`, `data_base_name`, `table_name`, `column_name`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COMMENT ='数据库表表';

CREATE TABLE `dc_data_schema` (
  `id`              VARCHAR(36) NOT NULL
  COMMENT '数据库记录主键',
  `data_source_id`  VARCHAR(36) NOT NULL
  COMMENT '数据源ID',
  `database_name`   VARCHAR(64) NOT NULL
  COMMENT '数据库名称',
  `database_encode` VARCHAR(64) DEFAULT 'utf8'
  COMMENT '数据库编码',
  `database_sort`   VARCHAR(64) DEFAULT 'utf8_general_ci'
  COMMENT '数据库排序规则',
  `remark`          TEXT COMMENT '数据库描述',
  `create_datetime` DATETIME    DEFAULT CURRENT_TIMESTAMP
  COMMENT '创建时间',
  `modify_datetime` DATETIME    DEFAULT NULL
  COMMENT '修改时间',
  `create_userid`   VARCHAR(36) DEFAULT NULL
  COMMENT '创建人id',
  `modify_userid`   VARCHAR(36) DEFAULT NULL
  COMMENT '修改人id',
  PRIMARY KEY (`id`),
  UNIQUE KEY `database_name` (`database_name`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COMMENT ='数据库表';

CREATE TABLE `dc_data_source` (
  `id`                 VARCHAR(36)  NOT NULL
  COMMENT '数据源id',
  `data_source_name`   VARCHAR(255) NOT NULL
  COMMENT '数据源名称',
  `create_datetime`    DATETIME    DEFAULT CURRENT_TIMESTAMP
  COMMENT '创建时间',
  `modify_datetime`    DATETIME    DEFAULT NULL
  COMMENT '修改时间',
  `create_userid`      VARCHAR(36) DEFAULT NULL
  COMMENT '创建人id',
  `modify_userid`      VARCHAR(36) DEFAULT NULL
  COMMENT '修改人id',
  `status`             TINYINT(4)  DEFAULT '1'
  COMMENT '是否有效 默认有效',
  `remark`             TEXT COMMENT '备注信息',
  `data_source_type`   VARCHAR(32) DEFAULT NULL,
  `data_source_config` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `data_source_name` (`data_source_name`),
  KEY `data_source_name_index` (`data_source_name`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COMMENT ='数据源表';

CREATE TABLE `dc_data_table` (
  `data_source_id`  VARCHAR(36) NOT NULL
  COMMENT '数据源ID',
  `data_base_name`  VARCHAR(64) NOT NULL
  COMMENT '数据库名称',
  `table_name`      VARCHAR(64) NOT NULL
  COMMENT '表名称',
  `remark`          TEXT COMMENT '表说明',
  `create_datetime` DATETIME    DEFAULT CURRENT_TIMESTAMP
  COMMENT '创建时间',
  `modify_datetime` DATETIME    DEFAULT NULL
  COMMENT '修改时间',
  `create_userid`   VARCHAR(36) DEFAULT NULL
  COMMENT '创建人id',
  `modify_userid`   VARCHAR(36) DEFAULT NULL
  COMMENT '修改人id',
  PRIMARY KEY (`data_source_id`, `data_base_name`, `table_name`),
  KEY `table_name_index` (`table_name`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COMMENT ='数据库表表';

CREATE TABLE `dc_environment` (
  `id`              VARCHAR(36)  NOT NULL
  COMMENT '环境ID',
  `env_name`        VARCHAR(255) NOT NULL
  COMMENT '环境KEY值',
  `env_value`       TEXT         NOT NULL
  COMMENT '环境VALUE值',
  `create_datetime` DATETIME    DEFAULT CURRENT_TIMESTAMP
  COMMENT '创建时间',
  `modify_datetime` DATETIME    DEFAULT NULL
  COMMENT '修改时间',
  `create_userid`   VARCHAR(36) DEFAULT NULL
  COMMENT '创建人id',
  `modify_userid`   VARCHAR(36) DEFAULT NULL
  COMMENT '修改人id',
  `remark`          TEXT COMMENT '备注信息',
  `project_id`      VARCHAR(36)  NOT NULL,
  PRIMARY KEY (`id`),
  KEY `envir_name_index` (`env_name`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COMMENT ='接口环境变量表';

CREATE TABLE `dc_folder` (
  `id`                 VARCHAR(36)  NOT NULL
  COMMENT '目录ID',
  `project_id`         VARCHAR(36)  DEFAULT NULL,
  `folder_name`        VARCHAR(255) NOT NULL
  COMMENT '目录名称',
  `folder_description` VARCHAR(512) DEFAULT NULL
  COMMENT '目录描述',
  `create_datetime`    DATETIME     DEFAULT CURRENT_TIMESTAMP
  COMMENT '创建时间',
  `modify_datetime`    DATETIME     DEFAULT NULL
  COMMENT '修改时间',
  `create_userid`      VARCHAR(36)  DEFAULT NULL
  COMMENT '创建人id',
  `modify_userid`      VARCHAR(36)  DEFAULT NULL
  COMMENT '修改人id',
  `status`             TINYINT(4)   DEFAULT '1'
  COMMENT '是否有效 默认有效',
  `remark`             TEXT COMMENT '目录备注',
  PRIMARY KEY (`id`),
  UNIQUE KEY `folder_name` (`folder_name`),
  KEY `folder_name_index` (`folder_name`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COMMENT ='目录表';

CREATE TABLE `dc_project` (
  `id`                  VARCHAR(36)  NOT NULL
  COMMENT '项目ID',
  `project_name`        VARCHAR(255) NOT NULL
  COMMENT '项目名称',
  `project_description` TEXT COMMENT '项目描述',
  `project_version`     VARCHAR(255) NOT NULL
  COMMENT '项目版本',
  `project_type_id`     VARCHAR(36)  NOT NULL
  COMMENT '项目类型ID',
  `create_datetime`     DATETIME    DEFAULT CURRENT_TIMESTAMP
  COMMENT '创建时间',
  `modify_datetime`     DATETIME    DEFAULT NULL
  COMMENT '修改时间',
  `create_userid`       VARCHAR(36) DEFAULT NULL
  COMMENT '创建人id',
  `modify_userid`       VARCHAR(36) DEFAULT NULL
  COMMENT '修改人id',
  `status`              TINYINT(4)  DEFAULT '1'
  COMMENT '是否有效 默认有效',
  `remark`              TEXT COMMENT '项目备注',
  PRIMARY KEY (`id`),
  UNIQUE KEY `project_name` (`project_name`),
  KEY `project_name_index` (`project_name`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COMMENT ='项目表';

CREATE TABLE `dc_project_doc` (
  `id`              VARCHAR(36)                   NOT NULL,
  `content`         TEXT,
  `title`           VARCHAR(255) DEFAULT 'noName' NULL,
  `content_type`    VARCHAR(32) DEFAULT NULL,
  `create_userid`   VARCHAR(36) DEFAULT NULL,
  `modify_userid`   VARCHAR(36) DEFAULT NULL,
  `create_datetime` DATETIME    DEFAULT NULL,
  `modify_datetime` DATETIME    DEFAULT NULL,
  `status`          TINYINT(4)  DEFAULT NULL,
  `project_id`      VARCHAR(36)                   NOT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE `dc_project_type` (
  `id`               VARCHAR(36)  NOT NULL DEFAULT ''
  COMMENT '项目类型ID',
  `type_name`        VARCHAR(255) NOT NULL
  COMMENT '分组名称',
  `type_description` VARCHAR(255)          DEFAULT NULL
  COMMENT '分组描述',
  `create_datetime`  DATETIME              DEFAULT CURRENT_TIMESTAMP
  COMMENT '创建时间',
  `modify_datetime`  DATETIME              DEFAULT NULL
  COMMENT '修改时间',
  `create_userid`    VARCHAR(36)           DEFAULT NULL
  COMMENT '创建人id',
  `modify_userid`    VARCHAR(36)           DEFAULT NULL
  COMMENT '修改人id',
  `status`           TINYINT(4)            DEFAULT '1'
  COMMENT '是否有效 默认有效',
  PRIMARY KEY (`id`),
  KEY `type_name_index` (`type_name`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COMMENT ='项目类型表';

CREATE TABLE `dc_user_api` (
  `id`              VARCHAR(36) NOT NULL
  COMMENT '主键ID',
  `user_id`         VARCHAR(36) NOT NULL
  COMMENT '用户的id',
  `api_id`          VARCHAR(36) NOT NULL
  COMMENT 'API 接口iD',
  `create_datetime` DATETIME DEFAULT CURRENT_TIMESTAMP
  COMMENT '创建日期',
  `create_userid`   VARCHAR(36) NOT NULL
  COMMENT '创建人id',
  `remark`          TEXT COMMENT '备注信息',
  PRIMARY KEY (`id`),
  KEY `user_id_index` (`user_id`),
  KEY `api_id_index` (`api_id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COMMENT ='用户API权限表';

CREATE TABLE `dc_user_schema`(
  id                 VARCHAR(36)                        NOT NULL
  COMMENT 'ID'
  PRIMARY KEY,
  data_source_id     VARCHAR(64)                        NOT NULL
  COMMENT '数据库ID',
  data_database_name VARCHAR(64)                        NOT NULL,
  user_id            VARCHAR(36)                        NOT NULL
  COMMENT '用户ID',
  create_datetime    DATETIME DEFAULT CURRENT_TIMESTAMP NULL
  COMMENT '创建日期',
  create_userid      VARCHAR(36)                        NOT NULL
  COMMENT '创建人id',
  remark             TEXT                               NULL
  COMMENT '备注信息'
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COMMENT '数据库权限授权表';

CREATE TABLE `dc_route`
  (
    `id`           VARCHAR(36)  NOT NULL
    COMMENT '路由id',
    `project_id`   VARCHAR(36)  NOT NULL
    COMMENT '项目ID',
    `env_id`       VARCHAR(36)  NOT NULL
    COMMENT '资源ID',
    `path`         VARCHAR(255) NOT NULL
    COMMENT '映射入口地址',
    `url`          VARCHAR(512) NOT NULL
    COMMENT '映射目标URL',
    `retryable`    TINYINT(1) DEFAULT '0'
    COMMENT '允许重试',
    `strip_prefix` TINYINT(1) DEFAULT '1'
    COMMENT '移除前缀'
  )
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COMMENT '代理地址映射规则表';

CREATE TABLE `dc_api_statistics` (
  `id`           VARCHAR(36) COMMENT '统计ID',
  `api_id`       VARCHAR(36)  NOT NULL
  COMMENT 'API',
  `user_id`      VARCHAR(36)  NOT NULL
  COMMENT '访问用户',
  `api_path`     VARCHAR(512) NOT NULL
  COMMENT '实际路径',
  `proxy_path`   VARCHAR(512) NOT NULL
  COMMENT '请求代理路径',
  `proxy_method` VARCHAR(32) COMMENT '请求方式',
  `consume_time` INTEGER  DEFAULT 0
  COMMENT '请求消耗时间(单位ms)',
  `status_code`  INTEGER  DEFAULT 200
  COMMENT '响应状态码',
  `access_time`  DATETIME DEFAULT NOW()
  COMMENT '访问时间'
)
  ENGINE = InNoDB
  DEFAULT CHARSET = utf8
  COMMENT 'API接口访问统计表';

CREATE TABLE dc_project_dynamic
  (
    id              VARCHAR(36)                        NOT NULL
    COMMENT '项目动态ID'
    PRIMARY KEY,
    project_id      VARCHAR(36)                        NOT NULL
    COMMENT '所属项目ID',
    type            VARCHAR(32)                        NOT NULL
    COMMENT '项目动态类型',
    content         TEXT                               NULL
    COMMENT '项目动态变动内容',
    create_user     VARCHAR(36)                        NULL
    COMMENT '创建人',
    create_datetime DATETIME DEFAULT CURRENT_TIMESTAMP NULL
    COMMENT '创建时间'
  )
  COMMENT '项目动态表';

 CREATE TABLE IF NOT EXISTS dc_access_token(
   `id` VARCHAR(36) PRIMARY KEY COMMENT '主键',
   `user_id` VARCHAR(36) NOT NULL  COMMENT  '访问用户ID',
   `api_id` VARCHAR(36) NOT NULL  COMMENT '接口ID',
   `env_id` VARCHAR(36) NOT NULL  COMMENT '环境ID',
   `access_token` TEXT NOT NULL COMMENT  'token',
   `create_datetime`  DATETIME DEFAULT  NOW() COMMENT 'token创建时间',
   `modify_datetime`  DATETIME DEFAULT  NULL COMMENT 'token更新时间'
 )COMMENT  '接口访问token授权表';
 ALTER TABLE `dc_access_token` ADD UNIQUE KEY find(`api_id`,`user_id`,`env_id`);


INSERT INTO `dc_project_type` (`id`, `type_name`, `type_description`, `status`)
VALUES
        ('2fc76258-4fa8-4bb6-8107-9060b46307c2', 'OTHERS', '其他项目', 1),
        ('63d461f2-4b61-4808-a0c6-73c246b90f07', 'PC', 'PC项目', 1),
        ('9807cdc5-43f9-4f37-a38d-f26e4bf50d5e', 'WEB', '简单的Web项目', 1),
        ('a19ec328-075e-4114-98a6-fb93fae7cfc0', 'APP', 'APP项目', 1),
        ('adb5711c-4928-41e6-b1da-52aff2d0cfbb', 'IOT', '物联网项目', 1);


-- API
INSERT INTO `dc_api` VALUES ('00737b4d-aa76-43c4-a271-043fa178795c','获取通知公告列表','GET','PROXY','HTTPS','{{PROXY_CENTER}}/{{ANNOUNCES}}/{column_code}/{menu_code}/{category_code}','获取通知公告列表','306197c5-e6b0-4ce3-96a9-3c3b883d037f','','2018-08-17 16:54:18','2018-08-17 17:03:45','6b81710b-5677-4743-b141-c6510c35593e','6b81710b-5677-4743-b141-c6510c35593e',1),('19bddb19-bbbe-4565-a682-4bb160ab9b9a','获取通知公告详情','GET','PROXY','HTTPS','{{PROXY_CENTER}}/{{ANNOUNCE}}/{columnCode}/{uuid}','通过指定的一级目录和通知公告的UUID获取通知公告详情','306197c5-e6b0-4ce3-96a9-3c3b883d037f','','2018-08-17 16:30:38','2018-08-17 16:33:22','6b81710b-5677-4743-b141-c6510c35593e','6b81710b-5677-4743-b141-c6510c35593e',1);


-- APIDOC
INSERT INTO `dc_api_doc` VALUES ('49415e33-43ba-4600-b63f-c720564f00cd',NULL,'# 参数说明：\n| 参数名称 | 参数说明 | 能否为空 |\n|---|-----|----|\n| column_code|一级目录code | 否 |\n| menu_code|二级目录code  | 否 |\n| category_code|三级目录code  | 否 |\n| page|分页（每页20条数据）  | 否 |\n\n# 返回结果：\n| 参数名称 | 参数说明 | \n|---|-----|\n|uuid|唯一主键|\n|title|公告标题|\n|announce_date|公告时间|\n|meta| 列表字段集合|\n|properties|列表字段显示值集合|\n\n> 更多返回参数说明，请参考返回参数模型数据\n\n\n# 附录: 目录名称说明\n\n |   一级目录名称 |   一级目录code |   二级目录名称 |   二级目录code |   三级目录名称 |   三级目录code |\n |   --- |   --- |   -- |   -- |   -- |   -- | \n |   政府采购 |   zfcg |     采购信息 |     67001   |   信息公告 |     67001001 |   \n |   政府采购 |   zfcg |     采购信息 |     67001   |   更正公告 |     67001002 |   \n |   政府采购 |   zfcg |     采购结果 |     67002   |   结果公告 |     67002001 |   \n |   政府采购 |   zfcg |     采购结果 |     67002   |   更正公告 |     67002002 |   \n |   房建市政 |   fjsz |     招标公告 |     68001   |   服务类   |   68001001 |   \n |   房建市政 |   fjsz |     招标公告 |     68001   |   工程类   |   68001002 |   \n |   房建市政 |   fjsz |     招标公告 |     68001   |   货物类   |   68001003 |   \n |   房建市政 |   fjsz |     中标公示 |     68002   |   工程类   |   68002001 |   \n |   房建市政 |   fjsz |     中标公示 |     68002   |   服务类 |     68002002 |   \n |   房建市政 |   fjsz |     中标公告 |     68003   |   服务类 |     68003001 |   \n |   房建市政 |   fjsz |     中标公告 |     68003   |   工程类 |     68003002 |   \n |   房建市政 |   fjsz |     中标公告 |     68003   |   货物类 |     68003003 |   \n |   房建市政 |   fjsz |     招标公告 |     68001   |   小型工程 |     68004001 |   \n |   房建市政 |   fjsz |     中标公告 |     68003   |   小型工程 |     68004002 |   \n |   房建市政 |   fjsz |     其他公示 |     68005   |   负责人变更 |     68005001 |   \n |   房建市政 |   fjsz |     其他公示 |     68005   |   负责人撤出 |     68005002 |   \n |   房建市政 |   fjsz |     其他公示 |     68005   |   两次不足三家 |     68005003 |   \n |   房建市政 |   fjsz |     其他公示 |     68005   |   资审未通过 |     68005004 |   \n |   房建市政 |   fjsz |     其他公示 |     68005   |   放弃投标行为 |     68005006 |   \n |   交通水利 |   jtsw |     交通招标公告 |     69001   |   施工   |   69001001 |   \n |   交通水利 |   jtsw |     交通招标公告 |     69001   |   监理 |     69001002 |   \n |   交通水利 |   jtsw |     交通招标公告 |     69001   |   勘察设计 |     69001003 |   \n |   交通水利 |   jtsw |     交通招标公告 |     69001   |   材料采购 |     69001004 |   \n |   交通水利 |   jtsw |     交通招标公告 |     69001   |   试验检测 |     69001005 |   \n |   交通水利 |   jtsw |     交通中标公示 |     69003   |   中标公示 |     69003 |   \n |   交通水利 |   jtsw |     水利招标公告 |     69005   |   工程类 |     69005001 |   \n |   交通水利 |   jtsw |     水利招标公告 |     69005   |   服务类   |   69005002 |   \n |   交通水利 |   jtsw |     水利招标公告 |     69005   |   货物类   |   69005003 |   \n |   交通水利 |   jtsw |     水利中标公示 |     69006   |   工程类   |   69006001 |   \n |   交通水利 |   jtsw |     水利中标公示 |     69006   |   服务类 |     69006002 |   \n |   交通水利 |   jtsw |     水利中标公示 |     69006   |   货物类 |     69006003 |   \n |   工程货物 |   gchw |   招标公告   |   70001   |   招标公告 |     70001 |   \n |   工程货物 |   gchw |     中标公示 |   70003   |   中标公示 |     70003 |   \n |   国有产权 |   gycq |     企业产权转让 |     71001 |     项目预公告   |   71001001 |   \n |   国有产权 |   gycq |     企业产权转让 |     71001 |     项目公告   |   71001002 |   \n |   国有产权 |   gycq |     企业产权转让 |     71001 |     成交公告   |   71001003 |   \n |   国有产权 |   gycq |     企业增资   |   71002 |     项目公告   |   71002001 |   \n |   国有产权 |   gycq |     企业资产处置 |     71003 |     房产出租项目 |     71003001 |   \n |   国有产权 |   gycq |     企业资产处置 |     71003 |     资产转让项目 |     71003002 |   \n |   国有产权 |   gycq |     企业资产处置 |     71003 |     成交公告   |   71003003 |   \n |   国有产权 |   gycq |     行政事业单位资产处置 |     71004 |     房产出租项目 |     71004001 |   \n |   国有产权 |   gycq |     行政事业单位资产处置 |     71004 |     成交公告   |   71004003 |   \n |   国有产权 |   gycq |     公共资源经营权转让   |   71005   |   广告经营权项目 |     71005001 |   \n |   国有产权 |   gycq |     公共资源经营权转让   |   71005   |   成交公告   |   71005003 |   \n |   国有产权 |   gycq |     排污权出让   |   71006   |   项目公告   |   71006001 |   \n |   国有产权 |   gycq |     排污权出让   |   71006   |   成交公告   |   71006002 |   \n |   国有产权 |   gycq |     商业性户外广告设置使用权出让 |     71007 |     项目公告   |   71007001 |   \n |   国有产权 |   gycq |     商业性户外广告设置使用权出让 |     71007 |     成交公告   |   71007002 |   \n |   土地矿产 |   tdkc |     出让公告   |   72001   |   出让公告   |   72001 |   \n |   土地矿产 |   tdkc |     成交结果公示 |     72002   |   成交结果公示 |     72002 |   \n |   铁路航运 |   tlhy |     招标公告 |     73001 |     施工类 |     73001001 |   \n |   铁路航运 |   tlhy |     招标公告 |     73001 |     监理类 |     73001002 |   \n |   铁路航运 |   tlhy |     招标公告 |     73001 |     物资类 |     73001003 |   \n |   铁路航运 |   tlhy |     招标公告 |     73001 |     咨询类 |     73001004 |   \n |   铁路航运 |   tlhy |     招标公告 |     73001 |     检测类 |     73001005 |   \n |   铁路航运 |   tlhy |     中标公示 |     73002 |     施工类 |     73002001 |   \n |   铁路航运 |   tlhy |     中标公示 |     73002 |     监理类 |     73002002 |   \n |   铁路航运 |   tlhy |     中标公示 |     73002 |     物资类 |     73002003 |   \n |   铁路航运 |   tlhy |     中标公示 |     73002 |     咨询类 |     73002004 |   \n |   铁路航运 |   tlhy |     中标公示 |     73002 |     检测类 |     73002005 |',NULL,'6b81710b-5677-4743-b141-c6510c35593e',NULL,'2018-08-17 17:03:45','2018-08-17 17:03:45',1,'00737b4d-aa76-43c4-a271-043fa178795c'),('d604d8dc-ce41-4688-aa85-acf94f70acdd',NULL,'## 参数说明\n| 参数名称 | 参数说明 | 能否为空 |\n|----|----|----|\n|column_code|一级目录code|否|\n|uuid|公告唯一主键|否|\n\n## 返回结果\ncontent：内容详情\n \n> 更多返回结果请参考返回参数模型 \n\n## 附录：一级目录代码表\n\n\n|一级目录名称|一级目录代码|\n|----|----|\n| 房建市政|     fjsz |\n| 工程货物|     gchw |\n| 国有产权|     gycq |\n| 交通水利|     jtsw |\n| 铁路航运|     tlhy |\n| 土地矿产|     tdkc |\n| 政府采购|     zfcg |',NULL,'6b81710b-5677-4743-b141-c6510c35593e',NULL,'2018-08-17 16:33:22','2018-08-17 16:33:22',1,'19bddb19-bbbe-4565-a682-4bb160ab9b9a');

--  API parameter
INSERT INTO `dc_api_param` VALUES ('54d2e4be-30aa-402b-bad5-7e0b7211c66a','00737b4d-aa76-43c4-a271-043fa178795c','PARAMETER','{\n  \"name\" : \"page\",\n  \"in\" : \"query\",\n  \"description\" : \"分页码，每页至多返回20条数据\",\n  \"required\" : null,\n  \"deprecated\" : null,\n  \"allowEmptyValue\" : null,\n  \"$ref\" : null,\n  \"style\" : null,\n  \"explode\" : null,\n  \"allowReserved\" : null,\n  \"schema\" : null,\n  \"examples\" : null,\n  \"example\" : null,\n  \"content\" : null,\n  \"extensions\" : null\n}','2018-08-17 17:03:45',NULL,'6b81710b-5677-4743-b141-c6510c35593e',NULL),('6f629b3c-32fb-4655-a4f6-8847e6c74f80','00737b4d-aa76-43c4-a271-043fa178795c','REQUEST_BODY','{ }','2018-08-17 17:03:45',NULL,'6b81710b-5677-4743-b141-c6510c35593e',NULL),('fe1f7155-91e3-4d37-8548-77aabeb03dd1','19bddb19-bbbe-4565-a682-4bb160ab9b9a','REQUEST_BODY','{ }','2018-08-17 16:33:22',NULL,'6b81710b-5677-4743-b141-c6510c35593e',NULL);


-- API response
INSERT INTO `dc_api_response` VALUES ('0d7d4809-1571-42d6-ae93-b0d4b09dcd5e','00737b4d-aa76-43c4-a271-043fa178795c','{\n  \"nullable\" : false,\n  \"type\" : \"object\",\n  \"properties\" : {\n    \"message\" : {\n      \"nullable\" : false,\n      \"description\" : \"接口执行结果描述\",\n      \"example\" : \"成功\",\n      \"type\" : \"string\"\n    },\n    \"data\" : {\n      \"nullable\" : false,\n      \"description\" : \"数据集合\",\n      \"example\" : \"\",\n      \"type\" : \"array\",\n      \"items\" : {\n        \"nullable\" : false,\n        \"type\" : \"object\",\n        \"properties\" : {\n          \"uuid\" : {\n            \"nullable\" : false,\n            \"description\" : \"id\",\n            \"example\" : \"d2ee29ee0d7b62f69274e75ee785fa2f\",\n            \"type\" : \"string\"\n          },\n          \"href\" : {\n            \"nullable\" : false,\n            \"description\" : \"网页详情连接地址\",\n            \"example\" : \"/njweb/fjsz/068002/068002001/20180817/1c9d0ada-2058-4eaf-b9f2-d8a72ba59475.html\",\n            \"type\" : \"string\"\n          },\n          \"title\" : {\n            \"nullable\" : false,\n            \"description\" : \"通知公告标题\",\n            \"example\" : \"（玄武区）南京市北京东路小学阳光分校塑胶跑道改造工程施工\",\n            \"type\" : \"string\"\n          },\n          \"announce_date\" : {\n            \"nullable\" : false,\n            \"description\" : \"通知公告发布时间\",\n            \"example\" : \"2018-08-16T16:00:00.000Z\",\n            \"type\" : \"string\"\n          },\n          \"meta\" : {\n            \"nullable\" : false,\n            \"description\" : \"标题列名称\",\n            \"type\" : \"array\",\n            \"items\" : {\n              \"nullable\" : false,\n              \"type\" : \"string\"\n            }\n          },\n          \"properties\" : {\n            \"nullable\" : false,\n            \"description\" : \"参数值，对应meta参数\",\n            \"type\" : \"array\",\n            \"items\" : {\n              \"nullable\" : false,\n              \"type\" : \"string\"\n            }\n          }\n        }\n      }\n    }\n  }\n}','2018-08-17 17:03:45',NULL,'6b81710b-5677-4743-b141-c6510c35593e',NULL),('56ac72ef-c48c-445c-9ae7-4c7ced7d0dab','19bddb19-bbbe-4565-a682-4bb160ab9b9a','{\n  \"nullable\" : true,\n  \"description\" : \"通知公告详情\",\n  \"example\" : \"\",\n  \"type\" : \"object\",\n  \"properties\" : {\n    \"_id\" : {\n      \"nullable\" : false,\n      \"description\" : \"自增ID\",\n      \"example\" : 123,\n      \"type\" : \"string\"\n    },\n    \"uuid\" : {\n      \"nullable\" : false,\n      \"description\" : \"公告唯一主键\",\n      \"example\" : \"f7ef0dc6f9c0f76bcd92986633b5c54a\",\n      \"type\" : \"string\"\n    },\n    \"content\" : {\n      \"nullable\" : false,\n      \"description\" : \"公告内容\",\n      \"type\" : \"string\"\n    },\n    \"modified_content\" : {\n      \"nullable\" : false,\n      \"description\" : \"修改内容\",\n      \"type\" : \"string\"\n    },\n    \"menu_code\" : {\n      \"nullable\" : false,\n      \"description\" : \"菜单代码\",\n      \"type\" : \"string\"\n    },\n    \"category_code\" : {\n      \"nullable\" : false,\n      \"description\" : \"分类编码\",\n      \"type\" : \"string\"\n    },\n    \"href\" : {\n      \"nullable\" : false,\n      \"description\" : \"链接地址\",\n      \"type\" : \"string\"\n    },\n    \"properties\" : {\n      \"nullable\" : false,\n      \"description\" : \"属性内容，内容为Array\",\n      \"type\" : \"string\"\n    },\n    \"announce_date\" : {\n      \"nullable\" : false,\n      \"description\" : \"公告发布时间\",\n      \"type\" : \"string\"\n    },\n    \"sort_date\" : {\n      \"nullable\" : false,\n      \"description\" : \"sort时间\",\n      \"type\" : \"string\"\n    },\n    \"title\" : {\n      \"nullable\" : false,\n      \"description\" : \"公告标题\",\n      \"example\" : \"（江宁分中心）九龙湖路南延等项目九龙湖路南延设计招标公告\",\n      \"type\" : \"string\"\n    }\n  }\n}','2018-08-17 16:33:22',NULL,'6b81710b-5677-4743-b141-c6510c35593e',NULL);

-- env
INSERT INTO `dc_environment` VALUES ('248965b1-f674-4bfa-b9c0-882ecccf3d44','交易中心-线上服务器','{\"PROXY_CENTER\":\"https://app.api.ggzy.truelore.cn/\",\"ANNOUNCES\":\"api/announcements\",\"ANNOUNCE\":\"api/announcement\"}','2018-08-17 08:22:52','2018-08-17 16:43:05','6b81710b-5677-4743-b141-c6510c35593e','6b81710b-5677-4743-b141-c6510c35593e','交易中心-正式服务接口','306197c5-e6b0-4ce3-96a9-3c3b883d037f');

-- project
INSERT INTO `dc_project` VALUES ('306197c5-e6b0-4ce3-96a9-3c3b883d037f','交易中心接口管理','交易中心接口管理','1.0.1','2fc76258-4fa8-4bb6-8107-9060b46307c2','2018-08-17 16:19:32',NULL,'6b81710b-5677-4743-b141-c6510c35593e',NULL,1,NULL);

-- project doc
INSERT INTO `dc_project_doc` VALUES ('6afc0bc6-a5e6-4df7-9b1c-967220dfd9de','# 如何使用\n数据中心目前提供了两个接口用来获取通知公告，在企业用户的界面中，您可以看到交易中心管理的项目，点击`查看详情按钮`，即可进入项目，查询所有的您有权使用的接口列表.\n选择您需要调试使用的接口，点击**查看详情**，即可进入`接口详情`界面,在这里您可以看到非常详细的API的信息，一般包含如下:\n+ 请求名称、请求方式、请求协议、接口类型、 接口状态、更新日期、描述\n+ 查询参数、请求头部、请求体、返回参数\n+ 接口文档、接口测试、统计\n\n确认需要调试此接口之后，您可以选择合适的环境参数来进行调试，选择环境之后，点击**获取Token**，然后切换到**接口测试选项卡** 填写相应的参数，点击**执行**按钮，查看执行结果.\n# 关于Token\ntoken是企业用户分配的访问某个具体API的凭证，具有有效时间，您可以在有效期内尝试访问指定API，当一旦Token失效，您将无法访问该API。即当出现Token过期或者无法使用的情况，您应当尝试重新获取，因此此token不能作为固定参数存储，您的应用应该能够具有自动更新token的功能(目前更新您可以通过登录web页面尝试获取，后期我们将提供SDK)','数据中心接口使用说明','string','6b81710b-5677-4743-b141-c6510c35593e','6b81710b-5677-4743-b141-c6510c35593e','2018-08-20 14:18:03','2018-08-20 14:18:03',1,'306197c5-e6b0-4ce3-96a9-3c3b883d037f');

-- project dynamic
INSERT INTO `dc_project_dynamic` VALUES ('01b01c0e-afec-47c6-8b78-abacdb4c324d','306197c5-e6b0-4ce3-96a9-3c3b883d037f','ADD_PROJECT','新增项目:交易中心接口管理 -','6b81710b-5677-4743-b141-c6510c35593e','2018-08-17 16:19:33'),('54d35de6-7088-4bfc-9767-4f8336cefd25','306197c5-e6b0-4ce3-96a9-3c3b883d037f','ADD_ENV','新增环境变量:交易中心-线上服务器','6b81710b-5677-4743-b141-c6510c35593e','2018-08-17 16:22:53'),('62e93392-dc92-45e5-bd0f-d9132f3691f5','306197c5-e6b0-4ce3-96a9-3c3b883d037f','ADD_API_DOC','新增接口文档:null','6b81710b-5677-4743-b141-c6510c35593e','2018-08-17 16:33:22'),('6ac54447-2e2c-4ab6-8980-b454d041a981','306197c5-e6b0-4ce3-96a9-3c3b883d037f','UPDATE_ENV','修改环境变量:交易中心-线上服务器','6b81710b-5677-4743-b141-c6510c35593e','2018-08-17 16:43:05'),('fa834088-e3d3-461e-934a-83b457a2c8c1','306197c5-e6b0-4ce3-96a9-3c3b883d037f','ADD_API_DOC','新增接口文档:null','6b81710b-5677-4743-b141-c6510c35593e','2018-08-17 17:03:45');

/*!40101 SET character_set_client = @saved_cs_client */;


/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;