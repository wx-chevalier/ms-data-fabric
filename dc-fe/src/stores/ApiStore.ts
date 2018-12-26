import { action, observable, computed } from 'mobx';
import FolderApi from '../apis/FolderApi';
import ApiApi, { TimeFormat } from '../apis/ApiApi';
import ApiModel from '../models/ApiModel';
import FolderModel from '../models/FolderModel';
import { FieldValues } from '../components/folder-form/FolderForm';
import { ApiDefinition } from '../components/api-editor/ApiEditor';
import { APIModification, APIParam } from '../apis/interface';
import EnvModel from '../models/EnvModel';
import EnvApi from '../apis/EnvApi';
import DocApi from '../apis/DocApi';
import ApiTestApi from '../apis/ApiTestApi';
import UserModel from '../models/UserModel';
import { HttpMethods } from '../apis/BaseApi';

export default class ApiStore {
  protected folderApi: FolderApi = new FolderApi();
  protected apiApi: ApiApi = new ApiApi();
  protected envApi: EnvApi = new EnvApi();
  protected docApi: DocApi = new DocApi();
  protected apiTestApi: ApiTestApi = new ApiTestApi();

  /** API cache: projectId -> ApiModel[] */
  apiMap: Map<string, ApiModel[]> = new Map();
  /** Folder cache: projectId -> FolderModel[] */
  folderMap: Map<string, FolderModel[]> = new Map();

  projectId: string;

  @observable
  apiList: ApiModel[] = [];

  @observable
  folderList: FolderModel[] = [];

  @observable
  selectedApi: ApiModel | null = null;

  @observable
  envList: EnvModel[] = [];

  // 接口文档编辑器当前内容
  @observable
  editorContent: string = '';

  @action.bound
  async loadListForProject(type: 'api' | 'folder', id: string, force: boolean = false) {
    this.projectId = id;
    let map: Map<string, ApiModel[] | FolderModel[]>;
    let api: ApiApi | FolderApi;
    switch (type) {
      case 'api':
        map = this.apiMap;
        api = this.apiApi;
        break;
      case 'folder':
        map = this.folderMap;
        api = this.folderApi;
        break;
      default:
        return;
    }
    if (!this.apiMap.has(id) || force) {
      const list = await api.getListByProjectId(id);
      map.set(id, list);
    }
    this[`${type}List`] = map.get(id) || [];
  }

  @action.bound
  async loadApiById(apiId: string) {
    this.selectedApi = await this.apiApi.getApiById(apiId);
    if (this.selectedApi.apiDoc) {
      this.editorContent = this.selectedApi.apiDoc.content;
    } else {
      this.editorContent = '';
    }
  }

  @action.bound
  clearSelectedApi() {
    this.selectedApi = null;
  }

  @action.bound
  async handleFolder(operation: 'create' | 'update', folderId: string, v: FieldValues) {
    switch (operation) {
      case 'create':
        await this.folderApi.postFolder(this.projectId, v);
        break;
      case 'update':
        await this.folderApi.updateFolder(folderId, v);
        break;
      default:
        return;
    }
    this.loadListForProject('folder', this.projectId, true);
  }

  @action.bound
  async removeFolder(fId: string) {
    await this.folderApi.deleteFolderById(fId);
    this.loadListForProject('folder', this.projectId, true);
  }

  @action.bound
  async removeApiById(id: string) {
    await this.apiApi.deleteApiById(id);
    this.loadListForProject('api', this.projectId, true);
  }

  @action.bound
  async create(pid: string, fid: string, data: ApiDefinition) {
    const api = await this.apiApi.create({
      projectId: pid,
      folderId: fid,
      ...this.apiDefToPost(data)
    });
    this.selectedApi = api;
    return api;
  }

  @action.bound
  async update(id: string, data: ApiDefinition) {
    return await this.apiApi.updateApi(id, this.apiDefToPost(data));
  }

  @action.bound
  async loadEnvList() {
    this.envList = await this.envApi.getListByProjectId(this.projectId);
  }

  @action.bound
  async createApiDoc(apiId: string, content: string) {
    await this.docApi.createApiDoc(apiId, content);
  }

  @action.bound
  async updateApiDoc(apiId: string, apiDocId: string, content: string) {
    await this.docApi.updateApiDoc(apiId, apiDocId, content);
  }

  @action.bound
  async getVisitTimes(
    format: TimeFormat,
    apiId: string,
    startTime?: string,
    endTime?: string
  ): Promise<{ time: string; count: number }[]> {
    return await this.apiApi.getVisitTimes(apiId, format, startTime, endTime);
  }

  @action.bound
  async getUserAccessDistribution(
    apiId: string,
    startTime?: string,
    endTime?: string
  ): Promise<{ user: UserModel; count: number }[]> {
    return await this.apiApi.getUserAccessDistribution(apiId, startTime, endTime);
  }

  @action.bound
  async getConsumeTimeDistribution(
    apiId: string,
    startTime?: string,
    endTime?: string
  ): Promise<{ interval: string; count: number }[]> {
    return await this.apiApi.getConsumeTimeDistribution(apiId, startTime, endTime);
  }

  @action.bound
  async getApiURI(apiId: string, envId?: string): Promise<string> {
    return await this.apiApi.getApiURI(apiId, envId);
  }

  @action.bound
  async apiTest(
    type: HttpMethods,
    url: string,
    header: Object,
    body?: Object
    // tslint:disable-next-line
  ): Promise<{ status: number; message: Array<any> }> {
    return await this.apiTestApi.apiTest(type, url, header, body);
  }

  @action.bound
  async clearApiToken(apiId: string) {
    await this.apiApi.clearApiToken(apiId);
  }

  @action.bound
  async clearProjectToken(projectId: string) {
    await this.apiApi.clearProjectToken(projectId);
  }

  @action.bound
  async clearUserToken() {
    await this.apiApi.clearUserToken();
  }

  apiDefToPost(data: ApiDefinition): Partial<APIModification> {
    let {
      name,
      verb,
      protocol,
      description,
      uri,
      requestBody,
      responseBody,
      headers,
      queries
    } = data;
    name = name || '';
    protocol = protocol || '';
    const operation = verb || '';
    const path = uri || '';
    const summary = description || '';
    const response = responseBody;
    const parameters: APIParam[] = [];
    parameters.push({
      type: 'REQUEST_BODY',
      schema: requestBody
    });
    headers.forEach(h =>
      parameters.push({
        type: 'PARAMETER',
        parameter: {
          in: 'header',
          name: h.name,
          description: h.description
        }
      })
    );
    queries.forEach(q =>
      parameters.push({
        type: 'PARAMETER',
        parameter: {
          in: 'query',
          name: q.name,
          description: q.description
        }
      })
    );
    return {
      type: 'PROXY',
      name,
      operation,
      protocol,
      path,
      response,
      summary,
      parameters
    };
  }

  @computed
  get editingDocContent() {
    return this.editorContent;
  }

  set editingDocContent(v: string) {
    this.editorContent = v;
  }
}
