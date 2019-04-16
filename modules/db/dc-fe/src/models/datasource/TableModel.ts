import { DatabaseModel } from './DatabaseModel';
import { ColumnModel } from './ColumnModel';

export class TableModel {
  database: DatabaseModel;
  id: string;
  columnList: ColumnModel[];

  constructor(public tableName: string, database?: DatabaseModel, columnList?: ColumnModel[]) {
    if (database != null) {
      this.database = database;
    }
    this.columnList = columnList || [];
    this.columnList.forEach(c => {
      c.table = this;
      c.id = `${this.id}.${c.columnName}`;
    });
  }

  get dataForG6() {
    return {
      id: this.id,
      name: this.tableName,
      columns: this.columnList.map(c => c.dataForG6),
      database: this.database.dataForG6,
      conditions: []
    };
  }
}
