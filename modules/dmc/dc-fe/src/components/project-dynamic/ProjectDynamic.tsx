import './ProjectDynamic.scss';
import React from 'react';
import { RouteComponentProps } from 'react-router-dom';
import { Row, Col, Select, DatePicker } from 'antd';
import ProjectStore from '../../stores/ProjectStore';
import { inject, observer } from '../../../node_modules/mobx-react';
import moment from 'moment';
import DynamicTable from './dynamic-table/DynamicTable';
import { DynamicDetailModel } from '../../models/DynamicModel';

export const OPERATEOBJECTDICT = {
  SELF_API: '自创建接口',
  PROJECT: '项目',
  PROJECT_DOC: '项目文档',
  PROXY_API: '代理接口',
  API_DOC: '接口文档',
  MODEL: '模型',
  ENV: '环境',
  FOLDER: '目录'
};

export const ACTIONTYPEDICT = {
  ADD: '新增',
  DELETE: '删除',
  UPDATE: '更新'
};

const Option = Select.Option;
const RangePicker = DatePicker.RangePicker;

interface ProjectDynamicProps extends RouteComponentProps<{ pid: string }> {
  projectStore?: ProjectStore;
}

interface ProjectDynamicState {
  operateObject: string;
  actionType: string;
  startDate?: string;
  endDate?: string;
  loading: boolean;
  dynamicData?: DynamicDetailModel[];
  total?: number;
  current?: number;
}

@inject('projectStore')
@observer
export default class ProjectDynamic extends React.Component<
  ProjectDynamicProps,
  ProjectDynamicState
> {
  constructor(props: ProjectDynamicProps) {
    super(props);

    this.state = {
      operateObject: 'ALL',
      actionType: 'ALL',
      startDate: '',
      endDate: '',
      loading: false
    };
  }

  async componentDidMount() {
    const projectStore = this.props.projectStore!;
    const pid = this.props.match.params.pid;
    this.setState({
      loading: true
    });
    await projectStore.loadProjectDynamic(pid, 1, 10).then(v => {
      this.setState({
        dynamicData: v.projectDynamics,
        loading: false,
        total: v.total,
        current: v.pageNum
      });
    });
  }

  handleChange = (params: { total: number; current: number; pageSize: number }) => {
    const { operateObject, actionType, startDate, endDate } = this.state;
    const projectStore = this.props.projectStore!;
    const pid = this.props.match.params.pid;
    this.setState({
      loading: true
    });
    projectStore
      .loadProjectDynamic(
        pid,
        params.current,
        10,
        operateObject === 'ALL' ? undefined : operateObject,
        actionType === 'ALL' ? undefined : actionType,
        startDate === '' ? undefined : startDate,
        endDate === '' ? undefined : endDate
      )
      .then(data => {
        this.setState({
          dynamicData: data.projectDynamics,
          current: params.current,
          loading: false
        });
      });
  };

  handleFilterChange = () => {
    this.setState({
      loading: true
    });
    const { operateObject, actionType, startDate, endDate, current } = this.state;
    const projectStore = this.props.projectStore!;
    const pid = this.props.match.params.pid;
    projectStore
      .loadProjectDynamic(
        pid,
        current,
        10,
        operateObject === 'ALL' ? undefined : operateObject,
        actionType === 'ALL' ? undefined : actionType,
        startDate === '' ? undefined : startDate,
        endDate === '' ? undefined : endDate
      )
      .then(data => {
        this.setState({
          dynamicData: data.projectDynamics,
          current: 1,
          loading: false,
          total: data.total
        });
      });
  };

  render() {
    return (
      <div className="project-dynamic">
        <div className="&-action-group">
          <Row>
            <Col span={6}>
              <b className="operation-title">操作对象</b>
              <Select
                style={{ width: '60%' }}
                defaultValue="ALL"
                onChange={v => {
                  this.setState(
                    {
                      operateObject: v as string
                    },
                    this.handleFilterChange
                  );
                }}
              >
                <Option value="ALL">全部</Option>
                {Object.keys(OPERATEOBJECTDICT).map(k => (
                  <Option key={k} value={k}>
                    {OPERATEOBJECTDICT[k]}
                  </Option>
                ))}
              </Select>
            </Col>
            <Col span={6}>
              <b className="operation-title">操作类型</b>
              <Select
                style={{ width: '60%' }}
                defaultValue="ALL"
                onChange={v => {
                  this.setState(
                    {
                      actionType: v as string
                    },
                    this.handleFilterChange
                  );
                }}
              >
                <Option value="ALL">全部</Option>
                {Object.keys(ACTIONTYPEDICT).map(k => (
                  <Option key={k} value={k}>
                    {ACTIONTYPEDICT[k]}
                  </Option>
                ))}
              </Select>
            </Col>
            <Col span={12} style={{ display: 'flex', justifyContent: 'flex-end' }}>
              <RangePicker
                onChange={(v, vString) => {
                  const selectedEndDate = vString[1];
                  const realEndDate =
                    selectedEndDate !== ''
                      ? moment(selectedEndDate)
                          .add(1, 'd')
                          .format('YYYY-MM-DD')
                      : '';
                  this.setState(
                    {
                      startDate: vString[0],
                      endDate: realEndDate
                    },
                    this.handleFilterChange
                  );
                }}
              />
            </Col>
          </Row>
        </div>
        <div className="&-content">
          <DynamicTable
            dataSource={this.state.dynamicData}
            loading={this.state.loading}
            pagination={{
              total: this.state.total,
              current: this.state.current
            }}
            onChange={this.handleChange}
          />
        </div>
      </div>
    );
  }
}
