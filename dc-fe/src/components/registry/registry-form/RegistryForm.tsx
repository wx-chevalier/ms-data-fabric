import './RegistryForm.scss';
import React from 'react';
import { Form, Input, Button, Select } from 'antd';
import { FormComponentProps } from 'antd/lib/form';
import { Link } from 'react-router-dom';

const FormItem = Form.Item;
const Option = Select.Option;

enum RegistryField {
  USERNAME = 'username',
  PASSWORD = 'password',
  CONFIRM = 'confirm',
  PREFIX = 'prefix',
  PHONE = 'phone',
  CAPTCHA = 'captcha',
  LICENSE = 'license'
}

type FieldValues = {
  [RegistryField.USERNAME]: string | undefined;
  [RegistryField.PASSWORD]: string | undefined;
  [RegistryField.CONFIRM]: string | undefined;
  [RegistryField.PREFIX]: string | undefined;
  [RegistryField.PHONE]: string | undefined;
  [RegistryField.CAPTCHA]: string | undefined;
  [RegistryField.LICENSE]: File | undefined;
};

interface Props extends FormComponentProps {
  onOk: (username: string, password: string, phone: string, license?: string) => void;
  linkToLogin: () => void;
}

interface State {
  confirmDirty: boolean;
  error: string;
}

export class Registry extends React.Component<Props, State> {
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
          const calcPhoneNum = `+${values['prefix']}-${values['phone']}`;
          // tslint:disable-next-line
          await this.props.onOk(values['username']!, values['password']!, calcPhoneNum);
        } catch (error) {
          this.setState({
            error: error.message
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
    if (value && value !== form.getFieldValue('password')) {
      callback('两次输入的密码不一致!');
    } else {
      callback();
    }
  };

  // tslint:disable-next-line
  validateToNextPassword = (rule: any, value: string, callback: (v?: string) => void) => {
    const form = this.props.form;
    if (value && this.state.confirmDirty) {
      // tslint:disable-next-line
      form.validateFields(['confirm'], { force: true }, () => {});
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

  // tslint:disable-next-line
  normFile = (e: any) => {
    if (Array.isArray(e)) {
      return e;
    }
    return e && e.fileList;
  };

  render() {
    const { getFieldDecorator } = this.props.form;
    const linkToLogin = this.props.linkToLogin;
    const prefixSelector = getFieldDecorator(RegistryField.PREFIX, {
      initialValue: '86'
    })(
      <Select style={{ width: 70 }} disabled={true}>
        <Option value="86">+86</Option>
      </Select>
    );

    return (
      <div className="registry-form">
        <Form onSubmit={this.handleSubmit}>
          <FormItem>
            {getFieldDecorator(RegistryField.USERNAME, {
              rules: [{ required: true, message: '请输入用户名' }]
            })(<Input placeholder="用户名建议使用企业名称" />)}
          </FormItem>
          <FormItem>
            {getFieldDecorator(RegistryField.PASSWORD, {
              rules: [
                { required: true, message: '请填写密码' },
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
          {/* <FormItem>
            {getFieldDecorator(RegistryField.LICENSE, {
              valuePropName: 'fileList',
              getValueFromEvent: this.normFile
            })(
              <Upload name="license" action="/upload.do" listType="picture">
                <Button>
                  <Icon type="upload">点击上传营业执照</Icon>
                </Button>
              </Upload>
            )}
          </FormItem> */}
          <FormItem className="actions">
            <Button type="primary" htmlType="submit" style={{ width: '176px' }}>
              注册
            </Button>
            <Link to="/login" className="to-login" onClick={linkToLogin}>
              使用已有账户登录
            </Link>
          </FormItem>
        </Form>
      </div>
    );
  }
}

const RegistryFrom = Form.create()(Registry);
export default RegistryFrom;
