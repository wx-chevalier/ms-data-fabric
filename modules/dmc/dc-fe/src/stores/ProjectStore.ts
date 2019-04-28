import { observable, computed, action, autorun } from 'mobx';
import ProjectModel from '../models/ProjectModel';
import ProjectApi from '../apis/ProjectApi';
import { ProjectType } from '../apis/interface';
import UserStore from './UserStore';
import { FieldValues } from '../components/project-form/ProjectForm';

export default class ProjectStore {
  protected projectApi = new ProjectApi();

  typeMap: Map<string, string> = new Map<string, string>();

  @observable
  projectList: ProjectModel[] = [];

  @observable
  editingProject: ProjectModel | null = null;

  @observable.ref
  projectTypeList: ProjectType[] = [];

  @observable
  selectedItem: ProjectModel | null = null;

  @computed
  get editingProjectInfo() {
    if (!this.editingProject) {
      return undefined;
    } else {
      return this.editingProject.baseInfo;
    }
  }

  @action.bound
  async selectItem(id: string) {
    this.selectedItem = await this.projectApi.getProjectById(id);
  }

  @action.bound
  clearSelectedItem() {
    this.selectedItem = null;
  }

  @action.bound
  async loadTypes() {
    this.projectTypeList = await this.projectApi.getProjectTypes();
  }

  @action.bound
  async loadProjectList() {
    this.projectList = await this.projectApi.getProjectList();
  }

  @action.bound
  async setEditingProject(project: ProjectModel) {
    await project.fetchDetail();
    this.editingProject = project;
  }

  @action.bound
  clearEditingProject() {
    this.editingProject = null;
  }

  @action.bound
  async confirmEditingProject() {
    this.clearEditingProject();
  }

  @action.bound
  async removeProjectById(id: string) {
    await this.projectApi.deleteProjectById(id);
    await this.loadProjectList();
  }

  @action.bound
  async createNewProject(data: FieldValues) {
    await this.projectApi.postProject(data);
    await this.loadProjectList();
  }

  @action.bound
  async updateEditingProject(values: FieldValues): Promise<void> {
    if (!this.editingProject) {
      return;
    }
    await this.editingProject.updateInfo(values);
    this.clearEditingProject();
  }

  @action.bound
  async loadProjectDynamic(
    pid: string,
    pageNum?: number,
    pageSize?: number,
    operateObjet?: string,
    type?: string,
    startDate?: string | null,
    endDate?: string | null
  ) {
    return await this.projectApi.getProjectDynamic(
      pid,
      pageNum,
      pageSize,
      operateObjet,
      type,
      startDate,
      endDate
    );
  }

  @action.bound
  async loadDynamicType() {
    return await this.projectApi.getDynamicType();
  }

  constructor(protected userStore: UserStore) {
    this.userStore = userStore;
    autorun(async () => {
      this.projectList = [];
      if (this.userStore.isLogin) {
        if (this.userStore.isApiManager) {
          this.loadTypes();
        }
        this.loadProjectList();
      }
    });
  }
}
