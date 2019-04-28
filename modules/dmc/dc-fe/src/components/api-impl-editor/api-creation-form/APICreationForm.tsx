import * as React from 'react';
import { Button, Input, Form } from 'antd';
import { FormComponentProps } from 'antd/lib/form';

import './APICreationForm.scss';

const FormItem = Form.Item;
const TextArea = Input.TextArea;

enum APICreationFormFields {
  APINAME = 'apiName',
  APIDESCRIPTION = 'apiDescription'
}

export interface APICreationFormProps extends FormComponentProps {
  // exception should be handled in this function already.
  handleSubmit: (name: string, description: string) => void;
  handleCancel: () => void;
  generating: boolean;
  submitErrMsg?: string;
}

export interface APICreationFormState {
  submitErrMsg?: string;
}

class APICreationForm extends React.Component<APICreationFormProps, {}> {
  handleSubmit = () => {
    this.props.form.validateFields(async (err, values) => {
      if (err) {
        return;
      }
      try {
        await this.props.handleSubmit(
          values[APICreationFormFields.APINAME],
          values[APICreationFormFields.APIDESCRIPTION]
        );
      } catch (e) {
        // should not be here
      }
    });
  };

  render() {
    const { getFieldDecorator } = this.props.form;
    return (
      <Form className="api-creation-form">
        <FormItem label="名称" labelCol={{ span: 4 }} wrapperCol={{ span: 18 }}>
          {getFieldDecorator(APICreationFormFields.APINAME, {
            rules: [{ required: true, message: '请填写名称' }]
          })(<Input placeholder="请填写名称" />)}
        </FormItem>
        <FormItem label="描述" labelCol={{ span: 4 }} wrapperCol={{ span: 18 }}>
          {getFieldDecorator(APICreationFormFields.APIDESCRIPTION)(<TextArea rows={6} />)}
        </FormItem>
        <div className="api-creation-form-btn-group">
          <Button onClick={this.props.handleCancel}>取消</Button>
          <Button type="primary" loading={this.props.generating} onClick={this.handleSubmit}>
            确定
          </Button>
        </div>
        {this.props.submitErrMsg && (
          <div className="api-creation-form-err">{this.props.submitErrMsg}</div>
        )}
      </Form>
    );
  }
}

export default Form.create()(APICreationForm);
