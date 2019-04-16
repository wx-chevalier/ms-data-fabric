import * as React from 'react';
import { Select } from 'antd';
import { DatabaseModel } from '../../../models/datasource';
import { TableModel } from '../../../models/datasource/TableModel';
import { ColumnModel } from '../../../models/datasource/ColumnModel';
import { SearchTree, TreeNode } from '../../search-tree';

import './DatabaseTreeView.scss';

const Option = Select.Option;

interface DatabaseTreeViewProps {
  databaseList: DatabaseModel[];

  selectedDatabase?: DatabaseModel;

  selectDatabase: (db: DatabaseModel | null) => void;
}

interface DatabaseTreeViewState {
  expandedKeys?: string[];
}

/**
 * 数据库选择、数据库详情展示
 */
export default class DatabaseTreeView extends React.Component<
  DatabaseTreeViewProps,
  DatabaseTreeViewState
> {
  constructor(props: DatabaseTreeViewProps) {
    super(props);
    this.state = {};
  }

  selectDb(db: DatabaseModel | null) {
    this.props.selectDatabase(db);
  }

  changeExpandedKeys = (keys: string[]) => {
    this.setState({ expandedKeys: keys });
  };

  render() {
    let selectedDb = this.props.selectedDatabase;

    // 数据库选择
    const dbSelection = (
      <Select
        style={{ width: '100%' }}
        placeholder="数据库名称/数据库类型"
        onChange={dbId => this.selectDb(this.findDbById(dbId as string))}
        defaultValue={selectedDb ? selectedDb.dbId : undefined}
      >
        {this.props.databaseList.map(db => {
          return (
            <Option value={db.dbId} key={db.dbId}>
              {`${db.name}/${db.databaseType}`}
            </Option>
          );
        })}
      </Select>
    );

    return (
      <div className="database-tree-view">
        <div className="&-content-wrapper">
          {/** 数据库选择 */}
          <div className="&-db-selection">{dbSelection}</div>
          <div className="&-table-list">
            <section className="&-content">
              {/** 选中数据库的展示 */}
              {selectedDb && (
                <SearchTree
                  data={this.generateSearchTreeData()}
                  showSearch={true}
                  defaultExpandedKeys={this.state.expandedKeys}
                  onChangeExpandedKeys={keys => this.changeExpandedKeys(keys)}
                />
              )}
            </section>
          </div>
        </div>
      </div>
    );
  }

  private findDbById(dbId: string): DatabaseModel | null {
    const dbList = this.props.databaseList;
    for (let db of dbList) {
      if (db.dbId === dbId) {
        return db;
      }
    }
    return null;
  }

  // 将tableList转换成SearchTree需要的数据
  private generateSearchTreeData = (): TreeNode[] => {
    const selectedDb = this.props.selectedDatabase;
    if (selectedDb) {
      return selectedDb.tableList!.map(
        (item: TableModel, index: number): TreeNode => {
          return {
            key: `${item.tableName}-${index}`,
            title: item.tableName,
            children: item.columnList.map(
              (c: ColumnModel, i: number): TreeNode => {
                return {
                  key: `${c.columnName}-${i}`,
                  title: `${c.columnName}　/${c.columnType}`
                };
              }
            )
          };
        }
      );
    } else {
      return [];
    }
  };
}
