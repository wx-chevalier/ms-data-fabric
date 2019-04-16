import React, { Component } from 'react';
import { Link } from 'react-router-dom';
import { Tabs, Button, Modal, Input, Card, message } from 'antd';
import { inject, observer } from 'mobx-react';

import chunk from 'lodash/chunk';

import ProjectForm from '../project-form/ProjectForm';
import ProjectStore from '../../stores/ProjectStore';
import ProjectModel from '../../models/ProjectModel';

import PVChart from './chart/PVChart';
import ProjectChart from './chart/ProjectChart';
import OverviewChart from './chart/OverviewChart';

import './ProjectOverviewWithCharts.scss';
import { FieldValues } from '../api-editor/api-info-form/ApiInfoForm';

const Search = Input.Search;
const TabPane = Tabs.TabPane;

interface ProjectOverviewWithChartsProps {
  projectStore?: ProjectStore;
}

interface ProjectOverviewWithChartsState {
  showProjectForm: boolean;
  keyWord: string;
}

const prefix = 'project-overview-with-charts';

@inject('projectStore')
@observer
export default class ProjectOverviewWithCharts extends Component<
  ProjectOverviewWithChartsProps,
  ProjectOverviewWithChartsState
> {
  state = {
    keyWord: '',
    showProjectForm: false
  };

  toggleForm = () => {
    this.setState({
      showProjectForm: !this.state.showProjectForm
    });
  };

  deleteProject = (p: ProjectModel) => {
    Modal.confirm({
      title: '确认删除',
      content: '确认要删除此项目吗？',
      onOk: async () => {
        try {
          await this.props.projectStore!.removeProjectById(p.id);
        } catch (err) {
          message.error(err.message);
        }
      }
    });
  };

  editProject = (p: ProjectModel) => {
    this.props.projectStore!.setEditingProject(p);
  };

  renderCards(group: ProjectModel[], key?: string | number) {
    let paddedGroups: (ProjectModel | null)[] = group;

    while (paddedGroups.length < 3) {
      paddedGroups.push(null);
    }

    return (
      <div className="row" key={key}>
        {group.map(
          (project, i) =>
            project ? (
              this.renderCard(project)
            ) : (
              <div
                key={`prj-nan-${i}`}
                style={{
                  flex: '400px 0 0',
                  visibility: 'hidden'
                }}
              />
            )
        )}
      </div>
    );
  }

  renderCard(project: ProjectModel) {
    return (
      <Card
        key={project.id}
        style={{ width: 380 }}
        className="project-card"
        actions={[
          <span key="delete">
            <a
              href="javascript:;"
              onClick={() => this.deleteProject(project)}
              style={{ color: '#E18C8B' }}
            >
              删除
            </a>
          </span>,
          <span key="edit">
            <a
              href="javascript:;"
              onClick={() => this.editProject(project)}
              style={{ color: '#13CBF7' }}
            >
              编辑
            </a>
          </span>
        ]}
      >
        <div className="name">
          <Link to={`/project/${project.id}/`}>{project.name}</Link>
        </div>
        <div className="info">
          <span>版本: {project.version}</span>
          <span>分类: {project.type}</span>
          <span>接口数: {project.apiNumber}</span>
        </div>
      </Card>
    );
  }

  render() {
    const projectStore = this.props.projectStore!;
    const { keyWord } = this.state;

    let projects = projectStore.projectList.slice();

    // 执行搜索操作
    if (!!keyWord) {
      projects = projects.filter(project => project.name.indexOf(keyWord) > -1);
    }

    let groupedProjects = chunk(projects, 3);

    return (
      <div className={prefix}>
        <div className={`${prefix}-charts ${prefix}-paper`}>
          <Tabs defaultActiveKey="1" tabBarGutter={32}>
            <TabPane tab="访问量" key="1">
              <PVChart />
            </TabPane>
            <TabPane tab="总览" key="2">
              <OverviewChart projects={projects} />
            </TabPane>
            <TabPane tab="项目" key="3">
              <ProjectChart />
            </TabPane>
          </Tabs>
        </div>
        <div className={`${prefix}-projects ${prefix}-paper`}>
          <div className="actions">
            <div className="left">
              <h2>项目总览</h2>
              <Button type="primary" onClick={this.toggleForm}>
                新增项目
              </Button>
              <Button disabled={true}>导入项目</Button>
            </div>
            <div className="right">
              <Search
                placeholder="输入关键词"
                style={{ width: 250 }}
                onSearch={value => {
                  this.setState({ keyWord: value });
                }}
              />
            </div>
          </div>
          <div className="cards">
            {groupedProjects.map((group, i) => this.renderCards(group, i))}
          </div>
        </div>
        <Modal
          visible={this.state.showProjectForm}
          footer={null}
          closable={false}
          title="新建项目"
          destroyOnClose={true}
        >
          <ProjectForm
            typeOptions={projectStore.projectTypeList}
            onCancel={this.toggleForm}
            onSave={async (values: FieldValues) => {
              await projectStore.createNewProject(values);
            }}
          />
        </Modal>
        <Modal visible={!!projectStore.editingProject} footer={null} closable={false}>
          <ProjectForm
            typeOptions={projectStore.projectTypeList}
            initialValues={projectStore.editingProjectInfo}
            onCancel={projectStore.clearEditingProject}
            onSave={async (values: FieldValues) => {
              await projectStore.updateEditingProject(values);
            }}
          />
        </Modal>
      </div>
    );
  }
}
