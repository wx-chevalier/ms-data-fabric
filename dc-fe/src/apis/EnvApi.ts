import { ROUTE_API } from './RouteApi';
import BaseApi from './BaseApi';
import EnvModel from '../models/EnvModel';
import { Env } from '../apis/interface';

export default class EnvApi extends BaseApi {
  async getListByProjectId(pid: string): Promise<EnvModel[]> {
    const result = await this.get<Env[]>({
      url: `/project/${pid}/environment`,
      errorNotice: {
        title: '网络错误',
        content: '获取项目环境列表失败'
      }
    });
    return [...result.map(e => new EnvModel(e))];
  }

  async createEnv(
    pid: string,
    envName: string,
    envValue: string,
    description?: string
  ): Promise<EnvModel> {
    const result = await this.post<EnvModel>({
      url: '/environment',
      data: {
        envName,
        envValue,
        remark: description,
        projectId: pid
      }
    });
    ROUTE_API.refresh();
    return result;
  }

  async updateEnv(
    envId: string,
    pid: string,
    envName?: string,
    envValue?: string,
    description?: string
  ): Promise<EnvModel> {
    const result = await this.patch<EnvModel>({
      url: `/environment/${envId}`,
      data: {
        envName,
        envValue,
        remark: description,
        projectId: pid
      }
    });
    ROUTE_API.refresh();
    return result;
  }

  async deleteEnvList(envIdList: string[]): Promise<number> {
    const result = await this.delete<{ count: number }>({
      url: '/environment',
      data: envIdList
    });
    ROUTE_API.refresh();
    return result.count;
  }
}
