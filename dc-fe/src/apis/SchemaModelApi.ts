import BaseApi from './BaseApi';
import { Model } from './interface';
import { SchemaObject } from './OpenApi3Interface';

export default class SchemaModelApi extends BaseApi {
  // 根据projectId获取模型列表
  async getSchemaModelList(pid: string): Promise<Model[]> {
    return await this.get<Model[]>({
      url: `/project/${pid}/model`
    });
  }

  /**
   * 新增模型
   * @param pid projectId
   * @param name 名称
   * @param modelDefinition 模型定义
   * @param description 描述
   */
  async createModel(
    pid: string,
    name: string,
    modelDefinition: SchemaObject,
    description: string
  ): Promise<Model> {
    return await this.post<Model>({
      url: '/model',
      data: {
        modelDefinition: modelDefinition,
        modelDescribe: description,
        modelName: name,
        projectId: pid
      }
    });
  }

  /**
   * 获取模型信息
   * @param modelId
   */
  async getModelInfo(modelId: string): Promise<Model> {
    const res = await this.get<Model>({
      url: `/model/${modelId}`
    });
    return res;
  }

  /**
   * 删除模型
   * @param modelId
   */
  async deleteModel(modelId: string) {
    await this.delete({
      url: `/model/${modelId}`
    });
  }

  /**
   * 更新模型信息
   */
  async updateModel(
    modelId: string,
    name: string,
    modelDefinition: SchemaObject,
    description?: string
  ): Promise<Model> {
    const res = await this.patch<Model>({
      url: `/model/${modelId}`,
      data: {
        modelDefinition,
        modelDescribe: description,
        modelName: name
      }
    });
    return res;
  }
}
