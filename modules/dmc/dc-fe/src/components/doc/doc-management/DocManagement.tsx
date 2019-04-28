import './DocManagement.scss';

import React from 'react';
import ProjectStore from '../../../stores/ProjectStore';
import DocStore from '../../../stores/DocStore';
import { Link, RouteComponentProps } from 'react-router-dom';
import { inject, observer } from 'mobx-react';
import DocTable from '../doc-table/DocTable';
import { Divider, Button, Input, Popconfirm, Popover, message } from 'antd';
import ProjectModel from '../../../models/ProjectModel';
import { appendPath } from '../../../utils';

const Search = Input.Search;

interface DocManagementProps extends RouteComponentProps<{ pid: string }> {
  projectStore?: ProjectStore;
  docStore?: DocStore;
}

interface DocManagementState {
  loading: boolean;
  // 存储checkbox选中的文档id（数组）
  selectedDocId: string[];
}

/**
 * 项目文档管理
 */
@inject('projectStore', 'docStore')
@observer
export class DocManagement extends React.Component<DocManagementProps, DocManagementState> {
  constructor(props: DocManagementProps) {
    super(props);
    this.state = {
      loading: false,
      selectedDocId: []
    };
  }

  async componentWillMount() {
    const docStore = this.props.docStore!;
    const pid = this.props.match.params.pid;
    await this.props.projectStore!.selectItem(pid);

    this.setState({
      loading: true
    });
    // 根据pid获取对应的文档列表
    await docStore.loadDocListByPid(pid);
    this.setState({
      loading: false
    });
  }

  render() {
    const projectStore = this.props.projectStore!;
    const docStore = this.props.docStore!;
    const { pathname } = this.props.location;
    const pid = this.props.match.params.pid;
    const project: ProjectModel | null = projectStore.selectedItem;
    const loading = this.state.loading;

    if (!project) {
      return null;
    }

    return (
      <div className="doc-management">
        <header>
          <h1>
            项目名称：
            {project.name}
          </h1>
        </header>
        <div className="&-workspace">
          <div className="&-header">
            <span className="&-button-group">
              <Button
                type="primary"
                className="create-doc"
                onClick={() => {
                  docStore.toggleEditorStatus('create');
                  docStore.selectDoc();
                }}
                size="large"
              >
                <Link to={appendPath(pathname, 'edit')}>新建文档</Link>
              </Button>
              <Popconfirm
                title="确认删除？"
                placement="right"
                onConfirm={async () => {
                  this.setState({
                    loading: true
                  });
                  if (this.state.selectedDocId.length !== 0) {
                    try {
                      await docStore.delete(this.state.selectedDocId);
                      await docStore.loadDocListByPid(pid);
                      this.setState({
                        loading: false
                      });
                    } catch (err) {
                      message.error(err.message);
                      this.setState({
                        loading: false
                      });
                    }
                  }
                }}
              >
                <Button
                  className="batch-delete"
                  size="large"
                  disabled={this.state.selectedDocId.length === 0}
                >
                  批量删除
                </Button>
              </Popconfirm>
            </span>
            <Search
              placeholder="输入关键词"
              onSearch={value => docStore.changeSearchText(value)}
              style={{ width: '30%' }}
            />
          </div>
          <DocTable
            dataSource={docStore.filteredData.slice()}
            docName={[
              {
                key: 'docName',
                title: '名称',
                dataIndex: 'docName',
                width: 200,
                render: (text, record) => {
                  return (
                    <div>
                      <Popover content={record.title}>
                        <Link
                          to={appendPath(pathname, record.id)}
                          onClick={() => {
                            docStore.selectDoc(record);
                          }}
                        >
                          {record.title}
                        </Link>
                      </Popover>
                    </div>
                  );
                }
              }
            ]}
            action={[
              {
                key: 'action',
                title: '操作',
                render: (text, record) => (
                  <span>
                    <Link
                      onClick={() => {
                        docStore.selectDoc(record);
                        docStore.toggleEditorStatus('update');
                      }}
                      to={appendPath(pathname, 'edit')}
                    >
                      编辑
                    </Link>
                    <Divider type="vertical" />
                    <Popconfirm
                      title="确认删除"
                      onConfirm={async () => {
                        this.setState({
                          loading: true
                        });
                        try {
                          await docStore.delete([record.id]);
                          await docStore.loadDocListByPid(pid);
                          this.setState({
                            loading: false
                          });
                        } catch (err) {
                          message.error(err.message);
                          this.setState({
                            loading: false
                          });
                        }
                      }}
                    >
                      <a href="javascript:;" style={{ color: '#E18C8B' }}>
                        删除
                      </a>
                    </Popconfirm>
                  </span>
                )
              }
            ]}
            rowSelection={{
              type: 'checkbox',
              onChange: (v: string[]) =>
                this.setState({
                  selectedDocId: v
                })
            }}
            loading={loading}
          />
        </div>
      </div>
    );
  }
}

export default DocManagement;
