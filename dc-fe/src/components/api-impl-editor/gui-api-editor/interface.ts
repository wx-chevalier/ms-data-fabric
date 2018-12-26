import { Database } from '../../../apis/interface/datasource';

export interface Point {
  x: number;
  y: number;
}

export interface Column {
  name: string;
  type: string;
  alias?: string;
  checked?: boolean;
  text?: string;
}

export interface AbstractCondition {
  isCondition: true;
  operator: string;
  checked?: boolean;
  text?: string;
}

export interface BasicCondition extends AbstractCondition {
  type: 'basic';
  left: Column;
  right: string;
}

export interface CompoundCondition extends AbstractCondition {
  type: 'compound';
  left: BasicCondition | CompoundCondition;
  right: BasicCondition | CompoundCondition;
}

export interface JoinCondition extends AbstractCondition {
  type: 'join';
  leftTableId: string;
  left: Column;
  rightTableId: string;
  right: Column;
}

export type TableCondition = BasicCondition | CompoundCondition;

export interface Table {
  name: string;
  id: string;
  columns: Column[];
  conditions: (BasicCondition | CompoundCondition)[];
  database: Database;
}

export interface Join {
  left: Table;
  right: Table;
  conditions: JoinCondition[];
}
