import './EnvDetail.scss';

import React, { ChangeEvent } from 'react';
import { Button, Input, Spin, message } from 'antd';
import EnvVarEditor, { KeyVal } from '../env-var-editor/EnvVarEditor';
import { inject } from 'mobx-react';
import EnvStore from '../../../stores/EnvStore';
import { RouteComponentProps } from 'react-router';

export interface EnvDetailProps extends RouteComponentProps<{ pid: string; envId: string }> {
  envStore?: EnvStore;
}

interface EnvDetailState {
  loading: boolean;
  editing: boolean;
  name: string;
  description: string;
  vars: KeyVal[];
}

@inject('envStore')
export default class EnvDetail extends React.Component<EnvDetailProps, EnvDetailState> {
  constructor(props: EnvDetailProps) {
    super(props);
    this.state = { loading: false, vars: [], name: '', editing: false, description: '' };
  }

  async componentDidMount() {
    this.setState({
      loading: true
    });
    await this.props.envStore!.loadEnvListByPid(this.props.match.params.pid);
    let env = this.props.envStore!.editEnvById(this.props.match.params.envId);

    if (env) {
      this.setState({
        vars: env.envValue.slice().map(e => ({ key: e.envKey, val: e.envValue })),
        name: env.envName,
        description: env.envDescription
      });
    }
    this.setState({
      loading: false
    });
  }

  async toggleEditing() {
    if (this.state.editing) {
      this.setState({
        loading: true
      });
      const { pid, envId } = this.props.match.params;
      try {
        await this.props.envStore!.update(
          envId,
          pid,
          this.state.name,
          undefined,
          this.state.description
        );
        this.setState({ loading: false, editing: false });
      } catch (err) {
        message.error(err.message);
        this.setState({
          loading: false
        });
      }
    } else {
      this.setState({ editing: true });
    }
  }

  updateVars() {
    const { envId, pid } = this.props.match.params;
    this.props.envStore!.update(
      envId,
      pid,
      undefined,
      this.state.vars.map(v => ({ envKey: v.key, envValue: v.val }))
    );
  }

  change(key: string, kv: [string, string]) {
    let vars = this.state.vars.slice();
    for (let v of vars) {
      if (v.key === key) {
        v.key = kv[0];
        v.val = kv[1];
      }
    }
    this.setState({ vars }, () => this.updateVars());
  }

  deleteVar(key: string) {
    this.setState({ vars: this.state.vars.slice().filter(v => v.key !== key) }, () =>
      this.updateVars()
    );
  }

  add(key: string, val: string) {
    this.setState({ vars: [...this.state.vars, { key, val }] }, () => this.updateVars());
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
    const loading = this.state.loading;

    return (
      <div className="env-detail">
        {loading ? (
          <div className="spin-container">
            <Spin size="large" />
          </div>
        ) : (
          <div className="content-container">
            <header className="&-info">
              <div className="&-name">
                <span className="&-val">
                  {this.state.editing ? (
                    <Input
                      onChange={(e: ChangeEvent<{ value: string }>) =>
                        this.setState({ ...this.state, name: e.target.value })
                      }
                      value={this.state.name}
                    />
                  ) : (
                    this.state.name
                  )}
                </span>
                <span className="&-edit-btn" onClick={() => this.toggleEditing()}>
                  {this.state.editing ? '完成' : '编辑'}
                </span>
              </div>
              <span className="&-desc">
                环境描述：
                {this.state.editing ? (
                  <Input.TextArea
                    placeholder="请在此输入环境描述"
                    autosize={{ minRows: 2, maxRows: 6 }}
                    value={this.state.description}
                    onChange={(e: ChangeEvent<{ value: string }>) =>
                      this.setState({ description: e.target.value })
                    }
                  />
                ) : (
                  this.state.description
                )}
              </span>
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
          </div>
        )}
      </div>
    );
  }
}
