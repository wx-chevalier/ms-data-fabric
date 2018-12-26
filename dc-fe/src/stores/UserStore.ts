import { observable, action, computed, autorun } from 'mobx';
import UserApi from '../apis/UserApi';
import UserModel, { QyUserModel } from '../models/UserModel';

type Role = 'accountManager' | 'apiManager' | 'qyUser';

const roleMap = {
  accountManager: '账号管理员',
  apiManager: '接口管理员',
  qyUser: '企业用户'
};

export default class UserStore {
  userApi: UserApi = new UserApi();
  clientMap: Map<string, string> = new Map<string, string>();
  @observable
  clientList: UserModel[] = [];

  @observable
  user: UserModel | null = null;

  @observable
  loginRole?: Role;

  @computed
  get isLogin() {
    return !!this.user;
  }

  /**
   * 是否是账号管理员
   */
  @computed
  get isAccountManager(): boolean {
    return (this.user && this.user.isAccountManager) || false;
  }

  /**
   * 是否是接口管理员
   */
  @computed
  get isApiManager(): boolean {
    return (this.user && this.user.isApiManager) || false;
  }

  @computed
  get isQyUser(): boolean {
    return (this.user && this.user.isQyUser) || false;
  }

  @action.bound
  async login(username: string, pwd: string, remember: boolean) {
    const r = await this.userApi.login(username, pwd, remember);
    const role: Role = r.isAccountManager
      ? 'accountManager'
      : r.isApiManager
        ? 'apiManager'
        : 'qyUser';
    if (role === this.loginRole) {
      this.user = r;
    } else {
      throw new Error(`该账号为${roleMap[role]}用户，无法登陆`);
    }
  }

  @action.bound
  clearUser() {
    this.user = null;
  }

  @action.bound
  logout() {
    this.clearUser();
    this.userApi.logout();
  }

  @action.bound
  async getClients() {
    const clientList = await this.userApi.getClients();
    clientList.forEach(u => UserModel.clientMap.set(u.id, u.username));
    this.clientMap = UserModel.clientMap;
    this.clientList = clientList;
  }

  @action.bound
  selectRole(role: Role) {
    this.loginRole = role;
  }

  // 企业用户注册 - 暂无上传营业执照功能
  @action.bound
  async registry(
    username: string,
    password: string,
    phone: string,
    businessLicense?: string
  ): Promise<QyUserModel> {
    return await this.userApi.registry(username, password, phone, businessLicense);
  }

  @action.bound
  async changePassword(oldPassword: string, newPassword: string) {
    await this.userApi.changePassword(oldPassword, newPassword);
  }

  @action.bound
  async checkPassword(password: string) {
    await this.userApi.checkPassword(password);
  }

  constructor() {
    this.user = this.userApi.getLocalUser();
    autorun(() => {
      if (this.isApiManager) {
        this.getClients();
      }
    });
  }
}
