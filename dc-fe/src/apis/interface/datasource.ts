import { Dialect } from '../../models/datasource/DatabaseModel';

/**
 * 数据源
 */
export interface DataSource {
  id?: string;
  name?: string;
  schemaList?: Database[];
}

/**
 * 数据源 - 数据库信息
 */
export interface Database {
  name: string;
  type: Dialect;
  dataSource: DataSource;
  createDatetime: string;
  tableList?: Table[];
  authUsers?: string[];
}

/**
 * 数据源 - 数据库表
 */
export interface Table {
  name: string;
  columnList: Column[];
}

/**
 * 数据源 - 数据库列
 */
export interface Column {
  name: string;
  type: string;
}
