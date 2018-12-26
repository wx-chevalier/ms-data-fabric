import './EnvVarEditor.scss';

import React, { ChangeEvent } from 'react';
import { Table, Input, Popconfirm, message } from 'antd';

export interface KeyVal {
  key: string;
  val: string;
}

export interface EnvVarEditorProps {
  vars: Array<KeyVal>;
  change: (key: string, kv: [string, string]) => void; // key -> newKey
  deleteVar: (key: string) => void;
  add: (key: string, val: string) => void;
  exists: (key: string) => boolean;
}

interface EnvVarEditorState {
  editing: { [key: string]: [string, string] };
}

export default class EnvVarEditor extends React.Component<EnvVarEditorProps, EnvVarEditorState> {
  constructor(props: EnvVarEditorProps) {
    super(props);
    this.state = {
      editing: {
        '': ['', '']
      }
    };
  }

  render() {
    let vars: KeyVal[] = [...this.props.vars.slice()];
    vars.push({ key: '', val: '' });
    return (
      <div className="env-var-editor">
        <Table size="middle" columns={this.createColumns()} dataSource={vars} />
      </div>
    );
  }

  private toggleEditing(k: string) {
    let editing = this.state.editing;
    if (editing[k]) {
      if (k === '') {
        // 新建
        const [newK, newV] = editing[k];
        if (newK === '') {
          message.warn('变量名为空');
          return;
        }
        if (this.props.exists(newK)) {
          message.warn('变量名已经存在');
          return;
        }
        this.props.add(newK, newV);
        editing[''] = ['', ''];
      } else {
        this.props.change(k, editing[k]);
        delete editing[k];
      }
    } else {
      for (let kv of this.props.vars) {
        if (kv.key === k) {
          editing[k] = [kv.key, kv.val];
          break;
        }
      }
    }
    this.setState({ editing });
  }

  private changeKey(key: string, newKey: string) {
    const editing = { ...this.state.editing };
    editing[key] = [newKey, editing[key][1]];
    this.setState({ editing });
  }

  private changeVal(key: string, newVal: string) {
    const editing = { ...this.state.editing };
    editing[key] = [editing[key][0], newVal];
    this.setState({ editing });
  }

  private isEditing(k: string) {
    return !!this.state.editing[k];
  }

  private createColumns() {
    const { deleteVar } = this.props;
    const { editing } = this.state;

    return [
      {
        title: '变量名',
        dataIndex: 'name',
        key: 'name',
        render: (text: string, record: KeyVal) => (
          <span className="env-var-editor-var-name">
            {this.isEditing(record.key) ? (
              <Input
                placeholder="请在此输入变量名"
                onChange={(e: ChangeEvent<{ value: string }>) =>
                  this.changeKey(record.key, e.target.value)
                }
                value={editing[record.key][0]}
              />
            ) : (
              record.key
            )}
          </span>
        )
      },
      {
        title: '变量值',
        dataIndex: 'value',
        key: 'value',
        render: (text: string, record: KeyVal) => (
          <span className="env-var-editor-var-value">
            {this.isEditing(record.key) ? (
              <Input
                placeholder="请在此输入变量值"
                onChange={(e: ChangeEvent<{ value: string }>) =>
                  this.changeVal(record.key, e.target.value)
                }
                value={editing[record.key][1]}
              />
            ) : (
              record.val
            )}
          </span>
        )
      },
      {
        title: '操作',
        key: 'action',
        width: '200px',
        render: (text: string, record: KeyVal) => (
          <span className="env-var-editor-action">
            <span className="&-modify" onClick={() => this.toggleEditing(record.key)}>
              {this.isEditing(record.key) ? '完成' : '修改'}
            </span>
            {record.key !== '' && (
              <span className="env-var-editor-action-delete">
                <Popconfirm
                  title="确认删除"
                  okText="确认"
                  cancelText="取消"
                  onConfirm={() => deleteVar(record.key)}
                >
                  删除
                </Popconfirm>
              </span>
            )}
          </span>
        )
      }
    ];
  }
}
