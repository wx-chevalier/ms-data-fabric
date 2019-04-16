import { CommmonFields } from './common';

export interface ApiAccessInfo {
  cousumeTime: number;
  accessCount: 0;
  accessTime: string;
  userId: string;
}

export interface DynamicDetail extends CommmonFields {
  createUser: { id: string };
  type: string;
  content: string;
}

export interface Dynamic {
  total: number;
  pages: number;
  pageNum: number;
  pageSize: number;
  startRow: number;
  endRow: number;
  prePage: number;
  nextPage: number;
  firstPage: boolean;
  lastPage: boolean;
  hasPreviousPage: boolean;
  hasNextPage: boolean;
  projectDynamics: DynamicDetail[];
}
