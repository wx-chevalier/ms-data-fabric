import BaseModel from './BaseModel';
import { Project } from '../apis/interface';
import { FieldValues } from '../components/project-form/ProjectForm';
import ProjectApi from '../apis/ProjectApi';
import { observable, computed } from 'mobx';

export default class ProjectModel extends BaseModel {
  static typeMap: Map<string, string> = new Map<string, string>();

  projectApi: ProjectApi = new ProjectApi();

  @observable
  name: string;
  @observable
  version: string;
  @observable
  type: string;
  @observable
  description: string;
  @observable
  apiList: { id: string }[];
  @observable
  doc: { id: string }[];
  @observable
  dynamicNum?: number;

  @computed
  get apiNumber(): number {
    if (this.apiList) {
      return this.apiList.length;
    } else {
      return 0;
    }
  }

  @computed
  get docNumber(): number {
    if (this.doc) {
      return this.doc.length;
    } else {
      return 0;
    }
  }

  assign(data: Project, dynamicNum?: number) {
    this.name = data.name;
    this.version = data.version;
    this.type = data.type;
    this.description = data.description;
    this.createdAt = data.createDatetime;
    this.updatedAt = data.modifyDatetime;
    this.apiList = data.apiList;
    this.doc = data.docList;
    this.dynamicNum = dynamicNum;
  }

  constructor(data: Project, dynamicNum?: number) {
    super(data);
    this.assign(data, dynamicNum);
  }

  get baseInfo() {
    return {
      name: this.name,
      type: this.type,
      version: this.version,
      description: this.description
    };
  }

  async updateInfo(data: FieldValues) {
    const result = await this.projectApi.updateProject(this.id, data);
    const dynamic = await this.projectApi.getProjectDynamic(this.id);
    this.assign(result, dynamic.total);
  }

  async fetchDetail() {
    const result = await this.projectApi.getProjectById(this.id);
    const { name, version, type, description, createdAt, updatedAt, apiList, doc } = result;
    Object.assign(this, {
      name,
      version,
      type,
      description,
      createdAt,
      updatedAt,
      apiList,
      doc
    });
  }
}
