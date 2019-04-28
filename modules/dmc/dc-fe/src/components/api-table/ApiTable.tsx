import React from 'react';
import { Table, Popover } from 'antd';
import { ColumnProps, TableProps } from 'antd/lib/table';
import ApiModel from '../../models/ApiModel';
import { observer } from 'mobx-react';

export type TableColumns = ColumnProps<ApiModel>[];
const defaultColumns: TableColumns = [
  {
    key: 'name',
    dataIndex: 'name',
    title: '接口名称',
    width: 100,
    render: (text, record) => (
      <Popover content={text} placement="topLeft">
        <span
          style={{
            display: 'inline-block',
            width: '100px',
            height: '21px',
            overflow: 'hidden',
            textOverflow: 'ellipsis',
            whiteSpace: 'nowrap'
          }}
        >
          {text}
        </span>
      </Popover>
    )
  },
  {
    key: 'uri',
    dataIndex: 'uri',
    title: '接口URI',
    width: 300,
    render: (text, record) => (
      <Popover content={text} placement="topLeft">
        <span
          style={{
            display: 'inline-block',
            width: '300px',
            height: '21px',
            overflow: 'hidden',
            textOverflow: 'ellipsis',
            whiteSpace: 'nowrap'
          }}
        >
          {text}
        </span>
      </Popover>
    )
  },
  {
    key: 'verb',
    dataIndex: 'verb',
    title: '请求类型',
    width: 88
  },
  {
    key: 'updatedAt',
    dataIndex: 'updatedAt',
    title: '更新时间'
  }
];

interface ApiTableProps extends TableProps<ApiModel> {
  action?: TableColumns;
}

const ApiTable: React.StatelessComponent<ApiTableProps> = ({
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
  // 手工 observer 属性
  if (dataSource) {
    dataSource.forEach(a => a.authedText);
  } else {
    dataSource = [];
  }
  return (
    <Table dataSource={dataSource.slice()} rowKey={r => r.id} {...rest} columns={finalColumns} />
  );
};

export default observer(ApiTable);
