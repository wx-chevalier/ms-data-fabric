import React, { Component } from 'react';
import { Chart, Axis, Tooltip, Geom, Coord, Guide } from 'bizcharts';

import './ProjectChart.scss';

interface ProjectChartProps {}

interface ProjectChartState {}

const Html = Guide.Html;

const prefix = 'project-chart';

// 接口类型映射
const TypeMap = {
  pc: 'PC 项目',
  web: 'WEB 项目',
  app: 'APP 项目',
  iot: '硬件、互联网项目',
  other: '其他'
};

const cols = {
  type: {
    type: 'cat',
    alias: '漏洞级别'
  },
  percent: {
    formatter: (val: number) => {
      return Math.round(val * 100) + '%';
    },
    alias: '占比'
  }
};

const data = [
  {
    type: 'pc',
    count: 165,
    color: '#3BA0FF'
  },
  {
    type: 'web',
    count: 261,
    color: '#4DCB73'
  },
  {
    type: 'app',
    count: 523,
    color: '#FAD337'
  },
  {
    type: 'iot',
    count: 123,
    color: '#F2637B'
  },
  {
    type: 'other',
    count: 101,
    color: '#8543E0'
  }
];

export default class ProjectChart extends Component<ProjectChartProps, ProjectChartState> {
  render() {
    const sum = data.reduce((p, c) => p + c.count, 0);
    const newData = data.map(v => ({
      ...v,
      percent: v.count / sum
    }));

    return (
      <section className={prefix}>
        <div className={`${prefix}-chart`}>
          <Chart forceFit={true} height={300} scale={cols} data={newData} padding={0}>
            <Axis name="percent" />
            <Coord type="theta" radius={0.85} innerRadius={0.75} />
            <Tooltip
              showTitle={false}
              itemTpl={
                '<li><span style="background-color:{color};" class="g2-tooltip-marker"></span>{name}: {value}</li>'
              }
            />
            <Guide>
              <Html
                position={['50%', '50%']}
                alignX="middle"
                alignY="middle"
                html={`<div style="color:#8c8c8c;font-size: 14px;text-align: center;width: 10em;">接口总数<br><span style="color:#8c8c8c;font-size:30px">${sum}</span></div>`}
              />
            </Guide>
            <Geom
              type="intervalStack"
              position="percent"
              color={['type', data.map(d => d.color)]}
              tooltip={[
                'type*percent',
                (type, percent) => {
                  percent = Math.round(percent * 100) + '%';
                  return {
                    name: TypeMap[type],
                    value: percent
                  };
                }
              ]}
              style={{ lineWidth: 1, stroke: '#fff' }}
            />
          </Chart>
        </div>
        <div className={`${prefix}-legend`}>
          {newData.map((d, i) => (
            <div className="item" key={d.type}>
              <span className="indicator" style={{ backgroundColor: d.color }} />
              <span className="name">{TypeMap[d.type]}</span>
              <span className="line" />
              <span className="percent">{Math.round(d.percent * 100) + '%'}</span>
              <span className="number">{d.count}个</span>
            </div>
          ))}
        </div>
      </section>
    );
  }
}
