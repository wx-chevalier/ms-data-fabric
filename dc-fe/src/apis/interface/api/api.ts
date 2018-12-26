/**
 * 接口主要包括：
 * - APIParam: 参数定义列表
 * - APIResponse: 响应定义
 * - Doc: 文档
 * - impl: 接口实现
 */
import { SchemaObject, ParameterObject } from './../../OpenApi3Interface';
import { CommmonFields, User, Project, Folder, Doc } from '..';
import { APIImpl, APIImplDef } from './apiimpl';

export interface APIRequestBody {
  id?: string;
  type: 'REQUEST_BODY';
  schema: SchemaObject;
}

export interface APIParameter {
  id?: string;
  type: 'PARAMETER';
  parameter: ParameterObject;
}

export type APIParam = APIRequestBody | APIParameter;

export interface APIResponse {
  id: string;
  model: SchemaObject;
}

interface CommonAPI extends CommmonFields {
  name: string;
  path: string;
  protocol: string;
  summary: string;
  operation: string;

  createUser: User;
  modifyUser: User;
  authedUserList: User[];

  project: Project;
  folder: Folder;
  doc: Doc[];
  /** 参数模型 */
  param: APIParam[];
  /** 响应模型定义 */
  response: APIResponse;
}

/**
 * 接口资源定义
 */
export type API = ProxyAPI | GeneratedAPI;

export interface ProxyAPI extends CommonAPI {
  type: 'PROXY';
}

export interface GeneratedAPI extends CommonAPI {
  type: 'GENERATED';
  impl: APIImpl;
}

/**
 * 接口创建、更新所用的传输数据类型
 */
export type APIModification = ProxyAPIModification | GeneratedAPIModification;

export interface AbstractAPIModification {
  name: string;
  projectId: string;
  summary?: string;
  folderId?: string;
}

export interface ProxyAPIModification extends AbstractAPIModification {
  type: 'PROXY';
  operation: string;
  protocol: string;
  path: string;
  response: SchemaObject;
  parameters: APIParam[];
}

export interface GeneratedAPIModification extends AbstractAPIModification {
  type: 'GENERATED';
  impl: APIImplDef;
}
