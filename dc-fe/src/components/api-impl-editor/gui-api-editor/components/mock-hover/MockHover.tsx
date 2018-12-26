import React, { Component } from 'react';
import { Tooltip } from 'antd';
import './MockHover.scss';
import { positionStyle } from '../../factory';

interface MockHoverProps {
  text?: string;
}

interface MockHoverState {
  visible: boolean;
}

class MockHover extends Component<MockHoverProps, MockHoverState> {
  state = {
    visible: true
  };

  timer: NodeJS.Timer;

  static getDerivedStateFromProps() {
    return {
      visible: true
    };
  }

  componentDidUpdate() {
    clearTimeout(this.timer);
    this.timer = setTimeout(() => {
      this.setState({
        visible: false
      });
    }, 2000);
  }

  componentDidMount() {
    // this.div.current!.click();
    this.timer = setTimeout(() => {
      this.setState({
        visible: false
      });
    }, 2000);
  }

  componentWillUnmount() {
    clearTimeout(this.timer);
  }

  render() {
    return (
      <Tooltip title={this.props.text || ''} trigger="click" visible={this.state.visible}>
        <div className="mock-hover" />
      </Tooltip>
    );
  }
}

export default positionStyle<MockHoverProps>(MockHover);
