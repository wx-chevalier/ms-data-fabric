import './AccountManagement.scss';
import React from 'react';
import QyUserAudit from './qy-user-audit/QyUserAudit';
import DatabaseAuth from './database-auth/DatabaseAuth';
import ApiUserManagement from './api-user-management/ApiUserManagement';
import * as classnames from 'classnames';
import { Avatar, Layout, Button } from 'antd';
import UserStore from '../../stores/UserStore';
import { inject, observer } from 'mobx-react';
import { Redirect } from 'react-router-dom';
import { FormComponentProps } from '../../../node_modules/antd/lib/form';
import AccounManagementStore from '../../stores/AccoutManagementStore';

const { Header, Content, Footer } = Layout;

const auditIcon = require('../../assets/audit-icon.png');
const auditIconRed = require('../../assets/audit-icon-red.png');
const authIcon = require('../../assets/auth-icon.png');
const authIconRed = require('../../assets/auth-icon-red.png');
const userManagementIcon = require('../../assets/user-management-icon.png');
const userManagementIconRed = require('../../assets/user-management-icon-red.png');

interface AccountManagementProps extends FormComponentProps {
  userStore?: UserStore;
  accountManagementStore?: AccounManagementStore;
}

interface AccountManagementState {
  currentTab: 'audit' | 'auth' | 'userManagement';
}

@inject('userStore', 'accountManagementStore')
@observer
export default class AccountManagement extends React.Component<
  AccountManagementProps,
  AccountManagementState
> {
  constructor(props: AccountManagementProps) {
    super(props);
    this.state = {
      currentTab: 'audit'
    };
  }

  async componentDidMount() {
    const accountManagementStore = this.props.accountManagementStore!;
    await accountManagementStore.getAvailableApiManagerList();
    await accountManagementStore.getDataSourceList();
  }

  render() {
    const { user, logout, isLogin } = this.props.userStore!;
    const currentTab = this.state.currentTab;

    if (!isLogin) {
      return <Redirect to="/PreLogin" />;
    }

    return (
      <Layout className="layout">
        <Header className="&-header">
          <div className="&-logo">江苏筑讯数据管理中心</div>
          <div className="&-user">
            <Avatar style={{ marginRight: 16 }} icon="user" />
            <span>{user && user.username}</span>
            <Button onClick={logout} style={{ marginLeft: 16 }}>
              注销
            </Button>
          </div>
        </Header>
        <Content className="&-content">
          <div className="app-container">
            <div className="account-management">
              <div className="&-tabs">
                <div
                  className={classnames({
                    audit: true,
                    active: currentTab === 'audit'
                  })}
                  onClick={() => {
                    this.setState({
                      currentTab: 'audit'
                    });
                  }}
                >
                  <img
                    src={currentTab === 'audit' ? auditIconRed : auditIcon}
                    style={{ marginRight: 10 }}
                  />
                  <b>企业用户审核</b>
                </div>
                <div
                  className={classnames({
                    auth: true,
                    active: currentTab === 'auth'
                  })}
                  onClick={() => {
                    this.setState({
                      currentTab: 'auth'
                    });
                  }}
                >
                  <img
                    src={currentTab === 'auth' ? authIconRed : authIcon}
                    style={{ marginRight: 10 }}
                  />
                  <b>数据库授权</b>
                </div>
                <div
                  className={classnames({
                    'user-management': true,
                    active: currentTab === 'userManagement'
                  })}
                  onClick={() => {
                    this.setState({
                      currentTab: 'userManagement'
                    });
                  }}
                >
                  <img
                    src={
                      currentTab === 'userManagement' ? userManagementIconRed : userManagementIcon
                    }
                    style={{ marginRight: 10 }}
                  />
                  <b>接口用户管理</b>
                </div>
              </div>
              <div className="&-content-container">
                {this.state.currentTab === 'audit' ? (
                  <QyUserAudit />
                ) : this.state.currentTab === 'auth' ? (
                  <DatabaseAuth />
                ) : (
                  <ApiUserManagement {...this.props} />
                )}
              </div>
            </div>
          </div>
        </Content>
        <Footer className="&-footer">
          <div className="&-logo" />
          <div className="&-text">
            <span>Copyright © 2018</span>
            <span>江苏筑讯信息技术有限公司</span>
            <span>苏ICP备14011892号</span>
          </div>
        </Footer>
      </Layout>
    );
  }
}
