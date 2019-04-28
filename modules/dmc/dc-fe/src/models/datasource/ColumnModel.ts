import { TableModel } from './TableModel';

export class ColumnModel {
  table: TableModel;
  id: string;

  constructor(public columnName: string, public columnType: string, table?: TableModel) {
    if (table != null) {
      this.table = table;
    }
  }

  get dataForG6() {
    return {
      name: this.columnName || '',
      type: this.columnType || ''
    };
  }
}
