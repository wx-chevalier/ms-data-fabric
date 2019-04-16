import React, { Component } from 'react';
import { Modal, Form, Input, Button } from 'antd';
import './ClearTokenConfirmModal.scss';
import { inject, observer } from 'mobx-react';
import UserStore from '../../stores/UserStore';
import { FormComponentProps } from 'antd/lib/form';

const FormItem = Form.Item;

interface ConfirmProps extends FormComponentProps {
  userStore?: UserStore;
  onOk: () => void;
  toggle: () => void;
}

interface ConfirmState {
  loading: boolean;
  error: string;
}

interface ClearTokenConfirmModalProps {
  onOk: () => void;
  // tslint:disable-next-line
  trigger?: React.ReactElement<any>;
}

interface ClearTokenConfirmModalState {
  showModal: boolean;
}

@inject('userStore')
@observer
class Confirm extends React.Component<ConfirmProps, ConfirmState> {
  constructor(props: ConfirmProps) {
    super(props);
    this.state = {
      loading: false,
      error: ''
    };
  }

  handleOk = () => {
    this.setState({
      loading: true
    });
    const userStore = this.props.userStore!;
    this.props.form.validateFields(async (err, values) => {
      if (!err) {
        try {
          // tslint:disable-next-line
          await userStore.checkPassword(values['password']);
          await this.props.onOk();
          this.props.toggle();
          this.setState({
            loading: false
          });
        } catch (e) {
          this.setState({
            loading: false,
            error: e.response.data.message
          });
        }
      } else {
        this.setState({
          loading: false
        });
      }
    });
  };

  render() {
    const { getFieldDecorator } = this.props.form;
    return (
      <div className="confirm-form">
        <FormItem>
          {getFieldDecorator('password', {
            rules: [
              {
                required: true,
                message: '请输入账户密码'
              }
            ]
          })(<Input placeholder="请输入账户密码" style={{ width: '436px' }} type="password" />)}
        </FormItem>
        <FormItem>
          <Button type="primary" onClick={this.handleOk} style={{ padding: '0 200px' }}>
            确定
          </Button>
        </FormItem>
        {this.state.error}
      </div>
    );
  }
}

const ConfirmForm = Form.create()(Confirm);

export default class ClearTokenConfirmModal extends Component<
  ClearTokenConfirmModalProps,
  ClearTokenConfirmModalState
> {
  constructor(props: ClearTokenConfirmModalProps) {
    super(props);
    this.state = {
      showModal: false
    };
  }

  toggleShowModal = () => {
    this.setState({
      showModal: !this.state.showModal
    });
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

  handleOk = () => {
    this.props.onOk();
  };

  render() {
    const { showModal } = this.state;

    return (
      <div className="confirm-modal">
        {this.renderTrigger()}

        <Modal
          visible={showModal}
          title="密码校验"
          closable={false}
          maskClosable={false}
          width={540}
          onOk={this.toggleShowModal}
          onCancel={this.toggleShowModal}
          footer={null}
          destroyOnClose={true}
        >
          <span className="close" onClick={this.toggleShowModal}>
            <b>×</b>
          </span>
          <div className="container">
            <p>清除当前token之后，对应接口获取到的token将失效！</p>
            <p>请确认是否进行清除操作</p>
            <ConfirmForm onOk={this.props.onOk} toggle={this.toggleShowModal} />
          </div>
        </Modal>
      </div>
    );
  }
}
