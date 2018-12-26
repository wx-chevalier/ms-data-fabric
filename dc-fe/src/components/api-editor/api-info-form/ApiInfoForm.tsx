import React, { Component } from 'react';
import { FormComponentProps } from 'antd/lib/form';
import { Form, Select, Input } from 'antd';
import './ApiInfoForm.scss';
import { isObject } from 'util';
const Option = Select.Option;
const FormItem = Form.Item;
import { cloneDeep } from 'lodash';
import { filterUndefined } from '../../../utils';

enum HttpVerb {
  GET = 'GET',
  POST = 'POST',
  PUT = 'PUT',
  PATCH = 'PATCH',
  DELETE = 'DELETE'
}

enum HttpProtocol {
  HTTP = 'http',
  HTTPS = 'https'
}

enum ApiField {
  VERB = 'verb',
  URI = 'uri',
  NAME = 'name',
  STATUS = 'status',
  PROTOCOL = 'protocol',
  DESCRIPTION = 'description'
}

export type FieldValues = Partial<{ [key in ApiField]: string }>;
export type FieldErrors = {
  [key in ApiField]: {
    errors: {
      message: string;
      field: ApiField;
    }[];
  }
};

interface ApiInfoFormProps extends FormComponentProps {
  value?: FieldValues;
}

interface ApiInfoFormState {}

class ApiInfoForm extends Component<ApiInfoFormProps, ApiInfoFormState> {
  componentDidMount() {
    if (isObject(this.props.value)) {
      const fields = {};
      const value = filterUndefined(cloneDeep(this.props.value!));
      Object.keys(value).forEach(k => {
        fields[k] = Form.createFormField({
          value: value[k]
        });
      });
      this.props.form.setFields(fields);
    }
  }

  render() {
    const form = this.props.form;
    const { getFieldDecorator } = form;
    return (
      <Form className="api-info-form">
        <div className="&-row">
          <FormItem className="&-item">
            <div className="&-label">请求方式</div>
            {getFieldDecorator(ApiField.VERB, {
              rules: [{ required: true, message: '请选择请求方式' }]
            })(
              <Select>
                {Object.keys(HttpVerb).map(verb => (
                  <Option key={verb} value={verb}>
                    {verb}
                  </Option>
                ))}
              </Select>
            )}
          </FormItem>
          <FormItem className="&-item">
            <div className="&-label">URI</div>
            {getFieldDecorator(ApiField.URI, {
              rules: [{ required: true, message: '请输入接口URI' }]
            })(<Input />)}
          </FormItem>
        </div>
        <div className="&-row">
          <FormItem className="&-item">
            <div className="&-label">名称</div>
            {getFieldDecorator(ApiField.NAME, {
              rules: [{ required: true, message: '请输入接口名称' }]
            })(<Input />)}
          </FormItem>
          <FormItem className="&-item">
            <div className="&-label">状态</div>
            {getFieldDecorator(ApiField.STATUS, {
              rules: [{ required: true, message: '请选择接口状态' }],
              initialValue: '1'
            })(
              <Select>
                <Option key="1" value="1">
                  启用
                </Option>
              </Select>
            )}
          </FormItem>
          <FormItem className="&-item">
            <div className="&-label">请求协议</div>
            {getFieldDecorator(ApiField.PROTOCOL, {
              rules: [{ required: true, message: '请选择请求协议' }]
            })(
              <Select>
                {Object.keys(HttpProtocol).map(protocol => (
                  <Option key={protocol} value={protocol}>
                    {protocol}
                  </Option>
                ))}
              </Select>
            )}
          </FormItem>
        </div>
        <div className="&-row">
          <FormItem className="&-item" style={{ flex: '100% 0 0' }}>
            <div className="&-label">描述</div>
            {getFieldDecorator(ApiField.DESCRIPTION, {})(<Input />)}
          </FormItem>
        </div>
      </Form>
    );
  }
}

export default Form.create<ApiInfoFormProps>()(ApiInfoForm);
