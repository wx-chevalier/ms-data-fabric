import React, { Component } from 'react';
import './ApiFolder.scss';
import { RouteComponentProps, Link } from 'react-router-dom';
import ApiTable from '../api-table/ApiTable';
import { inject, observer } from 'mobx-react';
import ProjectStore from '../../stores/ProjectStore';
import { Tooltip, Button, Divider, Menu, Dropdown, Icon, Modal, Badge, Spin, message } from 'antd';
import FolderForm, { FieldValues } from '../folder-form/FolderForm';
import ApiStore from '../../stores/ApiStore';
import DataBaseStore from '../../stores/DataBaseStore';
import AuthModal from '../auth-modal/AuthModal';
import { appendPath } from '../../utils';
import ProjectModel from '../../models/ProjectModel';
import ApiModel from '../../models/ApiModel';
const MenuItem = Menu.Item;

interface ApiFolderProps extends RouteComponentProps<{ pid: string }> {
  projectStore?: ProjectStore;
  apiStore?: ApiStore;
  dbStore?: DataBaseStore;
}

interface ApiFolderState {
  loading: boolean;
  modal?: 'create' | 'update';
  selectedFolder: string;
  selectedApi: ApiModel[];
}

@inject('projectStore', 'apiStore', 'dbStore')
@observer
export default class ApiFolder extends Component<ApiFolderProps, ApiFolderState> {
  constructor(props: ApiFolderProps) {
    super(props);
    this.state = {
      loading: false,
      modal: undefined,
      selectedFolder: 'ungroup',
      selectedApi: []
    };
  }

  async componentDidMount() {
    this.setState({
      loading: true
    });
    const pId = this.props.match.params.pid;
    await this.props.apiStore!.loadListForProject('api', pId, true);
    await this.props.apiStore!.loadListForProject('folder', pId, true);
    await this.props.projectStore!.selectItem(pId);
    this.setState({
      loading: false
    });
  }

  toggleModal = (modal?: 'create' | 'update') => () => {
    this.setState({ modal });
  };

  confirmForm = async (action: 'create' | 'update', v: FieldValues) => {
    await this.props.apiStore!.handleFolder(action, this.state.selectedFolder, v);
    this.setState({
      modal: undefined
    });
  };

  handleApiDelete = (api: ApiModel) => {
    Modal.confirm({
      title: '确认删除接口',
      content: `确定删除接口：${api.name} ？一旦删除，不可恢复。`,
      okType: 'danger',
      onOk: async () => {
        try {
          await this.props.apiStore!.removeApiById(api.id);
        } catch (err) {
          message.error(err.message);
        }
      }
    });
  };

  render() {
    const apiStore = this.props.apiStore!;
    const dbStore = this.props.dbStore!;
    const projectStore = this.props.projectStore!;
    const { apiList, folderList } = apiStore;
    const { selectedFolder, loading } = this.state;
    const folder = folderList.find(f => f.id === selectedFolder);
    const project: ProjectModel | null = projectStore.selectedItem;

    if (!project) {
      return null;
    }

    return (
      <div className="api-folder">
        {loading ? (
          <div className="spin-container">
            <Spin size="large" />
          </div>
        ) : (
          <div className="content-container">
            <header className="&-name">
              <h1>
                项目名称：
                {project.name}
              </h1>
            </header>
            <div className="&-project" />
            <div className="&-workspace">
              <div className="&-list">
                <div className="&-buttons">
                  <Button type="primary" onClick={this.toggleModal('create')}>
                    新增分组
                  </Button>
                </div>
                <Divider />
                <Menu
                  defaultSelectedKeys={['ungroup']}
                  onSelect={({ selectedKeys }) =>
                    this.setState({ selectedFolder: selectedKeys[0] })
                  }
                >
                  <MenuItem key="ungroup">
                    <span>默认分组</span>
                  </MenuItem>
                  {folderList.map(f => (
                    <MenuItem key={f.id}>
                      <span>{f.name}</span>
                      <Dropdown
                        overlay={
                          <Menu>
                            <MenuItem>
                              <span
                                onClick={() => (
                                  this.toggleModal('update')(),
                                  this.setState({ selectedFolder: f.id })
                                )}
                              >
                                重命名
                              </span>
                            </MenuItem>
                            <MenuItem>
                              <span onClick={() => apiStore.removeFolder(f.id)}>删除</span>
                            </MenuItem>
                          </Menu>
                        }
                      >
                        <Icon type="ellipsis" />
                      </Dropdown>
                    </MenuItem>
                  ))}
                </Menu>
              </div>
              <div className="&-table">
                <div className="&-buttons">
                  <Button type="primary">
                    <Link
                      to={appendPath(
                        this.props.location.pathname,
                        `editor-new?fid=${selectedFolder}`
                      )}
                    >
                      新增接口
                    </Link>
                  </Button>
                  <Button
                    onClick={() => {
                      dbStore.fId = this.state.selectedFolder;
                    }}
                  >
                    <Link to={appendPath(this.props.location.pathname, 'editor-impl')}>
                      生成接口
                    </Link>
                  </Button>
                  <Button disabled={true} type="primary">
                    导入接口
                  </Button>
                  <AuthModal
                    api={this.state.selectedApi}
                    trigger={
                      <Button disabled={this.state.selectedApi.length === 0}>批量授权</Button>
                    }
                  />
                </div>
                <Divider />
                <ApiTable
                  dataSource={apiList.filter(a => a.folderId === selectedFolder)}
                  action={[
                    {
                      key: 'auth',
                      title: '授权情况',
                      width: 88,
                      render: (text, record) => {
                        if (record.authedUser.length > 0) {
                          return (
                            <Tooltip title={record.authedText}>
                              <Badge status="success" text="已授权" />
                            </Tooltip>
                          );
                        } else {
                          return <Badge status="error" text="未授权" />;
                        }
                      }
                    },
                    {
                      key: 'action',
                      title: '操作',
                      render: (t, r) => (
                        <span>
                          <Link to={appendPath(this.props.location.pathname, r.id)}>查看详情</Link>
                          <Divider type="vertical" />
                          <AuthModal api={[r]} />
                          {r.isDeletable
                            ? [
                                <Divider key="0" type="vertical" />,
                                <a
                                  key="1"
                                  href="javascript:;"
                                  onClick={() => this.handleApiDelete(r)}
                                  style={{ color: '#E18C8B' }}
                                >
                                  删除
                                </a>
                              ]
                            : null}
                        </span>
                      )
                    }
                  ]}
                  rowSelection={{
                    type: 'checkbox',
                    onChange: (selectedRowKeys, selectedRow: ApiModel[]) => {
                      this.setState({
                        selectedApi: selectedRow
                      });
                    }
                  }}
                />
              </div>
            </div>
          </div>
        )}
        <Modal
          visible={this.state.modal === 'create'}
          title="新建分组"
          footer={null}
          onCancel={this.toggleModal()}
        >
          <FolderForm onCancel={this.toggleModal()} onSave={v => this.confirmForm('create', v)} />
        </Modal>
        <Modal
          visible={this.state.modal === 'update'}
          title="重命名分组"
          footer={null}
          onCancel={this.toggleModal()}
        >
          <FolderForm
            onCancel={this.toggleModal()}
            onSave={v => this.confirmForm('update', v)}
            initialValues={folder ? folder.formValues : undefined}
          />
        </Modal>
      </div>
    );
  }
}
