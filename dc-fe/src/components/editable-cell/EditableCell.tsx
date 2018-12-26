import React, { Component } from 'react';
import { Input, Icon } from 'antd';
import './EditableCell.scss';

interface EditableCellProps {
  value: string;
  onChange: (v: string) => void;
}

interface EditableCellState {
  editable: boolean;
  value: string;
}

export default class EditableCell extends Component<EditableCellProps, EditableCellState> {
  state = {
    value: this.props.value,
    editable: false
  };

  handleChange: React.ChangeEventHandler<HTMLInputElement> = e => {
    const value = e.target.value;
    this.setState({ value });
  };

  check = () => {
    this.setState({ editable: false });
    if (this.props.onChange) {
      this.props.onChange(this.state.value);
    }
  };

  edit = () => {
    this.setState({ editable: true });
  };

  render() {
    const { value, editable } = this.state;
    return (
      <div className="editable-cell">
        {editable ? (
          <Input
            value={value}
            onChange={this.handleChange}
            onPressEnter={this.check}
            suffix={<Icon type="check" className="editable-cell-icon-check" onClick={this.check} />}
          />
        ) : (
          <div style={{ paddingRight: 24 }}>
            {value || ' '}
            <Icon type="edit" className="editable-cell-icon" onClick={this.edit} />
          </div>
        )}
      </div>
    );
  }
}
