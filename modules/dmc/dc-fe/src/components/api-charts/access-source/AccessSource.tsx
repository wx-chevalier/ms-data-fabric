import React from 'react';
import echarts, { EChartOption } from 'echarts';
import ApiStore from '../../../stores/ApiStore';
import { inject, observer } from 'mobx-react';
import UserModel from '../../../models/UserModel';

interface AccessSourceGraphProps {
  apiId: string;
  apiStore?: ApiStore;
}

/**
 * 访问来源 - adminOnly
 */
@inject('apiStore')
@observer
export default class AccessSourceGraph extends React.Component<AccessSourceGraphProps, {}> {
  async componentDidMount() {
    const chart = echarts.init(document.getElementById('access-source') as HTMLCanvasElement);

    const apiId = this.props.apiId;
    const apiStore = this.props.apiStore!;

    const v = await apiStore.getUserAccessDistribution(apiId);

    chart.setOption(this.dataParser(v));
  }

  render() {
    return (
      <section id="access-source" style={{ display: 'inline-block', width: 480, height: 320 }} />
    );
  }

  // 将接口数据转化为图表所需数据
  private dataParser(data: { user: UserModel; count: number }[]): EChartOption {
    if (data.length !== 0) {
      return {
        title: {
          text: '接口访问者来源',
          left: 'center'
        },
        tooltip: {
          trigger: 'item',
          formatter: '{a} <br/>{b} : {c} ({d}%)'
        },
        series: [
          {
            name: '访问来源',
            type: 'pie',
            radius: '55%',
            center: ['50%', '60%'],
            data: data.map(v => ({
              value: v.count,
              name: v.user.username
            })),
            itemStyle: {
              emphasis: {
                shadowBlur: 10,
                shadowOffsetX: 0,
                shadowColor: 'rgba(0, 0, 0, 0.5)'
              }
            }
          }
        ]
      };
    } else {
      return {
        title: {
          text: '接口访问者来源',
          left: 'center'
        },
        tooltip: {
          trigger: 'item',
          formatter: '{a} <br/>{b} : {c} ({d}%)'
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
        }
      };
    }
  }
}
