import React from 'react';
import { Table } from 'antd';
import { ColumnProps, TableProps } from 'antd/lib/table';
import DocModel from '../../../models/DocModel';
import { observer } from 'mobx-react';

export type TableColumns = ColumnProps<DocModel>[];
const defaultColumns: TableColumns = [
  {
    key: 'creater',
    title: '创建者',
    dataIndex: 'creater',
    render: (text, record) => <span>{record.creater.username}</span>
  },
  {
    key: 'createdAt',
    title: '创建时间',
    dataIndex: 'createdAt'
  },
  {
    key: 'updatedAt',
    title: '更新时间',
    dataIndex: 'updatedAt'
  }
];

interface DocTableProps extends TableProps<DocModel> {
  docName: TableColumns;
  action?: TableColumns;
}

const DocTable: React.StatelessComponent<DocTableProps> = ({
  dataSource,
  docName,
  action,
  columns,
  ...rest
}) => {
  let finalColumns: TableColumns;
  if (!!columns && !action) {
    finalColumns = columns;
  } else {
    finalColumns = [...docName, ...defaultColumns, ...(action || [])];
  }
  return <Table dataSource={dataSource} rowKey={r => r.id} {...rest} columns={finalColumns} />;
};

export default observer(DocTable);
