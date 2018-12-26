import { AliasColumn } from './common';
import { Column } from '../datasource';

export interface BasicCond {
  type: 'basic';
  left: AliasColumn;
  operator: string;
  right: string;
}

export interface CompoundCond {
  type: 'compound';
  operator: string;
  left: Cond;
  right: Cond;
}

export type Cond = BasicCond | CompoundCond;

export interface JoinCond {
  type: 'join';
  leftTableGraphId: string;
  left: Column;
  rightTableGraphId: string;
  right: Column;
}
