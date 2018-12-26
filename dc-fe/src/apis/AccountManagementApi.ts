import BaseApi from './BaseApi';
import { DatabaseModel, TableModel, ColumnModel } from '../models/datasource';
import { Database } from './interface';
import UserApi from './UserApi';
import ApiUserManagementModel from '../models/ApiUserManagementModel';
import { ApiUserManagement } from './interface/user';
import UserModel from '../models/UserModel';

/**
 * 账号管理员api
 */
export default class AccountManagementApi extends BaseApi {
  userApi: UserApi = new UserApi();

  // 获取数据库列表
  async getDatabaseList(): Promise<DatabaseModel[]> {
    const res = await this.get<Database[]>({
      url: '/datasource/database',
      errorNotice: {
        title: '网络错误',
        content: '加载数据库列表失败'
      }
    });

    return Promise.all(
      res.map(async r => {
        const userList = await Promise.all(
          r.authUsers!.map(async u => {
            return await this.userApi.getApiManagementUserInfo(u);
          })
        );

        const v: DatabaseModel = new DatabaseModel(
          r.name,
          r.dataSource.id!,
          r.type,
          r.createDatetime,
          r.tableList!.map(
            t => new TableModel(t.name, v, t.columnList.map(c => new ColumnModel(c.name, c.type)))
          ),
          userList.filter(u => u.status !== 'ABANDONED')
        );
        return v;
      })
    );
  }

  /**
   * 更改数据库授权
   * @param dsId 数据源id
   * @param databaseName 数据库名称
   * @param userIdList 被授权的用户id数组
   */
  async changeAuthorization(dsId: string, databaseName: string, userIdList: string[]) {
    await this.post<Database>({
      url: `/datasource/${dsId}/database/${databaseName}`,
      data: userIdList
    });
  }

  // 获取apiUser列表，内容中包含被授权给该用户的数据库相关信息
  async getApiManagerList(): Promise<ApiUserManagementModel[]> {
    const res = await this.get<ApiUserManagement[]>({
      url: '/user/auth/database',
      errorNotice: {
        title: '网络错误',
        content: '加载接口管理员列表失败'
      }
    });
    const result = await Promise.all(
      res.map(async r => {
        const userInfo = await this.userApi.getApiManagementUserInfo(r.id);
        return new ApiUserManagementModel(userInfo, r.authSchema);
      })
    );
    return result.filter(v => v.status !== 'ABANDONED');
  }

  // 创建apiUser - 由于接口实现问题，要分成两步实现

  // 根据用户名和密码先创建用户
  async createApiManager(username: string, password: string): Promise<UserModel> {
    return await this.userApi.createApiManager(username, password);
  }

  // 授权数据库给指定用户
  async authDbListToUser(
    userId: string,
    authDbList: {
      // 数据库名称
      databaseName: string;
      // 数据源id
      dsId: string;
    }[]
  ) {
    await this.patch({
      url: `/user/${userId}/database`,
      data: authDbList
    });
  }
}
