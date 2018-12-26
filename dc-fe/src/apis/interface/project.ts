import { CommmonFields, User, API, Doc } from '.';

export interface Project extends CommmonFields {
  name: string;
  description: string;
  version: string;
  type: string;
  createUser: User;
  modifyUser: User;
  folderList: Folder[];
  apiList: API[];
  docList: Doc[];
}

export interface ProjectType {
  typeName: string;
  typeDescribe: string;
}

export interface Folder extends CommmonFields {
  name: string;
  description: string;
  apiList: API[];
  project: Project;
}

export interface Env extends CommmonFields {
  projectId: string;
  envName: string;
  envValue: string;
  remark?: string;
}
