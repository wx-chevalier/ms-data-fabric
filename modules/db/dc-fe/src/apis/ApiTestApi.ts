import axios from 'axios';
import { HttpMethods } from './BaseApi';

const testAgent = axios.create({});

export default class ApiTestApi {
  // 接口测试
  async apiTest(
    type: HttpMethods,
    url: string,
    headers: Object,
    body?: Object
    // tslint:disable-next-line
  ): Promise<{ status: number; message: Array<any> }> {
    const token = localStorage.getItem('DC_LOGIN_TOKEN');
    // tslint:disable-next-line
    delete testAgent.defaults.headers[type]['Content-Type'];
    // tslint:disable-next-line
    testAgent.defaults.headers.common['Authorization'] = `Bearer ${token}`;
    try {
      if (type === 'get') {
        body = undefined;
      } else {
        // 非get方法的请求content-type需要处理
        testAgent.defaults.headers[type]['Content-Type'] = 'application/json';
      }
      const res = await testAgent.request({
        url,
        method: type,
        data: body,
        headers
      });
      return {
        status: res.status,
        message: res.data
      };
    } catch (e) {
      const res = e.response;
      return {
        status: res.status,
        message: res.data
      };
    }
  }
}
