import React, { Component } from 'react';
import { Radio } from 'antd';
import { observer, inject } from 'mobx-react';
import { ProjectStore, APIImplEditorStore } from '../../stores';
import { RouteComponentProps } from 'react-router-dom';
import { SQLAPIEditor } from './sql-api-editor';
import SqlGuiEditor from './gui-api-editor/SqlGuiEditor';
import ProjectModel from '../../models/ProjectModel';
import { APIDevTool } from './api-dev-tool';

import './APIImplEditor.scss';

/**
 * pid: 项目ID
 */
interface APIImplEditorProps extends RouteComponentProps<{ pid: string }> {
  projectStore: ProjectStore;
  apiImplEditorStore: APIImplEditorStore;
}

interface APIImplEditorState {
  devToolHeight: number;
}

@inject('projectStore', 'apiImplEditorStore')
@observer
export default class APIImplEditor extends Component<APIImplEditorProps, APIImplEditorState> {
  constructor(props: APIImplEditorProps) {
    super(props);
    this.props.projectStore!.selectItem(this.props.match.params.pid);
    this.state = {
      devToolHeight: 0
    };
  }

  onDevToolHeightChange = (height: number) => {
    this.setState({ devToolHeight: height });
  };

  render() {
    const projectStore = this.props.projectStore!;
    const project: ProjectModel | null = projectStore.selectedItem;
    const devToolHeight = this.state.devToolHeight;

    return (
      <div className="api-impl-editor">
        <header className="&-head">
          <h1>
            项目名称：
            {project && project.name}
          </h1>
        </header>
        <div className="&-body">
          <div className="&-mode-switcher">
            <Radio.Group
              size="small"
              value={this.props.apiImplEditorStore!.implMode}
              onChange={this.props.apiImplEditorStore!.toggleImplMode}
            >
              <Radio.Button value="SQL">SQL模式</Radio.Button>
              <Radio.Button value="Graph">图形模式</Radio.Button>
            </Radio.Group>
          </div>
          <div className="&-editor" style={{ marginBottom: `${devToolHeight}px` }}>
            {this.props.apiImplEditorStore.implMode === 'SQL' ? <SQLAPIEditor /> : <SqlGuiEditor />}
          </div>
          <div className="&-dev-tool">
            <APIDevTool height={devToolHeight} onHeightChange={this.onDevToolHeightChange} />
          </div>
        </div>
      </div>
    );
  }
}
