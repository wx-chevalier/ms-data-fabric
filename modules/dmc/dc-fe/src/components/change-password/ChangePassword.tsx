import './ChangePassword.scss';
import React from 'react';
import { Icon, Button } from 'antd';
import ChangePasswordForm from './change-password-form/ChangePasswordForm';
import UserStore from '../../stores/UserStore';
import { inject, observer } from 'mobx-react';
import { RouteComponentProps, Link } from 'react-router-dom';
import ApiStore from '../../stores/ApiStore';

interface ChangePasswordProps extends RouteComponentProps<{}> {
  apiStore?: ApiStore;
  userStore?: UserStore;
}

interface ChangePasswordState {
  submitted: boolean;
}

@inject('userStore', 'apiStore')
@observer
export default class ChangePassword extends React.Component<
  ChangePasswordProps,
  ChangePasswordState
> {
  constructor(props: ChangePasswordProps) {
    super(props);
    this.state = {
      submitted: false
    };
  }

  onOk = async (oldPassword: string, newPassword: string) => {
    const userStore = this.props.userStore!;
    await userStore.changePassword(oldPassword, newPassword);
  };

  toggleStatus = () => {
    this.setState({
      submitted: !this.state.submitted
    });
  };

  deleteUserToken = async () => {
    const apiStore = this.props.apiStore!;
    const userStore = this.props.userStore!;
    const history = this.props.history;
    await apiStore.clearUserToken();
    userStore.logout();
    userStore.selectRole('qyUser');
    history.push('/login');
  };

  render() {
    const userStore = this.props.userStore!;
    const history = this.props.history;

    return (
      <div className="change-password-container">
        <div className="change-password">
          <a className="&-back" onClick={() => history.goBack()}>
            {this.state.submitted ? null : '< 返 回'}
          </a>
          <div className="&-content-container">
            <h1 className="&-title">江苏筑讯数据管理中心</h1>
            {this.state.submitted ? (
              <div className="success-info">
                <Icon type="check-circle" style={{ color: '#49B03D', fontSize: '64px' }} />
                <span className="success-content">
                  <b>密码修改成功</b>
                </span>
                <p className="tips">
                  为了保证账户安全性，建议修改密码后清除此账户已获取的所有token！
                </p>
                <Button
                  type="primary"
                  style={{ padding: '0 120px' }}
                  onClick={async () => {
                    await this.deleteUserToken();
                  }}
                >
                  确认清除
                </Button>
                <Link
                  to="/login"
                  onClick={() => {
                    userStore.logout();
                    userStore.selectRole('qyUser');
                  }}
                >
                  <span className="pass">—— 跳过 ——</span>
                </Link>
              </div>
            ) : (
              <div className="content">
                <span>
                  <b>修改密码</b>
                </span>
                <ChangePasswordForm onOk={this.onOk} toggleStatus={this.toggleStatus} />
              </div>
            )}
          </div>
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
    );
  }
}
