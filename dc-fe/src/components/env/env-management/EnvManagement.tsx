import './EnvManagement.scss';

import React from 'react';
import ProjectStore from '../../../stores/ProjectStore';
import EnvStore from '../../../stores/EnvStore';
import { RouteComponentProps, Link } from 'react-router-dom';
import { inject, observer } from 'mobx-react';
import { Card, Avatar, Row, Col, Spin, Popconfirm, message } from 'antd';
import ProjectModel from '../../../models/ProjectModel';

const Meta = Card.Meta;
const managerIcon = require('../../../assets/manager-icon.png');

interface EnvManagementProps extends RouteComponentProps<{ pid: string }> {
  projectStore?: ProjectStore;
  envStore?: EnvStore;
}

interface EnvManagementState {
  loading: boolean;
  modal?: 'create' | 'update';
  selectedEnvId: string[];
}

@inject('projectStore', 'envStore')
@observer
export default class EnvManagement extends React.Component<EnvManagementProps, EnvManagementState> {
  constructor(props: EnvManagementProps) {
    super(props);
    this.state = {
      loading: false,
      modal: undefined,
      selectedEnvId: []
    };
  }

  async componentDidMount() {
    this.setState({
      loading: true
    });
    const envStore = this.props.envStore!;
    const projectStore = this.props.projectStore!;
    const pid = this.props.match.params.pid;
    await projectStore!.selectItem(pid);

    await envStore.loadEnvListByPid(pid);
    this.setState({
      loading: false
    });
  }

  toggleModal = (modal?: 'create' | 'update') => {
    this.setState({ modal });
  };

  render() {
    const projectStore = this.props.projectStore!;
    const envStore = this.props.envStore!;
    const loading = this.state.loading;

    const project: ProjectModel | null = projectStore.selectedItem;

    if (!project) {
      return null;
    }

    return (
      <div className="env-management">
        <header className="&-project-info">
          <div className="&-name">{project.name}</div>
          <div className="&-description">
            项目简介：
            {project.description}
          </div>
        </header>
        <div className="&-workspace">
          <Row gutter={16} style={{ marginBottom: 16 }} className="&-row">
            <Col span={8} className="&-col">
              <Link className="&" to={`/project/${project.id}/envmanagement/new`}>
                <Card className="&-env-creation-card">+ 新增环境</Card>
              </Link>
            </Col>
            {loading ? (
              <div className="spin-container">
                <Spin size="large" />
              </div>
            ) : (
              envStore.envList.map((env, key) => (
                <Col key={key} span={8} className="env-management-workspace-row-col">
                  <Card
                    className="&-env-card"
                    actions={[
                      <Link key="1" to={`/project/${project.id}/envmanagement/${env.id}`}>
                        详情
                      </Link>,
                      <Popconfirm
                        key="2"
                        title="确认删除"
                        onConfirm={async () => {
                          this.setState({
                            loading: true
                          });
                          try {
                            await this.props.envStore!.delete([env.id]);
                            await this.props.envStore!.loadEnvListByPid(
                              this.props.match.params.pid
                            );
                            this.setState({
                              loading: false
                            });
                          } catch (err) {
                            message.error(err.message);
                            this.setState({
                              loading: false
                            });
                          }
                        }}
                      >
                        <a href="javascript:;" style={{ color: '#E18C8B' }}>
                          删除
                        </a>
                      </Popconfirm>
                    ]}
                  >
                    <Meta
                      key={key}
                      avatar={<Avatar src={managerIcon} />}
                      title={env.envName}
                      description={
                        <div>
                          <div>
                            创建时间：
                            {env.createdAt}
                          </div>
                          <div>
                            环境描述：
                            {env.envDescription || ' '}
                          </div>
                        </div>
                      }
                    />
                  </Card>
                </Col>
              ))
            )}
          </Row>
        </div>
      </div>
    );
  }
}
