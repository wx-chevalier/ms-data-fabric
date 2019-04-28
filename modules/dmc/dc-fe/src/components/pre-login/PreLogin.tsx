import './PreLogin.scss';
import React from 'react';
import { Link } from 'react-router-dom';
import UserStore from '../../stores/UserStore';
import { inject, observer } from 'mobx-react';

interface Props {
  userStore?: UserStore;
}

@inject('userStore')
@observer
export default class PreLogin extends React.Component<Props, {}> {
  async componentDidMount() {
    const userStore = this.props.userStore!;
    userStore.logout();
  }

  render() {
    const userStore = this.props.userStore!;

    return (
      <div className="pre-login">
        <div className="&-container">
          <div className="&-content-container">
            <h1>
              <b>江苏筑讯数据管理中心</b>
            </h1>
            <div className="&-select-role">
              <div className="&-account-manager">
                <span className="&-svg-container" />
                <Link
                  to="/login"
                  className="&-title"
                  onClick={() => userStore.selectRole('accountManager')}
                >
                  我是账号管理员
                </Link>
              </div>
              <div className="&-api-manager">
                <span className="&-svg-container" />
                <Link
                  to="/login"
                  className="&-title"
                  onClick={() => userStore.selectRole('apiManager')}
                >
                  我是接口管理员
                </Link>
              </div>
              <div className="&-enterprise-customer">
                <span className="&-svg-container" />
                <Link
                  to="/login"
                  className="&-title"
                  onClick={() => userStore.selectRole('qyUser')}
                >
                  我是企业用户
                </Link>
              </div>
            </div>
            <div className="&-registry">
              <Link to="/registry">企业用户注册</Link>
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
      </div>
    );
  }
}
