import { DatabaseModel } from './DatabaseModel';
import { DataSource } from '../../apis/interface';

export default class DataSourceModel {
  id?: string;
  name: string;
  schemaList: DatabaseModel[];

  constructor(data: DataSource) {
    this.name = data.name!;
    this.schemaList = data.schemaList!.map(
      s =>
        new DatabaseModel(s.name, s.dataSource.id!, s.type, s.createDatetime, undefined, undefined)
    );
  }
}
