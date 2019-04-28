import React, { Component } from 'react';
import './ProjectDetail.scss';
import ProjectModel from '../../models/ProjectModel';
import { inject, observer } from 'mobx-react';
import ProjectStore from '../../stores/ProjectStore';
import { RouteComponentProps, Link } from 'react-router-dom';
import { Row, Col, Modal, Card, Avatar, Spin } from 'antd';
import ProjectForm from '../project-form/ProjectForm';
import NavCard from '../nav-card/NavCard';
import ApiStore from '../../stores/ApiStore';
const Meta = Card.Meta;
const managerIcon = require('../../assets/manager-icon.png');
const exportIcon = require('../../assets/export-icon.png');
const generatorIcon = require('../../assets/generator-icon.png');

interface ProjectDetailProps extends RouteComponentProps<{ pid: string }> {
  projectStore?: ProjectStore;
  apiStore?: ApiStore;
}

interface ProjectDetailState {
  loading: boolean;
  showProjectForm: boolean;
}

@inject('projectStore', 'apiStore')
@observer
export default class ProjectDetail extends Component<ProjectDetailProps, ProjectDetailState> {
  constructor(props: ProjectDetailProps) {
    super(props);
    this.state = {
      loading: false,
      showProjectForm: false
    };
  }

  async componentDidMount() {
    this.setState({
      loading: true
    });
    const { apiStore, projectStore } = this.props;
    const pId = this.props.match.params.pid;
    projectStore!.clearSelectedItem();
    await projectStore!.selectItem(pId);
    await apiStore!.loadListForProject('folder', pId, true);
    await apiStore!.loadListForProject('api', pId, true);
    this.setState({
      loading: false
    });
  }

  toggleForm = () => {
    this.setState({
      showProjectForm: !this.state.showProjectForm
    });
  };

  render() {
    const pathname = this.props.location.pathname;
    const projectStore = this.props.projectStore!;
    const project: ProjectModel | null = projectStore.selectedItem;
    if (!project) {
      return null;
    }
    const loading = this.state.loading;

    return (
      <div className="project-detail">
        {loading ? (
          <div className="spin-container">
            <Spin size="large" />
          </div>
        ) : (
          <div className="detail-container">
            <div className="&-info">
              <div className="&-name">
                {project.name}
                <span className="&-edit" onClick={this.toggleForm}>
                  编辑
                </span>
              </div>
              <Row className="&-row">
                <Col span={6}>
                  项目类型：
                  {project.type}
                </Col>
                <Col span={6}>
                  版本号：
                  {project.version}
                </Col>
                <Col span={6}>
                  最后更新日期：
                  {project.updatedAt}
                </Col>
                <Col span={6}>
                  创建时间：
                  {project.createdAt}
                </Col>
              </Row>
              {/* <Row className="&-row">
            <Col span={12}>接口数：{project.apiNumber}</Col>
          </Row> */}
              <Row className="&-row">
                <Col span={24}>
                  描述：
                  {project.description}
                </Col>
              </Row>
            </div>
            <div className="&-stat">
              <Row gutter={16}>
                <Col span={8}>
                  <NavCard
                    title="接口总数"
                    description="项目中所有接口"
                    content={String(project.apiNumber)}
                    quantifier="个"
                    to="api"
                  />
                </Col>
                <Col span={8}>
                  <NavCard
                    title="项目文档"
                    description="关于项目详情、特殊说明等文档"
                    content={String(project.docNumber)}
                    quantifier="篇"
                    to="doc"
                  />
                </Col>
                <Col span={8}>
                  <NavCard
                    title="项目动态"
                    description="项目、接口、文档等操作日志"
                    content={String(project.dynamicNum)}
                    quantifier="条"
                    to="dynamic"
                  />
                </Col>
              </Row>
            </div>
            <div className="&-feature">
              <h3>特色功能</h3>
              <Row gutter={16} style={{ marginBottom: 16 }}>
                <Col span={8}>
                  <Card
                    actions={[
                      <Link key="1" to="envmanagement">
                        查看详情
                      </Link>
                    ]}
                  >
                    <Meta
                      avatar={<Avatar src={managerIcon} />}
                      title="环境管理"
                      description={
                        <div>
                          为不同部署环境定义一组变量，可以在接口定义过程中使用，方便用户进行接口执行环境的切换
                        </div>
                      }
                    />
                  </Card>
                </Col>
                <Col span={8}>
                  <Card
                    actions={[
                      <Link key="1" to="modelmanagement">
                        查看详情
                      </Link>
                    ]}
                  >
                    <Meta
                      avatar={<Avatar src={generatorIcon} />}
                      title="模型管理"
                      description={
                        <div style={{ height: '42px' }}>
                          定义项目中需要复用的业务对象，供接口定义时使用
                        </div>
                      }
                    />
                  </Card>
                </Col>
                <Col span={8}>
                  <Card
                    actions={[
                      <Link key="1" to={pathname}>
                        即将上线
                      </Link>
                    ]}
                  >
                    <Meta
                      avatar={<Avatar src={exportIcon} />}
                      title="备份项目"
                      description={<div style={{ height: '42px' }}>备份项目信息到本地</div>}
                    />
                  </Card>
                </Col>
              </Row>
              <Row gutter={16}>
                <Col span={8}>
                  <Card
                    actions={[
                      <Link key="1" to={pathname}>
                        即将上线
                      </Link>
                    ]}
                  >
                    <Meta
                      avatar={<Avatar src={managerIcon} />}
                      title="自动化测试"
                      description={
                        <div style={{ height: '42px' }}>
                          编写接口测试用例，定期进行接口健康检测，接口异常预警
                        </div>
                      }
                    />
                  </Card>
                </Col>
                <Col span={8}>
                  <Card
                    actions={[
                      <Link key="1" to={pathname}>
                        即将上线
                      </Link>
                    ]}
                  >
                    <Meta
                      avatar={<Avatar src={generatorIcon} />}
                      title="自动生成文档"
                      description={
                        <div style={{ height: '42px' }}>
                          生成整个项目、或者指定接口的 PDF 文档，方便交换
                        </div>
                      }
                    />
                  </Card>
                </Col>
                <Col span={8}>
                  <Card
                    actions={[
                      <Link key="1" to={pathname}>
                        即将上线
                      </Link>
                    ]}
                  >
                    <Meta
                      avatar={<Avatar src={exportIcon} />}
                      title="恢复项目"
                      description={<div style={{ height: '42px' }}>导入本地备份项目</div>}
                    />
                  </Card>
                </Col>
              </Row>
            </div>
          </div>
        )}
        <Modal visible={this.state.showProjectForm} footer={null} closable={false} title="编辑项目">
          <ProjectForm
            typeOptions={projectStore.projectTypeList}
            initialValues={project}
            onCancel={this.toggleForm}
            onSave={async values => {
              await project.updateInfo(values);
            }}
          />
        </Modal>
      </div>
    );
  }
}
