import { observable, action, computed } from 'mobx';
import EnvModel, { EnvKVModel } from '../models/EnvModel';
import EnvApi from '../apis/EnvApi';

export default class EnvStore {
  envApi: EnvApi = new EnvApi();

  @observable
  envList: EnvModel[] = [];

  // 编辑现有环境时的相关数据
  @observable
  editingEnv: EnvModel;

  @observable
  searchText: string = '';

  @action.bound
  editEnvById(envId: string): EnvModel | null {
    for (let env of this.envList) {
      if (env.id === envId) {
        this.editEnv(env);
        return env;
      }
    }
    return null;
  }

  @action.bound
  editEnv(env: EnvModel) {
    this.editingEnv = env;
  }

  // 根据projectId获取该项目下的环境
  async loadEnvListByPid(pid: string) {
    this.envList = await this.envApi.getListByProjectId(pid);
  }

  // 根据环境id数组删除环境
  async delete(envIdList: string[]) {
    await this.envApi.deleteEnvList(envIdList);
  }

  async create(pid: string, envName: string, envValue: EnvKVModel[], description?: string) {
    let v: { [key: string]: string } = {};
    envValue.forEach(t => (v[t.envKey] = t.envValue));
    return await this.envApi.createEnv(pid, envName, JSON.stringify(v), description);
  }

  async update(
    envId: string,
    pId: string,
    envName?: string,
    envValue?: EnvKVModel[],
    description?: string
  ) {
    if (envName == null && envValue == null) {
      return;
    }
    let tmp = undefined;
    if (envValue) {
      let v: { [key: string]: string } = {};
      envValue.forEach(t => (v[t.envKey] = t.envValue));
      tmp = JSON.stringify(v);
    }
    await this.envApi.updateEnv(envId, pId, envName, tmp, description);
  }

  @computed
  get filteredData(): EnvModel[] {
    const reg = new RegExp(this.searchText, 'gi');
    return this.searchText === ''
      ? this.envList.slice()
      : this.envList.slice().filter(i => i.envName.match(reg));
  }

  @action.bound
  changeSearchText(v: string) {
    this.searchText = v;
  }

  // 直接对编辑中的kvList进行操作
  @computed
  get editingKVList() {
    return this.editingEnv.envValue;
  }
}
