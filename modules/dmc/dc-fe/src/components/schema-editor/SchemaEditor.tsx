import React, { Component } from 'react';
import './SchemaEditor.scss';
import { SchemaObject } from '../../apis/OpenApi3Interface';
import { Alert, Divider, Button, Select } from 'antd';
import MetaForm from './meta-form/MetaForm';
import ObjectEditor from './object-editor/ObjectEditor';
import { cloneDeep } from 'lodash';
import ArrayEditor from './array-editor/ArrayEditor';
import JsonEditor from './json-editor/JsonEditor';
const Option = Select.Option;

interface SchemaEditorProps {
  initialValue: SchemaObject;
  onChange: (v: SchemaObject) => void;
  hideImport?: boolean;
}

interface SchemaEditorState {
  schemaCopy: SchemaObject;
  // jsonText: string;
  propertyStack: string[];
  jsonError: string;
  showJsonEditor: boolean;
}

export default class SchemaEditor extends Component<SchemaEditorProps, SchemaEditorState> {
  originSchema: SchemaObject;

  constructor(props: SchemaEditorProps) {
    super(props);
    // 记录原始值，用于重置
    this.originSchema = props.initialValue;
    this.state = this.init(props.initialValue);
  }

  // 初始化
  init(originSchema: SchemaObject): SchemaEditorState {
    const copy = cloneDeep(originSchema);
    return {
      schemaCopy: copy,
      propertyStack: ['模型'],
      jsonError: '',
      showJsonEditor: false
    };
  }

  // 重置
  reset = () => {
    this.setState(this.init(this.originSchema));
    this.props.onChange(this.originSchema);
  };

  // 处理 JSON 编辑
  handleJsonChange = (v: string, obj: SchemaObject) => {
    // tslint:disable-next-line
    const nextState: Pick<SchemaEditorState, any> = {
      schemaCopy: obj,
      propertyStack: ['模型'],
      jsonError: ''
    };
    this.setState(nextState);
    this.props.onChange(obj);
  };

  handleJsonError = (e: string) => {
    this.setState({
      jsonError: e || 'JSON 解析错误'
    });
  };

  // 寻找目标 schema
  travalSchema(): SchemaObject | null {
    const { schemaCopy, propertyStack } = this.state;
    let editingSchema = schemaCopy;
    let index = 1;
    const length = propertyStack.length;
    try {
      while (index < length) {
        const p = propertyStack[index];
        switch (editingSchema.type) {
          case 'object':
            editingSchema = editingSchema.properties![p];
            break;
          case 'array':
            editingSchema = editingSchema.items!;
            break;
          default:
            break;
        }
        index++;
      }
    } catch (error) {
      this.setState({
        jsonError: 'schema 格式有误！'
      });
      return null;
    }
    return editingSchema;
  }

  diffSchema(prev: SchemaObject, next: SchemaObject) {
    if (prev.type === 'object' && next.type !== 'object') {
      delete prev.properties;
    }
    if (prev.type === 'array' && next.type !== 'array') {
      delete prev.items;
    }
    Object.assign(prev, next);
    this.forceUpdate();
    this.props.onChange(this.state.schemaCopy);
    // this.updateJsonText();
  }

  pushRoute = (p?: string | string[]) => {
    const { propertyStack } = this.state;
    let newStack: string[] = [];
    newStack = propertyStack.slice();
    if (p) {
      if (!Array.isArray(p)) {
        p = [p];
      }
      newStack = newStack.concat(p);
    }
    this.setState({
      propertyStack: newStack
    });
  };

  renderAction(schema: SchemaObject) {
    switch (schema.type) {
      case 'object':
        return <ObjectEditor schema={schema} onEnterProperty={this.pushRoute} />;
      case 'array':
        return <ArrayEditor schema={schema} onEnterProperty={this.pushRoute} />;
      default:
        return null;
    }
  }

  // 渲染 gui 编辑器
  renderGuiEditor() {
    const hideImport = this.props.hideImport;
    const { jsonError } = this.state;
    const currentSchema = this.travalSchema();
    if (jsonError || !currentSchema) {
      return <Alert message={jsonError} type="error" />;
    } else {
      return (
        <div className="schema-editor-gui">
          <div className="&-route">{this.renderRoute()}</div>
          <Divider />
          {hideImport && hideImport === true ? null : (
            <div className="model-import">
              <Select defaultValue="1" className="&-model-select">
                <Option value="1">模型1</Option>
              </Select>
              <Button type="primary">导入模型</Button>
            </div>
          )}
          <MetaForm
            onChange={v => this.diffSchema(currentSchema, v)}
            initialValues={currentSchema}
          />
          <Divider />
          {this.renderAction(currentSchema)}
        </div>
      );
    }
  }

  // 渲染面包屑
  renderRoute() {
    const { propertyStack } = this.state;
    const result: JSX.Element[] = [];
    propertyStack.forEach((v, i) => {
      result.push(
        <span
          className="r-name"
          key={v}
          onClick={() => this.setState({ propertyStack: propertyStack.slice(0, i + 1) })}
        >
          {v}
        </span>
      );
      if (i < propertyStack.length - 1) {
        result.push(
          <span className="r-separator" key={`${i}-/`}>
            /
          </span>
        );
      }
    });
    return result;
  }

  toggleEditor = () => {
    this.setState({
      showJsonEditor: !this.state.showJsonEditor
    });
  };

  componentDidCatch(error: Error) {
    this.setState({
      jsonError: error.toString()
    });
  }

  render() {
    const { schemaCopy, showJsonEditor, jsonError } = this.state;
    return (
      <div className="schema-editor">
        <div className="&-toggle-buttons">
          <Button
            type="primary"
            onClick={() => this.setState({ showJsonEditor: false })}
            disabled={!!jsonError}
          >
            模型编辑
          </Button>
          <Button onClick={() => this.setState({ showJsonEditor: true })}>JSON编辑</Button>
        </div>
        <div className="&-editor">
          {showJsonEditor ? (
            <JsonEditor
              value={JSON.stringify(schemaCopy, null, 2)}
              onChange={this.handleJsonChange}
              onError={this.handleJsonError}
            />
          ) : (
            this.renderGuiEditor()
          )}
        </div>
      </div>
    );
  }
}
