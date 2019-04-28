import React, { Component } from 'react';
import './JoinCondition.scss';
import { Table, JoinCondition as IJoinCondition } from '../../interface';
import { Select, Button } from 'antd';
import { positionStyle } from '../../factory';
const { Option } = Select;
const deleteIcon = require('../../assets/delete.svg');

interface JoinConditionProps {
  conditions: IJoinCondition[];
  source: Table;
  target: Table;
  operators: string[];
  onOk: (newCondition: IJoinCondition) => void;
  onCancel: () => void;
  onDelete: (result: IJoinCondition[]) => void;
}

interface JoinConditionState {
  leftValue: string;
  operator: string;
  rightValue: string;
}

class JoinCondition extends Component<JoinConditionProps, JoinConditionState> {
  state = {
    leftValue: '',
    operator: '',
    rightValue: ''
  };

  listToOptions(list: string[]) {
    return list.map(v => (
      <Option value={v} key={v} title={v}>
        {v}
      </Option>
    ));
  }

  bindingSelectState = (key: keyof JoinConditionState) => {
    return {
      value: this.state[key],
      // tslint:disable-next-line
      onChange: (v: string) => this.setState({ [key as any]: v })
    };
  };

  handleOk = () => {
    const { leftValue, operator, rightValue } = this.state;
    const { target, source, onOk } = this.props;
    if (leftValue && operator && rightValue) {
      const left = source.columns.find(v => v.text === leftValue);
      const right = target.columns.find(v => v.text === rightValue);
      onOk({
        isCondition: true,
        type: 'join',
        leftTableId: source.id,
        left: left!,
        operator,
        rightTableId: target.id,
        right: right!
      });
    }
  };

  handleCacel = () => {
    this.props.onCancel();
  };

  hadnleDelete = (i: number) => {
    const copy = this.props.conditions.slice();
    copy.splice(i, 1);
    this.props.onDelete(copy);
  };

  render() {
    const { conditions, source, target, operators } = this.props;
    return (
      <div className="join-condition">
        <ul className="&-list">
          {conditions.map((c, i) => (
            <li className="join-condition-list-item">
              <span>
                {i + 1}、{`${c.left.text!} ${c.operator} ${c.right.text!}`}
              </span>
              <span
                className="&-icon"
                onClick={() => this.hadnleDelete(i)}
                style={{ backgroundImage: `url('${deleteIcon}')` }}
              />
            </li>
          ))}
        </ul>
        <div className="&-select">
          <Select className="&-left" {...this.bindingSelectState('leftValue')} size="small">
            {this.listToOptions(source.columns.map(c => c.text!))}
          </Select>
          <Select className="&-operator" {...this.bindingSelectState('operator')} size="small">
            {this.listToOptions(operators)}
          </Select>
          <Select className="&-right" {...this.bindingSelectState('rightValue')} size="small">
            {this.listToOptions(target.columns.map(c => c.text!))}
          </Select>
        </div>
        <div className="&-buttons">
          <Button onClick={this.handleCacel} size="small">
            取消
          </Button>
          <Button type="primary" onClick={this.handleOk} size="small">
            确定
          </Button>
        </div>
      </div>
    );
  }
}

export default positionStyle<JoinConditionProps>(JoinCondition);
