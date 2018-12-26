import { Schema } from './Schema';
import { SchemaObject } from './../../../apis/OpenApi3Interface';

export class ObjectSchema extends Schema<Object> implements SchemaObject {
  get type(): 'object' {
    return 'object';
  }

  properties?: { [propertyName: string]: SchemaObject };
}
