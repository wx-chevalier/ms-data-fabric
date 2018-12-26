import React, { Component } from 'react';
import { Divider, Button, Input, Table, Cascader } from 'antd';
import { ColumnProps } from 'antd/lib/table';
import './ObjectEditor.scss';
import { SchemaObject, primitiveType } from '../../../apis/OpenApi3Interface';
import typeAndFormat from '../meta-form/typeAndFormat';
import { filterUndefined } from '../../../utils';

type Property = SchemaObject & { name: string };
export type TableColumns = ColumnProps<Property>[];

interface ObjectEditorProps {
  schema: SchemaObject;
  onEnterProperty: (pName?: string) => void;
}

interface ObjectEditorState {
  inputValue: string;
  schemaCache: SchemaObject;
}

export default class ObjectEditor extends Component<ObjectEditorProps, ObjectEditorState> {
  state = {
    inputValue: '',
    schemaCache: {}
  };

  static getDerivedStateFromProps(props: ObjectEditorProps, state: ObjectEditorState) {
    const { schema } = props;
    if (!schema.properties) {
      schema.properties = {};
    }
    if (schema !== state.schemaCache) {
      return {
        schemaCache: schema,
        inputText: ''
      };
    } else {
      return null;
    }
  }

  /**
   * 新增标识参数r，用于判断是否需要进行路由跳转
   */
  handleAdd = (p: string, r: boolean = true) => {
    const { schema, onEnterProperty } = this.props;
    const properties = schema.properties!;
    if (!properties[p]) {
      properties[p] = {};
    }
    if (r) {
      onEnterProperty(p);
    } else {
      onEnterProperty();
    }
  };

  handleDelete = (k: string) => {
    const { schema, onEnterProperty } = this.props;
    delete schema.properties![k];
    onEnterProperty();
  };

  handleInput: React.ChangeEventHandler<HTMLInputElement> = e => {
    const value = e.target.value;
    this.setState({
      inputValue: value
    });
  };

  handleTypeChange = (tableRow: Property, v: string[]) => {
    const property = tableRow.name;
    const [type, format] = v;
    const traget = this.props.schema.properties![property];
    traget.type = type as primitiveType;
    traget.format = format;
    filterUndefined(traget);
    this.props.onEnterProperty();
  };

  render() {
    const { inputValue } = this.state;
    const columns: TableColumns = [
      {
        key: 'name',
        title: '属性名称',
        dataIndex: 'name'
      },
      {
        key: 'type',
        title: '类型',
        render: (t, r) => (
          <Cascader
            options={typeAndFormat}
            value={[r.type, r.format].filter(v => !!v) as string[]}
            onChange={v => this.handleTypeChange(r, v)}
          />
        )
      },
      {
        key: 'action',
        title: '操作',
        render: (t, r) => (
          <div>
            <span onClick={() => this.handleAdd(r.name)} className="action-edit">
              编辑
            </span>
            <Divider type="vertical" />
            <span onClick={() => this.handleDelete(r.name)} className="action-delete">
              删除
            </span>
          </div>
        )
      }
    ];
    const properties = this.props.schema.properties!;
    const tableData = Object.keys(properties).map(k => ({
      name: k,
      ...properties[k]
    }));
    return (
      <div className="object-editor">
        <div className="&-add">
          <Input
            style={{ width: 300, marginRight: 32 }}
            placeholder="请输入属性名称"
            value={inputValue}
            onChange={this.handleInput}
          />
          <Button
            type="primary"
            onClick={() => this.handleAdd(inputValue, false)}
            disabled={!inputValue}
          >
            添加属性
          </Button>
        </div>
        <div className="&-p-list">
          <Table
            rowKey={r => r.name}
            columns={columns}
            dataSource={tableData}
            size="small"
            pagination={{ defaultPageSize: 5 }}
          />
        </div>
      </div>
    );
  }
}
