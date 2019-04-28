import React from 'react';
import './ActionMask.scss';
import { observer } from 'mobx-react';
import InstructController from '../../InstructController';

const ActionMask: React.StatelessComponent<{ controller: InstructController }> = props => {
  const { actionConponents, showModal } = props.controller;
  return (
    <div className="action-mask" style={{ height: showModal ? '100%' : 0 }}>
      {actionConponents}
    </div>
  );
};

export default observer(ActionMask);
