import { RouterStore } from 'mobx-react-router';
import ProjectStore from './ProjectStore';
import ApiStore from './ApiStore';
import UserStore from './UserStore';
import DataBaseStore from './DataBaseStore';
import EnvStore from './EnvStore';
import DocStore from './DocStore';
import AccoutManagementStore from './AccoutManagementStore';
import ModelStore from './ModelStore';
import APIImplEditorStore from './APIImplEditorStore';

export {
  UserStore,
  RouterStore,
  ProjectStore,
  ApiStore,
  DataBaseStore,
  EnvStore,
  DocStore,
  AccoutManagementStore,
  APIImplEditorStore
};

export const userStore = new UserStore();
export const routingStore = new RouterStore();
export const projectStore = new ProjectStore(userStore);
export const apiStore = new ApiStore();
export const dbStore = new DataBaseStore();
export const envStore = new EnvStore();
export const docStore = new DocStore();
export const accountManagementStore = new AccoutManagementStore();
export const modelStore = new ModelStore();
export const apiImplEditorStore = new APIImplEditorStore();
