import React from 'react';
import { Avatar, Layout, Button } from 'antd';
import { Route, Switch, Redirect, RouteComponentProps, Link } from 'react-router-dom';
import { inject, observer } from 'mobx-react';

import ProjectOverviewWithCharts from '../../components/project-overview/ProjectOverviewWithCharts';
import ProjectDetail from '../../components/project-detail/ProjectDetail';
import ApiFolder from '../../components/api-folder/ApiFolder';
import UserStore from '../../stores/UserStore';
import EuProjectOverview from '../../components/eu-project-overview/EuProjectOverview';
import ApiDetail from '../../components/api-detail/ApiDetail';
import EuProjectDetail from '../../components/eu-project-detail/EuProjectDetail';
import { APIImplEditor } from '../../components/api-impl-editor';
import ProjectStore from '../../stores/ProjectStore';
import CustomBreadcrumb from '../../components/custom-breadcrumb/CustomBreadcrumb';
import EnvManagement from '../../components/env/env-management/EnvManagement';
import EnvCreation from '../../components/env/env-creation/EnvCreation';
import DocManagement from '../../components/doc/doc-management/DocManagement';
import ModelManagement from '../../components/model-management/ModelManagement';
import DocEditor from '../../components/doc/doc-editor/DocEditor';
import DocDetail from '../../components/doc/doc-detail/DocDetail';
import ApiCreator from '../../components/api-creator/ApiCreator';
import EnvDetail from '../../components/env/env-detail/EnvDetail';
import ProjectDynamic from '../../components/project-dynamic/ProjectDynamic';

import './Home.scss';

const { Header, Content, Footer } = Layout;

const Home: React.StatelessComponent<
  {
    userStore?: UserStore;
    projectStore?: ProjectStore;
  } & RouteComponentProps<{}>
> = props => {
  const { isLogin, isApiManager, isQyUser, logout, user } = props.userStore!;
  const location = props.location!;
  if (!isLogin) {
    return <Redirect to="/prelogin" />;
  } else {
    if (user && user.isAccountManager) {
      return <Redirect to="accountmanagement" />;
    }
  }

  return (
    <Layout className="layout">
      <Header className="&-header">
        <div className="&-logo">江苏筑讯数据管理中心</div>
        <div className="&-user">
          <Avatar style={{ marginRight: 16 }} icon="user" />
          <span>{user && user.username}</span>
          {isQyUser ? (
            <Link to="/changepassword">
              <Button style={{ marginLeft: 16 }}>修改密码</Button>
            </Link>
          ) : null}
          <Button onClick={logout} style={{ marginLeft: 16 }}>
            注销
          </Button>
        </div>
      </Header>
      <Content className="&-content">
        <CustomBreadcrumb isApiManager={isApiManager} pathname={location.pathname} />
        <div className="app-container">
          <Switch>
            <Route path="/" exact={true} render={() => <Redirect to="/project" />} />
            <Route exact={true} path="/project/:pid/api" component={ApiFolder} />
            <Route path="/project/:pid/api/editor-impl" component={APIImplEditor} />
            <Route path="/project/:pid/api/editor-new" component={ApiCreator} />
            <Route path="/project/:pid/api/:aid/editor-update" component={ApiCreator} />
            <Route path="/project/:pid/api/:id" component={ApiDetail} />
            <Route
              exact={true}
              path="/project/:pid"
              render={_props =>
                isApiManager ? <ProjectDetail {..._props} /> : <EuProjectDetail {..._props} />
              }
            />
            <Route exact={true} path="/project/:pid/envmanagement/new" component={EnvCreation} />
            <Route path="/project/:pid/envmanagement/:envId" component={EnvDetail} />
            <Route path="/project/:pid/envmanagement" component={EnvManagement} />
            <Route path="/project/:pid/modelmanagement" component={ModelManagement} />
            <Route path="/project/:pid/dynamic" component={ProjectDynamic} />
            <Route exact={true} path="/project/:pid/doc" component={DocManagement} />
            <Route path="/project/:pid/doc/edit" component={DocEditor} />
            <Route exact={true} path="/project/:pid/doc/:did" component={DocDetail} />
            <Route
              path="/project"
              render={() => (isApiManager ? <ProjectOverviewWithCharts /> : <EuProjectOverview />)}
            />
            <Route render={() => <div>404</div>} />
          </Switch>
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
};

export default inject('userStore', 'projectStore')(observer(Home));
