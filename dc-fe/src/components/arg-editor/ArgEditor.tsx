import React, { Component } from 'react';
import './ArgEditor.scss';
import { Row, Col, Select, Input } from 'antd';
import { SchemaModel } from '../../models/SchemaModel';
const Option = Select.Option;

interface ArgEditorProps {
  arg: { name: string; data: SchemaModel };
}

interface ArgEditorState {}

export default class ArgEditor extends Component<ArgEditorProps, ArgEditorState> {
  handleChange = (key: 'type' | 'example') => (value: string) => {
    const { data } = this.props.arg;
    if (key === 'example' && (data.type === 'number' || data.type === 'integer')) {
      data[key] = Number(value);
    } else {
      data[key] = value;
    }
    this.forceUpdate();
  };

  render() {
    const arg = this.props.arg;
    return (
      <Row className="arg-editor">
        <Col className="&-arg-name" span={4}>
          {arg.name}
        </Col>
        <Col className="&-type-selector" span={4}>
          <Select
            value={arg.data.type}
            onChange={this.handleChange('type')}
            style={{ width: '100%' }}
          >
            <Option value="integer">integer</Option>
            <Option value="number">number</Option>
            <Option value="string">string</Option>
            <Option value="boolean">boolean</Option>
          </Select>
        </Col>
        <Col span={15}>
          <Input
            value={arg.data.example}
            onChange={e => this.handleChange('example')(e.target.value)}
            style={{ marginLeft: '10px' }}
          />
        </Col>
      </Row>
    );
  }
}
