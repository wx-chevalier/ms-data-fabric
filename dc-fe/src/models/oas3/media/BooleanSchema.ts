import { Schema } from './Schema';
import { SchemaObject } from './../../../apis/OpenApi3Interface';

export class BooleanSchema extends Schema<boolean> implements SchemaObject {
  get type(): 'boolean' {
    return 'boolean';
  }
}
