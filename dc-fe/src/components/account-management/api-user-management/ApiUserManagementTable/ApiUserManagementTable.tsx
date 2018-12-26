import React from 'react';
import { Table } from 'antd';
import { ColumnProps, TableProps } from 'antd/lib/table';
import { observer } from 'mobx-react';
import ApiUserManagementTableModel from '../../../../models/ApiUserManagementModel';

export type TableColumns = ColumnProps<ApiUserManagementTableModel>[];

interface ApiUserManagementTableProps extends TableProps<ApiUserManagementTableModel> {
  action?: TableColumns;
}

const ApiUserManagementTable: React.StatelessComponent<ApiUserManagementTableProps> = ({
  dataSource,
  action,
  columns,
  ...rest
}) => {
  return <Table dataSource={dataSource} rowKey={r => r.user.id} {...rest} columns={columns} />;
};

export default observer(ApiUserManagementTable);
