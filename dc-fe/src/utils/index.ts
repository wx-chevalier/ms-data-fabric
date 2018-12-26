import { isObject } from 'util';
// import * as path from 'path';

export const isTest = process.env.NODE_ENV === 'test';
export const isDev = process.env.NODE_ENV === 'development';
export const isProd = process.env.NODE_ENV === 'production';
export const env = process.env.NODE_ENV as string;

export const appendPath = (from: string, to: string) => {
  return from.endsWith('/') ? from + to : from + '/' + to;
};

export const filterUndefined = (obj: Object) => {
  Object.keys(obj).forEach(k => {
    const v = obj[k];
    if (isObject(v)) {
      filterUndefined(v);
    }
    if (v === undefined) {
      delete obj[k];
    }
  });
  return obj;
};

export const safeJsonParse = function<T>(data: string, fallback: T): T {
  try {
    return JSON.parse(data) as T;
  } catch (error) {
    return fallback;
  }
};
