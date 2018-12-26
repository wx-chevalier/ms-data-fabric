import axios from 'axios';

// 需要自定义的几个地方
// 1. base URL
// 2. timeout 时间
// 3. 拦截器, 用于字段名的转换
// 4. 错误, 触发一些全局的提示

import { USER_HOST, APIM_HOST, APICR_HOST } from './constants';

export const userAgent = axios.create({
  baseURL: USER_HOST
  // timeout: 10000
});

export const dcAgent = axios.create({
  baseURL: APIM_HOST
  // timeout: 10000
});

export const apicrAgent = axios.create({
  baseURL: APICR_HOST
  // timeout: 10000
});
