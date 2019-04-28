import { Schema } from './Schema';
import { SchemaObject } from './../../../apis/OpenApi3Interface';

export class IntegerSchema extends Schema<number> implements SchemaObject {
  get type(): 'number' {
    return 'number';
  }

  get format() {
    return 'int32';
  }
}
