import { CommmonFields } from './common';
import { User } from './user';
import { Project } from './project';

// TODO: 项目文档的获取和更新接口对创建者的字段描述不一致
export interface Doc extends CommmonFields {
  status?: number;
  title?: string;
  content: string;
  createUser?: User;
  modifyUser: User;
  type: string;
  project?: Project;
}
