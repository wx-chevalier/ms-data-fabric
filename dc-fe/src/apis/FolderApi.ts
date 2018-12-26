import BaseApi from './BaseApi';
import FolderModel from '../models/FolderModel';
import { Folder } from './interface';
import { FieldValues } from '../components/folder-form/FolderForm';

export default class FolderApi extends BaseApi {
  async getListByProjectId(id: string): Promise<FolderModel[]> {
    const result = await this.get<Folder[]>({
      url: `/project/${id}/folder`,
      errorNotice: {
        title: '网络错误',
        content: '获取项目分组列表信息失败'
      }
    });
    return result.map(f => new FolderModel(f));
  }

  async getFolderById(id: string): Promise<FolderModel> {
    const result = await this.get<Folder>({
      url: `/folder/${id}`,
      errorNotice: {
        title: '网络错误',
        content: '获取指定分组信息失败'
      }
    });
    return new FolderModel(result);
  }

  async postFolder(projectId: string, data: FieldValues): Promise<void> {
    await this.post<Folder>({
      url: '/folder',
      data: {
        projectId,
        name: data.name
      }
    });
  }

  async updateFolder(folderId: string, data: FieldValues): Promise<FolderModel> {
    const result = await this.patch<Folder>({
      url: `/folder/${folderId}`,
      data: {
        name: data.name
      }
    });
    return new FolderModel(result);
  }

  async deleteFolderById(fId: string): Promise<void> {
    await this.delete<Folder>({
      url: `/folder/${fId}`
    });
  }
}
