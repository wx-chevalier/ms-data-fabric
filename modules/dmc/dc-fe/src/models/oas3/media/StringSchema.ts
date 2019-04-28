import { Schema } from './Schema';
import { SchemaObject } from './../../../apis/OpenApi3Interface';

export class StringSchema extends Schema<string> implements SchemaObject {
  get type(): 'string' {
    return 'string';
  }
}
