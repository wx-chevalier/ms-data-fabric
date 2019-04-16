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

CREATE TABLE `dc_user_schema` (
  `id`              VARCHAR(36) NOT NULL
  COMMENT 'ID',
  `data_source_id`  VARCHAR(64) NOT NULL
  COMMENT '数据库ID',
  `data_database_name`  VARCHAR(64) NOT NULL
  COMMENT '数据库ID',
  `user_id`         VARCHAR(36) NOT NULL
  COMMENT '用户ID',
  `create_datetime` DATETIME DEFAULT CURRENT_TIMESTAMP
  COMMENT '创建日期',
  `create_userid`   VARCHAR(36) NOT NULL
  COMMENT '创建人id',
  `remark`          TEXT COMMENT '备注信息',
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COMMENT ='数据库权限授权表';

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
