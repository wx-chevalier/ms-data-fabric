-- 新增接口token记录表
CREATE TABLE IF NOT EXISTS  `dc_access_token`(
  `id` VARCHAR(36) PRIMARY KEY COMMENT '主键',
  `user_id` VARCHAR(36) NOT NULL  COMMENT  '访问用户ID',
  `api_id` VARCHAR(36) NOT NULL  COMMENT '接口ID',
  `env_id` VARCHAR(36) NOT NULL  COMMENT '环境ID',
  `access_token` TEXT NOT NULL COMMENT  'token',
  `create_datetime`  DATETIME DEFAULT  NOW() COMMENT 'token创建时间',
  `modify_datetime`  DATETIME DEFAULT  NULL COMMENT 'token更新时间'
)COMMENT  '接口访问token授权表';

-- 业务逻辑中ENV可以为NULL,但是此处为了创建唯一键索引，设置不允许ENV为NULL,传递空字符串即可
alter table `dc_access_token` add unique KEY find(`api_id`,`user_id`,`env_id`);