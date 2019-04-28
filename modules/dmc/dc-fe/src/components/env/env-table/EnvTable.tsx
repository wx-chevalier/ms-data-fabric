import React from 'react';
import { Table } from 'antd';
import { ColumnProps, TableProps } from 'antd/lib/table';
import EnvModel from '../../../models/EnvModel';
import { observer } from 'mobx-react';

export type TableColumns = ColumnProps<EnvModel>[];

const defaultColumns: TableColumns = [
  {
    key: 'envName',
    title: '环境名称',
    dataIndex: 'envName'
  },
  {
    key: 'createdAt',
    title: '创建时间',
    dataIndex: 'createdAt'
  }
];

interface EnvTableProps extends TableProps<EnvModel> {
  action?: TableColumns;
}

const EnvTable: React.StatelessComponent<EnvTableProps> = ({
  dataSource,
  action,
  columns,
  ...rest
}) => {
  let finalColumns: TableColumns;

  if (!!columns && !action) {
    finalColumns = columns;
  } else {
    finalColumns = [...defaultColumns, ...(action || [])];
  }
  return <Table dataSource={dataSource} rowKey={r => r.id} {...rest} columns={finalColumns} />;
};

export default observer(EnvTable);
