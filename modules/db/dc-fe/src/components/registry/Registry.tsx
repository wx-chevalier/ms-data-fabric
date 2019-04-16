import './Registry.scss';
import React from 'react';
import RegistryForm from './registry-form/RegistryForm';
import UserStore from '../../stores/UserStore';
import { inject, observer } from 'mobx-react';
import { Link, Redirect } from 'react-router-dom';

interface RegistryProps {
  userStore?: UserStore;
}
interface RegistryState {
  submitted: boolean;
  errorInfo?: string;
  countDownTime: number;
}

@inject('userStore')
@observer
export default class Registry extends React.Component<RegistryProps, RegistryState> {
  constructor(props: RegistryProps) {
    super(props);
    this.state = {
      submitted: false,
      countDownTime: 5
    };
  }

  handleSubmitted = () => {
    let itv = setInterval(() => {
      this.setState(
        {
          countDownTime: this.state.countDownTime - 1
        },
        () => {
          if (this.state.countDownTime === 0) {
            clearInterval(itv);
          }
        }
      );
    }, 1000);
  };

  onOk = async (username: string, password: string, phone: string, license?: string) => {
    const userStore = this.props.userStore!;
    try {
      await userStore.registry(username, password, phone, license);
      this.setState(
        {
          submitted: true
        },
        this.handleSubmitted
      );
    } catch (error) {
      this.setState(
        {
          submitted: true,
          errorInfo: error.message
        },
        this.handleSubmitted
      );
    }
  };

  render() {
    const userStore = this.props.userStore!;
    const { submitted, errorInfo, countDownTime } = this.state;

    if (this.state.countDownTime === 0) {
      userStore.selectRole('qyUser');
      return <Redirect to="login" />;
    }

    return (
      <div className="registry-container">
        {!submitted ? (
          <div className="registry">
            <Link to="/prelogin" className="&-back-to-pre-login">
              {'< 返 回'}
            </Link>
            <div className="&-content-container">
              <h1 className="&-title">江苏筑讯数据管理中心</h1>
              <span>
                <b>企业用户注册</b>
              </span>
              <RegistryForm
                onOk={this.onOk}
                linkToLogin={() => {
                  userStore.selectRole('qyUser');
                }}
              />
            </div>
          </div>
        ) : (
          <div className="request-info">
            <div>
              {errorInfo
                ? errorInfo
                : '注册账户正在审核中，审核结果将通过短信通知，审核通过后即可登录'}
            </div>
            <div>{`${countDownTime}s后跳转到登录页面`}</div>
          </div>
        )}
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
