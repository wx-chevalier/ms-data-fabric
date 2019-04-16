import './ModelManagement.scss';
import React from 'react';
import { inject, observer } from 'mobx-react';
import ProjectStore from '../../stores/ProjectStore';
import ProjectModel from '../../models/ProjectModel';
import { Row, Col, Card, Spin, Modal, Popconfirm, message, Avatar } from 'antd';
import { RouteComponentProps } from 'react-router';
import ModelCreationForm from './model-creation-form/ModelCreationForm';
import ModelStore from '../../stores/ModelStore';
import { SchemaObject } from '../../apis/OpenApi3Interface';
import { Model } from '../../apis/interface';
import SchemaDisplay from '../schema-display/SchemaDisplay';

const Meta = Card.Meta;
const managerIcon = require('../../assets/manager-icon.png');
const exportIcon = require('../../assets/export-icon.png');
const generatorIcon = require('../../assets/generator-icon.png');

interface ModelManagementProps extends RouteComponentProps<{ pid: string }> {
  projectStore?: ProjectStore;
  modelStore?: ModelStore;
}

interface ModelManagementState {
  loading: boolean;
  showModal: boolean;
  status?: 'create' | 'update' | 'detail';
  selectedModel?: Model;
}

@inject('projectStore', 'modelStore')
@observer
export default class ModelManagement extends React.Component<
  ModelManagementProps,
  ModelManagementState
> {
  constructor(props: ModelManagementProps) {
    super(props);
    this.state = {
      loading: false,
      showModal: false
    };
  }

  async componentDidMount() {
    this.setState({
      loading: true
    });
    const projectStore = this.props.projectStore!;
    const modelStore = this.props.modelStore!;
    const pid = this.props.match.params.pid;
    await projectStore.selectItem(pid);
    await modelStore.getSchemaModelList(pid);

    this.setState({
      loading: false
    });
  }

  renderIcon = (index: number) => {
    if (index % 3 === 0) {
      return managerIcon;
    } else if (index % 3 === 1) {
      return exportIcon;
    } else {
      return generatorIcon;
    }
  };

  toggleModal = () => {
    if (this.state.showModal) {
      this.setState({
        showModal: false,
        selectedModel: undefined
      });
    } else {
      this.setState({
        showModal: true
      });
    }
  };

  render() {
    const projectStore = this.props.projectStore!;
    const modelStore = this.props.modelStore!;
    const pid = this.props.match.params.pid;
    const { loading, showModal, status, selectedModel } = this.state;
    const project: ProjectModel | null = projectStore.selectedItem;
    const modelList = modelStore.schemaModelList;

    if (!project) {
      return null;
    }

    return (
      <div className="model-management">
        <header>
          <h1 className="&-name">项目名称：{project.name}</h1>
        </header>
        <div className="&-workspace">
          <Row gutter={16} style={{ marginBottom: 16 }} className="&-row">
            <Col span={8} className="&-col">
              <Card
                className="&-model-creation-card"
                onClick={() => {
                  this.setState(
                    {
                      status: 'create'
                    },
                    this.toggleModal
                  );
                }}
              >
                + 新增模型
              </Card>
            </Col>
            {loading ? (
              <div className="spin-container">
                <Spin size="large" />
              </div>
            ) : (
              modelList.map((m, k) => {
                return (
                  <Col key={k} span={8} className="model-management-workspace-row-col">
                    <Card
                      className="&-model-card"
                      actions={[
                        <a
                          key="1"
                          onClick={() => {
                            this.setState(
                              {
                                status: 'detail',
                                selectedModel: m
                              },
                              this.toggleModal
                            );
                          }}
                        >
                          详情
                        </a>,
                        <a
                          key="2"
                          onClick={() => {
                            this.setState(
                              {
                                status: 'update',
                                selectedModel: m
                              },
                              this.toggleModal
                            );
                          }}
                        >
                          编辑
                        </a>,
                        <Popconfirm
                          key="3"
                          title="确认删除"
                          onConfirm={async () => {
                            this.setState({
                              loading: true
                            });
                            try {
                              await modelStore.deleteModel(m.id);
                              await modelStore.getSchemaModelList(pid);
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
                      <div className="card-content">
                        <Meta
                          key={k}
                          avatar={<Avatar src={this.renderIcon(k)} />}
                          title={m.modelName}
                          description={
                            <div>
                              <div>{m.modelDescribe}</div>
                            </div>
                          }
                        />
                      </div>
                    </Card>
                  </Col>
                );
              })
            )}
          </Row>
        </div>
        <Modal
          visible={showModal}
          onCancel={this.toggleModal}
          width={800}
          title={status === 'create' ? '新建模型' : status === 'update' ? '编辑模型' : '模型详情'}
          footer={null}
          destroyOnClose={true}
        >
          {status !== 'detail' ? (
            <ModelCreationForm
              initialValue={this.state.selectedModel}
              onOk={async (name: string, definition: SchemaObject, description: string) => {
                if (this.state.status === 'create') {
                  await modelStore.createModel(pid, name, definition, description);
                  await modelStore.getSchemaModelList(pid);
                }
                if (this.state.status === 'update') {
                  await modelStore.updateModel(
                    this.state.selectedModel!.id,
                    name,
                    definition,
                    description
                  );
                  await modelStore.getSchemaModelList(pid);
                }
              }}
              onCancel={this.toggleModal}
            />
          ) : (
            <SchemaDisplay data={selectedModel ? selectedModel.modelDefinition : {}} />
          )}
        </Modal>
      </div>
    );
  }
}
