import React from 'react';
import { Modal, Button } from 'antd';

interface DeleteButtonProps {
  onClick: () => void;
}

const DeleteButton = (props: DeleteButtonProps) => {
  const confirm = () =>
    Modal.confirm({
      title: '确认删除',
      content: '删除后无法恢复，请谨慎操作',
      onOk: () => {
        props.onClick();
      }
    });
  return (
    <Button type="danger" onClick={confirm}>
      删除
    </Button>
  );
};

export default DeleteButton;
