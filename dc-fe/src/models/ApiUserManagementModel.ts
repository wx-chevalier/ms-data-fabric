import { ApiUserManagementUserModel } from './UserModel';
import { APIUSERSTATUS } from '../apis/interface/user';

export class AuthSchema {
  name: string;
  dataSource: {
    name: string;
  };
}

export default class ApiUserManagementModel {
  user: ApiUserManagementUserModel;
  authedDatabase: AuthSchema[];
  status: APIUSERSTATUS;
  createdAt: string;

  get usingStatus() {
    return this.status === 'ALLOWABLE' ? '使用中' : '已停用';
  }

  constructor(user: ApiUserManagementUserModel, db: AuthSchema[]) {
    this.user = user;
    this.status = user.status;
    this.createdAt = user.createdAt;
    this.authedDatabase = db;
  }
}
