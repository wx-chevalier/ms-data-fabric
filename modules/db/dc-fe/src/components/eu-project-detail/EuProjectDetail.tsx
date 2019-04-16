import React, { Component } from 'react';
import { inject, observer } from 'mobx-react';
import { RouteComponentProps, Link } from 'react-router-dom';
import { Row, Col, Tabs, Spin, Popover, Button } from 'antd';
import './EuProjectDetail.scss';
import ProjectStore from '../../stores/ProjectStore';
import ApiStore from '../../stores/ApiStore';
import DocStore from '../../stores/DocStore';
import ProjectModel from '../../models/ProjectModel';
import ApiTable from '../api-table/ApiTable';
import { appendPath } from '../../utils';
import DocModel from '../../models/DocModel';
import DocTable from '../doc/doc-table/DocTable';
import ClearTokenConfirmModal from '../clear-token-comfirm-modal/ClearTokenConfirmModal';

const TabPane = Tabs.TabPane;

interface EuProjectDetailProps extends RouteComponentProps<{ pid: string }> {
  projectStore?: ProjectStore;
  apiStore?: ApiStore;
  docStore?: DocStore;
}

interface EuProjectDetailState {
  loading: boolean;
}

@inject('projectStore', 'apiStore', 'docStore')
@observer
export default class EuProjectDetail extends Component<EuProjectDetailProps, EuProjectDetailState> {
  constructor(props: EuProjectDetailProps) {
    super(props);
    this.state = {
      loading: false
    };
  }

  async componentDidMount() {
    this.setState({
      loading: true
    });
    const { match, projectStore, apiStore, docStore } = this.props;
    const pId = match.params.pid;
    await projectStore!.selectItem(pId);
    await apiStore!.loadListForProject('api', pId, true);
    await docStore!.loadDocListByPid(pId);
    this.setState({
      loading: false
    });
  }

  render() {
    const { projectStore, apiStore, docStore } = this.props;
    const { pathname } = this.props.location;
    const pId = this.props.match.params.pid;
    const project: ProjectModel | null = projectStore!.selectedItem;
    const docList: DocModel[] = docStore!.docList;
    const loading = this.state.loading;

    if (!project) {
      return null;
    }

    return (
      <div className="eu-project-detail">
        {loading ? (
          <div className="spin-container">
            <Spin size="large" />
          </div>
        ) : (
          <div className="content-container">
            <div className="&-info">
              <div className="&-header">
                <span className="&-name">{project.name}</span>
                <Button type="primary" disabled={true}>
                  SDK下载
                </Button>
              </div>
              <Row className="&-row">
                <Col span={12}>
                  项目类型：
                  {project.type}
                </Col>
                <Col span={12}>
                  版本号：
                  {project.version}
                </Col>
              </Row>
              <Row className="&-row">
                <Col span={12}>
                  最后更新日期：
                  {project.updatedAt}
                </Col>
                <Col span={12}>
                  创建时间：
                  {project.createdAt}
                </Col>
              </Row>
              <Row className="&-row">
                <Col span={12}>
                  接口数：
                  {project.apiNumber}
                </Col>
              </Row>
              <Row className="&-row">
                <Col span={24}>
                  描述：
                  {project.description}
                </Col>
              </Row>
            </div>
            <div className="&-resource">
              <Tabs>
                <TabPane tab="接口列表" key="api">
                  <div>
                    <ClearTokenConfirmModal
                      onOk={async () => {
                        await apiStore!.clearProjectToken(pId);
                      }}
                      trigger={
                        <Button type="primary" style={{ marginBottom: '16px' }}>
                          清除当前项目所有token
                        </Button>
                      }
                    />
                  </div>
                  <ApiTable
                    dataSource={apiStore!.apiList}
                    action={[
                      {
                        title: '操作',
                        key: 'action',
                        render: (t, r) => (
                          <Link to={appendPath(pathname, 'api/' + r.id)}>查看详情</Link>
                        )
                      },
                      {
                        title: '当前token',
                        key: 'curToken',
                        render: (t, r) => (
                          <ClearTokenConfirmModal
                            onOk={async () => {
                              await apiStore!.clearApiToken(r.id);
                            }}
                            trigger={<Button type="primary">清除</Button>}
                          />
                        )
                      }
                    ]}
                    rowKey={record => record.id}
                  />
                </TabPane>
                <TabPane tab="项目文档" key="doc">
                  <DocTable
                    dataSource={docList.slice()}
                    docName={[
                      {
                        key: 'docName',
                        title: '名称',
                        dataIndex: 'docName',
                        width: 200,
                        render: (text, record) => (
                          <Popover content={record.title!}>
                            <span>{record.title!}</span>
                          </Popover>
                        )
                      }
                    ]}
                    action={[
                      {
                        key: 'action',
                        title: '操作',
                        render: (text, record) => (
                          <Link to={appendPath(pathname, 'doc/' + record.id)}>查看详情</Link>
                        )
                      }
                    ]}
                  />
                </TabPane>
              </Tabs>
            </div>
          </div>
        )}
      </div>
    );
  }
}
