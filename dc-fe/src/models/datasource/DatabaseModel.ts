import { TableModel } from './TableModel';
import { ColumnModel } from './ColumnModel';
import { observable } from 'mobx';
import { ApiUserManagementUserModel } from '../UserModel';

export type Dialect = 'mysql' | 'oracle' | 'sqlserver' | 'presto';

/**
 * 数据源
 */
export class DatabaseModel {
  tableList: TableModel[] = [];

  id: string;

  createdAt: string;

  @observable
  authedUserList?: ApiUserManagementUserModel[];

  get dbId() {
    return `${this.datasourceName}=${this.name}`;
  }

  static mock(): DatabaseModel[] {
    return [
      new DatabaseModel('db1', 'ds1', 'mysql', '2018-5-1', [
        new TableModel('t0', undefined, [
          new ColumnModel('c1', 'varchar'),
          new ColumnModel('c2', 'varchar')
        ]),
        new TableModel('t1', undefined, [
          new ColumnModel('fuck', 'varchar'),
          new ColumnModel('shit', 'varchar')
        ])
      ]),
      new DatabaseModel('db2', 'ds2', 'oracle', '2018-6-1', [
        new TableModel('damn', undefined, [
          new ColumnModel('ass', 'varchar'),
          new ColumnModel('pee', 'varchar')
        ]),
        new TableModel('honey', undefined, [
          new ColumnModel('pot', 'varchar'),
          new ColumnModel('yellow', 'varchar')
        ])
      ]),
      new DatabaseModel('db3', 'ds3', 'mysql', '2018-7-1', [
        new TableModel('cock', undefined, [
          new ColumnModel('sucker', 'varchar'),
          new ColumnModel('ffffffff', 'varchar')
        ]),
        new TableModel('cunt', undefined, [
          new ColumnModel('ddddd', 'varchar'),
          new ColumnModel('aaaaaaa', 'varchar')
        ])
      ])
    ];
  }

  /**
   * @param name 数据库名
   * @param datasourceName 数据源名称
   */
  constructor(
    public name: string,
    public datasourceName: string,
    public databaseType: Dialect,
    createdAt: string,
    tableList?: TableModel[],
    authedUserList?: ApiUserManagementUserModel[]
  ) {
    this.id = `${this.datasourceName}.${this.name}`;
    if (tableList) {
      this.tableList = tableList;
      this.tableList.forEach(t => {
        t.database = this;
        t.id = `${this.id}.${t.tableName}`;
      });
    }
    this.createdAt = createdAt;
    this.authedUserList = authedUserList || [];
  }

  get dataForG6() {
    return {
      name: this.name,
      type: this.databaseType,
      dataSource: {
        name: this.datasourceName
      }
    };
  }
}
