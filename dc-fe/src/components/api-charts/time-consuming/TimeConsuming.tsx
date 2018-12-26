import React from 'react';
import echarts, { EChartOption } from 'echarts';
import { inject, observer } from 'mobx-react';
import ApiStore from '../../../stores/ApiStore';

interface TimeConsumingGraphProps {
  apiId: string;
  apiStore?: ApiStore;
}

/**
 * 访问耗时图表
 */
@inject('apiStore')
@observer
export default class TimeConsumingGraph extends React.Component<TimeConsumingGraphProps, {}> {
  async componentDidMount() {
    const chart = echarts.init(document.getElementById('time-consume') as HTMLCanvasElement);

    const apiId = this.props.apiId;
    const apiStore = this.props.apiStore!;
    const v = await apiStore.getConsumeTimeDistribution(apiId);

    chart.setOption(this.dataParser(v));
  }

  render() {
    return (
      <section id="time-consume" style={{ display: 'inline-block', width: 480, height: 320 }} />
    );
  }

  private dataParser(data: { interval: string; count: number }[]): EChartOption {
    if (data.length !== 0) {
      return {
        title: {
          text: '接口访问耗时',
          left: 'center'
        },
        tooltip: {
          trigger: 'item',
          formatter: '{b}ms: {c}次'
        },
        xAxis: {
          type: 'category',
          data: data.map(v => v.interval)
        },
        yAxis: {
          type: 'value'
        },
        series: [
          {
            data: data.map(v => v.count),
            type: 'bar'
          }
        ]
      };
    } else {
      return {
        title: {
          text: '接口访问耗时',
          left: 'center'
        },
        xAxis: {
          show: false
        },
        yAxis: {
          show: false
        },
        graphic: {
          type: 'text',
          z: 100,
          left: 'center',
          top: 'middle',
          style: {
            fill: '#333',
            text: ['暂无数据']
          }
        },
        series: []
      };
    }
  }
}
