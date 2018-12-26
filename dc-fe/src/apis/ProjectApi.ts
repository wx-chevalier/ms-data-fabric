import BaseApi from './BaseApi';
import { userApi } from './UserApi';
import ProjectModel from '../models/ProjectModel';
import { Project, ProjectType, Dynamic, User } from './interface';
import { FieldValues } from '../components/project-form/ProjectForm';
import DynamicModel, { DynamicDetailModel } from '../models/DynamicModel';
import UserModel from '../models/UserModel';

export default class ProjectApi extends BaseApi {
  /**
   * 项目列表，后端会根据当前登陆用户返回对应项目信息
   */
  async getProjectList(): Promise<ProjectModel[]> {
    const result = await this.get<Project[]>({
      url: '/project',
      errorNotice: {
        title: '网络错误',
        content: '加载项目失败'
      }
    });
    return result.map(p => new ProjectModel(p));
  }

  /**
   * 项目详情
   */
  async getProjectById(id: string): Promise<ProjectModel> {
    const result = await this.get<Project>({
      url: `/project/${id}`,
      errorNotice: {
        title: '网络错误',
        content: '获取项目详情失败'
      }
    });

    const dynamicInfo = await this.getProjectDynamic(id);
    return new ProjectModel(result, dynamicInfo.total);
  }

  /**
   * 获取所有项目类型信息，更新 ProjectModel 中类型名、类型描述之间的映射
   */
  async getProjectTypes(): Promise<ProjectType[]> {
    const typeNames = await this.get<string[]>({
      url: '/project/type',
      errorNotice: {
        title: '网络错误',
        content: '获取所有项目类型失败'
      }
    });
    let result: ProjectType[] = [];
    for (let n of typeNames) {
      let desc;
      switch (n) {
        case 'OTHERS':
          desc = '其它项目';
          break;
        case 'PC':
          desc = 'PC 项目';
          break;
        case 'WEB':
          desc = 'WEB 项目';
          break;
        case 'APP':
          desc = 'APP 项目';
          break;
        case 'IOT':
          desc = 'IOT 项目';
          break;
        default:
          continue;
      }
      ProjectModel.typeMap.set(n, desc);
      ProjectModel.typeMap.set(desc, n);
      result.push({
        typeName: n as string,
        typeDescribe: desc
      });
    }
    return result;
  }

  async postProject(data: FieldValues): Promise<ProjectModel> {
    const result = await this.post<Project>({
      url: '/project',
      data: this.transformData(data)
    });
    return new ProjectModel(result);
  }

  async updateProject(id: string, data: FieldValues): Promise<Project> {
    const result = await this.patch<Project>({
      url: `/project/${id}`,
      data: {
        id,
        ...this.transformData(data)
      }
    });
    return result;
  }

  async deleteProjectById(id: string) {
    await this.delete<Project>({
      url: `/project/${id}`
    });
  }

  async getProjectDynamic(
    projectId: string,
    pageNum?: number,
    pageSize?: number,
    opObject?: string,
    type?: string,
    startDate?: string | null,
    endDate?: string | null
  ): Promise<DynamicModel> {
    const result = await this.get<Dynamic>({
      url: this.generateDynamicUrl(
        projectId,
        pageNum,
        pageSize,
        opObject,
        type,
        startDate,
        endDate
      ),
      errorNotice: {
        title: '网络错误',
        content: '获取项目动态失败'
      }
    });
    let res = new DynamicModel(result);
    res.projectDynamics = await Promise.all(
      result.projectDynamics.map(async i => {
        let r = new DynamicDetailModel(i);
        r.createUser = new UserModel((await userApi.getUserInfo(i.createUser.id)) as User);
        return r;
      })
    );
    return res;
  }

  async getDynamicType() {
    return await this.get<{
      operateObject: string;
      type: string;
      typeName: string;
    }>({
      url: `/projectdynamic/type`
    });
  }

  transformData(data: FieldValues): Partial<Project> {
    return {
      name: data.name,
      type: data.type,
      version: data.version,
      description: data.description
    };
  }

  generateDynamicUrl(
    projectId: string,
    pageNum?: number,
    pageSize?: number,
    opObject?: string,
    type?: string,
    startDate?: string | null,
    endDate?: string | null
  ): string {
    const baseUrl = `/project/${projectId}/dynamic`;
    let argsInfo = [];

    // 获取参数名称列表
    const argsNameArr = [
      'projectId',
      'pageNum',
      'pageSize',
      'opObject',
      'type',
      'startDate',
      'endDate'
    ];

    // 计算参数名-参数值对应数组
    for (let i = 0; i < argsNameArr.length; i++) {
      if (argsNameArr[i] !== 'projectId' && arguments[i] !== undefined) {
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
