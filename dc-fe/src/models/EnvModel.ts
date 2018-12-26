import BaseModel from './BaseModel';
import { observable } from 'mobx';
import { Env } from '../apis/interface';

export class EnvKVModel {
  envKey: string;
  envValue: string;
}

export default class EnvModel extends BaseModel {
  @observable
  envName: string;
  remark?: string;
  projectId: string;
  @observable
  envValue: EnvKVModel[];
  @observable
  envDescription: string;

  constructor(data: Env) {
    super(data);
    this.projectId = data.projectId;
    this.envName = data.envName;
    this.envValue = this.parseObjToArr(JSON.parse(data.envValue));
    if (data.remark) {
      this.envDescription = data.remark;
    }
  }

  // 将kv对象转成表格需要的数据用于显示
  private parseObjToArr = (kvObj: { [key: string]: string }): Array<EnvKVModel> => {
    let r: Array<EnvKVModel> = [];
    Object.keys(kvObj).forEach(kv => {
      r.push({
        envKey: kv,
        envValue: kvObj[kv]
      });
    });
    return r;
  };
}
