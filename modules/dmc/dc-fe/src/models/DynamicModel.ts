import BaseModel from './BaseModel';
import { Dynamic, DynamicDetail } from '../apis/interface';
import UserModel from './UserModel';

export class DynamicDetailModel extends BaseModel {
  createUser: UserModel;
  type: string;
  content: string;

  constructor(data: DynamicDetail) {
    super(data);
    this.type = data.type;
    this.content = data.content;
  }
}

export default class DynamicModel {
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
  projectDynamics: DynamicDetailModel[];

  constructor(data: Dynamic) {
    this.total = data.total;
    this.pages = data.pages;
    this.pageNum = data.pageNum;
    this.pageSize = data.pageSize;
    this.startRow = data.startRow;
    this.endRow = data.endRow;
    this.prePage = data.prePage;
    this.nextPage = data.nextPage;
    this.firstPage = data.firstPage;
    this.lastPage = data.lastPage;
    this.hasPreviousPage = data.hasPreviousPage;
    this.hasNextPage = data.hasNextPage;
  }
}
