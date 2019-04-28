import './EnvForm.scss';
import React from 'react';
import { Form, Input, Button } from 'antd';
import { FormComponentProps } from 'antd/lib/form';
import EnvStore from '../../../stores/EnvStore';
import { inject, observer } from 'mobx-react';
import { EnvKVModel } from '../../../models/EnvModel';
import EnvEditableKVTable from '../env-editable-kv-table/EnvEditableKVTable';

const FormItem = Form.Item;

enum EnvField {
  ENVNAME = 'envName'
}

export type FieldValues = {
  [EnvField.ENVNAME]: string | undefined;
};

interface EnvFormProps extends FormComponentProps {
  onOk?: (name: string, param: string, cb: () => void, id?: string) => void;
  onCancel?: () => void;
  initialValues?: {
    envName: string;
  };
  status?: 'create' | 'update';
  envStore?: EnvStore;
}

interface EnvFormState {
  error: string;
  editingKvList: EnvKVModel[];
}

@inject('envStore')
@observer
class EnvForm extends React.Component<EnvFormProps, EnvFormState> {
  constructor(props: EnvFormProps) {
    super(props);
    this.state = {
      error: '',
      editingKvList: []
    };
  }

  componentWillMount() {
    const envStore = this.props.envStore!;
    this.setState({
      editingKvList:
        this.props.status && this.props.status === 'update' ? envStore.editingKVList.slice() : []
    });
  }

  handleSubmit = () => {
    const { form, onOk, status } = this.props;
    const envStore = this.props.envStore!;
    if (onOk != null) {
      form.validateFields(async (err, values: FieldValues) => {
        if (!err) {
          try {
            if (status === 'create') {
              await onOk(
                values.envName!,
                JSON.stringify(this.parseArrToObj(this.state.editingKvList)),
                () => {
                  form.resetFields();
                }
              );
            }
            if (status === 'update') {
              await onOk(
                values.envName!,
                JSON.stringify(this.parseArrToObj(this.state.editingKvList)),
                () => {
                  form.resetFields();
                },
                envStore.editingEnv.id
              );
            }
          } catch (error) {
            this.setState({ error: error.message });
          }
        }
      });
    }
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
      <div className="env-form">
        <Form>
          <FormItem label="环境名称" labelCol={{ span: 4 }} wrapperCol={{ span: 18 }}>
            {getFieldDecorator('envName', {
              rules: [{ required: true, message: '请填写环境名称' }]
            })(<Input />)}
          </FormItem>
          <FormItem label="环境变量">
            <EnvEditableKVTable
              value={this.state.editingKvList}
              onChange={v => {
                this.setState({
                  editingKvList: v
                });
              }}
            />
          </FormItem>
          <FormItem>
            <div className="env-form-buttons">
              <Button className="cancel" onClick={this.handleCancel}>
                取消
              </Button>
              <Button className="confirm" type="primary" onClick={this.handleSubmit}>
                确定
              </Button>
            </div>
          </FormItem>
        </Form>
      </div>
    );
  }

  // 将表格的数据数组转成kv对象传给后台
  private parseArrToObj = (list: Array<EnvKVModel>): { [key: string]: string } => {
    return list.reduce((prev, cur: EnvKVModel) => {
      let r = new Object();
      r[cur.envKey] = cur.envValue;
      return Object.assign(prev, r);
    }, {});
  };
}

export default Form.create({
  mapPropsToFields(props: EnvFormProps) {
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
})(EnvForm);
