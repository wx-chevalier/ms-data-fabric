import React, { Component } from 'react';
import './AliasColumn.scss';
import { positionStyle } from '../../factory';
const okIcon = require('../../assets/ok.svg');
const cancelIcon = require('../../assets/delete.svg');

interface AliasColumnProps {
  onOk: (value: string) => void;
  onCancel: (value: string) => void;
}

interface AliasColumnState {
  value: string;
}

class AliasColumn extends Component<AliasColumnProps, AliasColumnState> {
  state = {
    value: ''
  };

  render() {
    const { onOk, onCancel } = this.props;
    const { value } = this.state;
    return (
      <div className="alias-column">
        <input
          type="text"
          className="&-input"
          onChange={e => this.setState({ value: e.target.value })}
          value={value}
        />
        <div
          className="&-icon"
          style={{ backgroundImage: `url('${okIcon}')` }}
          onClick={() => onOk(value)}
        />
        <div
          className="&-icon"
          style={{ backgroundImage: `url('${cancelIcon}')` }}
          onClick={() => onCancel(value)}
        />
      </div>
    );
  }
}
export default positionStyle<AliasColumnProps>(AliasColumn);
