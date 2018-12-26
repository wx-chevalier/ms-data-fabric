import { SchemaObject } from '../../../apis/OpenApi3Interface';

export class Schema<T> implements SchemaObject {
  example?: T;
}
