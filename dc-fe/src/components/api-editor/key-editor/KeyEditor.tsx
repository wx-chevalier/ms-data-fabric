import React, { Component } from 'react';
import { ColumnProps } from 'antd/lib/table';
import { Col, Button, Input, Table } from 'antd';
import './KeyEditor.scss';

export interface KeyType {
  name: string;
  description?: string;
}

interface KeyEditorProps {
  value?: KeyType[];
  onChange?: (value: KeyType[]) => void;
  readOnly?: boolean;
}

interface KeyEditorState {
  list: KeyType[];
}

export default class KeyEditor extends Component<KeyEditorProps, KeyEditorState> {
  static defaultProps: Partial<KeyEditorProps> = {
    onChange: () => {
      //
    }
  };

  state = {
    list: [] as KeyType[]
  };

  static getDerivedStateFromProps(props: KeyEditorProps) {
    if ('value' in props) {
      return {
        list: props.value!
      };
    } else {
      return null;
    }
  }

  handleInput = (i: number, k: 'name' | 'description', v: string) => {
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
    list.push({ name: '', description: '' });
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
          key: 'key',
          title: '名称',
          dataIndex: 'name'
        },
        {
          key: 'desc',
          title: '描述',
          dataIndex: 'description'
        }
      ];
      return <Table className="key-editor" columns={cols} dataSource={list} />;
    }
    return (
      <div className="key-editor">
        <div className="&-header">
          <Col span={8} className="&-l">
            名称
          </Col>
          <Col span={14} className="&-m">
            描述
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
                    value={k.name}
                    onChange={e => this.handleInput(i, 'name', e.target.value)}
                  />
                </Col>
                <Col span={14} className="&-m">
                  <Input
                    value={k.description}
                    onChange={e => this.handleInput(i, 'description', e.target.value)}
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
          + 添加条目
        </Button>
      </div>
    );
  }
}
