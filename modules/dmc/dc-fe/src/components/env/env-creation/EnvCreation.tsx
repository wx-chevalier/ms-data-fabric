import './EnvCreation.scss';

import React, { ChangeEvent } from 'react';
import { Input, Button, message } from 'antd';
import EnvVarEditor, { KeyVal } from '../env-var-editor/EnvVarEditor';
import { inject } from 'mobx-react';
import EnvStore from '../../../stores/EnvStore';
import { RouteComponentProps } from 'react-router';

export interface EnvCreationProps extends RouteComponentProps<{ pid: string }> {
  envStore?: EnvStore;
}

interface EnvCreationState {
  loading: boolean;
  name: string;
  description: string;
  vars: KeyVal[];
}

@inject('envStore')
export default class EnvCreation extends React.Component<EnvCreationProps, EnvCreationState> {
  constructor(props: EnvCreationProps) {
    super(props);
    this.state = { loading: false, vars: [], name: '', description: '' };
  }

  onSubmit = async () => {
    this.setState({
      loading: true
    });
    if (this.state.name === '') {
      message.warn('环境名未填写');
      this.setState({
        loading: false
      });
    } else {
      try {
        let env = await this.props.envStore!.create(
          this.props.match.params.pid,
          this.state.name,
          this.state.vars.map(v => ({ envKey: v.key, envValue: v.val })),
          this.state.description
        );
        this.setState({
          loading: false
        });
        let pid = this.props.match.params.pid;
        this.props.history.push(`/project/${pid}/envmanagement/${env.id}`);
      } catch (err) {
        message.error(err.message);
        this.setState({
          loading: false
        });
      }
    }
  };

  onCancel = () => {
    this.props.history.goBack();
  };

  change(key: string, kv: [string, string]) {
    let vars = this.state.vars.slice();
    for (let v of vars) {
      if (v.key === key) {
        v.key = kv[0];
        v.val = kv[1];
      }
    }
    this.setState({ vars });
  }

  deleteVar(key: string) {
    this.setState({ vars: this.state.vars.slice().filter(v => v.key !== key) });
  }

  add(key: string, val: string) {
    this.setState({ vars: [...this.state.vars, { key, val }] });
  }

  exists(key: string): boolean {
    for (let v of this.state.vars) {
      if (v.key === key) {
        return true;
      }
    }
    return false;
  }

  render() {
    return (
      <div className="env-creation">
        <header className="&-info">
          <Input
            className="&-name"
            size="small"
            placeholder="请在此输入环境名称（必填）"
            value={this.state.name}
            onChange={(e: ChangeEvent<{ value: string }>) =>
              this.setState({ name: e.target.value })
            }
          />
          <Input.TextArea
            className="&-desc"
            placeholder="请在此输入环境描述"
            autosize={{ minRows: 2, maxRows: 6 }}
            onChange={(e: ChangeEvent<{ value: string }>) =>
              this.setState({ description: e.target.value })
            }
            value={this.state.description}
          />
        </header>
        <div className="&-vars">
          <div className="&-header">
            <div className="&-title">环境变量</div>
            <Button className="&-btn-new-env" type="primary">
              新增变量
            </Button>
          </div>
          <div className="&-edit">
            <EnvVarEditor
              vars={this.state.vars}
              change={(k, kv) => this.change(k, kv)}
              deleteVar={k => this.deleteVar(k)}
              add={(k, v) => this.add(k, v)}
              exists={k => this.exists(k)}
            />
          </div>
        </div>
        <div className="&-control">
          <Button className="&-btn-cancel" onClick={this.onCancel}>
            取消
          </Button>
          <Button
            type="primary"
            className="&-btn-submit"
            loading={this.state.loading}
            onClick={this.onSubmit}
          >
            提交
          </Button>
        </div>
      </div>
    );
  }
}
