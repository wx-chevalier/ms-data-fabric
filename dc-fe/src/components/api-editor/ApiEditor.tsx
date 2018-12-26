import React, { Component } from 'react';
import './ApiEditor.scss';
import { Divider, Tabs, Button, Form } from 'antd';
// import ApiInfoForm from './api-info-form/ApiInfoForm';
import KeyEditor, { KeyType } from './key-editor/KeyEditor';
import SchemaEditor from '../schema-editor/SchemaEditor';
import ApiInfoForm, { FieldValues } from './api-info-form/ApiInfoForm';
import { SchemaObject } from '../../apis/OpenApi3Interface';
const TabPane = Tabs.TabPane;
import { cloneDeep } from 'lodash';
import { isObject } from 'util';
import ApiModel from '../../models/ApiModel';
import Editor from '../editor/Editor';
import DocModel from '../../models/DocModel';

interface ApiEditorProps {
  loading: boolean;
  onOk?: (data: ApiDefinition) => void;
  onCancel?: () => void;
  initialValue?: ApiModel;
  setEditingContent: (content: string) => void;
}

interface ApiSchemaDefinition {
  headers: KeyType[];
  queries: KeyType[];
  requestBody: SchemaObject;
  responseBody: SchemaObject;
}

interface ApiDoc {
  apiDoc?: DocModel;
}

export type ApiDefinition = ApiSchemaDefinition & FieldValues & ApiDoc;
interface ApiEditorState extends ApiDefinition {}

export default class ApiEditor extends Component<ApiEditorProps, ApiEditorState> {
  infoForm: Form;

  constructor(props: ApiEditorProps) {
    super(props);
    const initialState: ApiDefinition = {
      headers: [],
      queries: [],
      responseBody: {},
      requestBody: {},
      name: undefined,
      verb: undefined,
      uri: undefined,
      description: undefined,
      protocol: undefined,
      status: undefined,
      apiDoc: undefined
    };
    if (isObject(props.initialValue)) {
      Object.assign(initialState, props.initialValue!);
    }
    this.state = initialState;
  }

  handleSubmit = () => {
    // tslint:disable-next-line
    this.infoForm.props.form!.validateFieldsAndScroll((errors: any, values: FieldValues) => {
      if (!errors) {
        const { headers, queries, requestBody, responseBody, apiDoc } = cloneDeep(this.state);
        const dataCopy: ApiDefinition = {
          headers,
          queries,
          requestBody,
          responseBody,
          apiDoc,
          ...values
        };
        this.props.onOk!(dataCopy);
      }
    });
  };

  handleCancel = () => {
    this.infoForm.props.form!.resetFields();
    this.props.onCancel!();
  };

  handleChange = <T extends keyof ApiEditorState>(key: T, v: ApiEditorState[T]) => {
    const state = { ...this.state };
    state[key] = v;
    this.setState(state);
  };

  render() {
    const loading = this.props.loading;

    const {
      headers,
      queries,
      responseBody,
      requestBody,
      verb,
      name,
      uri,
      description,
      protocol,
      status,
      apiDoc
    } = this.state;
    return (
      <div className="api-editor">
        <div className="&-info">
          <h3>基本信息</h3>
          <Divider />
          <ApiInfoForm
            wrappedComponentRef={(f: Form) => (this.infoForm = f)}
            value={{ verb, name, uri, description, protocol, status }}
          />
        </div>
        <div className="&-defination">
          <h3>高级选项（选填）</h3>
          <Divider />
          <Tabs>
            <TabPane key="query" tab="查询参数">
              <KeyEditor
                value={queries}
                onChange={v => {
                  this.handleChange('queries', v);
                }}
              />
            </TabPane>
            <TabPane key="request-header" tab="请求头部">
              <KeyEditor
                value={headers}
                onChange={v => {
                  this.handleChange('headers', v);
                }}
              />
            </TabPane>
            <TabPane key="request-body" tab="请求体">
              <SchemaEditor
                initialValue={requestBody}
                onChange={v => this.handleChange('requestBody', v)}
              />
            </TabPane>
            <TabPane key="response-body" tab="返回参数">
              <SchemaEditor
                initialValue={responseBody}
                onChange={v => this.handleChange('responseBody', v)}
              />
            </TabPane>
          </Tabs>
        </div>
        <div className="&-doc">
          <h3>接口文档</h3>
          <Divider />
          <Editor
            setEditingContent={this.props.setEditingContent}
            defaultContent={apiDoc ? apiDoc.content : ''}
          />
        </div>
        <div className="&-action">
          <Button onClick={this.handleCancel}>取消</Button>
          <Button type="primary" onClick={this.handleSubmit} loading={loading}>
            提交
          </Button>
        </div>
      </div>
    );
  }
}
