import { RelGraphImplDef } from './../apis/interface/api/apiimpl';
import { action, observable, computed } from 'mobx';
import { AbstractAPIModification, SQLImplDef, GraphDef } from '../apis/interface/api';
import { DatabaseModel } from '../models/datasource';
import { ArgModel } from '../models/SchemaModel';
import { apiApi } from '../apis';
import { apiImplDevApi } from '../apis/APIImplDevApi';
import { ObjectSchema } from '../models/oas3/media/ObjectSchema';

/**
 * 接口实现编辑
 */
export default class APIImplEditorStore {
  // 接口实现模式
  @observable
  implMode: 'SQL' | 'Graph' = 'Graph';

  // 编辑区 SQL
  @observable
  sql: string;

  // 图形界面图绘制数据，人工管理该状态的更新同步，不要保存自动刷新
  graphData: Object;

  // 图模式接口定义
  graphDef: GraphDef;

  // SQL 编辑：单表查询模式
  @observable
  singleDatabaseMode: boolean = true;

  // 选中进行单库操作的库
  @observable
  selectedDatabase: DatabaseModel | null = null;

  // 解析得到的参数 Schema
  @observable
  parameterSchema: ArgModel | null = null;

  // 接口测试返回结果
  @observable
  response: string;

  // 接口测试参数
  @computed
  get paramTestSchema(): ObjectSchema {
    let v = new ObjectSchema();
    if (this.parameterSchema) {
      v.properties = this.parameterSchema.properties!;
    }
    return v;
  }

  @action.bound
  async toggleImplMode() {
    this.implMode = this.implMode === 'SQL' ? 'Graph' : 'SQL';
  }

  @action.bound
  toggleSingleDatabaseMode() {
    this.singleDatabaseMode = !this.singleDatabaseMode;
  }

  @action.bound
  selectDatabase(db: DatabaseModel | null) {
    this.selectedDatabase = db;
  }

  @action.bound
  updateSQL(sql: string) {
    this.parameterSchema = null;
    this.sql = sql;
  }

  @action.bound
  createAPI(projectId: string, name: string, description?: string, folderId?: string) {
    let info: AbstractAPIModification = {
      name,
      summary: description,
      projectId,
      folderId
    };
    switch (this.implMode) {
      case 'SQL':
        return this.createBySQL(info);
      case 'Graph':
        return this.createByRelGraph(info);
      default:
        throw new Error(`Unknown mode ${this.implMode}`);
    }
  }

  // 解析实现
  @action.bound
  async parseImpl() {
    switch (this.implMode) {
      case 'SQL':
        await this.parseSQLImpl();
        break;
      case 'Graph':
        await this.parseGraphImpl();
        break;
      default:
        break;
    }
  }

  // 测试接口实现
  @action.bound
  async sendTestRequest() {
    // { type: 'SQL', dialect, sql, datasourceName }
    if (this.implMode === 'SQL') {
      if (this.selectedDatabase != null) {
        this.response = await apiImplDevApi.exec(
          {
            type: 'SQL',
            dialect: this.selectedDatabase.databaseType,
            sql: this.sql,
            datasourceName: this.selectedDatabase!.datasourceName
          },
          this.paramTestSchema
        );
      } else {
        this.response = await apiImplDevApi.exec(
          { type: 'SQL', dialect: 'presto', sql: this.sql },
          this.paramTestSchema
        );
      }
    } else {
      this.response = await apiImplDevApi.exec(
        { type: 'REL_GRAPH', graph: JSON.stringify(this.graphData), def: this.graphDef },
        this.paramTestSchema
      );
    }
  }

  private async parseSQLImpl() {
    if (this.singleDatabaseMode) {
      if (this.selectedDatabase == null) {
        throw new Error('单库模式未指定数据库');
      }
      this.parameterSchema = await apiImplDevApi.analyzeParameter({
        type: 'SQL',
        dialect: this.selectedDatabase.databaseType,
        sql: this.sql,
        datasourceName: this.selectedDatabase!.datasourceName
      });
    } else {
      this.parameterSchema = await apiImplDevApi.analyzeParameter({
        type: 'SQL',
        dialect: 'presto',
        sql: this.sql
      });
    }
  }

  private async parseGraphImpl() {
    this.parameterSchema = await apiImplDevApi.analyzeParameter({
      type: 'REL_GRAPH',
      graph: JSON.stringify(this.graphData),
      def: this.graphDef
    });
  }

  private async createByRelGraph(apiInfoFields: AbstractAPIModification) {
    let impl: RelGraphImplDef = {
      type: 'REL_GRAPH',
      graph: JSON.stringify(this.graphData),
      def: this.graphDef
    };
    return await apiApi.create({
      ...apiInfoFields,
      type: 'GENERATED',
      impl
    });
  }

  private async createBySQL(apiInfoFields: AbstractAPIModification) {
    let impl: SQLImplDef;
    if (this.singleDatabaseMode) {
      if (!this.selectedDatabase) {
        throw new Error('单库模式未指定数据库');
      }
      impl = {
        type: 'SQL',
        sql: this.sql,
        dialect: this.selectedDatabase.databaseType,
        datasourceName: this.selectedDatabase.datasourceName
      };
    } else {
      impl = {
        type: 'SQL',
        sql: this.sql,
        dialect: 'presto'
      };
    }
    return await apiApi.create({
      ...apiInfoFields,
      type: 'GENERATED',
      impl
    });
  }
}
