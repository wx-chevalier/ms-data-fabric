import './ApiUserManagement.scss';
import React from 'react';
import { Button, Col, Row, Input, Select, Modal, Divider, Popconfirm } from 'antd';
import CreateApiUser from './CreateApiUser/CreateApiUser';
import { FormComponentProps } from 'antd/lib/form';
import ApiUserManagementTable from './ApiUserManagementTable/ApiUserManagementTable';
import ApiUserManagementModel from '../../../models/ApiUserManagementModel';
import { inject, observer } from 'mobx-react';
import AccountManagementStore from '../../../stores/AccoutManagementStore';

const Option = Select.Option;

interface ApiUserManagementProps extends FormComponentProps {
  accountManagementStore?: AccountManagementStore;
}

interface ApiUserManagementState {
  loading: boolean;
  dataSource: Array<ApiUserManagementModel>;
  visible: boolean;
  // 用户名搜索框搜索值
  searchName?: string;
  // 状态搜索框搜索值
  searchStatus?: '使用中' | '已停用';
}

@inject('accountManagementStore')
@observer
export default class ApiUserManagement extends React.Component<
  ApiUserManagementProps,
  ApiUserManagementState
> {
  constructor(props: ApiUserManagementProps) {
    super(props);
    this.state = {
      loading: false,
      dataSource: [],
      visible: false
    };
  }

  async componentDidMount() {
    this.setState({
      loading: true
    });
    const accountManagementStore = this.props.accountManagementStore!;
    await accountManagementStore.getApiManagerList();
    const dataSource = accountManagementStore.computedApiManagerList.slice();
    this.setState({
      loading: false,
      dataSource
    });
  }

  handleSearch = () => {
    const dataSource = this.props.accountManagementStore!.computedApiManagerList.slice();
    const { searchName, searchStatus } = this.state;
    if (!searchName || searchName === '') {
      if (!searchStatus) {
        this.setState({
          dataSource
        });
      } else {
        this.setState({
          dataSource: dataSource.filter(d => d.usingStatus === searchStatus)
        });
      }
    } else {
      if (!searchStatus) {
        this.setState({
          dataSource: dataSource.filter(d => d.user.username.includes(searchName))
        });
      } else {
        this.setState({
          dataSource: dataSource.filter(
            d => d.user.username.includes(searchName) && d.usingStatus === searchStatus
          )
        });
      }
    }
  };

  handleUpdate = async (actionType: 'start' | 'stop' | 'delete', id: string) => {
    this.setState({
      loading: true
    });
    const accountManagementStore = this.props.accountManagementStore!;
    switch (actionType) {
      case 'start':
        await accountManagementStore.start(id);
        break;
      case 'stop':
        await accountManagementStore.stop(id);
        break;
      case 'delete':
        await accountManagementStore.deleteApiUser(id);
        break;
      default:
    }

    const dataSource = this.props.accountManagementStore!.computedApiManagerList.slice();
    this.setState({
      loading: false,
      dataSource
    });
  };

  handleCreateApiManager = async (
    userName: string,
    password: string,
    authedDbList: {
      databaseName: string;
      dsId: string;
    }[]
  ) => {
    const accountManagementStore = this.props.accountManagementStore!;
    const user = await accountManagementStore.createApiUser(userName, password);
    await accountManagementStore.authDbToUser(user.id, authedDbList);
    const dataSource = accountManagementStore.computedApiManagerList.slice();
    this.setState({
      visible: false,
      dataSource
    });
  };

  render() {
    const accountManagementStore = this.props.accountManagementStore!;
    const dataSourceList = accountManagementStore.dataSourceList;
    const { loading, dataSource, visible } = this.state;

    return (
      <div className="api-user-management">
        <div className="&-action-group">
          <Row>
            <Col span={6}>
              <Button
                type="primary"
                onClick={() => {
                  this.setState({
                    visible: true
                  });
                }}
              >
                新增用户
              </Button>
            </Col>
            <Col span={18} style={{ display: 'flex', justifyContent: 'flex-end' }}>
              <span className="filter-container">
                用户名：
                <Input
                  style={{ width: 300 }}
                  placeholder="请输入"
                  onChange={e => {
                    this.setState({
                      searchName: e.target.value
                    });
                  }}
                />
              </span>
              <span className="filter-container">
                状态：
                <Select
                  style={{ width: 300 }}
                  onChange={value => {
                    this.setState({
                      searchStatus: value ? (value === 'using' ? '使用中' : '已停用') : undefined
                    });
                  }}
                  allowClear={true}
                >
                  <Option value="using">使用中</Option>
                  <Option value="disabled">已停用</Option>
                </Select>
              </span>
              <Button type="primary" onClick={this.handleSearch}>
                查询
              </Button>
            </Col>
          </Row>
        </div>
        <div className="&-content-wrapper">
          <ApiUserManagementTable
            loading={loading}
            dataSource={dataSource}
            columns={[
              {
                key: 'username',
                title: '用户名',
                dataIndex: 'username',
                render: (text, record) => {
                  return <div>{record.user.username}</div>;
                }
              },
              {
                key: 'authedDatabase',
                title: '所授权的数据库',
                dataIndex: 'authedDatabase',
                render: (text, record) => {
                  return <div>{[...record.authedDatabase.map(d => d.name)].join('、')}</div>;
                }
              },
              {
                key: 'status',
                title: '状态',
                dataIndex: 'status',
                render: (text, record) => {
                  return (
                    <span
                      style={{ color: record.usingStatus === '使用中' ? '#48A84A' : '#D9255E' }}
                    >
                      {record.usingStatus}
                    </span>
                  );
                }
              },
              {
                key: 'createdAt',
                title: '创建时间',
                dataIndex: 'createdAt'
              },
              {
                key: 'action',
                title: '操作',
                dataIndex: 'action',
                render: (text, record) => {
                  return (
                    <span>
                      {record.usingStatus === '使用中' ? (
                        <a
                          href="javascript:;"
                          onClick={async () => {
                            await this.handleUpdate('stop', record.user.id);
                          }}
                          style={{ color: '#D9255E' }}
                        >
                          停用
                        </a>
                      ) : (
                        <span>
                          <a
                            href="javascript:;"
                            onClick={async () => {
                              await this.handleUpdate('start', record.user.id);
                            }}
                          >
                            启用
                          </a>
                          <Divider type="vertical" />
                          <Popconfirm
                            title={'注销管理员账号后，所有数据将会删除，确定注销？'}
                            okText="注销"
                            onConfirm={async () => {
                              await this.handleUpdate('delete', record.user.id);
                            }}
                          >
                            <a href="javascript:;" style={{ color: '#FC7873' }}>
                              注销
                            </a>
                          </Popconfirm>
                        </span>
                      )}
                    </span>
                  );
                }
              }
            ]}
          />
        </div>
        <Modal
          title="新增用户"
          visible={visible}
          onCancel={() =>
            this.setState({
              visible: false
            })
          }
          maskClosable={false}
          footer={null}
          destroyOnClose={true}
        >
          <CreateApiUser
            dataSourceList={dataSourceList}
            onCancel={() => {
              this.setState({
                visible: false
              });
            }}
            onOk={this.handleCreateApiManager}
            {...this.props}
          />
        </Modal>
      </div>
    );
  }
}
