import { CommmonFields } from '../apis/interface';
import { observable } from 'mobx';

export default abstract class BaseModel {
  id: string;
  createdAt: string;
  @observable
  updatedAt?: string;
  // 这个字段后期需要配合后端进行重构，有的返回了UserModel类型，有的返回了UserId,是string类型
  createUserid?: string;
  @observable
  modifyUserid?: string;

  constructor(data: CommmonFields) {
    this.id = data.id;
    this.createdAt = data.createDatetime;
    this.updatedAt = data.modifyDatetime;
  }
}
