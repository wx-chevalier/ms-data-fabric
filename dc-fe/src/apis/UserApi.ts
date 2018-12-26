import BaseApi, { LOGIN_TOKEN_STORAGE_KEY } from './BaseApi';
import jwt from 'jsonwebtoken';
import { User, LoginToken, QyUser } from './interface';
import UserModel, { QyUserModel, ApiUserManagementUserModel } from '../models/UserModel';
import * as agents from './agent';
import { ApiUserManagementUser, APIUSERSTATUS } from './interface/user';

export default class UserApi extends BaseApi {
  /**
   *
   * @param username
   * @param password
   * @param phone
   * @param businessLicense 营业执照url
   */
  async registry(username: string, password: string, phone: string, businessLicense?: string) {
    const res = await this.post<QyUser>({
      url: '/register',
      data: {
        name: username,
        password,
        phoneNumber: phone,
        businessLicense
      }
    });
    return new QyUserModel(res);
  }

  async login(username: string, password: string, rememberMe: boolean): Promise<UserModel> {
    const jwtToken = (await this.post<{ token: string }>({
      url: '/auth/login',
      data: {
        rememberMe,
        username,
        password
      }
    })).token;
    this.setAgentAuthorization(jwtToken);
    localStorage.setItem(LOGIN_TOKEN_STORAGE_KEY, jwtToken);
    let decoded;
    try {
      decoded = this.parseToken(jwtToken);
    } catch (e) {
      // 返回无效 TOKEN
      throw new Error('登陆服务器存在问题');
    }
    return UserModel.ofToken(decoded);
  }

  logout() {
    localStorage.removeItem(LOGIN_TOKEN_STORAGE_KEY);

    this.removeAgentAuthorization();
  }

  async changePassword(oldPassword: string, newPassword: string) {
    await this.patch({
      url: '/user/password',
      data: {
        newPassword,
        oldPassword
      }
    });
  }

  // 校验密码
  async checkPassword(password: string) {
    await this.post({
      url: '/auth/validation',
      data: {
        password
      }
    });
  }

  async getClients(): Promise<UserModel[]> {
    const result = await this.get<User[]>({
      url: '/user/client'
    });
    return result.map(u => new UserModel(u));
  }

  /**
   * 根据userId获取UserInfo
   */
  async getUserInfo(id: string | string[]): Promise<User | User[]> {
    if (id instanceof Array) {
      return Promise.all(
        id.map(async i => {
          return await this.get<User>({
            url: `/user/${id}`,
            errorNotice: {
              title: '网络错误',
              content: '获取用户信息失败'
            }
          });
        })
      );
    } else {
      return await this.get<User>({
        url: `/user/${id}`,
        errorNotice: {
          title: '网络错误',
          content: '获取用户信息失败'
        }
      });
    }
  }

  // 获取企业用户列表
  async getQyUserList(): Promise<QyUserModel[]> {
    const res = await this.get<QyUser[]>({
      url: '/user/client',
      errorNotice: {
        title: '网络错误',
        content: '获取企业用户列表失败'
      }
    });
    return res.map(r => new QyUserModel(r));
  }

  /**
   * 企业用户审核
   * @param id 企业用户id
   */
  async qyUserAudit(id: string): Promise<QyUserModel> {
    const res = await this.patch<QyUser>({
      url: `/register/${id}/checked`,
      data: {}
    });
    return new QyUserModel(res);
  }

  async getApiManagementUserInfo(id: string): Promise<ApiUserManagementUserModel> {
    const res = await this.get<ApiUserManagementUser>({
      url: `/user/${id}`,
      errorNotice: {
        title: '网络错误',
        content: '获取接口管理员信息失败'
      }
    });
    return new ApiUserManagementUserModel(res);
  }

  // 更新apiManager状态
  async updateApiUserStatus(
    userId: string,
    status: APIUSERSTATUS
  ): Promise<ApiUserManagementUserModel> {
    const res = await this.patch<ApiUserManagementUser>({
      url: `/user/${userId}/status/${status}`,
      data: {}
    });
    return new ApiUserManagementUserModel(res);
  }

  // 获取可用的apiManager列表 - 过滤掉已经注销的用户
  async getAvailableApiManagerList(): Promise<ApiUserManagementUserModel[]> {
    const res = await this.get<ApiUserManagementUser[]>({
      url: '/user/manager',
      errorNotice: {
        title: '网络错误',
        content: '获取接口管理员列表失败'
      }
    });
    return res.filter(i => i.status !== 'ABANDONED').map(r => new ApiUserManagementUserModel(r));
  }

  // 新增管理员用户
  async createApiManager(username: string, password: string): Promise<UserModel> {
    const res = await this.post<User>({
      url: '/user',
      data: {
        name: username,
        password
      }
    });
    return new UserModel(res);
  }

  /**
   * 用户相关接口
   */
  constructor() {
    super(agents.userAgent);
  }

  /**
   * 获取 local storage 中 TOKEN 解析得当前登陆用户
   */
  getLocalUser(): UserModel | null {
    const jwtToken = localStorage.getItem(LOGIN_TOKEN_STORAGE_KEY);
    if (!jwtToken) {
      return null;
    }

    let parsed;
    try {
      parsed = this.parseToken(jwtToken);
    } catch (e) {
      return null;
    }
    this.setAgentAuthorization(jwtToken);
    return UserModel.ofToken(parsed);
  }

  /**
   * 设定所有接口访问 agent 的访问验证 token
   */
  private setAgentAuthorization(jwtToken: string) {
    Object.keys(agents).forEach(k => {
      // tslint:disable-next-line
      agents[k].defaults.headers.common['Authorization'] = this.BEARER + jwtToken;
    });
  }

  /**
   * 移除接口访问 agent 的访问验证 token
   */
  private removeAgentAuthorization() {
    Object.keys(agents).forEach(k => {
      // tslint:disable-next-line
      delete agents[k].defaults.headers.common['Authorization'];
    });
  }

  /**
   * 解析 JWT Token
   */
  private parseToken(jwtToken: string) {
    return jwt.decode(jwtToken) as LoginToken;
  }
}

export const userApi = new UserApi();
