import React, { Component } from 'react';
import './ArrayEditor.scss';
import { SchemaObject } from '../../../apis/OpenApi3Interface';
import { Button } from 'antd';

interface ArrayEditorProps {
  schema: SchemaObject;
  onEnterProperty: (pName?: string) => void;
}

interface ArrayEditorState {}

export default class ArrayEditor extends Component<ArrayEditorProps, ArrayEditorState> {
  render() {
    const schema = this.props.schema;
    if (!schema.items) {
      schema.items = {};
    }
    const items = schema.items as SchemaObject;
    return (
      <div className="array-editor">
        <span className="&-label">数组成员类型： {items.type ? items.type : '无'}</span>
        <Button type="primary" onClick={() => this.props.onEnterProperty('数组成员')}>
          {items.type ? '编辑' : '添加'}
        </Button>
      </div>
    );
  }
}
