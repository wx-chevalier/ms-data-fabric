import React, { Component } from 'react';
import { RouteComponentProps } from 'react-router-dom';
import './ApiDetail.scss';
import ApiStore from '../../stores/ApiStore';
import { inject, observer } from 'mobx-react';
import { Row, Col, Tabs, Select, Button, Input, Popover, Spin } from 'antd';
import UserStore from '../../stores/UserStore';
import ApiModel from '../../models/ApiModel';
import { Link } from 'react-router-dom';
// import { appendPath } from '../../utils';
import * as path from 'path';
import AuthModal from '../auth-modal/AuthModal';
import KeyEditor from '../api-editor/key-editor/KeyEditor';
// import DeleteButton from '../delete-button/DeleteButton';
import DocModel from '../../models/DocModel';
import VisitTimesGraph from '../api-charts/visit-times/VisitTimes';
import AccessSourceGraph from '../api-charts/access-source/AccessSource';
import TimeConsumingGraph from '../api-charts/time-consuming/TimeConsuming';
import SchemaDisplay from '../schema-display/SchemaDisplay';
import ClipboardJS from 'clipboard';
import ApiTest from '../api-test/ApiTest';
import ClearTokenConfirmModal from '../clear-token-comfirm-modal/ClearTokenConfirmModal';
import DocRender from '../doc/doc-render/DocRenderer';

const TabPane = Tabs.TabPane;
const Option = Select.Option;

interface ApiDetailProps extends RouteComponentProps<{ pid: string; id: string }> {
  apiStore?: ApiStore;
  userStore?: UserStore;
}

interface ApiDetailState {
  loading: boolean;
  getTokenLoading: boolean;
  selectedEnvId?: string;
  uri?: string;
}

@inject('apiStore', 'userStore')
@observer
export default class ApiDetail extends Component<ApiDetailProps, ApiDetailState> {
  constructor(props: ApiDetailProps) {
    super(props);

    this.state = {
      loading: false,
      getTokenLoading: false
    };
  }

  async onEnvSelected(selectedEnvId?: string) {
    const apiStore = this.props.apiStore!;
    const api = apiStore.selectedApi!;
    const id = this.props.match.params.id;
    let uri;
    if (api.type === 'PROXY') {
      uri = await apiStore.getApiURI(id, selectedEnvId);
      this.setState({
        uri
      });
    }
    this.setState({ selectedEnvId });
  }

  /**
   * 复制内容到剪贴板
   * @param id 按钮id
   * @param targetId 目标内容id
   */
  copyContent(id: string, targetId: string) {
    const clipboard = new ClipboardJS(id, {
      target: () => document.querySelector(targetId)!
    });
    clipboard.on('success', e => {
      e.clearSelection();
    });
    clipboard.on('error', e => {
      e.clearSelection();
    });
  }

  async componentDidMount() {
    this.setState({
      loading: true
    });
    const { id, pid } = this.props.match.params;
    const apiStore = this.props.apiStore!;

    apiStore.projectId = pid;
    await apiStore.loadApiById(id);
    await apiStore.loadEnvList();
    const envList = apiStore.envList;
    const api = apiStore.selectedApi!;
    let uri;
    if (api.type === 'PROXY' && envList.length !== 0) {
      uri = await apiStore.getApiURI(id, envList[0].id);
      await this.onEnvSelected(envList[0].id);
    }
    if (api.type === 'GENERATED') {
      await api.getToken();
      uri = api.accessUrl;
    }

    this.setState({
      loading: false,
      uri
    });

    this.copyContent('#copy-token', '#api-token');
    this.copyContent('#copy-accessUrl', '#api-accessUrl');
  }

  renderOptional(api: ApiModel) {
    const apiStore = this.props.apiStore!;
    const { envList } = apiStore;

    return (
      <div className="api-detail-optional">
        <Row className="&-row">
          <span>环境：</span>
          <Select
            disabled={api.type === 'GENERATED'}
            defaultValue={
              api.type === 'PROXY' ? (envList.length !== 0 ? envList[0]!.id : 'default') : 'default'
            }
            onSelect={async v => {
              if (v === 'default') {
                await this.onEnvSelected();
              } else {
                await this.onEnvSelected(v as string);
              }
            }}
          >
            <Option value="default">默认环境</Option>
            {envList.map(env => (
              <Option value={env.id} key={env.id}>
                {env.envName}
              </Option>
            ))}
          </Select>
          <Popover
            content={
              <div>
                <Row>
                  <Input
                    id="api-token"
                    value={api.token}
                    addonAfter={<span id="copy-token">复制</span>}
                  />
                </Row>
                <Row>
                  <Input
                    id="api-accessUrl"
                    value={api.accessUrl}
                    addonAfter={<span id="copy-accessUrl">复制</span>}
                  />
                </Row>
              </div>
            }
            overlayStyle={{ width: '85%' }}
            placement="bottomRight"
            trigger="click"
          >
            <Button
              type="primary"
              style={{ marginLeft: 8 }}
              loading={this.state.getTokenLoading}
              onClick={async () => {
                this.setState({
                  getTokenLoading: true
                });
                try {
                  await api.getToken(this.state.selectedEnvId);
                  this.setState({
                    getTokenLoading: false
                  });
                } catch (e) {
                  this.setState({
                    getTokenLoading: false
                  });
                }
              }}
            >
              获取Token
            </Button>
          </Popover>
        </Row>
      </div>
    );
  }

  // 删除接口
  // handleApiDelete = async() => {

  // }

  render() {
    const api = this.props.apiStore!.selectedApi;
    const { isApiManager } = this.props.userStore!;
    const apiStore = this.props.apiStore!;
    const apiId = this.props.match.params.id;
    const loading = this.state.loading;
    let doc: DocModel;
    if (!api) {
      return null;
    } else {
      doc = api.apiDoc!;
    }

    return (
      <div className="api-detail">
        {loading ? (
          <div className="spin-container">
            <Spin size="large" />
          </div>
        ) : (
          <div className="content-container">
            <div className="&-info">
              <div className="&-name">
                <span className="&-uri">{isApiManager ? api.uri : this.state.uri}</span>
                {isApiManager && api.isEditable ? (
                  <Link to={path.join(this.props.location.pathname, 'editor-update')}>编辑</Link>
                ) : null}
                {isApiManager ? (
                  <AuthModal api={[api]} trigger={<Button type="primary">编辑授权</Button>} />
                ) : null}
              </div>
              <Row className="&-row">
                <Col span={8}>
                  请求名称：
                  {api.name}
                </Col>
                <Col span={8}>
                  请求方式：
                  {api.verb}
                </Col>
                <Col span={8}>
                  请求协议：
                  {api.protocol}
                </Col>
              </Row>
              <Row className="&-row">
                <Col span={8}>
                  接口类型：
                  {api.type}
                </Col>
                <Col span={8}>
                  接口状态：
                  <span className="status">已启用</span>
                </Col>
                <Col span={8}>
                  更新日期：
                  {api.updatedAt}
                </Col>
              </Row>
              <Row className="&-row">
                描述：
                {api.description}
              </Row>
              <Row>
                <Col span={12}>
                  {isApiManager ? (
                    <div className="api-detail-optional">
                      <Row className="&-row">
                        授权用户：
                        {api.authedText}
                      </Row>
                    </div>
                  ) : (
                    this.renderOptional(api)
                  )}
                </Col>
                <Col span={12} style={{ textAlign: 'right' }}>
                  {isApiManager ? null : (
                    <ClearTokenConfirmModal
                      onOk={async () => {
                        await apiStore.clearApiToken(apiId);
                      }}
                      trigger={<Button type="primary">清除当前接口所有token</Button>}
                    />
                  )}
                </Col>
              </Row>
              {/* {api.isDeletable ? <DeleteButton onClick={this.handleApiDelete}/> : null} */}
            </div>
            <div className="&-resource">
              <Tabs>
                <TabPane tab="查询参数" key="queries">
                  <KeyEditor value={api.queries} readOnly={true} />
                </TabPane>
                <TabPane tab="请求头部" key="header">
                  <KeyEditor value={api.headers} readOnly={true} />
                </TabPane>
                <TabPane tab="请求体" key="request-body">
                  <SchemaDisplay data={api.requestBody} />
                </TabPane>
                <TabPane tab="返回参数" key="response-body">
                  <SchemaDisplay data={api.responseBody} />
                </TabPane>
                <TabPane tab="接口文档" key="api-doc">
                  {doc ? <DocRender doc={doc} /> : '该接口未提供文档'}
                </TabPane>
                <TabPane tab="接口测试" key="api-test">
                  {isApiManager ? this.renderOptional(api) : null}
                  <ApiTest isApiManager={isApiManager} api={api} />
                </TabPane>
                <TabPane tab="统计" key="statistics">
                  <div className="charts-container">
                    {isApiManager ? <AccessSourceGraph apiId={apiId} /> : null}
                    <VisitTimesGraph apiId={apiId} />
                    <TimeConsumingGraph apiId={apiId} />
                  </div>
                </TabPane>
              </Tabs>
            </div>
          </div>
        )}
      </div>
    );
  }
}
