import { Cond, JoinCond } from './cond';
import { Database } from '../datasource';

/**
 * 接口实现
 */
export interface APIImpl {
  id: string;
  impl: APIImplDef;
}

/**
 * SQL 方式接口实现定义
 */
export interface SQLImplDef {
  type: 'SQL';
  dialect: string;
  sql: string;
  datasourceName?: string;
}

/**
 * 关系图方式接口实现定义
 */
export interface RelGraphImplDef {
  type: 'REL_GRAPH';
  graph: string;
  def: GraphDef;
}

/**
 * 接口实现定义
 */
export type APIImplDef = SQLImplDef | RelGraphImplDef;

export interface SelectedColumn {
  name: string;
  alias?: string;
}

/**
 * 图定义
 */
export interface TableGraphDef {
  type: 'TABLE';
  id: string;
  name: string;
  columns: SelectedColumn[];
  conditions: Cond[];
  database: Database;
}

export interface JoinGraphDef {
  type: 'JOIN';
  tables: TableGraphDef[];
  conds: JoinCond[];
}

export type GraphDef = TableGraphDef | JoinGraphDef;
