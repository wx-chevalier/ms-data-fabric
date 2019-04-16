import { apiApi } from './../apis/ApiApi';
import { GeneratedAPIModification } from './../apis/interface/api';
import { observable, action, computed } from 'mobx';
import { DatabaseModel } from '../models/datasource';
import DatasourceApi from '../apis/DatasourceApi';
import { Dialect } from '../models/datasource/DatabaseModel';
import SQLApiApi from '../apis/SQLApiApi';
import { ArgModel } from '../models/SchemaModel';
import { ObjectSchema } from '../models/oas3/media/ObjectSchema';

export default class DataBaseStore {
  protected datasourceApi = new DatasourceApi();

  protected sqlApiApi = new SQLApiApi();

  @observable
  curFolderId: string;

  /**
   * 数据库列表
   */
  @observable
  dataBaseList: DatabaseModel[] = DatabaseModel.mock();

  /**
   * 当前选中进行查询的数据库，为 null 表示进行多库查询
   */
  @observable
  selectedDatabase: DatabaseModel | null;

  /**
   * 编辑区 SQL'
   */
  @observable
  sql: string = '';

  /**
   * 解析得到的参数 Schema
   */
  @observable
  parameterSchema: ArgModel | undefined = undefined;

  /**
   * 测试执行返回结果
   */
  @observable
  response: string = '';

  /**
   * 当前选中数据库类型
   */
  @computed
  get dialect(): Dialect | null {
    return this.selectedDatabase == null ? null : this.selectedDatabase.databaseType;
  }

  @computed
  get paramTestSchema(): ObjectSchema {
    let v = new ObjectSchema();
    if (this.parameterSchema) {
      v.properties = this.parameterSchema.properties!;
    }
    return v;
  }

  @action.bound
  selectDatabase(db: DatabaseModel | null) {
    this.selectedDatabase = db;
  }

  @action.bound
  setSql(sql: string) {
    this.sql = sql;
  }

  /** 生成接口 */
  @action
  async genInterface(
    singleDbMode: boolean,
    pid: string,
    fid: string,
    apiName: string,
    apiDescription: string,
    cb: (createdProjectId: string) => void
  ) {
    let apiModification: GeneratedAPIModification;

    if (this.sql == null || this.sql.trim() === '' || (singleDbMode && !this.dialect)) {
      throw new Error('请选择数据库，输入接口实现 SQL 语句');
    }

    apiModification = {
      name: apiName,
      summary: apiDescription,
      projectId: pid,
      folderId: fid,
      type: 'GENERATED',
      impl: {
        type: 'SQL',
        dialect: (this.dialect || 'presto') as string,
        sql: this.sql,
        datasourceName: this.selectedDatabase ? this.selectedDatabase.datasourceName : undefined
      }
    };
    try {
      let createdAPI = await apiApi.create(apiModification);

      if (createdAPI && createdAPI.id) {
        cb(createdAPI.id);
      }
    } catch (e) {
      throw new Error('接口创建失败: ' + e.message);
    }
  }

  @action.bound
  async loadDatabaseList() {
    // this.dataBaseList.replace(await this.datasourceApi.getDatabaseList());
    this.dataBaseList = await this.datasourceApi.getDatabaseList();
  }

  @action.bound
  init() {
    this.sql = '';
    this.parameterSchema = undefined;
    this.response = '';
  }

  get fId() {
    return this.curFolderId === 'ungroup' ? '' : this.curFolderId;
  }

  set fId(v: string) {
    this.curFolderId = v;
  }
}
