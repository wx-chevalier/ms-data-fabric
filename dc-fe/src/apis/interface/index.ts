export { CommmonFields } from './common';
export { User, Role, Permission, QyUser } from './user';
export { Project, ProjectType, Folder, Env } from './project';
export {
  API,
  APIParam,
  APIResponse,
  APIImpl,
  APIModification,
  ProxyAPIModification,
  GeneratedAPIModification
} from './api';
import { SchemaObject } from '../OpenApi3Interface';

export { SchemaObject };

export interface SqlGenerator {
  // 绘图数据
  graphData: object;

  // 关系键值对
  // relationId 的构造为 `${sourceColumnId}-${targetColumnId}`
  // columnId 的构造为 `${datasourceName}.${databaseName}.${tableName}.${columnName}`
  relations: {
    [relationId: string]: string;
  };

  // 所选字段
  // tableId 的构造为 `${datasourceName}.${databaseName}.${tableName}`
  tables: {
    [tableId: string]: {
      name: string;
      alias: string;
      type: string;
      checked: boolean;
    }[];
  };
}

export interface Model {
  id: string;
  modelDefinition: SchemaObject;
  modelDescribe: string;
  modelName: string;
  projectId?: string;
}

export { Doc } from './doc';
export { DataSource, Database, Table, Column } from './datasource';
export { LoginToken, AccessToken } from './auth';
export { ApiAccessInfo, Dynamic, DynamicDetail } from './statistics';
