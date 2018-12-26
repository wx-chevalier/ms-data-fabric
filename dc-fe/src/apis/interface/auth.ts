/**
 * 后端登陆 JWT TOKEN 解析后的结果
 */
export interface LoginToken {
  // JWT Subject: userId
  sub: string;
  username: string;
  // 角色/权限列表，逗号分割字符串，ROLE_ 开头的表示角色
  auth: string;
  // 过期时间 (单位：s)
  exp: number;
}

export interface AccessToken {
  apiId: string;
  envId: string;
  token: string;
  apiURL: string;
  userId: string;
}
