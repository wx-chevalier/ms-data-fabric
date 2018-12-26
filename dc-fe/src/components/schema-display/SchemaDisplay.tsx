import './SchemaDisplay.scss';

import React from 'react';
import { Table } from 'antd';
import { SchemaObject, primitiveType } from '../../apis/OpenApi3Interface';
import { TableProps } from 'antd/lib/table';
import { SchemaModel } from '../../models/SchemaModel';

interface SchemaTableProps extends TableProps<SchemaModel> {
  name: string;
  type?: primitiveType;
  required?: boolean;
  description?: string;
  example?: string;
  children?: SchemaTableProps[];
}

const columns = [
  {
    title: '参数名',
    dataIndex: 'name',
    key: 'name'
  },
  {
    title: '类型',
    dataIndex: 'type',
    key: 'type',
    width: '12%'
  },
  {
    title: '必填',
    dataIndex: 'required',
    key: 'required',
    width: '12%',
    render: (text: boolean) => {
      return <span>{text ? '是' : '否'}</span>;
    }
  },
  {
    title: '说明',
    dataIndex: 'description',
    key: 'description',
    width: '12%'
  },
  {
    title: '示例',
    dataIndex: 'example',
    key: 'example',
    width: '12%'
  }
];

interface Props {
  data: SchemaObject;
}

/**
 * schema展示组件
 */
export default class SchemaDisplay extends React.Component<Props, {}> {
  render() {
    const data = this.props.data;

    return (
      <Table
        columns={columns}
        dataSource={Object.keys(data).length !== 0 ? [this.parseSchemaToDataSource(data)] : []}
        rowKey={k => {
          return `${k.type}-${Math.random() * 10}`;
        }}
        pagination={false}
      />
    );
  }

  // schema转换成表格数据
  private parseSchemaToDataSource(data: SchemaObject, title?: string): SchemaTableProps {
    if (data.type! === 'array') {
      try {
        return {
          name: title ? title : 'param(Array type)',
          type: data.type,
          required: data.nullable ? !data.nullable : false,
          description: data.description,
          example: data.example,
          children: [this.parseSchemaToDataSource(data.items!)]
        };
      } catch (e) {
        return {
          name: title ? title : 'param(Array type)',
          type: data.type,
          required: !data.nullable,
          description: data.description,
          example: data.example
        };
      }
    } else if (data.type! === 'object') {
      try {
        return {
          name: title ? title : 'param(Object type)',
          type: data.type,
          required: data.nullable ? !data.nullable : false,
          description: data.description,
          example: data.example,
          children: Object.keys(data.properties!).map(k =>
            this.parseSchemaToDataSource(data.properties![k], k)
          )
        };
      } catch (e) {
        return {
          name: title ? title : 'param(Object type)',
          type: data.type,
          required: data.nullable ? !data.nullable : false,
          description: data.description,
          example: data.example
        };
      }
    } else {
      return {
        name: title ? title : '',
        type: data.type,
        required: data.nullable ? !data.nullable : false,
        description: data.description,
        example: data.example
      };
    }
  }
}
