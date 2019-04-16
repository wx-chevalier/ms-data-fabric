import * as React from 'react';
import { Provider } from 'mobx-react';
import * as allStores from './stores';
import { Router, Route, Switch } from 'react-router';
import Home from './pages/home/Home';
import { createHashHistory } from 'history';
import { syncHistoryWithStore } from 'mobx-react-router';
import './App.scss';
import 'ant-design-pro/dist/ant-design-pro.css';
import Login from './components/login/Login';
import PreLogin from './components/pre-login/PreLogin';
import Registry from './components/registry/Registry';
import { LocaleProvider } from 'antd';
import zh_CN from 'antd/lib/locale-provider/zh_CN';
import 'moment/locale/zh-cn';
import AccountManagement from './components/account-management/AccountManagement';
import ChangePassword from './components/change-password/ChangePassword';

const hashHistory = createHashHistory();
const history = syncHistoryWithStore(hashHistory, allStores.routingStore);

export default class App extends React.Component {
  render() {
    return (
      <Provider {...allStores}>
        <LocaleProvider locale={zh_CN}>
          <Router history={history}>
            <Switch>
              {/* 登陆 */}
              <Route path="/prelogin" component={PreLogin} />
              <Route path="/registry" component={Registry} />
              <Route path="/login" component={Login} />
              {/* 内容 - 账号管理员页面没有面包屑，刷新后store中的状态会刷新，导致面包屑会出现，故分开写 */}
              <Route path="/accountmanagement" component={AccountManagement} />
              <Route path="/changepassword" component={ChangePassword} />
              <Route component={Home} />
            </Switch>
          </Router>
        </LocaleProvider>
      </Provider>
    );
  }
}
