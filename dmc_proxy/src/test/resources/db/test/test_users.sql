-- 更新环境参数
INSERT INTO dc_environment VALUES
('73a33a7b-8f63-4795-99bd-77c7c2749c18', '测试环境 1', '{"PROXY_LOCAL":"http://localhost:5000"}', '2018-07-16 16:21:48', '2018-07-24 14:04:39', '6b81710b-5677-4743-b141-c6510c35593e', '6b81710b-5677-4743-b141-c6510c35593e', null, '0d1b1e7b-89c7-415b-8ddd-ae9d007ce5d2');

--更新查询的API的参数
INSERT INTO dc_api_param  VALUES ('891b1dc0-398a-4fd8-b43a-31301d739a30', 'c6fb0622-eb2c-4bfe-bcd2-b977c5ff9c7b', 'PARAMETER', '{
  "name" : "age",
  "in" : "query",
  "description" : "年龄",
  "required" : null,
  "deprecated" : null,
  "allowEmptyValue" : null,
  "$ref" : null,
  "style" : null,
  "explode" : null,
  "allowReserved" : null,
  "schema" : null,
  "examples" : null,
  "example" : null,
  "content" : null,
  "extensions" : null
}', '2018-07-24 14:09:14', null, '6b81710b-5677-4743-b141-c6510c35593e', null);