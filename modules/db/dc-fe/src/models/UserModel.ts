import { LoginToken } from './../apis/interface';
import { User, QyUser } from '../apis/interface';
import { QYUSERSTATUS, ApiUserManagementUser, APIUSERSTATUS } from '../apis/interface/user';
import { observable, computed } from '../../node_modules/mobx';
// import { observable } from 'mobx';

export default class UserModel {
  static ROLE_PREFIX = 'ROLE_';

  static clientMap: Map<string, string> = new Map<string, string>();
  username: string;
  roles: string[] = [];
  id: string;

  static ofToken(token: LoginToken): UserModel {
    let u = new UserModel();
    u.id = token.sub;
    u.username = token.username;
    if (token.auth != null) {
      for (const auth of token.auth.split(',').map(s => s.trim())) {
        if (auth.startsWith(this.ROLE_PREFIX)) {
          u.roles.push(auth.substr(this.ROLE_PREFIX.length));
        }
      }
    }
    return u;
  }

  static mock(): UserModel[] {
    return [
      {
        username: 'test1',
        roles: ['MANAGER', 'ACCOUNT'],
        id: 'sdfdsfdsafdsafdsfdf',
        isApiManager: true,
        isAccountManager: true,
        isQyUser: false
      },
      {
        username: 'test2',
        roles: ['MANAGER', 'ACCOUNT'],
        id: 'sdfdsfdzsdfdsffdsfdf1',
        isApiManager: true,
        isAccountManager: true,
        isQyUser: false
      },
      {
        username: 'test3',
        roles: ['MANAGER', 'ACCOUNT'],
        id: 'sdfdssdaggghhsafdsfdf2',
        isApiManager: true,
        isAccountManager: true,
        isQyUser: true
      },
      {
        username: 'test4',
        roles: ['MANAGER', 'ACCOUNT'],
        id: 'sdfdsld;f,d;ksafdsfdf3',
        isApiManager: true,
        isAccountManager: true,
        isQyUser: false
      },
      {
        username: 'test5',
        roles: ['MANAGER', 'ACCOUNT'],
        id: 'sdf;lgk;lkg;kg;lsafdsfdf4',
        isApiManager: true,
        isAccountManager: true,
        isQyUser: true
      },
      {
        username: 'test6',
        roles: ['MANAGER', 'ACCOUNT'],
        id: 'sdfdl;kghjk;fljgk;lsafdsfdf5',
        isApiManager: true,
        isAccountManager: true,
        isQyUser: false
      },
      {
        username: 'test7',
        roles: ['MANAGER', 'ACCOUNT'],
        id: 'sdfdsfkljlfkjlfjlkjflkhjglfsfdf6',
        isApiManager: true,
        isAccountManager: true,
        isQyUser: true
      },
      {
        username: 'test8',
        roles: ['MANAGER', 'ACCOUNT'],
        id: 'lklfjglgfjlkgjlkfjgljfgsafdsfdf7',
        isApiManager: true,
        isAccountManager: true,
        isQyUser: false
      },
      {
        username: 'test9',
        roles: ['MANAGER', 'ACCOUNT'],
        id: 'sdflpkgkfdcl;gkgljdg;lsdgsafdsfdf8',
        isApiManager: true,
        isAccountManager: true,
        isQyUser: true
      }
    ];
  }

  constructor(data?: User) {
    if (data != null) {
      this.id = data.id;
      this.username = data.name;
      if (data.roles) {
        this.roles = data.roles.map(r => r.name);
      }
    }
  }

  // 是否接口管理员
  get isApiManager(): boolean {
    return this.roles.some(r => r === 'MANAGER');
  }

  // 是否账号管理员
  get isAccountManager(): boolean {
    return this.roles.some(r => r === 'ACCOUNT');
  }

  // 是否企业用户
  get isQyUser(): boolean {
    return this.roles.some(r => r === 'CLIENT');
  }
}

export class QyUserModel extends UserModel {
  createdAt: string;

  phone: string;

  @observable
  status: QYUSERSTATUS;

  businessLicense?: string;

  @computed
  get audited(): boolean {
    return this.status !== 'PENDING';
  }

  constructor(data: QyUser) {
    super(data);
    this.createdAt = data.createDatetime;
    this.phone = data.phoneNumber;
    this.status = data.status;
    this.businessLicense = data.businessLicense;
  }
}

// 接口管理员用户模型
export class ApiUserManagementUserModel extends UserModel {
  status: APIUSERSTATUS;
  createdAt: string;
  constructor(data: ApiUserManagementUser) {
    super(data);
    this.status = data.status;
    this.createdAt = data.createDatetime;
  }
}
