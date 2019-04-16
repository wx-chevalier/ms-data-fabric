import React, { Component } from 'react';
import { ColumnProps } from 'antd/lib/table';
import { Col, Button, Input, Table } from 'antd';
import './EnvEditableKVTable.scss';

export interface KeyType {
  envKey: string;
  envValue: string;
}

interface Props {
  value?: KeyType[];
  onChange?: (value: KeyType[]) => void;
  readOnly?: boolean;
}

interface State {
  list: KeyType[];
}

export default class EnvKvEditableTable extends Component<Props, State> {
  static defaultProps: Partial<Props> = {
    onChange: () => {
      //
    }
  };

  state = {
    list: [] as KeyType[]
  };

  static getDerivedStateFromProps(props: Props) {
    if ('value' in props) {
      return {
        list: props.value!
      };
    } else {
      return null;
    }
  }

  handleInput = (i: number, k: 'envKey' | 'envValue', v: string) => {
    const { list } = this.state;
    list[i][k] = v;
    // 自动添加下一行
    // if (list[list.length-1].key) {
    //   list.push({key: '', description: ''});
    // }
    const newList = list.slice();
    this.setState({
      list: newList
    });
    this.props.onChange!(newList);
  };

  handleDelete = (i: number) => {
    const { list } = this.state;
    list.splice(i, 1);
    const newList = list.slice();
    this.setState({
      list: newList
    });
    this.props.onChange!(newList);
  };

  handleAdd = () => {
    const { list } = this.state;
    list.push({ envKey: '', envValue: '' });
    const newList = list.slice();
    this.setState({
      list: newList
    });
    this.props.onChange!(newList);
  };

  render() {
    const { list } = this.state;
    if (this.props.readOnly) {
      const cols: ColumnProps<KeyType>[] = [
        {
          key: 'envKey',
          title: '变量名',
          dataIndex: 'envKey'
        },
        {
          key: 'envValue',
          title: '变量值',
          dataIndex: 'envValue'
        }
      ];
      return <Table className="key-editor" columns={cols} dataSource={list} />;
    }
    return (
      <div className="key-editor">
        <div className="&-header">
          <Col span={8} className="&-l">
            变量名
          </Col>
          <Col span={14} className="&-m">
            变量值
          </Col>
          <Col span={2} className="&-r">
            操作
          </Col>
        </div>
        <ul className="&-list">
          {list.map((k, i) => (
            <li key={`item-${i}`}>
              <div className="key-editor-row">
                <Col span={8} className="&-l">
                  <Input
                    value={k.envKey}
                    onChange={e => this.handleInput(i, 'envKey', e.target.value)}
                  />
                </Col>
                <Col span={14} className="&-m">
                  <Input
                    value={k.envValue}
                    onChange={e => this.handleInput(i, 'envValue', e.target.value)}
                  />
                </Col>
                <Col span={2} className="&-r">
                  <div className="action" onClick={() => this.handleDelete(i)}>
                    删除
                  </div>
                </Col>
              </div>
            </li>
          ))}
        </ul>
        <Button className="&-add" style={{ width: '100%' }} type="dashed" onClick={this.handleAdd}>
          添加
        </Button>
      </div>
    );
  }
}
