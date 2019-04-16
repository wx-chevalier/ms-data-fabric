import React from 'react';
import { Table } from 'antd';
import { ColumnProps, TableProps } from 'antd/lib/table';
import { DynamicDetailModel } from '../../../models/DynamicModel';
import { observer } from 'mobx-react';
import { OPERATEOBJECTDICT, ACTIONTYPEDICT } from '../ProjectDynamic';

export type TableColumns = ColumnProps<DynamicDetailModel>[];

const columns: TableColumns = [
  {
    key: 'operateObject',
    title: '操作对象',
    dataIndex: 'operateObject',
    render: (text, record) => {
      const index = record.type.indexOf('_');
      const operateObject = record.type.slice(index + 1);
      return <span>{OPERATEOBJECTDICT[operateObject]}</span>;
    }
  },
  {
    key: 'type',
    title: '操作类型',
    dataIndex: 'type',
    render: (text, record) => {
      const index = record.type.indexOf('_');
      const actionType = record.type.slice(0, index);
      return <span>{ACTIONTYPEDICT[actionType]}</span>;
    }
  },
  {
    key: 'user',
    title: '操作用户',
    dataIndex: 'user',
    render: (text, record) => <span>{record.createUser.username}</span>
  },
  {
    key: 'createdAt',
    title: '操作时间',
    dataIndex: 'createdAt'
  },
  {
    key: 'content',
    title: '操作内容',
    dataIndex: 'content'
  }
];

interface DynamicTableProps extends TableProps<DynamicDetailModel> {}

const DynamicTable: React.StatelessComponent<DynamicTableProps> = ({ dataSource, ...rest }) => (
  <Table dataSource={dataSource} rowKey={r => r.id} columns={columns} {...rest} />
);

export default observer(DynamicTable);
