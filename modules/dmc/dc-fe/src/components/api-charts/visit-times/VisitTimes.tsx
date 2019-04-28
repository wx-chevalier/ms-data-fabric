import React from 'react';
import echarts, { EChartOption } from 'echarts';
import ApiStore from '../../../stores/ApiStore';
import { inject, observer } from 'mobx-react';

interface VisitTimesGraphProps {
  apiId: string;
  apiStore?: ApiStore;
}

/**
 * 日期-访问次数图表
 */
@inject('apiStore')
@observer
export default class VisitTimesGraph extends React.Component<VisitTimesGraphProps, {}> {
  async componentDidMount() {
    const chart = echarts.init(document.getElementById('visit-times') as HTMLCanvasElement);

    const apiId = this.props.apiId;
    const apiStore = this.props.apiStore!;
    const v = await apiStore.getVisitTimes('%Y-%m-%d', apiId);

    chart.setOption(this.dataParser(v));
  }

  render() {
    return (
      <section id="visit-times" style={{ display: 'inline-block', width: 480, height: 320 }} />
    );
  }

  // 将接口数据转化为图表所需数据
  private dataParser(data: { time: string; count: number }[]): EChartOption {
    if (data.length !== 0) {
      return {
        title: {
          text: '每日访问次数',
          left: 'center'
        },
        tooltip: {
          trigger: 'item',
          formatter: '{b}：{c}次'
        },
        xAxis: {
          type: 'category',
          name: '日期',
          data: data.map(d => d.time)
        },
        yAxis: {
          name: '访问量',
          type: 'value'
        },
        series: [
          {
            data: data.map(d => d.count),
            type: 'line'
          }
        ]
      };
    } else {
      return {
        title: {
          text: '每日访问次数',
          left: 'center'
        },
        xAxis: {
          name: '日期'
        },
        yAxis: {
          name: '访问量'
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
