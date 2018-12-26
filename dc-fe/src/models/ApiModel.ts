import { API } from '../apis/interface';
import { observable, computed, action } from 'mobx';
import BaseModel from './BaseModel';
import UserModel from './UserModel';
import ApiApi from '../apis/ApiApi';
import { ArgModel } from './SchemaModel';
import { SchemaObject, ParameterObject } from '../apis/OpenApi3Interface';
import DocModel from './DocModel';

export default class ApiModel extends BaseModel {
  apiApi: ApiApi = new ApiApi();
  name: string;
  uri: string;
  verb: string;
  type: string;
  description: string;
  protocol: string;
  folderName: string;
  folderId: string = 'ungroup';
  responseBody: SchemaObject = {};
  requestBody: SchemaObject = {};
  parameters: ParameterObject[] = [];
  headers: ParameterObject[] = [];
  queries: ParameterObject[] = [];
  impl: string;
  @observable
  apiDoc?: DocModel;

  @observable
  argSchema: ArgModel;

  @observable
  token: string = '';

  @observable
  accessUrl: string = '';

  @observable
  authedUser: UserModel[] = [];
  @computed
  get authedText() {
    if (this.authedUser.length === 0) {
      return '未授权';
    }
    return this.authedUser.map(u => UserModel.clientMap.get(u.id) || u.id).join('、');
  }

  constructor(data: API) {
    super(data);
    this.verb = data.operation;
    this.name = data.name;
    this.uri = data.path;
    this.type = data.type;
    this.description = data.summary;
    this.protocol = data.protocol;
    switch (data.type) {
      case 'GENERATED':
        if (data.impl) {
          this.impl = JSON.stringify(data.impl.impl);
        }
        break;
      default:
    }
    if (data.folder) {
      this.folderId = data.folder.id || 'ungroup';
      this.folderName = data.folder.name;
    }
    if (data.response) {
      this.responseBody = data.response.model;
    }
    if (Array.isArray(data.param)) {
      let parameters: ParameterObject[] = [];
      for (let p of data.param) {
        switch (p.type) {
          case 'REQUEST_BODY':
            this.requestBody = p.schema;
            break;
          case 'PARAMETER':
            parameters.push(p.parameter);
            break;
          default:
        }
      }
      if (parameters) {
        this.headers = parameters.filter(p => p.in === 'header');
        this.queries = parameters.filter(p => p.in === 'query');
      }
    }
    this.getAuthUsers();
    // 返回数据为数组，目前只存在一个文档
    if (data.doc && data.doc.length !== 0) {
      this.getApiDoc(data.doc[0].id!);
    }
  }

  @action.bound
  async getAuthUsers() {
    this.authedUser = await this.apiApi.getAuthedUsersForApi(this.id);
  }

  @action.bound
  async getApiDoc(docId: string) {
    this.apiDoc = await this.apiApi.getApiDocById(docId);
  }

  @action.bound
  async auth(userList: string[]) {
    const needToAdd = userList.filter(id => !this.authedUser.some(u => u.id === id));
    const needToDelete = this.authedUser
      .filter(u => !userList.some(id => id === u.id))
      .map(u => u.id);
    if (needToAdd.length > 0) {
      await this.apiApi.authByApiId(this.id, needToAdd);
    }
    if (needToDelete.length > 0) {
      await this.apiApi.deleteAuthByApiId(this.id, needToDelete);
    }
    this.getAuthUsers();
  }

  @action.bound
  async getToken(envId?: string) {
    let token = await this.apiApi.getApiAccessToken(this.id, envId);
    this.token = token.token;
    this.accessUrl = token.apiURL;
  }

  get isDeletable(): boolean {
    return !this.authedUser.length;
  }

  get isEditable(): boolean {
    return !this.impl;
  }
}
