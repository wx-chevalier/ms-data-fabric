import { observable, action } from 'mobx';
import BaseModel from '../models/BaseModel';

export default abstract class ModelManagerStore<T extends BaseModel> {
  @observable
  modelList: T[] = [];

  @observable
  selectedItem: T | null = null;

  abstract loadList(force: boolean): Promise<void>;
  abstract loadItemById(id: string): Promise<T>;

  @action.bound
  clearSelection() {
    this.selectedItem = null;
  }

  @action.bound
  async selectItem(item: string | T) {
    if (typeof item === 'string') {
      let inList = this.modelList.find(m => m.id === item);
      if (!inList) {
        inList = await this.loadItemById(item);
        // this.selectItem(inList);
        this.selectedItem = inList;
      }
    } else {
      this.selectedItem = item;
    }
  }
}
