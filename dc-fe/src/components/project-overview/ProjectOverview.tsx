import React, { Component } from 'react';
import ProjectTable, { TableColumns } from '../project-table/ProjectTable';
import { Divider, Button, Modal } from 'antd';
import ProjectForm from '../project-form/ProjectForm';
import { inject, observer } from 'mobx-react';
import ProjectStore from '../../stores/ProjectStore';
import ProjectModel from '../../models/ProjectModel';

import './ProjectOverview.scss';
import { FieldValues } from '../folder-form/FolderForm';

interface ProjectOverviewProps {
  projectStore?: ProjectStore;
}

interface ProjectOverviewState {
  showProjectForm: boolean;
}

@inject('projectStore')
@observer
export default class ProjectOverview extends Component<ProjectOverviewProps, ProjectOverviewState> {
  state = {
    showProjectForm: false
  };

  toggleForm = () => {
    this.setState({
      showProjectForm: !this.state.showProjectForm
    });
  };

  deleteProject = async (p: ProjectModel) => {
    Modal.confirm({
      title: '确认删除',
      content: '确认要删除此项目吗？',
      onOk: () => {
        this.props.projectStore!.removeProjectById(p.id);
      }
    });
  };

  editProject = (p: ProjectModel) => {
    this.props.projectStore!.setEditingProject(p);
  };

  render() {
    const projectStore = this.props.projectStore!;
    const action: TableColumns = [
      {
        key: 'actions',
        title: '操作',
        render: (text, record) => (
          <span>
            <a href="javascript:;" onClick={() => this.editProject(record)}>
              编辑
            </a>

            <Divider type="vertical" />
            <a
              href="javascript:;"
              onClick={() => this.deleteProject(record)}
              style={{ color: '#E18C8B' }}
            >
              删除
            </a>
          </span>
        )
      }
    ];
    return (
      <div className="project-overview">
        <div className="&-title">项目总览</div>
        <Divider />
        <div className="&-actions">
          <Button type="primary" onClick={this.toggleForm}>
            + 新增项目
          </Button>
          <Button disabled={true}>导入项目</Button>
        </div>
        <div className="&-table">
          <ProjectTable action={action} dataSource={projectStore.projectList.slice()} />
        </div>
        <Modal visible={this.state.showProjectForm} footer={null} closable={false} title="新建项目">
          <ProjectForm
            typeOptions={projectStore.projectTypeList}
            onCancel={this.toggleForm}
            onSave={async (values: FieldValues) => {
              await projectStore.createNewProject(values);
              this.toggleForm();
            }}
          />
        </Modal>
        <Modal visible={!!projectStore.editingProject} footer={null} closable={false}>
          <ProjectForm
            typeOptions={projectStore.projectTypeList}
            initialValues={projectStore.editingProjectInfo}
            onCancel={projectStore.clearEditingProject}
            onSave={(values: FieldValues) => {
              projectStore.updateEditingProject(values);
            }}
          />
        </Modal>
      </div>
    );
  }
}
