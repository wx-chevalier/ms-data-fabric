import { action, observable, computed } from 'mobx';
import AccounManagementApi from '../apis/AccountManagementApi';
import { DatabaseModel } from '../models/datasource';
import UserApi from '../apis/UserApi';
import ApiUserManagementModel from '../models/ApiUserManagementModel';
import DataSourceModel from '../models/datasource/DataSourceModel';
import DatasourceApi from '../apis/DatasourceApi';
import UserModel, { ApiUserManagementUserModel, QyUserModel } from '../models/UserModel';

export default class AccounManagementStore {
  accoutManagementApi: AccounManagementApi = new AccounManagementApi();

  dataSourceApi: DatasourceApi = new DatasourceApi();

  userApi: UserApi = new UserApi();

  // 账号管理员-企业用户审核原始数据
  @observable
  originQyUserData: QyUserModel[] = [];

  @observable
  originDbData: DatabaseModel[] = [];

  // 获取可用接口管理员数据
  @observable
  availableApiManagerList: ApiUserManagementUserModel[] = [];

  // 接口用户管理数据源，会过滤掉未授权的管理员
  @observable
  apiManagerList: ApiUserManagementModel[] = [];

  @observable
  dataSourceList: DataSourceModel[] = [];

  // 根据availableApiManagerList 与 apiManagerList 生成
  @computed
  get computedApiManagerList(): ApiUserManagementModel[] {
    return this.availableApiManagerList.map(u => {
      const index = this.apiManagerList.findIndex(i => i.user.id === u.id);
      if (index !== -1) {
        return this.apiManagerList[index];
      } else {
        return new ApiUserManagementModel(u, []);
      }
    });
  }

  @action.bound
  async getQyUserList() {
    this.originQyUserData = await this.userApi.getQyUserList();
  }

  @action.bound
  async qyUserAudit(id: string) {
    await this.userApi.qyUserAudit(id);
  }

  @action.bound
  async getDatabaseList() {
    this.originDbData = await this.accoutManagementApi.getDatabaseList();
  }

  @action.bound
  async getAvailableApiManagerList() {
    this.availableApiManagerList = await this.userApi.getAvailableApiManagerList();
  }

  @action.bound
  async changeAuthorization(dsId: string, databaseName: string, userIdList: string[]) {
    await this.accoutManagementApi.changeAuthorization(dsId, databaseName, userIdList);
  }

  @action.bound
  async getApiManagerList() {
    this.apiManagerList = await this.accoutManagementApi.getApiManagerList();
  }

  @action.bound
  async createApiUser(userName: string, password: string): Promise<UserModel> {
    const res = await this.accoutManagementApi.createApiManager(userName, password);
    await this.getAvailableApiManagerList();
    await this.getApiManagerList();
    return res;
  }

  @action.bound
  async authDbToUser(
    userId: string,
    authDbList: {
      // 数据库名称
      databaseName: string;
      // 数据源id
      dsId: string;
    }[]
  ) {
    await this.accoutManagementApi.authDbListToUser(userId, authDbList);
    await this.getApiManagerList();
  }

  @action.bound
  async start(userId: string) {
    await this.userApi.updateApiUserStatus(userId, 'ALLOWABLE');
    await this.getApiManagerList();
  }

  @action.bound
  async stop(userId: string) {
    await this.userApi.updateApiUserStatus(userId, 'UNALLOWABLE');
    await this.getApiManagerList();
  }

  @action.bound
  async deleteApiUser(userId: string) {
    await this.userApi.updateApiUserStatus(userId, 'ABANDONED');
    await this.getAvailableApiManagerList();
    await this.getApiManagerList();
  }

  @action.bound
  async getDataSourceList() {
    this.dataSourceList = await this.dataSourceApi.getDataSourceList();
  }
}
