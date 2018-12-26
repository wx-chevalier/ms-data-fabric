import './ChangePasswordForm.scss';
import React from 'react';
import { Form, Input, Button, Select } from 'antd';
import { FormComponentProps } from 'antd/lib/form';

const FormItem = Form.Item;
const Option = Select.Option;

enum RegistryField {
  OLDPASSWORD = 'oldPassword',
  NEWPASSWORD = 'newPassword',
  CONFIRM = 'confirm',
  PREFIX = 'prefix',
  PHONE = 'phone',
  CAPTCHA = 'captcha'
}

type FieldValues = {
  [RegistryField.OLDPASSWORD]: string | undefined;
  [RegistryField.NEWPASSWORD]: string | undefined;
  [RegistryField.CONFIRM]: string | undefined;
  [RegistryField.PREFIX]: string | undefined;
  [RegistryField.PHONE]: string | undefined;
  [RegistryField.CAPTCHA]: string | undefined;
};

interface Props extends FormComponentProps {
  onOk: (oldPassword: string, newPassword: string) => void;
  toggleStatus: () => void;
  linkToLogin?: () => void;
}

interface State {
  confirmDirty: boolean;
  error: string;
}

export class ChangePassword extends React.Component<Props, State> {
  constructor(props: Props) {
    super(props);
    this.state = {
      confirmDirty: false,
      error: ''
    };
  }

  // tslint:disable-next-line
  handleSubmit = (e: any) => {
    e.preventDefault();

    // tslint:disable-next-line
    this.props.form.validateFieldsAndScroll(async (err: any, values: FieldValues) => {
      if (!err) {
        try {
          // tslint:disable-next-line
          await this.props.onOk(values['oldPassword']!, values['newPassword']!);
          this.props.toggleStatus();
        } catch (error) {
          this.setState({
            error: error.response ? error.response.data.message : ''
          });
        }
      }
    });
  };

  // tslint:disable-next-line
  handleConfirmBlur = (e: any) => {
    const value = e.target.value;
    this.setState({
      confirmDirty: this.state.confirmDirty || !!value
    });
  };

  // tslint:disable-next-line
  compareToFirstPassword = (rule: any, value: string, callback: (v?: string) => void) => {
    const form = this.props.form;
    if (value && value !== form.getFieldValue('newPassword')) {
      callback('两次输入的密码不一致!');
    } else {
      callback();
    }
  };

  // tslint:disable-next-line
  validateToNextPassword = (rule: any, value: string, callback: (v?: string) => void) => {
    const form = this.props.form;
    if (value && this.state.confirmDirty) {
      form.validateFields(['confirm'], { force: true }, () => {
        //
      });
    }
    callback();
  };

  // tslint:disable-next-line
  validatePhoneNumber = (rule: any, value: string, callback: (v?: string) => void) => {
    const phoneReg = /^1\d{10}$/;
    // const phoneReg = /^(13[0-9]|14[5|7]|15[0|1|2|3|5|6|7|8|9]|18[0|1|2|3|5|6|7|8|9])\d{8}$/;
    if (value && !phoneReg.test(value)) {
      callback('请输入正确的手机号');
    } else {
      callback();
    }
  };

  render() {
    const { getFieldDecorator } = this.props.form;
    const prefixSelector = getFieldDecorator(RegistryField.PREFIX, {
      initialValue: '86'
    })(
      <Select style={{ width: 70 }} disabled={true}>
        <Option value="86">+86</Option>
      </Select>
    );

    return (
      <div className="change-password-form">
        <Form onSubmit={this.handleSubmit}>
          <FormItem>
            {getFieldDecorator(RegistryField.OLDPASSWORD, {
              rules: [{ required: true, message: '请填写原密码' }]
            })(<Input type="password" placeholder="当前账户原密码" maxLength={16} />)}
          </FormItem>
          <FormItem>
            {getFieldDecorator(RegistryField.NEWPASSWORD, {
              rules: [
                { required: true, message: '请填写新密码' },
                { validator: this.validateToNextPassword }
              ]
            })(
              <Input
                type="password"
                placeholder="6-16位密码，区分大小写"
                minLength={6}
                maxLength={16}
              />
            )}
          </FormItem>
          <FormItem>
            {getFieldDecorator(RegistryField.CONFIRM, {
              rules: [
                { required: true, message: '请确认密码' },
                { validator: this.compareToFirstPassword }
              ]
            })(
              <Input
                type="password"
                onBlur={this.handleConfirmBlur}
                placeholder="确认密码"
                minLength={6}
                maxLength={16}
              />
            )}
          </FormItem>
          <FormItem>
            {getFieldDecorator(RegistryField.PHONE, {
              rules: [
                { required: true, message: '请输入手机号' },
                { validator: this.validatePhoneNumber }
              ]
            })(
              <Input
                addonBefore={prefixSelector}
                style={{ width: '100%' }}
                minLength={11}
                maxLength={11}
              />
            )}
          </FormItem>
          {/* <FormItem>
            <Row gutter={8}>
              <Col span={12}>
                {getFieldDecorator(RegistryField.CAPTCHA, {
                  rules: [{ required: true, message: '请输入验证码' }]
                })(<Input placeholder="输入验证码" />)}
              </Col>
              <Col span={12}>
                <Button>获取验证码</Button>
              </Col>
            </Row>
          </FormItem> */}
          <FormItem className="actions">
            <Button type="primary" htmlType="submit" style={{ width: '360px' }}>
              确认修改
            </Button>
          </FormItem>
        </Form>
        <div className="error-message">{this.state.error}</div>
      </div>
    );
  }
}

const ChangePasswordForm = Form.create()(ChangePassword);
export default ChangePasswordForm;
