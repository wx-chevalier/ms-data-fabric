// import React, { ReactElement } from 'react';
import { EventEmitter } from 'events';
import { observable, action } from 'mobx';
import { Point } from './interface';

const INSTRUCT_PREFIX = '$click';

export default class InstructController extends EventEmitter {
  @observable.ref
  actionConponents: JSX.Element[] = [];

  @observable
  showModal: boolean = false;

  // tslint:disable-next-line
  parseClickInstuct(ev: any) {
    const { item, shape } = ev;
    if (!shape || !shape.get('class')) {
      return;
    }
    const instructArr = (shape.get('class') as string).split('-');
    const [prefix, name, startPointString, argsString] = instructArr;
    if (prefix !== INSTRUCT_PREFIX || !name) {
      return;
    }
    this.emit(name, item, JSON.parse(startPointString), JSON.parse(argsString));
  }

  // position {x: number, y: number}
  // component: string;
  // onOk: () => void;
  @action.bound
  show(component: JSX.Element, showModal: boolean = true, append: boolean = false) {
    // append ? (this.actionConponents = [...this.actionConponents, component]) : (this.actionConponents = [component]);
    this.actionConponents = [component];
    this.showModal = showModal;
  }

  @action.bound
  clear() {
    this.actionConponents = [];
    this.showModal = false;
  }

  // tslint:disable-next-line
  static instructBuilder = (name: string, offset: Point, args: any) => {
    return `${INSTRUCT_PREFIX}-${name}-${JSON.stringify(offset)}-${JSON.stringify(args)}`;
  };
}
