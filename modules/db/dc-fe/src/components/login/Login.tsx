import React, { Component } from 'react';
import { Form, Input, Icon, Button, Checkbox } from 'antd';
import { FormComponentProps } from 'antd/lib/form';
import './Login.scss';
const FormItem = Form.Item;
import { inject, observer } from 'mobx-react';
import UserStore from '../../stores/UserStore';
import { Redirect, Link } from 'react-router-dom';

enum LoginField {
  USERNAME = 'username',
  PASSWORD = 'password',
  REMEMBER = 'remember'
}

export type FieldValues = {
  [LoginField.USERNAME]: string | undefined;
  [LoginField.PASSWORD]: string | undefined;
  [LoginField.REMEMBER]: boolean;
};
export type FieldErrors = {
  [key in LoginField]: {
    errors: {
      message: string;
      field: LoginField;
    }[];
  }
};

interface LoginProps extends FormComponentProps {
  userStore?: UserStore;
}

interface LoginState {
  loading: boolean;
  error: string;
}

@inject('userStore')
@observer
class Login extends Component<LoginProps, LoginState> {
  state = {
    loading: false,
    error: ''
  };

  handleLogin = () => {
    this.setState({
      error: ''
    });
    this.props.form.validateFields(async (err, values: FieldValues) => {
      if (!err) {
        this.setState({
          loading: true
        });
        try {
          await this.props.userStore!.login(values.username!, values.password!, values.remember);
          this.setState({
            loading: false
          });
        } catch (error) {
          this.setState({
            loading: false,
            error: error.message
          });
        }
      }
    });
  };

  render() {
    const userStore = this.props.userStore!;
    if (userStore.isLogin) {
      if (userStore.loginRole !== 'accountManager') {
        return <Redirect to="/project" />;
      } else {
        return <Redirect to="/accountmanagement" />;
      }
    } else {
      if (!userStore.loginRole) {
        return <Redirect to="prelogin" />;
      }
    }
    const form = this.props.form;
    const { getFieldDecorator } = form;
    const loginRole = userStore.loginRole;
    const loading = this.state.loading;

    return (
      <div className="login">
        <Link to="/prelogin" className="&-back-to-pre-login">
          {'< 返 回'}
        </Link>
        <div className="&-container">
          <div className="&-content-container">
            <h1 className="&-logo">
              {loginRole === 'accountManager'
                ? '账号管理员'
                : loginRole === 'apiManager'
                  ? '接口管理员'
                  : '企业用户'}
              登录
            </h1>
            <Form className="&-form">
              <FormItem>
                {getFieldDecorator(LoginField.USERNAME, {
                  rules: [{ required: true, message: '请输入用户名' }]
                })(<Input placeholder="用户名" />)}
              </FormItem>
              <FormItem>
                {getFieldDecorator(LoginField.PASSWORD, {
                  rules: [{ required: true, message: '请输入密码' }]
                })(
                  <Input
                    prefix={<Icon type="lock" style={{ color: 'rgba(0,0,0,.25)' }} />}
                    type="password"
                    placeholder="密码"
                  />
                )}
              </FormItem>
              <FormItem>
                {getFieldDecorator(LoginField.REMEMBER, {
                  valuePropName: 'checked',
                  initialValue: false
                })(<Checkbox>记住密码</Checkbox>)}
                <Button
                  className="login-button"
                  type="primary"
                  onClick={this.handleLogin}
                  loading={loading}
                >
                  登录
                </Button>
              </FormItem>
            </Form>
            <div className="&-err">{this.state.error}</div>
          </div>
          <div className="&-footer-container">
            <div className="&-logo" />
            <div className="&-text">
              <span>Copyright © 2018</span>
              <span>江苏筑讯信息技术有限公司</span>
              <span>苏ICP备14011892号</span>
            </div>
          </div>
        </div>
      </div>
    );
  }
}

export default Form.create()(Login);
