import React, { Component } from 'react';
import { FormComponentProps } from 'antd/lib/form';
import { Form, Input, Button } from 'antd';
import './FolderForm.scss';
const FormItem = Form.Item;

enum FolderField {
  NAME = 'name'
}

export type FieldValues = { [key in FolderField]: string | undefined };
export type FieldErrors = {
  [key in FolderField]: {
    errors: {
      message: string;
      field: FolderField;
    }[];
  }
};

interface FolderFormProps extends FormComponentProps {
  onSave?: (values: FieldValues) => void;
  onCancel?: () => void;
  initialValues?: FieldValues;
}

interface FolderFormState {}

class FolderForm extends Component<FolderFormProps, FolderFormState> {
  handleSubmit = () => {
    const { form, onSave } = this.props;
    form.validateFieldsAndScroll((errors: FieldErrors, values: FieldValues) => {
      if (!errors && onSave) {
        onSave(values);
      }
    });
  };

  handleCancel = () => {
    const { form, onCancel } = this.props;
    form.resetFields();
    if (onCancel != null) {
      onCancel();
    }
  };

  render() {
    const form = this.props.form;
    const { getFieldDecorator } = form;
    return (
      <div className="folder-form">
        <Form>
          <FormItem label="分组名称" labelCol={{ span: 4 }} wrapperCol={{ span: 18 }}>
            {getFieldDecorator(FolderField.NAME, {
              rules: [
                { required: true, message: '请填写分组名称' },
                { max: 255, message: '最大长度不能超过255' }
              ]
            })(<Input />)}
          </FormItem>
          <FormItem>
            <div className="folder-form-buttons">
              <Button onClick={this.handleCancel}>取消</Button>
              <Button type="primary" onClick={this.handleSubmit}>
                确定
              </Button>
            </div>
          </FormItem>
        </Form>
      </div>
    );
  }
}

export default Form.create({
  mapPropsToFields(props: FolderFormProps) {
    const values = {};
    const initialValues = props.initialValues;
    if (!!initialValues) {
      Object.keys(initialValues).forEach(k => {
        values[k] = Form.createFormField({
          value: initialValues[k]
        });
      });
    }
    return values;
  }
})(FolderForm);
