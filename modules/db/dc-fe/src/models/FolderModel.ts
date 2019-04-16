import { Folder } from '../apis/interface';
// import { observable } from 'mobx';
import BaseModel from './BaseModel';

export default class FolderModel extends BaseModel {
  name: string;
  projectId: string;
  description: string;

  constructor(data: Folder) {
    super(data);
    this.name = data.name;
    if (data.project) {
      this.projectId = data.project.id;
    }
    this.description = data.description;
  }

  get formValues() {
    return {
      name: this.name
    };
  }
}
