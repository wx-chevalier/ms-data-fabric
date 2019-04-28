/**
 * 图数据 -> API 数据
 */
import { Table, Column, BasicCondition, CompoundCondition, JoinCondition } from './interface';
import {
  TableGraphDef,
  AliasColumn,
  BasicCond,
  CompoundCond,
  JoinCond
} from '../../../apis/interface/api';

export function adaptColumn(column: Column): AliasColumn {
  return { name: column.name, alias: column.alias };
}

export function adaptCond(cond: BasicCondition | CompoundCondition): BasicCond | CompoundCond {
  switch (cond.type) {
    case 'basic':
      return {
        type: 'basic',
        left: adaptColumn(cond.left),
        operator: cond.operator,
        right: cond.right
      };
    case 'compound':
      return {
        type: 'compound',
        operator: cond.operator,
        left: adaptCond(cond.left),
        right: adaptCond(cond.right)
      };
    default:
      throw new Error();
  }
}

export function adaptTable(src: Table): TableGraphDef {
  return {
    type: 'TABLE',
    id: src.id,
    name: src.name,
    columns: src.columns.filter(c => c.checked).map(adaptColumn),
    conditions: src.conditions.filter(c => c.checked).map(adaptCond),
    database: src.database
  };
}

export function adaptJoinCond(cond: JoinCondition): JoinCond {
  return {
    type: 'join',
    leftTableGraphId: cond.leftTableId,
    left: cond.left,
    rightTableGraphId: cond.rightTableId,
    right: cond.right
  };
}

// export function adaptJoin(join: Join): JoinGraphDef {
//   return {
//     type: 'JOIN',
//     left: adaptTable(join.left),
//     right: adaptTable(join.right),
//     conditions: join.conditions.map(adaptJoinCond)
//   };
// }
