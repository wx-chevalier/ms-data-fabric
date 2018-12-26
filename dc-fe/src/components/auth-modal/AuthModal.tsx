import React, { Component } from 'react';
import { Modal, Transfer } from 'antd';
import './AuthModal.scss';
// import UserModel from '../../models/UserModel';
import { inject, observer } from 'mobx-react';
import UserStore from '../../stores/UserStore';
import ApiModel from '../../models/ApiModel';

interface AuthModalProps {
  // authedUser: UserModel[];
  userStore?: UserStore;
  onSave?: (targetKeys: string[]) => void;
  onCancel?: (targetKeys: string[]) => void;
  api: ApiModel[];
  // tslint:disable-next-line
  trigger?: React.ReactElement<any>;
}

interface AuthModalState {
  showModal: boolean;
  targetKeys: string[];
  selectedKeys: string[];
}

@inject('userStore')
@observer
export default class AuthModal extends Component<AuthModalProps, AuthModalState> {
  static getDerivedStateFromProps(props: AuthModalProps) {
    if (props.api.length === 1) {
      return {
        targetKeys: props.api[0].authedUser.map(u => u.id)
      };
    } else {
      return {
        targetKeys: []
      };
    }
  }

  constructor(props: AuthModalProps) {
    super(props);
    this.state = {
      showModal: false,
      selectedKeys: [],
      targetKeys: []
    };
  }

  toggleShowModal = () => {
    this.setState({
      showModal: !this.state.showModal
    });
  };

  handleCancel = () => {
    if (this.props.onCancel) {
      this.props.onCancel(this.state.targetKeys);
    }
    this.toggleShowModal();
    this.reset();
  };

  handleSave = async () => {
    const { targetKeys } = this.state;
    if (this.props.api.length === 1) {
      await this.props.api[0].auth(targetKeys);
    } else {
      await Promise.all(
        this.props.api.map(async a => {
          a.auth(targetKeys);
        })
      );
    }

    if (this.props.onSave) {
      this.props.onSave(targetKeys);
    }
    this.toggleShowModal();
    this.reset();
  };

  reset = () => {
    if (this.props.api.length === 1) {
      this.setState({
        targetKeys: this.props.api[0].authedUser.map(u => u.id)
      });
    } else {
      this.setState({
        targetKeys: []
      });
    }
  };

  handleChange = (targetKeys: string[]) => {
    this.setState({ targetKeys });
  };

  handleSelectChange = (sSelectedKeys: string[], tSelectedKeys: string[]) => {
    this.setState({ selectedKeys: [...sSelectedKeys, ...tSelectedKeys] });
  };

  renderTrigger() {
    const { trigger } = this.props;
    if (!trigger) {
      return (
        <span className="auth-modal-text" onClick={this.toggleShowModal}>
          编辑授权
        </span>
      );
    } else {
      return React.cloneElement(trigger, {
        onClick: this.toggleShowModal
      });
    }
  }

  render() {
    const { userStore } = this.props;
    const { showModal, selectedKeys, targetKeys } = this.state;
    const dataSource = userStore!.clientList.map(u => ({
      key: u.id,
      title: u.username
    }));
    return (
      <div className="auth-modal">
        {this.renderTrigger()}

        <Modal
          visible={showModal}
          closable={false}
          maskClosable={false}
          width={660}
          onOk={async () => {
            await this.handleSave();
          }}
          onCancel={this.handleCancel}
        >
          <Transfer
            targetKeys={targetKeys}
            dataSource={dataSource}
            selectedKeys={selectedKeys}
            onSelectChange={this.handleSelectChange}
            onChange={this.handleChange}
            showSearch={true}
            operations={['授权', '取消授权']}
            render={i => i.title}
            titles={['未授权用户', '已授权用户']}
            searchPlaceholder="输入用户名称搜索"
            listStyle={{
              width: 250,
              height: 300
            }}
          />
        </Modal>
      </div>
    );
  }
}
