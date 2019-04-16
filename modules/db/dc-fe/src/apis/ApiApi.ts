import BaseApi from './BaseApi';
import { API, User, APIModification, AccessToken, Doc, ApiAccessInfo } from './interface';
import ApiModel from '../models/ApiModel';
import UserModel from '../models/UserModel';
import DocModel from '../models/DocModel';
import { userApi } from './UserApi';
import { userStore } from '../stores';

export type TimeFormat = '%Y-%m-%d';

export default class ApiApi extends BaseApi {
  /**
   * 获取项目下的接口列表
   *
   * @param pid 项目 ID
   */
  async getListByProjectId(pid: string): Promise<ApiModel[]> {
    const result = await this.get<API[]>({
      url: `/project/${pid}/api`,
      errorNotice: {
        title: '网络错误',
        content: '获取项目下的接口列表失败'
      }
    });
    return result.map(a => new ApiModel(a));
  }

  /**
   * 获取指定接口 ID 的接口详情
   */
  async getApiById(apiId: string): Promise<ApiModel> {
    const result = await this.get<API>({
      url: `/api/${apiId}`,
      errorNotice: {
        title: '网络错误',
        content: '获取接口详情失败'
      }
    });
    return new ApiModel(result);
  }

  /**
   * 创建接口
   */
  async create(data: Partial<APIModification>): Promise<ApiModel> {
    const result = await this.post<API>({
      url: '/api',
      data
    });
    return new ApiModel(result);
  }

  /**
   * 更新接口
   */
  async updateApi(apiId: string, data: Partial<APIModification>): Promise<ApiModel> {
    const result = await this.patch<API>({
      url: `/api/${apiId}`,
      data: JSON.parse(JSON.stringify(data, (k, v) => (v != null ? v : undefined)))
    });
    return new ApiModel(result);
  }

  /**
   * 删除接口
   */
  async deleteApiById(apiId: string) {
    return await this.delete({
      url: `/api/${apiId}`
    });
  }

  /**
   * 获取指定接口被授权给使用的用户列表
   */
  async getAuthedUsersForApi(apiId: string): Promise<UserModel[]> {
    let result = await this.get<User[]>({
      url: `/api/${apiId}/user`,
      errorNotice: {
        title: '网络错误',
        content: '获取接口被授权给使用的用户列表失败'
      }
    });
    let clients = userStore.clientList || (await userApi.getClients());
    return clients.filter(u => result.some(iu => iu.id === u.id));
  }

  /**
   * 授权指定接口的访问权限给一组用户
   */
  async authByApiId(apiId: string, userIdList: string[]): Promise<number> {
    const result = await this.post<{ count: number }>({
      url: `/auth/api/${apiId}`,
      data: userIdList
    });
    return result.count;
  }

  /**
   * 移除一组用户对指定接口的访问权限
   */
  async deleteAuthByApiId(apiId: string, userIdList: string[]): Promise<number> {
    const result = await this.delete<{ count: number }>({
      url: `/auth/api/${apiId}`,
      data: userIdList
    });
    return result.count;
  }

  async getApiAccessToken(apiId: string, envId?: string): Promise<AccessToken> {
    let token = await this.post<AccessToken>({
      url: `/api/${apiId}/token`,
      data: { envId },
      errorNotice: {
        title: '网络错误',
        content: '获取Token失败'
      }
    });

    return token;
  }

  // 根据apiDocId获取指定api的文档内容
  async getApiDocById(apiDocId: string): Promise<DocModel> {
    const result = await this.get<Doc>({
      url: `apidoc/${apiDocId}`,
      errorNotice: {
        title: '网络错误',
        content: '获取接口文档内容失败'
      }
    });
    let r = new DocModel(result);
    r.creater = new UserModel((await userApi.getUserInfo(result.createUser!.id)) as User);
    return r;
  }

  // 根据指定的API和ENV，生成接口的URI地址
  async getApiURI(apiId: string, envId?: string): Promise<string> {
    const result = await this.post<{ apiURI: string }>({
      url: `/api/${apiId}/uri`,
      data: {
        envId
      },
      errorNotice: {
        title: '网络错误',
        content: '获取URI地址失败'
      }
    });

    return result.apiURI;
  }

  // 获取日期-访问次数集合，用于绘制图表
  /**
   * @param format 日期格式 - 决定图表横轴的时间区间
   * @param start 开始时间，可选
   * @param end 结束时间，可选
   */
  async getVisitTimes(
    apiId: string,
    format: TimeFormat,
    startTime?: string,
    endTime?: string
  ): Promise<{ time: string; count: number }[]> {
    const result = await this.get<ApiAccessInfo[]>({
      url: this.generateUrl('time', apiId, startTime, endTime, format),
      errorNotice: {
        title: '网络错误',
        content: '获取访问次数信息失败'
      }
    });

    return result.map(r => ({
      time: r.accessTime,
      count: r.accessCount
    }));
  }

  // 获取被各用户访问的次数
  async getUserAccessDistribution(
    apiId: string,
    startTime?: string,
    endTime?: string
  ): Promise<{ user: UserModel; count: number }[]> {
    const result = await this.get<ApiAccessInfo[]>({
      url: this.generateUrl('user', apiId, startTime, endTime),
      errorNotice: {
        title: '网络错误',
        content: '获取接口被各用户访问次数信息失败'
      }
    });

    const filteredRes = result.filter(v => {
      return v.userId !== 'bad request' && v.userId !== 'NO TOKEN';
    });

    const res = await Promise.all(
      filteredRes.map(async r => {
        const user = new UserModel((await userApi.getUserInfo(r.userId)) as User);
        return {
          user,
          count: r.accessCount
        };
      })
    );

    return res;
  }

  // 获取访问耗时统计信息
  async getConsumeTimeDistribution(
    apiId: string,
    startTime?: string,
    endTime?: string
  ): Promise<{ interval: string; count: number }[]> {
    const result = await this.get<ApiAccessInfo[]>({
      url: this.generateUrl('consuming', apiId, startTime, endTime),
      errorNotice: {
        title: '网络错误',
        content: '获取访问耗时统计信息失败'
      }
    });

    return Object.keys(result).map(r => ({
      interval: r,
      count: result[r]
    }));
  }

  // 清除某个api下的所有token
  async clearApiToken(apiId: string) {
    await this.delete({
      url: `/apitoken/api/${apiId}`
    });
  }

  // 清除某个项目下的所有token
  async clearProjectToken(projectId: string) {
    await this.delete({
      url: `/apitoken/project/${projectId}`
    });
  }

  // 清除某个用户生成的token
  async clearUserToken() {
    await this.delete({
      url: '/apitoken/user',
      errorNotice: {
        title: '网络错误',
        content: '清除token失败'
      }
    });
  }

  // 由查询参数生成url
  generateUrl(
    type: 'time' | 'user' | 'consuming',
    apiId: string,
    startTime?: string | null,
    endTime?: string | null,
    format?: string | null
  ): string {
    const baseUrl = `/api/${apiId}/statistics/${type}`;
    let argsInfo = [];

    // 获取参数名称列表
    const argsNameArr = ['type', 'apiId', 'startTime', 'endTime', 'format'];

    // 计算参数名-参数值对应数组
    for (let i = 0; i < argsNameArr.length; i++) {
      if (argsNameArr[i] !== 'apiId' && argsNameArr[i] !== 'type' && arguments[i] !== undefined) {
        argsInfo.push(`${argsNameArr[i]}=${arguments[i]}`);
      }
    }

    if (argsInfo.length === 0) {
      return baseUrl;
    } else {
      return baseUrl + '?' + `${argsInfo.join('&')}`;
    }
  }
}

export const apiApi = new ApiApi();
