import React, { Component } from 'react';
import { Select, Input } from 'antd';
import './ConditionEditor.scss';
import { positionStyle } from '../../factory';
import { BasicCondition, CompoundCondition } from '../../interface';
const { Option } = Select;
const okIcon = require('../../assets/ok.svg');
const cancelIcon = require('../../assets/delete.svg');

interface ConditionEditorProps {
  operators: string[];
  rightOptions?: (BasicCondition | CompoundCondition | null)[];
  onOk: (o: string, r: string | number) => void;
  onCancel: (o: string, r: string) => void;
}

interface ConditionEditorState {
  operator: string;
  rightValue: string;
}

class ConditionEditor extends Component<ConditionEditorProps, ConditionEditorState> {
  state = {
    operator: '',
    rightValue: ''
  };

  bindingSelectState = (key: keyof ConditionEditorState) => {
    return {
      value: this.state[key],
      // tslint:disable-next-line
      onChange: (v: string) => this.setState({ [key as any]: v })
    };
  };

  hadnleOk = () => {
    const { onOk } = this.props;
    const { rightValue, operator } = this.state;
    if (!rightValue) {
      return;
    } else {
      onOk(operator, rightValue);
    }
  };

  render() {
    const { operators, rightOptions, onCancel } = this.props;
    const { operator, rightValue } = this.state;
    return (
      <div className="condition-editor">
        <Select className="&-operator" {...this.bindingSelectState('operator')} size="small">
          {operators.map(o => (
            <Option value={o} key={o} title={o}>
              {o}
            </Option>
          ))}
        </Select>
        {Array.isArray(rightOptions) ? (
          <Select
            className="condition-editor-right-value"
            {...this.bindingSelectState('rightValue')}
            size="small"
          >
            {rightOptions.map(
              (v, i) =>
                v ? (
                  <Option value={String(i)} key={i} title={v.text!}>
                    {v.text!}
                  </Option>
                ) : null
            )}
          </Select>
        ) : (
          <Input
            className="condition-editor-right-value"
            size="small"
            value={this.state.rightValue}
            onChange={e => this.setState({ rightValue: e.target.value })}
          />
        )}
        <div className="&-buttons">
          <div
            className="&-icon"
            style={{ backgroundImage: `url('${okIcon}')` }}
            onClick={this.hadnleOk}
          />
          <div
            className="&-icon"
            style={{ backgroundImage: `url('${cancelIcon}')` }}
            onClick={() => onCancel(operator, rightValue)}
          />
        </div>
      </div>
    );
  }
}

export default positionStyle<ConditionEditorProps>(ConditionEditor);
