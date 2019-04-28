import axios, { AxiosRequestConfig, AxiosInstance } from 'axios';
import jwt from 'jsonwebtoken';
import { LoginToken } from './interface';
import { dcAgent } from './agent';
import Raven from 'raven-js';
import moment from 'moment';
import notice, { Notice } from '../utils/notice';
import { USER_HOST } from './constants';
import { routingStore } from '../stores';

export const LOGIN_TOKEN_STORAGE_KEY = 'DC_LOGIN_TOKEN';

interface ResultNotice {
  title: string;
  content: string;
}

export enum HttpMethods {
  GET = 'get',
  POST = 'post',
  PUT = 'put',
  DELETE = 'delete',
  PATCH = 'patch'
}

interface BaseConfig {
  url: string;
  params?: Object;
  successNotice?: ResultNotice;
  errorNotice?: ResultNotice;
}

interface PostConfig extends BaseConfig {
  data: Object;
}

interface DeleteConfig extends BaseConfig {
  data?: Object;
}

export default class BaseApi {
  BEARER = 'Bearer ';

  constructor(protected agent: AxiosInstance = dcAgent, public resultNotice: Notice = notice) {}

  async get<T>(config: BaseConfig, extra?: AxiosRequestConfig) {
    return this.request<T>(HttpMethods.GET, config, extra);
  }

  async post<T>(config: PostConfig, extra?: AxiosRequestConfig) {
    return this.request<T>(HttpMethods.POST, config, extra);
  }

  async delete<T>(config: DeleteConfig, extra?: AxiosRequestConfig) {
    return this.request<T>(HttpMethods.DELETE, config, extra);
  }

  async put<T>(config: PostConfig, extra?: AxiosRequestConfig) {
    return this.request<T>(HttpMethods.PUT, config, extra);
  }

  async patch<T>(config: PostConfig, extra?: AxiosRequestConfig) {
    return this.request<T>(HttpMethods.PATCH, config, extra);
  }

  async wait(s: number): Promise<{}> {
    return new Promise(resolve => {
      setTimeout(() => {
        resolve();
      }, s * 1000);
    });
  }

  /**
   * 获取Token有效期
   */
  getTokenExp() {
    const jwtToken = localStorage.getItem(LOGIN_TOKEN_STORAGE_KEY);
    if (!jwtToken) {
      return null;
    }

    let parsed;
    try {
      parsed = jwt.decode(jwtToken) as LoginToken;
      return parsed.exp;
    } catch (e) {
      return null;
    }
  }

  tokenValidCheck(): boolean {
    const tokenExp = this.getTokenExp();
    if (tokenExp) {
      // token有效期Unix时间戳处理
      const expDate = moment.unix(tokenExp);
      if (expDate.isAfter(moment())) {
        return true;
      } else {
        return false;
      }
    } else {
      // 用户未登录，此时应返回true否则无法登录
      return true;
    }
  }

  private async request<T>(
    method: HttpMethods,
    config: BaseConfig | PostConfig,
    extra: AxiosRequestConfig = {}
  ): Promise<T> {
    const requestConfig: AxiosRequestConfig = {
      method: method,
      url: config.url,
      params: config.params || {},
      ...extra
    };
    if (
      method === HttpMethods.POST ||
      method === HttpMethods.PUT ||
      method === HttpMethods.PATCH ||
      method === HttpMethods.DELETE
    ) {
      requestConfig.data = (config as PostConfig).data || {};
    }

    // 拦截器在请求前检查token
    this.agent.interceptors.request.use((conf: AxiosRequestConfig) => {
      if (this.tokenValidCheck()) {
        const tokenExp = this.getTokenExp();
        if (tokenExp) {
          const expDate = moment.unix(tokenExp);
          // 需要确定距离过期多久时才刷新token - 暂时设置为1h
          if (expDate.isAfter(moment()) && expDate.diff(moment()) < 60 * 60 * 1000) {
            axios
              .post(
                `${
                  // 由于线上和开发环境的链接有问题，在这里处理'/'的问题
                  USER_HOST[USER_HOST.length - 1] === '/'
                    ? USER_HOST.slice(0, USER_HOST.length - 1)
                    : USER_HOST
                }/user/token`,
                {}
              )
              .then(res => {
                localStorage.setItem(LOGIN_TOKEN_STORAGE_KEY, res.data.token);
              });
          }
        }

        return conf;
      } else {
        return Promise.reject('token失效');
      }
    });

    try {
      const result = await this.agent.request<T>(requestConfig);

      if (config.successNotice) {
        this.resultNotice('success', config.successNotice.title, config.successNotice.content);
      }

      return result.data;
    } catch (error) {
      if (error === 'token失效') {
        this.resultNotice('error', 'token已过期', '请重新登录');
        routingStore.history.push('/prelogin');
        throw error;
      }
      // 上报 sentry
      Raven.captureException(error);
      if (config.errorNotice) {
        this.resultNotice('error', config.errorNotice.title, config.errorNotice.content);
      }
      throw error;
    }
  }
}
