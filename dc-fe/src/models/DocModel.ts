import BaseModel from './BaseModel';
import { observable } from 'mobx';
import { Doc } from '../apis/interface';
import UserModel from './UserModel';

/**
 * 项目文档/接口文档 公用模型类
 */
export default class DocModel extends BaseModel {
  @observable
  title?: string;
  @observable
  content: string;
  type: string;
  creater: UserModel;

  constructor(data: Doc) {
    super(data);
    this.title = data.title;
    this.content = data.content;
    this.type = data.type;
  }
}
