import { Schema } from './Schema';
import { SchemaObject } from './../../../apis/OpenApi3Interface';

export class NumberSchema extends Schema<number> implements SchemaObject {
  get type(): 'number' {
    return 'number';
  }
}
