/*
 * @file 访问量总览
 * @description 详细说明
 * 
 * @author 王下邀月熊 <384924552@qq.com>
 * 
 * Created Date: Sat, 2018-06-30 00:00:14
 * 
 * Last Modified: Sat, 2018-06-30 16:29:02
 * Last Modified By: 王下邀月熊 <384924552@qq.com>
 * 
 * This code is licensed under the MIT License.
 */

import React, { Component } from 'react';
import { Chart, Axis, Tooltip, Geom } from 'bizcharts';

import './PVChart.scss';

interface PVChartProps {}

interface PVChartState {}

const prefix = 'pv-chart';

function getRandomArbitrary(min: number, max: number) {
  return Math.floor(Math.random() * (max - min) + min);
}

const data = Array.from({ length: 24 }).map((_, i) => {
  let time = i < 10 ? `0${i}:00` : `${i}:00`;

  return {
    time,
    today: getRandomArbitrary(10000, 50000),
    yesterday: getRandomArbitrary(10000, 50000)
  };
});

const scale = {
  today: { min: 0 },
  yesterday: { min: 0 },
  time: { range: [0, 1] }
};

export default class PVChart extends Component<PVChartProps, PVChartState> {
  render() {
    return (
      <section className={`${prefix}`}>
        <div className="left">
          <div className={`${prefix}-info`}>
            <div className="total">
              <div className="label">今日访问总量</div>
              <div className="value">12, 138</div>
            </div>
            <div className="rate">
              <div className="label">每秒访问量</div>
              <div className="value">222</div>
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
              <Geom type="line" position="time*today" size={2} color="#fa7976" />
              <Geom type="line" position="time*yesterday" size={2} />
            </Chart>
          </div>
        </div>
        <div className="right">
          <div className={`${prefix}-toggle`}>
            <span className="label--active">今日</span>
            <span>本周</span>
            <span>本月</span>
            <span>今年</span>
          </div>
          <div className={`${prefix}-ranking`}>
            <h3>接口访问量排名</h3>
            {Array.from({ length: 7 }).map((_, i) => (
              <div className="item" key={i}>
                <span className="left">
                  <span className="label">{i + 1}</span>
                  <span>接口名 {i + 1}</span>
                </span>
                <span className="right">323,324</span>
              </div>
            ))}
          </div>
        </div>
      </section>
    );
  }
}
