import './ModelCreationForm.scss';
import React from 'react';
import { Form, Input, Button } from 'antd';
import { FormComponentProps } from 'antd/lib/form';
import SchemaEditor from '../../schema-editor/SchemaEditor';
import { SchemaObject } from '../../../apis/OpenApi3Interface';
import { Model } from '../../../apis/interface';

const FormItem = Form.Item;
const TextArea = Input.TextArea;

enum SchemaField {
  SCHEMANAME = 'schemaName',
  DESCRIPTION = 'description'
}

type FieldValues = {
  [SchemaField.SCHEMANAME]: string;
  [SchemaField.DESCRIPTION]: string | undefined;
};

interface ModelCreationFormProps extends FormComponentProps {
  initialValue?: Model;
  onOk: (name: string, definition: SchemaObject, description?: string) => void;
  onCancel: () => void;
}

interface ModelCreationFormState {
  loading: boolean;
  error: string;
  schema: SchemaObject;
}

export class ModelCreation extends React.Component<ModelCreationFormProps, ModelCreationFormState> {
  constructor(props: ModelCreationFormProps) {
    super(props);
    this.state = {
      loading: false,
      error: '',
      schema: {}
    };
  }

  handleSubmit = () => {
    this.setState({
      loading: true
    });
    this.props.form.validateFields(async (err, values: FieldValues) => {
      if (!err) {
        const { schemaName, description } = values;
        const schema = this.state.schema;

        try {
          await this.props.onOk(schemaName, schema, description);
          this.setState({
            loading: false
          });
          // 关闭Modal
          this.props.onCancel();
        } catch (e) {
          this.setState({
            loading: false,
            error: e.message
          });
        }
      } else {
        this.setState({
          loading: false
        });
      }
    });
  };

  render() {
    const { form, initialValue, onCancel } = this.props;
    const { getFieldDecorator } = form;
    const { loading } = this.state;

    return (
      <div className="schema-creation-form">
        <Form>
          <FormItem label="名称" labelCol={{ span: 4 }} wrapperCol={{ span: 18 }}>
            {getFieldDecorator('schemaName', {
              rules: [{ required: true, message: '请填写模型名称' }],
              initialValue: initialValue ? initialValue.modelName : ''
            })(<Input />)}
          </FormItem>
          <FormItem label="描述" labelCol={{ span: 4 }} wrapperCol={{ span: 18 }}>
            {getFieldDecorator('description', {
              initialValue: initialValue ? initialValue.modelDescribe : ''
            })(<TextArea rows={6} />)}
          </FormItem>
          <FormItem label="模型定义" labelCol={{ span: 4 }} wrapperCol={{ span: 18 }}>
            <div className="schema-editor-container">
              <SchemaEditor
                initialValue={initialValue ? initialValue.modelDefinition : {}}
                onChange={v => {
                  this.setState({
                    schema: v
                  });
                }}
                hideImport={true}
              />
            </div>
          </FormItem>
          <FormItem>
            <div className="doc-form-buttons">
              <Button className="cancel" onClick={onCancel}>
                取消
              </Button>
              <Button
                className="confirm"
                type="primary"
                loading={loading}
                onClick={this.handleSubmit}
              >
                确定
              </Button>
            </div>
          </FormItem>
        </Form>
      </div>
    );
  }
}

const ModelCreationForm = Form.create()(ModelCreation);
export default ModelCreationForm;
