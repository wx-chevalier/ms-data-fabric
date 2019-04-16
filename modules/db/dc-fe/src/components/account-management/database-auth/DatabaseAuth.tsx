import './DatabaseAuth.scss';
import React from 'react';
import { Card, Modal, Checkbox, Row, Col, Spin } from 'antd';
import { TableModel, DatabaseModel } from '../../../models/datasource';
import { observer, inject } from 'mobx-react';
import AccounManagementStore from '../../../stores/AccoutManagementStore';

const CheckboxGroup = Checkbox.Group;

interface DatabaseAuthProps {
  accountManagementStore?: AccounManagementStore;
}

interface DatabaseAuthState {
  loading: boolean;
  confirmLoading: boolean;
  tableList: TableModel[];
  curModal?: 'changeAuthorization' | 'viewTableName';
  curDatabase?: DatabaseModel;
  authedUserList: Array<string>;
  error: string;
}

@inject('accountManagementStore')
@observer
export default class DatabaseAuth extends React.Component<DatabaseAuthProps, DatabaseAuthState> {
  constructor(props: DatabaseAuthProps) {
    super(props);
    this.state = {
      loading: false,
      confirmLoading: false,
      tableList: [],
      curModal: undefined,
      curDatabase: undefined,
      authedUserList: [],
      error: ''
    };
  }

  async componentDidMount() {
    this.setState({
      loading: true
    });
    const accountManagementStore = this.props.accountManagementStore!;
    await accountManagementStore.getDatabaseList();
    this.setState({
      loading: false
    });
  }

  toggleModal = (modal?: 'changeAuthorization' | 'viewTableName') => {
    this.setState({
      curModal: modal
    });
  };

  handleOk = async () => {
    this.setState({
      confirmLoading: true
    });
    const { curDatabase, authedUserList } = this.state;
    const accountManagementStore = this.props.accountManagementStore!;

    try {
      await accountManagementStore.changeAuthorization(
        curDatabase!.datasourceName,
        curDatabase!.name,
        authedUserList
      );

      await accountManagementStore.getDatabaseList();

      this.setState({
        confirmLoading: false
      });
      this.toggleModal();
    } catch (err) {
      this.setState({ confirmLoading: false, error: err.message });
    }
  };

  render() {
    const accountManagementStore = this.props.accountManagementStore!;
    const { curModal, authedUserList, loading, confirmLoading } = this.state;
    const data = accountManagementStore.originDbData;
    const availableApiManagerList = accountManagementStore.availableApiManagerList;

    return (
      <div className="database-auth">
        {loading ? (
          <div className="spin-container">
            <Spin size="large" />
          </div>
        ) : (
          data.map(i => (
            <Card
              title={i.name}
              key={`${i.dbId}`}
              style={{ display: 'inline-block', width: 360, margin: '20px 40px' }}
              actions={[
                <span
                  key="change-authorization"
                  onClick={() => {
                    this.setState({
                      curDatabase: i,
                      authedUserList: i.authedUserList!.map(u => u.id)
                    });
                    this.toggleModal('changeAuthorization');
                  }}
                >
                  更改授权
                </span>,
                <span
                  key="view-tablename"
                  onClick={() => {
                    this.toggleModal('viewTableName');
                    this.setState({
                      tableList: i.tableList!
                    });
                  }}
                >
                  查看表名
                </span>
              ]}
            >
              <div>
                数据库类型：
                {i.databaseType}
              </div>
              <div>
                创建时间：
                {i.createdAt}
              </div>
              <div>
                已授权用户：
                {i.authedUserList!.map(u => u.username).join('、')}
              </div>
            </Card>
          ))
        )}
        <Modal
          className="change-authorization-modal"
          visible={curModal === 'changeAuthorization'}
          confirmLoading={confirmLoading}
          onOk={this.handleOk}
          onCancel={() => this.toggleModal()}
          title="更改授权"
          destroyOnClose={true}
        >
          <CheckboxGroup
            onChange={values => {
              this.setState({
                authedUserList: values as string[]
              });
            }}
            defaultValue={authedUserList}
            style={{ width: '100%' }}
          >
            <Row>
              {availableApiManagerList.map(i => (
                <Col span={8} key={i.id}>
                  <Checkbox value={i.id}>{i.username}</Checkbox>
                </Col>
              ))}
            </Row>
          </CheckboxGroup>
          <div className="&-err">{this.state.error}</div>
        </Modal>
        <Modal
          visible={curModal === 'viewTableName'}
          onCancel={() => this.toggleModal()}
          onOk={() => this.toggleModal()}
          title="查看表名"
        >
          <Row>
            {this.state.tableList.map((i, id) => (
              <Col span={12} key={`${i.tableName}-${id}`}>
                <div>{i.tableName}</div>
              </Col>
            ))}
          </Row>
        </Modal>
      </div>
    );
  }
}
