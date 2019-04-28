import { Schema } from './Schema';
import { SchemaObject } from './../../../apis/OpenApi3Interface';

export class DateSchema extends Schema<string> implements SchemaObject {
  get type(): 'string' {
    return 'string';
  }

  get format() {
    return 'date';
  }
}
