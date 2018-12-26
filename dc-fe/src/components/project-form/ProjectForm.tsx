import React, { Component } from 'react';
import { FormComponentProps } from 'antd/lib/form';
import { Form, Input, Select, Row, Col, Button } from 'antd';
import './ProjectForm.scss';
import { ProjectType } from '../../apis/interface';
const FormItem = Form.Item;
const Option = Select.Option;

enum ProjectField {
  NAME = 'name',
  TYPE = 'type',
  VERSION = 'version',
  DESCRIPTION = 'description'
}

export type FieldValues = Partial<{ [key in ProjectField]: string | undefined }>;
export type FieldErrors = {
  [key in ProjectField]: {
    errors: {
      message: string;
      field: ProjectField;
    }[];
  }
};

interface ProjectFormProps extends FormComponentProps {
  onSave?: (values: FieldValues) => void;
  onCancel?: () => void;
  initialValues?: FieldValues;
  typeOptions: ProjectType[];
}

interface ProjectFormState {
  error: string;
}

class ProjectForm extends Component<ProjectFormProps, ProjectFormState> {
  constructor(props: ProjectFormProps) {
    super(props);
    this.state = {
      error: ''
    };
  }

  hadleSubmit = () => {
    this.setState({
      error: ''
    });
    const { form, onSave, onCancel } = this.props;
    form.validateFieldsAndScroll(async (errors: FieldErrors, values: FieldValues) => {
      if (!errors && onSave && onCancel) {
        try {
          await onSave(values);
          onCancel();
        } catch (err) {
          this.setState({
            error: err.message
          });
        }
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
      <div className="project-form">
        <Form>
          <FormItem label="项目名称" labelCol={{ span: 4 }} wrapperCol={{ span: 18 }}>
            {getFieldDecorator(ProjectField.NAME, {
              rules: [
                { required: true, message: '请填写项目名称' },
                { max: 255, message: '最大长度不能超过255' }
              ]
            })(<Input />)}
          </FormItem>
          <Row>
            <Col span={12}>
              <FormItem label="项目类型" labelCol={{ span: 8 }} wrapperCol={{ span: 12 }}>
                {getFieldDecorator(ProjectField.TYPE, {
                  rules: [{ required: true, message: '请选择项目类型' }]
                })(
                  <Select>
                    {this.props.typeOptions.map(t => (
                      <Option key={t.typeName}>{t.typeDescribe}</Option>
                    ))}
                  </Select>
                )}
              </FormItem>
            </Col>
            <Col span={12}>
              <FormItem label="版本号" labelCol={{ span: 8 }} wrapperCol={{ span: 12 }}>
                {getFieldDecorator(ProjectField.VERSION, {
                  rules: [
                    { required: true, message: '请填写项目版本' },
                    { max: 255, message: '最大长度不能超过255' }
                  ]
                })(<Input />)}
              </FormItem>
            </Col>
          </Row>
          <FormItem label="项目描述" labelCol={{ span: 4 }} wrapperCol={{ span: 18 }}>
            {getFieldDecorator(ProjectField.DESCRIPTION, {
              rules: [{ max: 255, message: '最大长度不能超过255' }]
            })(<Input.TextArea rows={5} />)}
          </FormItem>
          <FormItem>
            <div className="project-form-buttons">
              <Button onClick={this.handleCancel}>取消</Button>
              <Button type="primary" onClick={this.hadleSubmit}>
                确定
              </Button>
            </div>
          </FormItem>
        </Form>
        <div className="&-err">{this.state.error}</div>
      </div>
    );
  }
}

export default Form.create({
  mapPropsToFields(props: ProjectFormProps) {
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
})(ProjectForm);
