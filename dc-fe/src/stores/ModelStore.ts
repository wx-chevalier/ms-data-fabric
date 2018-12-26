import { observable, action } from 'mobx';
import SchemaModelApi from '../apis/SchemaModelApi';
import { Model } from '../apis/interface';
import { SchemaObject } from '../apis/OpenApi3Interface';

export default class ModelStore {
  schemaModelApi: SchemaModelApi = new SchemaModelApi();

  @observable schemaModelList: Model[] = [];

  @action.bound
  async getSchemaModelList(pid: string) {
    this.schemaModelList = await this.schemaModelApi.getSchemaModelList(pid);
  }

  @action.bound
  async createModel(pid: string, name: string, modelDefinition: SchemaObject, description: string) {
    await this.schemaModelApi.createModel(pid, name, modelDefinition, description);
  }

  @action.bound
  async updateModel(
    modelId: string,
    name: string,
    modelDefinition: SchemaObject,
    description?: string
  ) {
    await this.schemaModelApi.updateModel(modelId, name, modelDefinition, description);
  }

  @action.bound
  async deleteModel(modelId: string) {
    await this.schemaModelApi.deleteModel(modelId);
  }

  @action.bound
  async getModelInfo(modelId: string) {
    return await this.schemaModelApi.getModelInfo(modelId);
  }
}
