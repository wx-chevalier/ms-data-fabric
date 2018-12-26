import React from 'react';
import { Breadcrumb } from 'antd';
import { Link } from 'react-router-dom';

const pathMap: { regexp: RegExp; text: string; adminOnly?: boolean }[] = [
  {
    regexp: /^\/project\/?$/,
    text: '首页'
  },
  {
    regexp: /^\/project\/[a-z0-9-]{36}\/?$/,
    text: '项目概况'
  },
  {
    regexp: /^\/project\/[a-z0-9-]{36}\/dynamic\/?$/,
    text: '项目动态'
  },
  {
    regexp: /^\/project\/[a-z0-9-]{36}\/envmanagement\/?$/,
    text: '环境管理',
    adminOnly: true
  },
  {
    regexp: /^\/project\/[a-z0-9-]{36}\/envmanagement\/new\/?$/,
    text: '新增环境',
    adminOnly: true
  },
  {
    regexp: /^\/project\/[a-z0-9-]{36}\/envmanagement\/[a-z0-9-]{36}\/?$/,
    text: '环境详情',
    adminOnly: true
  },
  {
    regexp: /^\/project\/[a-z0-9-]{36}\/modelmanagement\/?$/,
    text: '模型管理',
    adminOnly: true
  },
  {
    regexp: /^\/project\/[a-z0-9-]{36}\/doc\/?$/,
    text: '项目文档',
    adminOnly: true
  },
  {
    regexp: /^\/project\/[a-z0-9-]{36}\/doc\/edit\/?$/,
    text: '编辑文档',
    adminOnly: true
  },
  {
    regexp: /^\/project\/[a-z0-9-]{36}\/doc\/[a-z0-9-]{36}\/?$/,
    text: '文档详情'
  },
  {
    regexp: /^\/project\/[a-z0-9-]{36}\/api\/?$/,
    text: '接口',
    adminOnly: true
  },
  {
    regexp: /^\/project\/[a-z0-9-]{36}\/api\/editor\-impl\/?$/,
    text: '生成接口'
  },
  {
    regexp: /^\/project\/[a-z0-9-]{36}\/api\/editor\-new\/?$/,
    text: '新建接口',
    adminOnly: true
  },
  {
    regexp: /^\/project\/[a-z0-9-]{36}\/api\/[a-z0-9-]{36}\/editor\-update\/?$/,
    text: '编辑接口',
    adminOnly: true
  },
  {
    regexp: /^\/project\/[a-z0-9-]{36}\/api\/[a-z0-9-]{36}\/?$/,
    text: '接口详情'
  }
];

const path2Text = (p: string, isManager: boolean) => {
  const result = pathMap.find(i => {
    if (!i.regexp.test(p)) {
      return false;
    }
    if (!isManager && i.adminOnly) {
      return false;
    }
    return true;
  });
  return result ? result.text : '';
};

const getBreadcrumbItems = (pathname: string, isManager: boolean): JSX.Element[] => {
  const items: JSX.Element[] = [];
  pathname.split('/').reduce((p, c) => {
    if (!c) {
      return p;
    }
    const url = `${p}/${c}`;
    const text = path2Text(url, isManager);
    if (text) {
      items.push(
        <Breadcrumb.Item key={url}>
          <Link to={url + '/'}>{text}</Link>
        </Breadcrumb.Item>
      );
    }
    return url;
  });
  if (items.length === 0) {
    items.push(
      <Breadcrumb.Item key="/product">
        <Link to="/product">主页</Link>
      </Breadcrumb.Item>
    );
  }
  return items;
};

const CustomBreadcrumb: React.StatelessComponent<{ pathname: string; isApiManager: boolean }> = ({
  pathname,
  isApiManager
}) => {
  return (
    <Breadcrumb style={{ margin: '16px 0' }}>
      {getBreadcrumbItems(pathname, isApiManager)}
    </Breadcrumb>
  );
};

export default CustomBreadcrumb;
