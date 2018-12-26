import React from 'react';
import { Table } from 'antd';
import { ColumnProps, TableProps } from 'antd/lib/table';
import { Link } from 'react-router-dom';

import ProjectModel from '../../models/ProjectModel';

import './ProjectTable.scss';

export type TableColumns = ColumnProps<ProjectModel>[];
const defaultColumns: TableColumns = [
  {
    key: 'name',
    title: '项目名称',
    render: (text, record) => {
      return <Link to={`/project/${record.id}/`}>{record.name}</Link>;
    }
  },
  {
    key: 'version',
    dataIndex: 'version',
    title: '版本'
  },
  {
    key: 'type',
    dataIndex: 'type',
    title: '类型'
  },
  {
    key: 'updatedAt',
    dataIndex: 'updatedAt',
    title: '最后修改时间'
  }
];

interface ProjectTableProps extends TableProps<ProjectModel> {
  action?: TableColumns;
}

const ProjectTable: React.StatelessComponent<ProjectTableProps> = ({
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
  return <Table {...rest} columns={finalColumns} rowKey={r => r.id} />;
};

export default ProjectTable;
