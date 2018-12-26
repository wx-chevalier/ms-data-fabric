import { AuthSchema } from '../../models/ApiUserManagementModel';

/**
 * ALLOWABLE - 允许登录
 * ABANDONED - 被删除的账户
 * UNALLOWABLE - 停用
 * PENDING - 待审核
 */
export type QYUSERSTATUS = 'ALLOWABLE' | 'ABANDONED' | 'UNALLOWABLE' | 'PENDING';

export type APIUSERSTATUS = 'ALLOWABLE' | 'ABANDONED' | 'UNALLOWABLE';

export interface User {
  id: string;
  name: string;
  roles: Role[];
  permissions: Permission[];
  expireAt: string;
}

export interface Role {
  id: string;
  name: string;
}

export interface Permission {
  id: string;
  name: string;
}

export interface QyUser extends User {
  createDatetime: string;
  phoneNumber: string;
  status: QYUSERSTATUS;
  businessLicense?: string;
}

export interface ApiUserManagementUser extends User {
  status: APIUSERSTATUS;
  createDatetime: string;
}

export interface ApiUserManagement {
  // 用户id
  id: string;
  // 授权的数据库列表
  authSchema: AuthSchema[];
}
