import React from 'react';
import { Table } from 'antd';
import { ColumnProps, TableProps } from 'antd/lib/table';
import { SchemaObject } from '../../apis/OpenApi3Interface';

type Arg = {
  name: string;
  data: SchemaObject;
};
export type TableColumns = ColumnProps<Arg>[];
const defaultColumns: TableColumns = [
  {
    key: 'name',
    title: '参数名',
    dataIndex: 'name'
  },
  {
    key: 'type',
    title: '类型',
    dataIndex: 'data.type'
  },
  {
    key: 'nullable',
    title: '必填',
    dataIndex: 'data.must'
  },
  {
    key: 'desc',
    title: '说明',
    dataIndex: 'data.description'
  },
  {
    key: 'example',
    title: '示例',
    dataIndex: 'data.example'
  }
];

interface ArgTableProps extends TableProps<Arg> {
  action?: TableColumns;
}

const ArgTable: React.StatelessComponent<ArgTableProps> = ({ action, columns, ...rest }) => {
  let finalColumns: TableColumns;
  if (!!columns && !action) {
    finalColumns = columns;
  } else {
    finalColumns = [...defaultColumns, ...(action || [])];
  }
  return <Table rowKey={r => r.name} {...rest} columns={finalColumns} />;
};

export default ArgTable;
