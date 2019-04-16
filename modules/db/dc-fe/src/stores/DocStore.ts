import { observable, action, computed } from 'mobx';
import DocModel from '../models/DocModel';
import DocApi from '../apis/DocApi';

export default class EnvStore {
  docApi: DocApi = new DocApi();

  @observable
  docList: DocModel[] = [];

  @observable
  selectedDoc: DocModel | null = null;

  // 当前编辑器状态，新建文档/编辑已有文档
  @observable
  editorStatus: 'create' | 'update' | undefined;

  // 转换后的编辑的文档内容
  @observable
  editingContent: string = '';

  // 搜索框的搜索值
  @observable
  searchText: string;

  @computed
  get filteredData(): DocModel[] {
    const reg = new RegExp(this.searchText, 'gi');
    return this.searchText === ''
      ? this.docList.slice()
      : this.docList.slice().filter(i => i.title!.match(reg));
  }

  @action.bound
  selectDoc(doc?: DocModel) {
    if (doc) {
      this.selectedDoc = doc;
    } else {
      this.selectedDoc = null;
    }
  }

  @action.bound
  initEditorStatus() {
    // 清空selectedDoc
    this.selectDoc();
    this.editorStatus = undefined;
    this.editingContent = '';
  }

  @action.bound
  toggleEditorStatus(status?: 'create' | 'update') {
    this.editorStatus = status;
  }

  @action.bound
  async loadDocListByPid(pid: string) {
    this.docList = await this.docApi.fetchDocList(pid);
  }

  // docId为数组
  @action.bound
  async delete(docIdList: string[]) {
    await this.docApi.deleteDoc(docIdList);
  }

  @action.bound
  async create(pid: string, title: string, content: string): Promise<DocModel> {
    return await this.docApi.createDoc(pid, title, content);
  }

  @action.bound
  async update(docId: string, title: string, content: string, pid: string): Promise<DocModel> {
    return await this.docApi.updateDoc(docId, title, content, pid);
  }

  @action.bound
  changeSearchText(v: string) {
    this.searchText = v;
  }

  get currentEditorContent() {
    return this.editingContent;
  }

  set currentEditorContent(v: string) {
    this.editingContent = v;
  }
}
