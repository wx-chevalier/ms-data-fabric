import './QyUserAudit.scss';
import React from 'react';
import { QyUserModel } from '../../../models/UserModel';
import { Radio, Input, Card, Col, Row, Modal, Spin } from 'antd';
import { inject, observer } from 'mobx-react';
import AccounManagementStore from '../../../stores/AccoutManagementStore';

const RadioButton = Radio.Button;
const RadioGroup = Radio.Group;
const Search = Input.Search;

interface QyUserAuditProps {
  accountManagementStore?: AccounManagementStore;
}

interface QyUserAuditState {
  data: Array<QyUserModel>;
  loading: boolean;
  status: 'toAudit' | 'audited' | 'all';
  curModal?: 'license' | 'audit';
  searchValue?: string;
  curUser?: QyUserModel;
  error: string;
}

@inject('accountManagementStore')
@observer
export default class EnterpriseUserAudit extends React.Component<
  QyUserAuditProps,
  QyUserAuditState
> {
  constructor(props: QyUserAuditProps) {
    super(props);
    this.state = {
      data: [],
      loading: false,
      status: 'toAudit',
      curModal: undefined,
      searchValue: undefined,
      curUser: undefined,
      error: ''
    };
  }

  async componentDidMount() {
    this.setState({
      loading: true
    });
    const accountManagementStore = this.props.accountManagementStore!;
    await accountManagementStore.getQyUserList();
    const originData = accountManagementStore.originQyUserData;
    this.setState({
      loading: false,
      data: originData.filter(i => i.audited === false)
    });
  }

  toggleModal = (modal?: 'license' | 'audit') => {
    this.setState({
      curModal: modal
    });
  };

  generateDataByStatus(status: 'toAudit' | 'audited' | 'all'): Array<QyUserModel> {
    const originData = this.props.accountManagementStore!.originQyUserData;
    return status === 'toAudit'
      ? originData.filter(i => i.audited === false)
      : status === 'audited'
        ? originData.filter(i => i.audited === true)
        : originData;
  }

  // 状态tab切换
  // tslint:disable-next-line
  onChange = (e: any) => {
    const status = e.target.value;
    this.setState({
      status,
      data: this.generateDataByStatus(status)
    });
  };

  onSearch = (value: string) => {
    const status = this.state.status;
    if (value === '') {
      this.setState({
        searchValue: value,
        data: this.generateDataByStatus(status)
      });
    } else {
      const data = this.generateDataByStatus(status);
      this.setState({
        searchValue: value,
        data: data.filter(i => i.username.includes(value))
      });
    }
  };

  handleOk = async () => {
    const accountManagementStore = this.props.accountManagementStore!;
    const curUser = this.state.curUser;
    try {
      await accountManagementStore.qyUserAudit(curUser!.id);
      this.toggleModal();
      await this.refreshData();
    } catch (err) {
      this.setState({
        error: err.message
      });
    }
  };

  async refreshData() {
    const accountManagementStore = this.props.accountManagementStore!;
    const status = this.state.status;
    await accountManagementStore.getQyUserList();
    this.setState({
      data: this.generateDataByStatus(status)
    });
  }

  render() {
    const { data, curModal, curUser, loading } = this.state;

    return (
      <div className="enterprise-user-audit">
        <div className="&-select-status">
          <Row>
            <Col span={18}>
              <div className="&-status-list">
                <b>状态：</b>
                <RadioGroup onChange={this.onChange} defaultValue="toAudit">
                  <RadioButton value="toAudit">待审核</RadioButton>
                  <RadioButton value="audited">已审核</RadioButton>
                  <RadioButton value="all">全部</RadioButton>
                </RadioGroup>
              </div>
            </Col>
            <Col span={6} style={{ display: 'flex', justifyContent: 'flex-end' }}>
              <Search placeholder="输入关键词" onSearch={this.onSearch} style={{ width: 400 }} />
            </Col>
          </Row>
        </div>
        <div className="&-audit-list">
          {loading ? (
            <div className="spin-container">
              <Spin size="large" />
            </div>
          ) : (
            <div className="content-container">
              {data.map(i => (
                <Card
                  title={i.username}
                  key={`${i.id}`}
                  style={{ display: 'inline-block', width: 388, height: 196, margin: '20px 30px' }}
                  actions={
                    i.audited
                      ? [
                          <span
                            key="view-license"
                            onClick={() => {
                              this.toggleModal('license');
                              this.setState({
                                curUser: i
                              });
                            }}
                          >
                            查看营业执照
                          </span>
                        ]
                      : [
                          <span
                            key="view-license"
                            onClick={() => {
                              this.toggleModal('license');
                              this.setState({
                                curUser: i
                              });
                            }}
                          >
                            查看营业执照
                          </span>,
                          <span
                            key="audit"
                            onClick={() => {
                              this.toggleModal('audit');
                              this.setState({
                                curUser: i
                              });
                            }}
                          >
                            审核
                          </span>
                        ]
                  }
                >
                  <div>
                    联系方式：
                    {i.phone}
                  </div>
                  <div>
                    申请时间：
                    {i.createdAt}
                  </div>
                </Card>
              ))}
            </div>
          )}
        </div>
        <Modal
          className="view-license-modal"
          visible={curModal === 'license'}
          onOk={() => this.toggleModal()}
          onCancel={() => this.toggleModal()}
          maskClosable={false}
          title="查看营业执照"
        >
          {curUser && curUser.businessLicense ? (
            <img src={curUser.businessLicense} style={{ width: 472, height: 300 }} />
          ) : (
            '未上传营业执照'
          )}
        </Modal>
        <Modal
          className="audit-modal"
          visible={curModal === 'audit'}
          onOk={this.handleOk}
          onCancel={() => this.toggleModal()}
          maskClosable={false}
          title="审核"
        >
          <div className="audit-confirm">确认该企业通过审核？</div>
          <div className="&-err">{this.state.error}</div>
        </Modal>
      </div>
    );
  }
}
