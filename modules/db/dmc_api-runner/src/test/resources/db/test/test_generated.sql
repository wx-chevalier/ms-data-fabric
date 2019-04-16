INSERT INTO dc_api(id, api_name, api_operation, api_type, api_protocol, api_path, api_summary, project_id, create_userid)
VALUES('api0', 'api0', 'post', 'GENERATED', 'http', '/api0', 'hello', 'prj0', 'create-user0');
INSERT INTO dc_api_impl(id, api_id, api_impl, create_userid)
VALUES('api0impl', 'api0',
  '{"type":"SQL","dialect":"mysql","sql":"SELECT id, api_name AS type FROM dc_api WHERE api_name={name: integer}","datasourceName":"dmc_runner","isQuery":true}',
  'create-user0');
INSERT INTO dc_api_param(id, api_id, param_type, param_model, create_userid)
VALUES('api0param', 'api0', 'REQUEST_BODY', '{"type":"object","properties":{"name":{"type":"string"}}}', 'create-user0');

INSERT INTO dc_user_api(id, user_id, api_id, create_userid)
VALUES('api0-auth', '6defc1a2-3724-4494-84d2-2ff347a212fd', 'api0', 'create-user0');