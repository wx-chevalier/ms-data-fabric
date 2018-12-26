import React, { Component } from 'react';
import { Chart, Axis, Tooltip, Geom } from 'bizcharts';
import moment from 'moment';
import { DatePicker, Select } from 'antd';
import ProjectModel from '../../../models/ProjectModel';

import './OverviewChart.scss';

interface OverviewChartProps {
  projects: ProjectModel[];
}

interface OverviewChartState {
  //
}

const { RangePicker } = DatePicker;
const Option = Select.Option;

const prefix = 'overview-chart';

function getRandomArbitrary(min: number, max: number) {
  return Math.floor(Math.random() * (max - min) + min);
}

const today = moment();

const data = Array.from({ length: 7 }).map((_, i) => {
  return {
    time: moment(today)
      .subtract(7 - i, 'day')
      .format('MM-DD'),
    today: getRandomArbitrary(10000, 50000)
  };
});

const scale = {
  today: { min: 0 },
  yesterday: { min: 0 },
  time: { range: [0, 1] }
};

export default class OverviewChart extends Component<OverviewChartProps, OverviewChartState> {
  render() {
    const { projects } = this.props;
    return (
      <section className={`${prefix}`}>
        <div className="top">
          <div className={`${prefix}-info`}>
            <div className="label">本周访问总量</div>
            <div className="value">
              <span className="number">4320</span>
              <span className="compare">
                <span>周同比</span>
                <span className="indicator" />
                <span>12%</span>
              </span>
            </div>
          </div>
          <div className={`${prefix}-toggle`}>
            <div className="date">
              <span>本周</span>
              <span>本月</span>
              <span>全年</span>
              <RangePicker
                style={{ width: 250 }}
                onChange={() => {
                  // TODO:
                }}
              />
            </div>
            <div className="project">
              <Select
                defaultValue={projects[0] ? projects[0].id : ''}
                style={{ width: 250 }}
                onChange={() => {
                  // TODO:
                }}
              >
                {projects.map(project => (
                  <Option key={project.id} value={project.id}>
                    {project.name}
                  </Option>
                ))}
              </Select>
            </div>
          </div>
        </div>
        <div className={`${prefix}-chart`}>
          <Chart
            height={250}
            padding={{
              top: 10,
              left: 50,
              right: 40,
              bottom: 50
            }}
            data={data}
            scale={scale}
            forceFit={true}
          >
            <Axis name="time" />
            <Axis name="today" />
            <Axis name="yesterday" visible={false} />
            <Tooltip crosshairs={{ type: 'y' }} />
            <Geom type="area" position="time*today" />
            <Geom type="line" position="time*today" size={2} />
          </Chart>
        </div>
      </section>
    );
  }
}
