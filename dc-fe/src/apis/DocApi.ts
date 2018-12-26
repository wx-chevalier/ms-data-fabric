import BaseApi from './BaseApi';
import DocModel from '../models/DocModel';
import { Doc, User } from '../apis/interface';
import UserApi from '../apis/UserApi';
import UserModel from '../models/UserModel';

export default class DocApi extends BaseApi {
  userApi: UserApi = new UserApi();

  /**
   * 获取项目文档列表
   */
  async fetchDocList(projectId: string): Promise<DocModel[]> {
    const result = await this.get<Doc[]>({
      url: `/project/${projectId}/doc`,
      errorNotice: {
        title: '网络错误',
        content: '获取项目文档列表失败'
      }
    });
    const res = await Promise.all(
      result.map(async r => {
        let s = await this.fillDocModelUserInfo(r, new DocModel(r));
        // 需要放在接口层获取内容，否则无法解决顺序问题
        s.content = await this.fetchDocDetail(r.id).then(v => v.content);
        return s;
      })
    );
    return res;
  }

  /**
   * 获取项目文档详情
   */
  async fetchDocDetail(docId: string) {
    const result = await this.get<Doc>({
      url: `/projectdoc/${docId}`,
      errorNotice: {
        title: '网络错误',
        content: '获取文档详情失败'
      }
    });
    return this.fillDocModelUserInfo(result, new DocModel(result));
  }

  /**
   * 创建项目文档
   *
   * @param title 文档标题/名字
   * @param content 文档内容
   * @param contentType 文档存储格式类型：如 string, markdown, asciidoc 等
   */
  async createDoc(
    projectId: string,
    title: string,
    content: string,
    contentType: string = 'string'
  ): Promise<DocModel> {
    const result = await this.post<Doc>({
      url: '/projectdoc',
      data: {
        projectId,
        content,
        contentType,
        title
      }
    });
    return this.fillDocModelUserInfo(result, new DocModel(result));
  }

  /**
   * 更新项目文档，返回更新后的项目文档
   */
  async updateDoc(
    docId: string,
    title: string,
    content: string,
    projectId: string,
    contentType: string = 'string'
  ): Promise<DocModel> {
    const result = await this.patch<Doc>({
      url: `projectdoc/${docId}`,
      data: {
        content,
        contentType,
        projectId,
        title
      }
    });
    return this.fillDocModelUserInfo(result, new DocModel(result));
  }

  /**
   * 删除项目文档
   */
  async deleteDoc(docIdList: string[]) {
    await Promise.all(
      docIdList.map(async docId => {
        await this.delete<null>({
          url: `/projectdoc/${docId}`
        });
      })
    );
  }

  /****************************** api文档相关接口 *****************************/
  async createApiDoc(
    apiId: string,
    content: string,
    contentType?: string,
    title?: string
  ): Promise<DocModel> {
    const resp = await this.post<Doc>({
      url: '/apidoc',
      data: {
        apiId,
        content,
        contentType,
        title
      }
    });
    return this.fillDocModelUserInfo(resp, new DocModel(resp));
  }

  async updateApiDoc(
    apiId: string,
    apiDocId: string,
    content: string,
    contentType?: string,
    title?: string
  ) {
    await this.patch<Doc>({
      url: `/apidoc/${apiDocId}`,
      data: {
        apiId,
        content,
        contentType,
        title
      }
    });
  }

  /**
   * 获取 DocModel 中创建者用户名
   * TODO: 考虑后端可能需要优化这部分接口
   *
   * @param respDoc 接口返回文档信息
   * @param model 文档模型类
   */
  private async fillDocModelUserInfo(respDoc: Doc, model: DocModel): Promise<DocModel> {
    if (respDoc.createUser) {
      try {
        model.creater = new UserModel((await this.userApi.getUserInfo(
          respDoc.createUser.id
        )) as User);
      } catch (e) {
        //
      }
    }
    return model;
  }
}

export const docApi: DocApi = new DocApi();
