import { SchemaObject, primitiveType } from '../apis/OpenApi3Interface';

export enum PritiveType {}

export class SchemaModel implements SchemaObject {
  title: string;
  description?: string;
  type: primitiveType;
  // tslint:disable-next-line
  example?: any;
  format?: string;
  properties?: { [index: string]: SchemaModel };
  nullable?: boolean;

  constructor(data: SchemaObject) {
    this.title = data.title || '';
    this.type = data.type!;
    if (this.type === 'object' && typeof data.properties === 'object') {
      this.properties = {};
      Object.keys(data.properties).forEach(
        k => (this.properties![k] = new SchemaModel(data.properties![k]))
      );
    }
    this.description = data.description || '';
    this.format = data.format || '';
    this.nullable = !!data.nullable;
    this.example = data.example || '';
  }

  get must(): string {
    return !!this.nullable ? '是' : '否';
  }
}

export class ArgModel extends SchemaModel {
  constructor(data: SchemaObject) {
    super(data);
  }

  get argList(): ({ name: string; data: SchemaModel })[] {
    if (typeof this.properties === 'object') {
      return Object.keys(this.properties).map(k => ({ name: k, data: this.properties![k] }));
    } else {
      return [];
    }
  }
}
