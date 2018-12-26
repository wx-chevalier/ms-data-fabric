import React, { Component } from 'react';
import './ApiCreator.scss';
import { inject, observer } from 'mobx-react';
import { RouteComponentProps } from 'react-router-dom';
import ApiEditor, { ApiDefinition } from '../api-editor/ApiEditor';
import ProjectStore from '../../stores/ProjectStore';
import ApiStore from '../../stores/ApiStore';
import { Spin, message } from 'antd';
import ApiModel from '../../models/ApiModel';

interface ApiCreatorProps extends RouteComponentProps<{ pid: string; aid?: string }> {
  projectStore?: ProjectStore;
  apiStore?: ApiStore;
}

interface ApiCreatorState {
  loading: boolean;
}

@inject('projectStore', 'apiStore')
@observer
export default class ApiCreator extends Component<ApiCreatorProps, ApiCreatorState> {
  isEditing: boolean;
  constructor(props: ApiCreatorProps) {
    super(props);
    this.state = {
      loading: false
    };
  }

  async componentWillMount() {
    const { projectStore, match, apiStore, location } = this.props;
    this.isEditing = location.pathname.includes('editor-update');
    await projectStore!.selectItem(match.params.pid);
    if (match.params.aid) {
      await apiStore!.loadApiById(match.params.aid);
    }
  }

  handleSubmit = async (data: ApiDefinition) => {
    this.setState({
      loading: true
    });
    const pid = this.props.match.params.pid;
    data.headers = data.headers.filter(v => v.name);
    data.queries = data.queries.filter(v => v.name);
    let executor: () => Promise<ApiModel>;
    // 编辑
    if (this.isEditing) {
      executor = async () => {
        const apiStore = this.props.apiStore!;
        const request = await apiStore.update(apiStore.selectedApi!.id, data);
        const api = apiStore.selectedApi!;
        if (!api.apiDoc) {
          if (apiStore.editingDocContent !== '') {
            await apiStore.createApiDoc(request.id, apiStore.editingDocContent);
          }
        } else {
          await apiStore.updateApiDoc(api.id, api.apiDoc!.id, apiStore.editorContent);
        }
        this.setState({
          loading: false
        });
        return request;
      };
    } else {
      // 新建
      const fid = this.props.location.search.split('=')[1];
      executor = async () => {
        const apiStore = this.props.apiStore!;
        const request = await apiStore.create(pid, fid === 'ungroup' ? '' : fid, data);
        if (apiStore.editingDocContent !== '') {
          // 创建文档
          await apiStore.createApiDoc(request.id, apiStore.editingDocContent);
        }
        this.setState({
          loading: false
        });
        return request;
      };
    }
    try {
      await executor();
      this.props.history.push(`/project/${pid}/api/${this.props.apiStore!.selectedApi!.id}`);
    } catch (error) {
      message.error(error.message);
      this.setState({
        loading: false
      });
    }
  };

  handleCancel = () => {
    this.props.history.goBack();
  };

  render() {
    const project = this.props.projectStore!.selectedItem;
    const apiStore = this.props.apiStore!;
    const api = apiStore.selectedApi;
    const loading = this.state.loading;

    let initialValue;
    if (this.isEditing) {
      if (!api) {
        return <Spin className="loading" />;
      }
      initialValue = api;
    }
    return (
      <div className="api-creator">
        <header className="&-name">
          <h1>
            项目名称：
            {project ? project.name : ''}
          </h1>
        </header>
        <div className="&-editor-wrapper">
          <ApiEditor
            setEditingContent={(content: string) => {
              apiStore.editingDocContent = content;
            }}
            initialValue={initialValue}
            onOk={this.handleSubmit}
            onCancel={this.handleCancel}
            loading={loading}
          />
        </div>
      </div>
    );
  }
}
