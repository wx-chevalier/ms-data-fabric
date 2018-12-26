import React, { Component } from 'react';
import './EuProjectOverview.scss';
import { Card } from 'antd';
import { inject, observer } from 'mobx-react';
import ProjectStore from '../../stores/ProjectStore';
import { Link } from 'react-router-dom';

interface EuProjectOverviewProps {
  projectStore?: ProjectStore;
}

interface EuProjectOverviewState {}

@inject('projectStore')
@observer
export default class EuProjectOverview extends Component<
  EuProjectOverviewProps,
  EuProjectOverviewState
> {
  render() {
    const projectStore = this.props.projectStore!;
    return (
      <div className="eu-project-overview">
        <h3>所有项目</h3>
        <div className="&-list">
          {projectStore.projectList.map(p => (
            <Card
              style={{ width: 300, marginBottom: 60, marginRight: 32 }}
              key={p.id}
              title={p.name}
              actions={[
                <Link key="1" to={`/project/${p.id}`}>
                  查看详情
                </Link>
              ]}
            >
              <p>{`项目类型：${p.type}`}</p>
              <p>{`版本号：${p.version}`}</p>
              <p>{`接口数: ${p.apiNumber}`}</p>
            </Card>
          ))}
        </div>
      </div>
    );
  }
}
