import { APIImplDef } from './interface/api/apiimpl';
import { BooleanSchema } from './../models/oas3/media/BooleanSchema';
import { IntegerSchema } from './../models/oas3/media/IntegerSchema';
import { DateTimeSchema } from './../models/oas3/media/DateTimeSchema';
import { DateSchema } from './../models/oas3/media/DateSchema';
import { StringSchema } from './../models/oas3/media/StringSchema';
import { NumberSchema } from './../models/oas3/media/NumberSchema';
import { Schema } from './../models/oas3/media/Schema';
import { ObjectSchema } from './../models/oas3/media/ObjectSchema';
import { ArgModel } from './../models/SchemaModel';
import BaseApi from './BaseApi';

export type TimeFormat = '%Y-%m-%d';

export default class APIImplDevApi extends BaseApi {
  /**
   * 解析 SQL 得到其中定义的参数 Schema
   *
   * @param dialect SQL 方言
   * @param sql SQL 语句
   * @param datasourceName 数据源，当 dialect 不是 presto 时，该值必须
   */
  async analyzeParameter(impl: APIImplDef): Promise<ArgModel> {
    // tslint:disable-next-line
    let json = await this.post<ObjectSchema>({
      url: '/api-dev/analyze',
      data: impl
    });

    return new ArgModel(json);
  }

  /**
   * 参数 dialect, sql, datasourceName 和 analyzeParameter 中参数意义一致
   * @param schema 参数 Schema, 其属性对应 Schema 为基本类型，应包含测试执行所需测试值（example），例如
   * {
   *   "type" : "object",
   *   "properties" : {
   *     "v_number" : {
   *       "type" : "number",
   *       "example": 10
   *     }
   *   }
   * }
   */
  async exec(impl: APIImplDef, parameterSchema: ObjectSchema): Promise<string> {
    // tslint:disable-next-line
    let param: { [key: string]: any } = {};
    if (parameterSchema && parameterSchema.properties) {
      for (let k of Object.keys(parameterSchema.properties)) {
        let prop = parameterSchema.properties[k];
        if (prop && prop.example) {
          param[k] = prop.example;
        }
      }
    }

    // tslint:disable-next-line
    let json = await this.post<any>({
      url: '/api-dev/execute',
      data: { impl, param }
    });
    return JSON.stringify(json);
  }

  /**
   * JSON 对象 -> 对应 Schema 值，只处理一些基本类型
   */
  // tslint:disable-next-line
  toPrimitiveSchema(json: any): Schema<any> {
    switch (json.type) {
      case 'number':
        return new NumberSchema();
      case 'string':
        if (!json.format) {
          return new StringSchema();
        }
        switch (json.format) {
          case 'date':
            return new DateSchema();
          case 'date-time':
            return new DateTimeSchema();
          default:
            throw new Error(`不支持的 Schema: ${json.type} - ${json.format}`);
        }
      case 'integer':
        return new IntegerSchema();
      case 'boolean':
        return new BooleanSchema();
      default:
        throw new Error(`不支持的 Schema: ${json.type} - ${json.format}`);
    }
  }
}

export const apiImplDevApi = new APIImplDevApi();
