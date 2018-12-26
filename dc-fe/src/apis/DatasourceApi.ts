import { ColumnModel } from './../models/datasource/ColumnModel';
import BaseApi from './BaseApi';
import { Database, Table, DataSource } from './interface';
import { DatabaseModel, TableModel } from './../models/datasource';
import { Dialect } from '../models/datasource/DatabaseModel';
import DataSourceModel from '../models/datasource/DataSourceModel';

export default class DatasourceApi extends BaseApi {
  async getDataSourceList(): Promise<DataSourceModel[]> {
    const res = await this.get<DataSource[]>({
      url: '/datasource',
      errorNotice: {
        title: '网络错误',
        content: '获取数据源列表失败'
      }
    });
    return res.map(r => new DataSourceModel(r));
  }

  async getDatabaseList(): Promise<DatabaseModel[]> {
    let dbList = await this.get<Database[]>({
      url: '/datasource/database',
      errorNotice: {
        title: '网络错误',
        content: '获取数据库信息失败'
      }
    });
    let result: DatabaseModel[] = [];
    for (let db of dbList) {
      let dsName = db.dataSource ? db.dataSource.name! : '';
      let tblList: TableModel[] = [];
      try {
        tblList = await this.getTableList(db.name, dsName);
      } catch (e) {
        console.warn('获取表信息出错', e);
      }
      let tmp = new DatabaseModel(db.name, dsName, db.type as Dialect, db.createDatetime, tblList);
      result.push(tmp);
    }
    return result;
  }

  async getTableList(databaseName: string, datasourceName: string): Promise<TableModel[]> {
    let tableList = await this.get<Table[]>({
      url: `/datasource/${datasourceName}/database/${databaseName}`,
      errorNotice: {
        title: '网络错误',
        content: '获取表信息失败'
      }
    });
    return tableList.map(
      table =>
        new TableModel(
          table.name,
          undefined,
          table.columnList.map(c => new ColumnModel(c.name, c.type))
        )
    );
  }
}
