use dmc_api;
CREATE TABLE IF NOT EXISTS dc_api_statistics
(
  id           VARCHAR(36)                        NULL COMMENT '统计ID',
  api_id       VARCHAR(36)                        NOT NULL COMMENT 'API',
  user_id      VARCHAR(36)                        NOT NULL COMMENT '访问用户',
  api_path     VARCHAR(512)                       NOT NULL COMMENT '实际路径',
  proxy_path   VARCHAR(512)                       NOT NULL COMMENT '请求代理路径',
  proxy_method VARCHAR(32)                        NULL COMMENT '请求方式',
  consume_time INT DEFAULT '0'                    NULL COMMENT '请求消耗时间(单位ms)',
  status_code  INT DEFAULT '200'                  NULL COMMENT '响应状态码',
  access_time  DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '访问时间'
)COMMENT 'API接口访问统计表';

-- auto-generated definition
CREATE TABLE IF NOT EXISTS dc_environment
(
  id              VARCHAR(36)                        NOT NULL COMMENT '环境ID'
    PRIMARY KEY,
  env_name        VARCHAR(255)                       NOT NULL COMMENT '环境KEY值',
  env_value       TEXT                               NOT NULL COMMENT '环境VALUE值',
  create_datetime DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '创建时间',
  modify_datetime DATETIME                           NULL COMMENT '修改时间',
  create_userid   VARCHAR(36)                        NULL COMMENT '创建人id',
  modify_userid   VARCHAR(36)                        NULL COMMENT '修改人id',
  remark          TEXT                               NULL COMMENT '备注信息',
  project_id      VARCHAR(36)                        NOT NULL
)COMMENT '接口环境变量表';


-- auto-generated definition
CREATE TABLE IF NOT EXISTS dc_route
(
  id           VARCHAR(36)            NOT NULL COMMENT '路由id',
  project_id   VARCHAR(36)            NOT NULL COMMENT '项目ID',
  env_id       VARCHAR(36)            NOT NULL COMMENT '资源ID',
  path         VARCHAR(255)           NOT NULL COMMENT '映射入口地址',
  url          VARCHAR(512)           NOT NULL COMMENT '映射目标URL',
  retryable    TINYINT(1) DEFAULT '0' NULL COMMENT '允许重试',
  strip_prefix TINYINT(1) DEFAULT '1' NULL COMMENT '移除前缀'
)COMMENT '代理地址映射规则表';

--  API 接口表
CREATE TABLE  IF NOT EXISTS `dc_api`  (
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


--   API 参数表
  CREATE TABLE IF NOT EXISTS `dc_api_param`   (
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


-- API相应表

CREATE TABLE IF NOT EXISTS `dc_api_response`   (
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



